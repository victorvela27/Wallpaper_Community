package com.example.wallpaper_community.ApartadoInformativo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderInformacion extends RecyclerView.ViewHolder{


    View mView;

    private ViewHolderInformacion.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);

    }

    public void setOnClickListener(ViewHolderInformacion.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderInformacion(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });



    }

    public void SeteoInformacion(Context context, String nombre, String imagen){

        ImageView ImagenInformativo;
        TextView TituloinformativoTXT;


        //Conexion Item

        ImagenInformativo = mView.findViewById(R.id.ImagenInformativo);
        TituloinformativoTXT = mView.findViewById(R.id.TituloinformativoTXT);


        //Setear Datos

        TituloinformativoTXT.setText(nombre);



        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenInformativo);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(ImagenInformativo);

        }


    }
}
