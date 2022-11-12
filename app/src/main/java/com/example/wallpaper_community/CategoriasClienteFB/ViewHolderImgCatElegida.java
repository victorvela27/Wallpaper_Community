package com.example.wallpaper_community.CategoriasClienteFB;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

public class ViewHolderImgCatElegida extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderImgCatElegida.ClickListener miClickListener;

    public interface ClickListener{
        void OnItemClick(View view,int position);

    }

    public void setOnClickListener(ViewHolderImgCatElegida.ClickListener clickListener){

        miClickListener = clickListener;

    }

    public ViewHolderImgCatElegida(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });



    }

    public void SeteoCategoriaFElegida(Context context, String nombre,int vista, String imagen){

        ImageView ImgCatFElegida;
        TextView NombreImg_Cat_Elegida, Vistas_Img_Cat_Elegida;


        //Conexion Item

        ImgCatFElegida = mView.findViewById(R.id.ImgCatFElegida);
        NombreImg_Cat_Elegida = mView.findViewById(R.id.NombreImg_Cat_Elegida);
        Vistas_Img_Cat_Elegida = mView.findViewById(R.id.Vistas_Img_Cat_Elegida);


        //Setear Datos

        NombreImg_Cat_Elegida.setText(nombre);

        String VistaString = String.valueOf(vista);
        Vistas_Img_Cat_Elegida.setText(VistaString);



        try{
            //Si la imagen es traido correctamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImgCatFElegida);

        }catch (Exception e){
            //Si falla al traerla

            Picasso.get().load(R.drawable.categoria).into(ImgCatFElegida);

        }


    }
}
