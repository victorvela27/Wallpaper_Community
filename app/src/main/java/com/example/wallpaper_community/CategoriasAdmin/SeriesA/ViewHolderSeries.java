package com.example.wallpaper_community.CategoriasAdmin.SeriesA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderSeries extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderSeries.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);
        void OnItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolderSeries.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderSeries(@NonNull View itemView) {
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

    public void SeteoSeries(Context context, String nombre, int vista, String imagen){

        ImageView Imagen_Series;
        TextView NombreImagen_Series;
        TextView Visitas_Series;

        //Conexion Item

        Imagen_Series = mView.findViewById(R.id.Imagen_Series);
        NombreImagen_Series = mView.findViewById(R.id.NombreImagen_Series);
        Visitas_Series = mView.findViewById(R.id.Visitas_Series);

        //Setear Datos

        NombreImagen_Series.setText(nombre);

        //Convertir a String visitas
        String VistaString = String.valueOf(vista);

        Visitas_Series.setText(VistaString);

        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(Imagen_Series);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(Imagen_Series);
        }


    }
}



