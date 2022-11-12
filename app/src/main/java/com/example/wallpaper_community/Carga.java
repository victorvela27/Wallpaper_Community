package com.example.wallpaper_community;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        final int DURACION = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(Carga.this,MainActivityAdministrador.class);
                startActivity(intent);
                finish();
            }
        },DURACION);

    }
}