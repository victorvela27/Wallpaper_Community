package com.example.wallpaper_community.CategoriasAdmin.PeliculasA;

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

public class PeliculasA extends AppCompatActivity {

    RecyclerView recyclerViewPelicula;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Pelicula,ViewHolderPelicula> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    SharedPreferences sharedPrefrences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);

        Toolbar toolbarL = findViewById(R.id.toobarP);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("??Pel??culas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewPelicula = findViewById(R.id.recyclerViewPelicula);
        recyclerViewPelicula.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("PELICULAS");

        dialog = new Dialog(PeliculasA.this);

        ListarImagenesPeliculas();

    }

    private void ListarImagenesPeliculas(){
        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(mRef,Pelicula.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPelicula viewHolderPelicula, int i, @NonNull Pelicula pelicula) {
                viewHolderPelicula.SeteoPeliculas(
                        getApplicationContext(),
                        pelicula.getNombre(),
                        pelicula.getVistas(),
                        pelicula.getImagen()
                );

            }

            @NonNull
            @Override
            public ViewHolderPelicula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelicula,parent,false);

                ViewHolderPelicula viewHolderPelicula = new ViewHolderPelicula(itemview);
                
                viewHolderPelicula.setOnClickListener(new ViewHolderPelicula.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Toast.makeText(PeliculasA.this, "Item Click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                        final String Nombre = getItem(position).getNombre();
                        final String Imagen = getItem(position).getImagen();
                        int Vista = getItem(position).getVistas();
                        final String VistaString = String.valueOf(Vista);


                        AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasA.this);

                        String [] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i == 0){
                                    Intent intent = new Intent(PeliculasA.this,AgregarPelicula.class);
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
                return viewHolderPelicula;
            }
        };
        sharedPrefrences = PeliculasA.this.getSharedPreferences("PELICULAS",MODE_PRIVATE);
        String ordenar_en = sharedPrefrences.getString("Ordenar","Dos");

        if(ordenar_en.equals("Dos")) {
            recyclerViewPelicula.setLayoutManager(new GridLayoutManager(PeliculasA.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPelicula.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewPelicula.setLayoutManager(new GridLayoutManager(PeliculasA.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPelicula.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private void EliminarDatos(final String NombreActual, final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("??Estas seguro de elimnar la imagen");

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
                        Toast.makeText(PeliculasA.this, "La imagen ha sido borrada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PeliculasA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PeliculasA.this, "Se elimin?? correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PeliculasA.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PeliculasA.this, "Cancelado", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(PeliculasA.this,AgregarPelicula.class));
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