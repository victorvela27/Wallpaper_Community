package com.example.wallpaper_community.FragmentosAdministrador;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.wallpaper_community.MainActivityAdministrador;
import com.example.wallpaper_community.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class PerfilAdmin extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference ADMINISTRADORES;

    StorageReference storageReference;
    String RutaDeAlmacenamiento = "Foto_Perfil_Administradores/*";


    private Uri imagen_uri;
    private String imagen_perfil;
    private ProgressDialog progressDialog;
    ImageView FOTOPERFILIMG;
    TextView UDIPerfil, NOMBRESPERFIL, APELLIDOS, CORREO, PASSWORD, EDAD;
    Button ACTUALIZARPASS, ACTUALIZARDATOS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        FOTOPERFILIMG = view.findViewById(R.id.FOTOPERFILIMG);
        UDIPerfil = view.findViewById(R.id.UDIPerfil);
        NOMBRESPERFIL = view.findViewById(R.id.NOMBRESPERFIL);
        APELLIDOS = view.findViewById(R.id.APELLIDOS);
        CORREO = view.findViewById(R.id.CORREO);
        PASSWORD = view.findViewById(R.id.PASSWORD);
        EDAD = view.findViewById(R.id.EDAD);

        ACTUALIZARPASS = view.findViewById(R.id.ACTUALIZARPASS);
        ACTUALIZARDATOS = view.findViewById(R.id.ACTUALIZARDATOS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        progressDialog = new ProgressDialog(getActivity());


        ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("ADMINISTRADORES");
        ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uid = "" + snapshot.child("UID").getValue();
                    String nombre = "" + snapshot.child("NOMBRES").getValue();
                    String apellidos = "" + snapshot.child("APELLIDOS").getValue();
                    String correo = "" + snapshot.child("CORREO").getValue();
                    String contraseña = "" + snapshot.child("CONTRASEÑA").getValue();
                    String edad = "" + snapshot.child("EDAD").getValue();
                    String imagen = "" + snapshot.child("IMAGEN").getValue();

                    UDIPerfil.setText(uid);
                    NOMBRESPERFIL.setText(nombre);
                    APELLIDOS.setText(apellidos);
                    CORREO.setText(correo);
                    PASSWORD.setText(contraseña);
                    EDAD.setText(edad);

                    try {

                        Picasso.get().load(imagen).placeholder(R.drawable.perfil).into(FOTOPERFILIMG);

                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.perfil).into(FOTOPERFILIMG);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ACTUALIZARPASS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Cambio_Pass.class));
                getActivity().finish();
            }
        });

        ACTUALIZARDATOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarDatos();
            }
        });

        FOTOPERFILIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarImagenPerfilAdminsitrador();
            }
        });


        return view;
    }

    private void EditarDatos() {

        String [] opciones = {"Editar nombres","Editar apellidos","Editar edad"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elegir Opción: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0){
                    EditarNombres();
                }else if(i == 1){
                    EditarApellidos();
                }else if(i== 2){
                    EditarEdad();
                }
            }
        });
        builder.create().show();


    }

    private void EditarNombres() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actulizar Información: ");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato..");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoDato = editText.getText().toString().trim();
                if(!nuevoDato.equals("")){
                   HashMap<String,Object> resultado = new HashMap<>();
                   resultado.put("NOMBRES",nuevoDato);
                   ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(getActivity(), "Dato Actualizado", Toast.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });
                }else{
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void EditarApellidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actulizar Información: ");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato..");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoDato = editText.getText().toString().trim();
                if(!nuevoDato.equals("")){
                    HashMap<String,Object> resultado = new HashMap<>();
                    resultado.put("APELLIDOS",nuevoDato);
                    ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato Actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();

    }


    private void EditarEdad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actulizar Información: ");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato..");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoDato = editText.getText().toString().trim();
                if(!nuevoDato.equals("")){
                    int nuevoDatoentero = Integer.parseInt(nuevoDato);
                    HashMap<String,Object> resultado = new HashMap<>();
                    resultado.put("EDAD",nuevoDatoentero);
                    ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato Actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();

    }


    private void CambiarImagenPerfilAdminsitrador() {
        String[] opcion = {"Cambiar Foto de perfil"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elegir una opción");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    imagen_perfil = "IMAGEN";
                    ElegirFoto();
                }
            }
        });
        builder.create().show();
    }



    private void ElegirFoto() {
        String [] opciones = {"Cámara", "Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar imagen de:");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0){
                    if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED){
                        Elegirdecamara();
                    }else{
                        Solicitud_permiso_camara.launch(Manifest.permission.CAMERA);
                    }
                }else if (i == 1){

                    if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED){
                        Elegir_de_galeria();
                    }else{
                        Solicitud_permiso_camara.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }

                }
            }
        });
        builder.create().show();

    }

    private void Elegir_de_galeria(){
        Intent GaleriaIntent = new Intent(Intent.ACTION_PICK);
        GaleriaIntent.setType("image/*");
        Obtener_imagen_galeria.launch(GaleriaIntent);

    }

    private void Elegirdecamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Descripción Temporal");
        imagen_uri = (Objects.requireNonNull(getActivity())).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imagen_uri);
        Obtener_imagen_camara.launch(camaraIntent);

    }

    private ActivityResultLauncher<Intent> Obtener_imagen_camara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        ActualizarImagenBD(imagen_uri);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se esta cambiando,espere por favor");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }else{
                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> Obtener_imagen_galeria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        imagen_uri = data.getData();
                        ActualizarImagenBD(imagen_uri);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se esta cambiando,espere por favor");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }else{
                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<String>Solicitud_permiso_camara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted ->{
                if(isGranted){
                    Elegirdecamara();
                }else{
                    Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String>Solicitud_permiso_galeria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted ->{
                if(isGranted){
                    Elegir_de_galeria();
                }else{
                    Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });
    private void ActualizarImagenBD(Uri uri){
        String Ruta_de_archivo = RutaDeAlmacenamiento + "" + imagen_perfil + "_" + user.getUid();
        StorageReference storageReference2 = storageReference.child(Ruta_de_archivo);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){
                            HashMap<String , Object> results = new HashMap<>();
                            results.put(imagen_perfil,downloadUri.toString());
                            ADMINISTRADORES.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                            getActivity().finish();
                                            Toast.makeText(getActivity(), "Imagen Cambiada con exito", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage()
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }



}