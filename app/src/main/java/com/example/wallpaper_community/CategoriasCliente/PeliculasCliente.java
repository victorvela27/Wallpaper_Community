package com.example.wallpaper_community.CategoriasCliente;

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

import com.example.wallpaper_community.CategoriasAdmin.PeliculasA.Pelicula;
import com.example.wallpaper_community.CategoriasAdmin.PeliculasA.ViewHolderPelicula;
import com.example.wallpaper_community.DetalleCliente.DetalleCliente;
import com.example.wallpaper_community.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PeliculasCliente extends AppCompatActivity {

    RecyclerView recyclerViewPeliculaC;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas_cliente);

        Toolbar toolbarL = findViewById(R.id.toobarL);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Peliculas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewPeliculaC = findViewById(R.id.recyclerViewPeliculaC);
        recyclerViewPeliculaC.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("PELICULAS");

        dialog = new Dialog(PeliculasCliente.this);

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

                       String Imagen = getItem(position).getImagen();
                       String Nombres = getItem(position).getNombre();
                       int Vistas = getItem(position).getVistas();
                       String VistasString = String.valueOf(Vistas);


                       Intent intent = new Intent(PeliculasCliente.this, DetalleCliente.class);
                       intent.putExtra("Imagen", Imagen);
                        intent.putExtra("Nombre", Nombres);
                        intent.putExtra("Vista", VistasString);
                        startActivity(intent);

                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {


                    }
                });
                return viewHolderPelicula;
            }
        };
        sharedPreferences = PeliculasCliente.this.getSharedPreferences("PELICULAS",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");

        if(ordenar_en.equals("Dos")) {
            recyclerViewPeliculaC.setLayoutManager(new GridLayoutManager(PeliculasCliente.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPeliculaC.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewPeliculaC.setLayoutManager(new GridLayoutManager(PeliculasCliente.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPeliculaC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    protected void onStart(){
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
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

        TextView OrdenarTXT;
        Button Dos_columnas, Tres_columnas;

        dialog.setContentView(R.layout.dialog_ordenar);

        OrdenarTXT = dialog.findViewById(R.id.Ordenar);
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
    public boolean onSupportNavigateUp () {
            onBackPressed();
            return super.onSupportNavigateUp();

    }
}