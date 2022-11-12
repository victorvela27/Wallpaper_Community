package com.example.wallpaper_community.CategoriasAdmin.VideoJuegosA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVideojuegos extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderVideojuegos.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);
        void OnItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolderVideojuegos.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderVideojuegos(@NonNull View itemView) {
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

    public void SeteoVideojuegos(Context context, String nombre, int vista, String imagen){

        ImageView Imagen_Videojuegos;
        TextView NombreImagen_Videojuegos;
        TextView Visitas_Videojuegos;

        //Conexion Item

        Imagen_Videojuegos = mView.findViewById(R.id.Imagen_Videojuegos);
        NombreImagen_Videojuegos = mView.findViewById(R.id.NombreImagen_Videojuegos);
        Visitas_Videojuegos = mView.findViewById(R.id.Visitas_Videojuegos);

        //Setear Datos

        NombreImagen_Videojuegos.setText(nombre);

        //Convertir a String visitas
        String VistaString = String.valueOf(vista);

        Visitas_Videojuegos.setText(VistaString);

        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(Imagen_Videojuegos);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(Imagen_Videojuegos);
        }


    }
}



