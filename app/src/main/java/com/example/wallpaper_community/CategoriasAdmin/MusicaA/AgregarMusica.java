package com.example.wallpaper_community.CategoriasAdmin.MusicaA;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wallpaper_community.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AgregarMusica extends AppCompatActivity {

    EditText NombreMusica;
    TextView VistaMusica;
    ImageView ImagenAgregarMusica;
    Button PublicarMusica;
    String RutaDeAlmacenamiento = "Musica_subida/";
    String RutaDeBaseDeDatos= "MUSICA";
    Uri RutaArchivosUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;

    String rNombre, rImagen, rVista;

    int CODIGO_DE_SOLICITUD_IMAGEN= 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_musica);

        Toolbar toolbarAP = findViewById(R.id.toobarAM);
        setSupportActionBar(toolbarAP);
        getSupportActionBar().setTitle("¨Publicar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NombreMusica = findViewById(R.id.NombreMusica);
        VistaMusica = findViewById(R.id.VistaMusica);
        ImagenAgregarMusica = findViewById(R.id.ImagenAgregarMusica);
        PublicarMusica = findViewById(R.id.PublicarMusica);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarMusica.this);

        Bundle intent = getIntent().getExtras();
        if(intent != null){
            //Recuperar Datos
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rVista = intent.getString("VistaEnviada");

            //Setear
            NombreMusica.setText(rNombre);
            VistaMusica.setText(rVista);
            Picasso.get().load(rImagen).into(ImagenAgregarMusica);

            //Cambiar Action
            getSupportActionBar().setTitle("Actualizar");
            String actualizar = "Actualizar";
            PublicarMusica.setText(actualizar);

        }


        ImagenAgregarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);

            }
        });

        PublicarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirImagen();
            }
        });
    }

    private void SubirImagen() {
        if(RutaArchivosUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen Musica...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtension(RutaArchivosUri));
            storageReference2.putFile(RutaArchivosUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            Uri donwnloadURI = uriTask.getResult();

                            String mNombre = NombreMusica.getText().toString();
                            String mVista = VistaMusica.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Musica musica = new Musica(donwnloadURI.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();
                            DatabaseReference.child(ID_IMAGEN).setValue(musica);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarMusica.this, "Subido Correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AgregarMusica.this, MusicaA.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarMusica.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressDialog.setTitle("Publicando..");
                            progressDialog.setCancelable(false);
                        }
                    });
        }
        else{
            Toast.makeText(this, "Debe Asignar una Imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private String ObtenerExtension(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODIGO_DE_SOLICITUD_IMAGEN
                && resultCode==RESULT_OK
                && data !=null
                && data.getData() != null){

            RutaArchivosUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivosUri);
                ImagenAgregarMusica.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}