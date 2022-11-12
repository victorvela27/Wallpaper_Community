package com.example.wallpaper_community.CategoriasAdmin.MusicaA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class MusicaA extends AppCompatActivity {

    RecyclerView recyclerViewMusica;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Musica, ViewHoldermMusica> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Musica> options;

    SharedPreferences sharedPrefrences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        Toolbar toolbarL = findViewById(R.id.toobarM);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Música");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewMusica = findViewById(R.id.recyclerViewMusica);
        recyclerViewMusica.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("MUSICA");

        dialog = new Dialog(MusicaA.this);

        ListarImagenesMusica();
    }

    private void ListarImagenesMusica() {

        options = new FirebaseRecyclerOptions.Builder<Musica>().setQuery(mRef, Musica.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Musica, ViewHoldermMusica>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHoldermMusica ViewHoldermMusica, int i, @NonNull Musica musica) {
                ViewHoldermMusica.SeteoMusica(
                        getApplicationContext(),
                        musica.getNombre(),
                        musica.getVistas(),
                        musica.getImagen()
                );

            }
            @NonNull
            @Override
            public ViewHoldermMusica onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musica,parent,false);

                ViewHoldermMusica viewHoldermMusica = new ViewHoldermMusica(itemview);

                viewHoldermMusica.setOnClickListener(new ViewHoldermMusica.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Toast.makeText(MusicaA.this, "Item Click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                        final String Nombre = getItem(position).getNombre();
                        final String Imagen = getItem(position).getImagen();
                        int Vista = getItem(position).getVistas();
                        final String VistaString = String.valueOf(Vista);


                        AlertDialog.Builder builder = new AlertDialog.Builder(MusicaA.this);

                        String [] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i == 0){
                                    Intent intent = new Intent(MusicaA.this, AgregarMusica.class);
                                    intent.putExtra("NombreEnviado", Nombre);
                                    intent.putExtra("ImagenEnviada", Imagen);
                                    intent.putExtra("VistaEnviada",VistaString);
                                    startActivity(intent);
                                }
                                if(i == 1){
                                    EliminarDatos(Nombre, Imagen);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHoldermMusica;
            }
        };

        sharedPrefrences = MusicaA.this.getSharedPreferences("MUSICA",MODE_PRIVATE);
        String ordenar_en = sharedPrefrences.getString("Ordenar","Dos");

        if(ordenar_en.equals("Dos")) {
            recyclerViewMusica.setLayoutManager(new GridLayoutManager(MusicaA.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewMusica.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewMusica.setLayoutManager(new GridLayoutManager(MusicaA.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewMusica.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private void EliminarDatos(final String NombreActual, final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(MusicaA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Estas seguro de elimnar la imagen");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = mRef.orderByChild("nombre").equalTo(NombreActual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(MusicaA.this, "La imagen ha sido borrada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MusicaA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MusicaA.this, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MusicaA.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MusicaA.this, "Cancelado por Adminstrador", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar,menu);
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.Agregar:
                startActivity(new Intent(MusicaA.this, AgregarMusica.class));
                finish();
                break;
            case R.id.Vista:
                Ordenar_Imagenes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes(){

        TextView Ordenar;
        Button Dos_columnas, Tres_columnas;

        dialog.setContentView(R.layout.dialog_ordenar);

        Ordenar = dialog.findViewById(R.id.Ordenar);
        Dos_columnas = dialog.findViewById(R.id.Dos_columnas);
        Tres_columnas = dialog.findViewById(R.id.Tres_columnas);

        Dos_columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPrefrences.edit();
                editor.putString("Ordenar","Dos");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });
        Tres_columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPrefrences.edit();
                editor.putString("Ordenar","Tres");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}