package com.example.wallpaper_community.FragmentosClientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.ApartadoInformativo.Informacion;
import com.example.wallpaper_community.ApartadoInformativo.ViewHolderInformacion;
import com.example.wallpaper_community.Categorias.Cat_Dispositivo.CategoriaD;
import com.example.wallpaper_community.Categorias.Cat_Dispositivo.ViewHolderCD;
import com.example.wallpaper_community.Categorias.Cat_FireBase.CategioriaF;
import com.example.wallpaper_community.Categorias.Cat_FireBase.ViewHolderCF;
import com.example.wallpaper_community.Categorias.ControladorCD;
import com.example.wallpaper_community.CategoriasClienteFB.ListaCategoriaFirebase;
import com.example.wallpaper_community.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InicioCliente extends Fragment {

    RecyclerView recyclerviewCategoriaD,recyclerviewCategoriaF, recyclerviewInfo ;
    FirebaseDatabase firebaseDatabaseD, firebaseDatabaseF,firebaseDatabaseInfo;
    DatabaseReference referenceD, referenceF, referenceInfo;
    LinearLayoutManager linearLayoutManagerD, linearLayoutManagerF, linearLayoutManagerInfo;

    FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD> firebaseRecyclerAdapterD;
    FirebaseRecyclerAdapter<CategioriaF, ViewHolderCF> firebaseRecyclerAdapterF;
    FirebaseRecyclerAdapter<Informacion, ViewHolderInformacion> firebaseRecyclerAdapterInfo;
    FirebaseRecyclerOptions<CategoriaD> optionsD;
    FirebaseRecyclerOptions<CategioriaF> optionsF;
    FirebaseRecyclerOptions<Informacion> optionsInfo;

    TextView Fecha;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        firebaseDatabaseD = FirebaseDatabase.getInstance();
        firebaseDatabaseF = FirebaseDatabase.getInstance();
        firebaseDatabaseInfo = FirebaseDatabase.getInstance();

        referenceD = firebaseDatabaseD.getReference("CATEGORIAS_D");
        referenceF = firebaseDatabaseF.getReference("CATEGORIAS_F");
        referenceInfo = firebaseDatabaseInfo.getReference("INFORMACION");

        linearLayoutManagerD = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerF = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerInfo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerviewCategoriaD = view.findViewById(R.id.recyclerviewCategoriaD);
        recyclerviewCategoriaD.setHasFixedSize(true);
        recyclerviewCategoriaD.setLayoutManager(linearLayoutManagerD);

        recyclerviewCategoriaF = view.findViewById(R.id.recyclerviewCategoriaF);
        recyclerviewCategoriaF.setHasFixedSize(true);
        recyclerviewCategoriaF.setLayoutManager(linearLayoutManagerF);

        recyclerviewInfo = view.findViewById(R.id.recyclerviewInfo);
        recyclerviewInfo.setHasFixedSize(true);
        recyclerviewInfo.setLayoutManager(linearLayoutManagerInfo);

        Fecha = view.findViewById(R.id.Fecha);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String StringFecha = simpleDateFormat.format(date);
        Fecha.setText(StringFecha);


        VerCategoriasD();

        VerCategoriasF();

        VerApartadoInformativo();

        return view;
    }

   private void VerCategoriasD() {
        optionsD = new FirebaseRecyclerOptions.Builder<CategoriaD>().setQuery(referenceD, CategoriaD.class).build();
        firebaseRecyclerAdapterD = new FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD>(optionsD) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCD viewHolderCD, int i, @NonNull CategoriaD categoriaD) {
                viewHolderCD.SeteoCategoriaD(
                     getActivity(),
                     categoriaD.getCategoria(),
                     categoriaD.getImagen()
                );
             }

            @NonNull
            @Override
            public ViewHolderCD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_dispositivo,parent,false);
               ViewHolderCD viewHolderCD = new ViewHolderCD(itemview);

               viewHolderCD.setOnClickListener(new ViewHolderCD.ClickListener() {
                   @Override
                   public void OnItemClick(View view, int position) {

                       String categoria = getItem(position).getCategoria();

                       Intent intent = new Intent(view.getContext(), ControladorCD.class);
                       intent.putExtra("Categoria",categoria);
                       startActivity(intent);

                       Toast.makeText(getActivity(), "Bienvenido a la sección " + categoria, Toast.LENGTH_SHORT).show();
                   }
               });

                return viewHolderCD;
            }
        };
        recyclerviewCategoriaD.setAdapter(firebaseRecyclerAdapterD);
    }

    private void VerCategoriasF() {
        optionsF = new FirebaseRecyclerOptions.Builder<CategioriaF>().setQuery(referenceF, CategioriaF.class).build();
        firebaseRecyclerAdapterF = new FirebaseRecyclerAdapter<CategioriaF, ViewHolderCF>(optionsF) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCF viewHolderCF, int i, @NonNull CategioriaF categoriaF) {
                viewHolderCF.SeteoCategoriaF(
                        getActivity(),
                        categoriaF.getCategoria(),
                        categoriaF.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderCF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_firebase,parent,false);
                ViewHolderCF ViewHolderCF = new ViewHolderCF(itemview);

                ViewHolderCF.setOnClickListener(new ViewHolderCF.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {

                        String NOMBRE_CATEGORIA = getItem(position).getCategoria();

                        Intent intent = new Intent(view.getContext(), ListaCategoriaFirebase.class);
                        intent.putExtra("NOMBRE_CATEGORIA", NOMBRE_CATEGORIA);
                        Toast.makeText(view.getContext(), "Bienvenido a: " + NOMBRE_CATEGORIA, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }
                });

                return ViewHolderCF;
            }
        };
        recyclerviewCategoriaF.setAdapter(firebaseRecyclerAdapterF);


    }

    private void VerApartadoInformativo() {
        optionsInfo = new FirebaseRecyclerOptions.Builder<Informacion>().setQuery(referenceInfo, Informacion.class).build();
        firebaseRecyclerAdapterInfo = new FirebaseRecyclerAdapter<Informacion, ViewHolderInformacion>(optionsInfo) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderInformacion viewHolderInformacion, int i, @NonNull Informacion informacion) {
                viewHolderInformacion.SeteoInformacion(
                        getActivity(),
                        informacion.getNombre(),
                        informacion.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderInformacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartado_informativo,parent,false);
                ViewHolderInformacion viewHolderInformacion = new ViewHolderInformacion(itemview);

                viewHolderInformacion.setOnClickListener(new ViewHolderInformacion.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {


                    }
                });

                return viewHolderInformacion;
            }
        };
        recyclerviewInfo.setAdapter(firebaseRecyclerAdapterInfo);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapterD != null && firebaseRecyclerAdapterF != null && firebaseRecyclerAdapterInfo !=null){
            firebaseRecyclerAdapterD.startListening();
            firebaseRecyclerAdapterF.startListening();
            firebaseRecyclerAdapterInfo.startListening();
        }
    }
}