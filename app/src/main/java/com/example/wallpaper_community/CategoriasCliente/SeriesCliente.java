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

import com.example.wallpaper_community.CategoriasAdmin.SeriesA.Series;
import com.example.wallpaper_community.CategoriasAdmin.SeriesA.ViewHolderSeries;
import com.example.wallpaper_community.DetalleCliente.DetalleCliente;
import com.example.wallpaper_community.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeriesCliente extends AppCompatActivity {

    RecyclerView recyclerViewSeriesC;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Series, ViewHolderSeries> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Series> options;
    SharedPreferences sharedPrefrences;
    Dialog dialog;

    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_cliente);

        Toolbar toolbarL = findViewById(R.id.toobarL);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Series");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewSeriesC = findViewById(R.id.recyclerViewSeriesC);
        recyclerViewSeriesC.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("SERIES");

        dialog = new Dialog(SeriesCliente.this);

        ListarImagenesSeries();
    }
    private void ListarImagenesSeries() {

        options = new FirebaseRecyclerOptions.Builder<Series>().setQuery(mRef, Series.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Series, ViewHolderSeries>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderSeries ViewHolderSeries, int i, @NonNull Series series) {
                ViewHolderSeries.SeteoSeries(
                        getApplicationContext(),
                        series.getNombre(),
                        series.getVistas(),
                        series.getImagen()
                );

            }
            @NonNull
            @Override
            public ViewHolderSeries onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series,parent,false);

                ViewHolderSeries viewHolderSeries = new ViewHolderSeries(itemview);

                viewHolderSeries.setOnClickListener(new ViewHolderSeries.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {


                        String Imagen = getItem(position).getImagen();
                        String Nombres = getItem(position).getNombre();
                        int Vistas = getItem(position).getVistas();
                        String VistasString = String.valueOf(Vistas);




                        Intent intent = new Intent(SeriesCliente.this, DetalleCliente.class);
                        intent.putExtra("Imagen", Imagen);
                        intent.putExtra("Nombre", Nombres);
                        intent.putExtra("Vista", VistasString);
                        startActivity(intent);
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                    }
                });
                return viewHolderSeries;
            }
        };

        sharedPrefrences = SeriesCliente.this.getSharedPreferences("SERIES",MODE_PRIVATE);
        String ordenar_en = sharedPrefrences.getString("Ordenar","Dos");

        if(ordenar_en.equals("Dos")) {
            recyclerViewSeriesC.setLayoutManager(new GridLayoutManager(SeriesCliente.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewSeriesC.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewSeriesC.setLayoutManager(new GridLayoutManager(SeriesCliente.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewSeriesC.setAdapter(firebaseRecyclerAdapter);
        }
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