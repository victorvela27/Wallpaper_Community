package com.example.wallpaper_community.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wallpaper_community.CategoriasAdmin.MusicaA.MusicaA;
import com.example.wallpaper_community.CategoriasAdmin.PeliculasA.PeliculasA;
import com.example.wallpaper_community.CategoriasAdmin.SeriesA.SeriesA;
import com.example.wallpaper_community.CategoriasAdmin.VideoJuegosA.VideojuegosA;
import com.example.wallpaper_community.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InicioAdmin extends Fragment {

Button Peliculas, Series, Musica, Videojuegos;

    TextView FechaAdmin, NombreTXT;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Peliculas = view.findViewById(R.id.Peliculas);
        Series = view.findViewById(R.id.Series);
        Musica = view.findViewById(R.id.Musica);
        Videojuegos = view.findViewById(R.id.Videojuegos);

        FechaAdmin = view.findViewById(R.id.FechaAdmin);
        NombreTXT = view.findViewById(R.id.NombreTXT);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("ADMINISTRADORES");


        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String StringFecha = simpleDateFormat.format(date);
        FechaAdmin.setText("Hoy es: " + StringFecha);




        Peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PeliculasA.class));

            }
        });

        Series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SeriesA.class));
            }
        });
        Musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MusicaA.class));
            }
        });
        Videojuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideojuegosA.class));
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ComprobarUsuarioActivo();
    }

    private void ComprobarUsuarioActivo(){
        if(user!=null){
            CargaDeDatos();
        }
    }

    private void CargaDeDatos(){

        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombre = "" + snapshot.child("NOMBRES").getValue();
                    NombreTXT.setText(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}