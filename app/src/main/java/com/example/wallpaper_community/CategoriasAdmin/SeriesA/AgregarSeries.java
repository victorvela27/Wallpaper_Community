package com.example.wallpaper_community.CategoriasAdmin.SeriesA;

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

public class AgregarSeries extends AppCompatActivity {

    EditText NombreSeries;
    TextView VistaSeries;
    ImageView ImagenAgregarSeries;
    Button PublicarSeries;
    String RutaDeAlmacenamiento = "Series_subida/";
    String RutaDeBaseDeDatos= "SERIES";
    Uri RutaArchivosUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;

    String rNombre, rImagen, rVista;

    int CODIGO_DE_SOLICITUD_IMAGEN= 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_series);

        Toolbar toolbarAP = findViewById(R.id.toobarAS);
        setSupportActionBar(toolbarAP);
        getSupportActionBar().setTitle("¨Publicar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NombreSeries = findViewById(R.id.NombreSeries);
        VistaSeries = findViewById(R.id.VistaSeries);
        ImagenAgregarSeries = findViewById(R.id.ImagenAgregarSeries);
        PublicarSeries = findViewById(R.id.PublicarSeries);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarSeries.this);

        Bundle intent = getIntent().getExtras();
        if(intent != null){
            //Recuperar Datos
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rVista = intent.getString("VistaEnviada");

            //Setear
            NombreSeries.setText(rNombre);
            VistaSeries.setText(rVista);
            Picasso.get().load(rImagen).into(ImagenAgregarSeries);

            //Cambiar Action
            getSupportActionBar().setTitle("Actualizar");
            String actualizar = "Actualizar";
            PublicarSeries.setText(actualizar);

        }


        ImagenAgregarSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);

            }
        });

        PublicarSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirImagen();
            }
        });


    }
    private void SubirImagen() {
        if(RutaArchivosUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen Series...");
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

                            String mNombre = NombreSeries.getText().toString();
                            String mVista = VistaSeries.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Series series = new Series(donwnloadURI.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();
                            DatabaseReference.child(ID_IMAGEN).setValue(series);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarSeries.this, "Subido Correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AgregarSeries.this, SeriesA.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarSeries.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                ImagenAgregarSeries.setImageBitmap(bitmap);
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