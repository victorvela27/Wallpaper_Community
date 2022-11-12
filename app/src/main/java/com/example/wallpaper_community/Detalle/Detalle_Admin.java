package com.example.wallpaper_community.Detalle;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalle_Admin extends AppCompatActivity {

    CircleImageView Imagen_detalleAdmin;
    TextView UidDetalleAdmin, NombresDetalleAdmin, ApellidosDetalleAdmin,CorreoDetalleAdmin,EdadDetalleAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_admin);

        Toolbar toolbarL = findViewById(R.id.toobarL);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Detalles de Administrador");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Imagen_detalleAdmin = findViewById(R.id.Imagen_detalleAdmin);
        UidDetalleAdmin = findViewById(R.id.UidDetalleAdmin);
        NombresDetalleAdmin = findViewById(R.id.NombresDetalleAdmin);
        ApellidosDetalleAdmin = findViewById(R.id.ApellidosDetalleAdmin);
        CorreoDetalleAdmin = findViewById(R.id.CorreoDetalleAdmin);
        EdadDetalleAdmin = findViewById(R.id.EdadDetalleAdmin);

        String UIDDetalla= getIntent().getStringExtra("UID");
        String NombreDetalla= getIntent().getStringExtra("NOMBRES");
        String ApellidosDetalla= getIntent().getStringExtra("APELLIDOS");
        String CorreoDetalla= getIntent().getStringExtra("CORREO");
        String EdadDetalla= getIntent().getStringExtra("EDAD");
        String ImagenDetalla= getIntent().getStringExtra("IMAGEN");

        //Seteo de imagenes

        UidDetalleAdmin.setText("UID: "+ UIDDetalla);
        NombresDetalleAdmin.setText("Nombres: "+ NombreDetalla);
        ApellidosDetalleAdmin.setText("Apellidos: "+ ApellidosDetalla);
        CorreoDetalleAdmin.setText("Correo: "+ CorreoDetalla);
        EdadDetalleAdmin.setText("Edad: "+ EdadDetalla);

        try{
            Picasso.get().load(ImagenDetalla).placeholder(R.drawable.perfil_item).into(Imagen_detalleAdmin);
        }
        catch(Exception e){
            Picasso.get().load(R.drawable.perfil_item).into(Imagen_detalleAdmin);
        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}