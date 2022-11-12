package com.example.wallpaper_community.CategoriasAdmin.PeliculasA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPelicula extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderPelicula.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);
        void OnItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolderPelicula.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderPelicula(@NonNull View itemView) {
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

    public void SeteoPeliculas(Context context,String nombre, int vista,String imagen){

        ImageView ImagenPelicula;
        TextView NombreImagenPeliculas;
        TextView Visitas;

        //Conexion Item

        ImagenPelicula = mView.findViewById(R.id.ImagenPelicula);
        NombreImagenPeliculas = mView.findViewById(R.id.NombreImagenPeliculas);
        Visitas = mView.findViewById(R.id.Visitas);

        //Setear Datos

        NombreImagenPeliculas.setText(nombre);

        //Convertir a String visitas
        String VistaString = String.valueOf(vista);

        Visitas.setText(VistaString);
        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenPelicula);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(ImagenPelicula);
        }

    }
}
