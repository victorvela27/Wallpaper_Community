package com.example.wallpaper_community;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.wallpaper_community.FragmentosClientes.AcercaDeCliente;
import com.example.wallpaper_community.FragmentosClientes.CompartirCliente;
import com.example.wallpaper_community.FragmentosClientes.InicioCliente;
import com.example.wallpaper_community.FragmentosClientes.Maps;
import com.example.wallpaper_community.FragmentosClientes.login_admin;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Fragmento por defecto
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InicioCliente()).commit();
            navigationView.setCheckedItem(R.id.InicioCliente);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.InicioCliente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InicioCliente()).commit();
                break;
            case R.id.AcercaDe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AcercaDeCliente()).commit();
                break;
            case R.id.Compartir:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CompartirCliente()).commit();
                break;
            case R.id.Login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new login_admin()).commit();
                break;
            case R.id.Maps:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Maps()).commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
