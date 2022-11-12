package com.example.wallpaper_community.CategoriasClienteFB;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.DetalleCliente.DetalleCliente;
import com.example.wallpaper_community.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaCategoriaFirebase extends AppCompatActivity {



    RecyclerView recyclerViewCategoriaElegida;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<ImgCatFirebaseElegida,ViewHolderImgCatElegida> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<ImgCatFirebaseElegida> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_firebase);

        Toolbar toolbarL = findViewById(R.id.toobarL);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Lista Imágenes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String BD_CAT_FIREBASE = getIntent().getStringExtra("NOMBRE_CATEGORIA");
            recyclerViewCategoriaElegida = findViewById(R.id.recyclerViewCategoriaElegida);
            recyclerViewCategoriaElegida.setHasFixedSize(true);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CATEGORIA_SUBIDAS_FIREBASE").child(BD_CAT_FIREBASE);

        dialog = new Dialog(ListaCategoriaFirebase.this);

        ListarCategoriaSeleccionada();


    }

    private void ListarCategoriaSeleccionada() {

        options = new FirebaseRecyclerOptions.Builder<ImgCatFirebaseElegida>().setQuery(databaseReference, ImgCatFirebaseElegida.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImgCatFirebaseElegida, ViewHolderImgCatElegida>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderImgCatElegida viewHolderImgCatElegida, int i, @NonNull ImgCatFirebaseElegida imgCatFirebaseElegida) {
                viewHolderImgCatElegida.SeteoCategoriaFElegida(
                        getApplicationContext(),
                        imgCatFirebaseElegida.getNombre(),
                        imgCatFirebaseElegida.getVistas(),
                        imgCatFirebaseElegida.getImagen()
                );

            }
            @NonNull
            @Override
            public ViewHolderImgCatElegida onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_cat_f_elegida,parent,false);

                ViewHolderImgCatElegida viewHolderImgCatElegida = new ViewHolderImgCatElegida(itemview);

                viewHolderImgCatElegida.setOnClickListener(new ViewHolderImgCatElegida.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {

                        String Imagen = getItem(position).getImagen();
                        String Nombres = getItem(position).getNombre();
                        int Vistas = getItem(position).getVistas();
                        String VistasString = String.valueOf(Vistas);

                        Intent intent = new Intent(ListaCategoriaFirebase.this, DetalleCliente.class);
                        intent.putExtra("Imagen", Imagen);
                        intent.putExtra("Nombre", Nombres);
                        intent.putExtra("Vista", VistasString);
                        startActivity(intent);
                    }


                    public void OnItemLongClick(View view, int position) {

                    }
                });
                return viewHolderImgCatElegida;
            }
        };



        sharedPreferences = ListaCategoriaFirebase.this.getSharedPreferences("MUSICA",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");

        if(ordenar_en.equals("Dos")) {
            recyclerViewCategoriaElegida.setLayoutManager(new GridLayoutManager(ListaCategoriaFirebase.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewCategoriaElegida.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewCategoriaElegida.setLayoutManager(new GridLayoutManager(ListaCategoriaFirebase.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewCategoriaElegida.setAdapter(firebaseRecyclerAdapter);
        }

    }

    @Override
    protected void onStart() {
        if(firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vista) {
            Ordenar_Imagenes();
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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar","Dos");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });
        Tres_columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
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