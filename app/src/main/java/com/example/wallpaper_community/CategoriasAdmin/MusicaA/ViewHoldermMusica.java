package com.example.wallpaper_community.CategoriasAdmin.MusicaA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHoldermMusica extends RecyclerView.ViewHolder{

    View mView;

    private ViewHoldermMusica.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);
        void OnItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHoldermMusica.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHoldermMusica(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miClickListener.OnItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                miClickListener.OnItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

    }

    public void SeteoMusica(Context context, String nombre, int vista, String imagen){

        ImageView Imagen_Musica;
        TextView NombreImagen_Musica;
        TextView Visitas_Musica;

        //Conexion Item

        Imagen_Musica = mView.findViewById(R.id.Imagen_Musica);
        NombreImagen_Musica = mView.findViewById(R.id.NombreImagen_Musica);
        Visitas_Musica = mView.findViewById(R.id.Visitas_Musica);

        //Setear Datos

        NombreImagen_Musica.setText(nombre);

        //Convertir a String visitas
        String VistaString = String.valueOf(vista);

        Visitas_Musica.setText(VistaString);

        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(Imagen_Musica);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(Imagen_Musica);
        }


    }
}



