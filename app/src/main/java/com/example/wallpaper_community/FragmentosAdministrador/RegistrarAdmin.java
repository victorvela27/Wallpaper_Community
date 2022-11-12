package com.example.wallpaper_community.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wallpaper_community.MainActivityAdministrador;
import com.example.wallpaper_community.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class RegistrarAdmin extends Fragment {

    TextView FechadeRegistro;
    EditText Correo, Contraseña,Nombres, Apellidos, Edad;
    Button Registrar;

    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_admin, container, false);

        FechadeRegistro = view.findViewById(R.id.FechadeRegistro);
        Correo = view.findViewById(R.id.Correo);
        Contraseña = view.findViewById(R.id.Contraseña);
        Nombres = view.findViewById(R.id.Nombres);
        Apellidos = view.findViewById(R.id.Apellidos);
        Edad = view.findViewById(R.id.Edad);

        Registrar = view.findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String Sfecha = fecha.format(date);
        FechadeRegistro.setText(Sfecha);

        //Boton

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString();
                String contraseña = Contraseña.getText().toString();
                String nombres = Nombres.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String edad = Edad.getText().toString();

                if (correo.equals("") || contraseña.equals("") || nombres.equals("") ||
                        apellidos.equals("") || edad.equals("")) {
                    Toast.makeText(getActivity(), "Favor de llenar los datos completos",
                            Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {

                        Correo.setError("Correo Invalido");
                        Correo.setFocusable(true);
                        return;

                    } else if (contraseña.length() < 8) {

                        Contraseña.setError("Contraseña debe ser mayor o igual a 8 digitos");
                        Contraseña.setFocusable(true);
                        return;

                    } if (validarpassword(contraseña)==false){
                        Toast.makeText(getActivity(), "La contraseña tiene que tener numeros, letras y al menos un caracter especial", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        RegistroAdministradores(correo, contraseña);
                        return;
                    }
                }
            }
            });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, por favor espere");
        progressDialog.setCancelable(false);

        return view;
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

    //metodo admin
    private void RegistroAdministradores(String correo, String contraseña) {

        progressDialog.show();
        auth.createUserWithEmailAndPassword(correo,contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;

                            //Cadena
                            String UID = user.getUid();
                            String correo = Correo.getText().toString();
                            String contraseña = Contraseña.getText().toString();
                            String nombre = Nombres.getText().toString();
                            String apellidos = Apellidos.getText().toString();
                            String edad = Edad.getText().toString();
                            int EdadInt = Integer.parseInt(edad);


                            HashMap<Object,Object> Administradores = new HashMap<>();

                            Administradores.put("UID",UID);
                            Administradores.put("CORREO",correo);
                            Administradores.put("CONTRASEÑA",contraseña);
                            Administradores.put("NOMBRES",nombre);
                            Administradores.put("APELLIDOS",apellidos);
                            Administradores.put("EDAD",EdadInt);
                            Administradores.put("IMAGEN","");


                            //FIREDATEBASE

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("ADMINISTRADORES");
                            reference.child(UID).setValue(Administradores);
                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
