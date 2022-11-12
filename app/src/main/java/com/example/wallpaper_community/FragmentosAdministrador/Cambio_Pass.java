package com.example.wallpaper_community.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wallpaper_community.Login;
import com.example.wallpaper_community.MainActivityAdministrador;
import com.example.wallpaper_community.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Cambio_Pass extends AppCompatActivity {

    TextView PassActual;
    EditText ActualPassET, NuevoPassET;
    Button CAMBIARPASSBTN,IRINICIOBTN;

    DatabaseReference ADMINISTRADORES;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pass);

        Toolbar toolbarL = findViewById(R.id.toobarL);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Actualiza tu contraseña");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        PassActual = findViewById(R.id.PassActual);
        ActualPassET = findViewById(R.id.ActualPassET);
        NuevoPassET = findViewById(R.id.NuevoPassET);
        CAMBIARPASSBTN = findViewById(R.id.CAMBIARPASSBTN);
        IRINICIOBTN = findViewById(R.id.IRINICIOBTN);

        ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("ADMINISTRADORES");
        firebaseAuth =  FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(Cambio_Pass.this);


        //Consulta
        Query query = ADMINISTRADORES.orderByChild("CORREO").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){

                    String pass = ""+ds.child("CONTRASEÑA").getValue();
                    PassActual.setText(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CAMBIARPASSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ACTUAL_PASS = ActualPassET.getText().toString().trim();
                String NUEVO_PASS = NuevoPassET.getText().toString().trim();

                if(TextUtils.isEmpty(ACTUAL_PASS)){

                    Toast.makeText(Cambio_Pass.this, "Campo contraseña actual esta vacio", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (validarpassword(NUEVO_PASS)==false) {
                    Toast.makeText(Cambio_Pass.this, "La contraseña nueva tiene que tener numeros, letras y al menos un caracter especial", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(NUEVO_PASS)){

                    Toast.makeText(Cambio_Pass.this, "Campo contraseña nueva esta vacio", Toast.LENGTH_SHORT).show();
                    return;
                }if(!ACTUAL_PASS.equals("") && !NUEVO_PASS.equals("") && NUEVO_PASS.length()>=8){
                    Cambio_Password(ACTUAL_PASS,NUEVO_PASS);
                    return;
                }

                else{
                    NuevoPassET.setError("La contraseña debe ser mayor o igual a 8");
                    NuevoPassET.setFocusable(true);
                    return;
                }
            }
        });

        IRINICIOBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cambio_Pass.this,MainActivityAdministrador.class));

            }
        });
    }

    private void Cambio_Password(String pass_actual,String nuevo_pass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere porfavor");


        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),pass_actual);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(nuevo_pass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        String NUEVO_PASS = NuevoPassET.getText().toString().trim();
                                        HashMap<String,Object> resultado = new HashMap<>();
                                        resultado.put("CONTRASEÑA", NUEVO_PASS);
                                        //Actualizar Contraseña
                                        ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(Cambio_Pass.this, "Cambio exitoso", Toast.LENGTH_SHORT).show();
                                                        firebaseAuth.signOut();
                                                        startActivity(new Intent(Cambio_Pass.this, Login.class));
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean validarpassword(String mcontraseña) {
        boolean numeros = false;
        boolean letras = false;
        for(int x=0; x< mcontraseña.length();x++){
            char c = mcontraseña.charAt(x);

            if(((c>='a' && c<='z')||(c>='A' && c<='Z')|| c=='ñ' ||c=='Ñ'
                    ||c=='á'||c=='é'||c=='í'||c=='ó'||c=='ú'
                    ||c=='Á'||c=='É'||c=='Í'||c=='Ó'||c=='Ú')){
                letras = true;
            }
            if(c>='0' && c>='9'){
                numeros = true;
            }
        }
        if(numeros == true && letras==true){
            return  true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(Cambio_Pass.this, MainActivityAdministrador.class));
        finish();
        return super.onSupportNavigateUp();
    }
}