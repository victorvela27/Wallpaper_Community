package com.example.wallpaper_community.Categorias.Cat_Dispositivo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCD extends RecyclerView.ViewHolder{


    View mView;

    private ViewHolderCD.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);

    }

    public void setOnClickListener(ViewHolderCD.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderCD(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });



    }

    public void SeteoCategoriaD(Context context, String categoria, String imagen){

        ImageView ImagenCategoriaD;
        TextView NombreCategoriaD;


        //Conexion Item

        ImagenCategoriaD = mView.findViewById(R.id.ImagenCategoriaD);
        NombreCategoriaD = mView.findViewById(R.id.NombreCategoriaD);


        //Setear Datos

        NombreCategoriaD.setText(categoria);



        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenCategoriaD);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(ImagenCategoriaD);

        }


    }
}
