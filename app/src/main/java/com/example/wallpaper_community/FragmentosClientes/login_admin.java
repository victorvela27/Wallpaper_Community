package com.example.wallpaper_community.FragmentosClientes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wallpaper_community.Login;
import com.example.wallpaper_community.R;


public class login_admin extends Fragment {

    Button Acceder;
    TextView Aviso;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);

        Acceder = view.findViewById(R.id.Acceder);
        Aviso = view.findViewById(R.id.Aviso);

        Acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), Login.class));
            }
        });


        Aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://drive.google.com/file/d/1Tn5kJTmG62CLKcJFDnqy1j8ut_uEmnWY/view?usp=sharing");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });


        return view;


    }
}