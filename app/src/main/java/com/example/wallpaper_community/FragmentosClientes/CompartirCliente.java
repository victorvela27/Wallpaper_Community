package com.example.wallpaper_community.FragmentosClientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wallpaper_community.R;


public class CompartirCliente extends Fragment {


    Button BotonCompartir;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compartir_cliente, container, false);

        BotonCompartir = view.findViewById(R.id.BotonCompartir);

        BotonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompartirApp();
            }
        });

        return view;
    }

    private void CompartirApp() {

        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
            String mensaje = "Hola, te invito a probar esta app, te va a gustar, descargalo en:\n";
            mensaje = mensaje + "https://drive.google.com/file/d/1uQLklHq9zrsb3v3OsGjC9qbbi78iJ8Bg/view?usp=sharing";
            intent.putExtra(Intent.EXTRA_TEXT,mensaje);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}