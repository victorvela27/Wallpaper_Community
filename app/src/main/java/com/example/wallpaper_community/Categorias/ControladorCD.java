package com.example.wallpaper_community.Categorias;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wallpaper_community.CategoriasCliente.MusicaCliente;
import com.example.wallpaper_community.CategoriasCliente.PeliculasCliente;
import com.example.wallpaper_community.CategoriasCliente.SeriesCliente;
import com.example.wallpaper_community.CategoriasCliente.VideojuegosCliente;
import com.example.wallpaper_community.R;

public class ControladorCD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlador_cd);

        String CategoriaRecuperada = getIntent().getStringExtra("Categoria");

        if (CategoriaRecuperada.equals("Peliculas")) {
            startActivity(new Intent(ControladorCD.this, PeliculasCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Series")) {
            startActivity(new Intent(ControladorCD.this, SeriesCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Música")) {
            startActivity(new Intent(ControladorCD.this, MusicaCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Videojuegos")) {
            startActivity(new Intent(ControladorCD.this, VideojuegosCliente.class));
            finish();
        }
    }

}