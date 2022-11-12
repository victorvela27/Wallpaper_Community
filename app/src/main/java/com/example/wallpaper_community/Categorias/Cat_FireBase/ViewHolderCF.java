package com.example.wallpaper_community.Categorias.Cat_FireBase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCF extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderCF.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);

    }

    public void setOnClickListener(ViewHolderCF.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderCF(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });



    }

    public void SeteoCategoriaF(Context context, String categoria, String imagen){

        ImageView ImagenCategoriaF;
        TextView NombreCategoriaF;


        //Conexion Item

        ImagenCategoriaF = mView.findViewById(R.id.ImagenCategoriaF);
        NombreCategoriaF = mView.findViewById(R.id.NombreCategoriaF);


        //Setear Datos

        NombreCategoriaF.setText(categoria);



        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenCategoriaF);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(ImagenCategoriaF);

        }


    }
}
