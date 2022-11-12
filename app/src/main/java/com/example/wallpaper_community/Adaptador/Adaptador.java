package com.example.wallpaper_community.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.Detalle.Detalle_Admin;
import com.example.wallpaper_community.Modelo.Administrador;
import com.example.wallpaper_community.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends  RecyclerView.Adapter<Adaptador.MyHolder>{

    private Context context;
    private List<Administrador> adminsitradores;

    public Adaptador(Context context, List<Administrador> adminsitradores) {
        this.context = context;
        this.adminsitradores = adminsitradores;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflar el ADMIN_LAYOUT

        View view = LayoutInflater.from(context).inflate(R.layout.admin_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //Obtenemos los datos del modelo
        String UID = adminsitradores.get(position).getUID();
        String IMAGEN = adminsitradores.get(position).getIMAGEN();
        String NOMBRES = adminsitradores.get(position).getNOMBRES();
        String APELLIDOS = adminsitradores.get(position).getAPELLIDOS();
        String CORREO = adminsitradores.get(position).getCORREO();
        int EDAD = adminsitradores.get(position).getEDAD();
        String EDADSTRING = String.valueOf(EDAD);

        //SETEO DE DATOS

        holder.NombresADMIN.setText(NOMBRES);
        holder.CorreoADMIN.setText(CORREO);

        try{
            Picasso.get().load(IMAGEN).placeholder(R.drawable.perfil_item).into(holder.perfilADMIN);
        }catch (Exception e){
            Picasso.get().load(R.drawable.perfil_item).into(holder.perfilADMIN);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detalle_Admin.class);
                //
                intent.putExtra("UID",UID);
                intent.putExtra("NOMBRES",NOMBRES);
                intent.putExtra("APELLIDOS",APELLIDOS);
                intent.putExtra("CORREO",CORREO);
                intent.putExtra("EDAD",EDADSTRING);
                intent.putExtra("IMAGEN",IMAGEN);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {

        return adminsitradores.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        //Vistas
        CircleImageView perfilADMIN;
        TextView NombresADMIN, CorreoADMIN;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            perfilADMIN = itemView.findViewById(R.id.perfilADMIN);
            NombresADMIN = itemView.findViewById(R.id.NombresADMIN);
            CorreoADMIN = itemView.findViewById(R.id.CorreoADMIN);

        }
    }
}
