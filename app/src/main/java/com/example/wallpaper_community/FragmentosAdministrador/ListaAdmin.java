package com.example.wallpaper_community.FragmentosAdministrador;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper_community.Adaptador.Adaptador;
import com.example.wallpaper_community.Modelo.Administrador;
import com.example.wallpaper_community.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListaAdmin extends Fragment {

RecyclerView Administradores_recycleView;
Adaptador adaptador;
List<Administrador> administradoresList;
FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_admin, container, false);
        Administradores_recycleView = view.findViewById(R.id.Administradores_recycleView);
        Administradores_recycleView.setHasFixedSize(true);
        Administradores_recycleView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        administradoresList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        ObtenerLista();


        return view;
    }

    private void ObtenerLista() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ADMINISTRADORES");
        reference.orderByChild("APELLIDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                administradoresList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Administrador administrador = ds.getValue(Administrador.class);
                    assert administrador != null;
                    assert user != null;
                    if(!administrador.getUID().equals(user.getUid())){
                        administradoresList.add(administrador);
                    }
                adaptador = new Adaptador(getActivity(),administradoresList);
                    Administradores_recycleView.setAdapter(adaptador);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void BuscarAdministrador(String consulta) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ADMINISTRADORES");
        reference.orderByChild("APELLIDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                administradoresList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Administrador administrador = ds.getValue(Administrador.class);
                    assert administrador != null;
                    assert user != null;
                    if(!administrador.getUID().equals(user.getUid())){
                        administradoresList.add(administrador);

                        //Buscar por nombre y correo
                        if(administrador.getNOMBRES().toLowerCase().contains(consulta.toLowerCase()) ||
                        administrador.getCORREO().toLowerCase().contains(consulta.toLowerCase())){
                            administradoresList.add(administrador);

                        }
                    }
                    adaptador = new Adaptador(getActivity(),administradoresList);
                    Administradores_recycleView.setAdapter(adaptador);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar,menu);
        MenuItem item = menu.findItem(R.id.buscar_administrador);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String consulta) {

                if(!TextUtils.isEmpty(consulta.trim())){
                    BuscarAdministrador(consulta);
                }else{
                    ObtenerLista();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String consulta) {
                if(!TextUtils.isEmpty(consulta.trim())){
                    BuscarAdministrador(consulta);
                }else{
                    ObtenerLista();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //Visualizar

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }
}