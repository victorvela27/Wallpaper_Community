package com.example.wallpaper_community.DetalleCliente;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wallpaper_community.R;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class DetalleCliente extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView NombreImagenDetalle, VistaDetalle;
    FloatingActionButton fabDescargar, fabCompartir, fabEstablecer;

    Bitmap bitmap;
    
    private static final int CODIGO_DE_ALMACENAMIENTO = 1;
    private static  final int CODIGO_DE_ALMACENAMIENTO11 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cliente);

        Toolbar toolbarL = findViewById(R.id.toobarL);
        setSupportActionBar(toolbarL);
        getSupportActionBar().setTitle("Detalle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        NombreImagenDetalle = findViewById(R.id.NombreImagenDetalle);
        VistaDetalle = findViewById(R.id.VistaDetalle);

        fabDescargar = findViewById(R.id.fabDescargar);
        fabCompartir = findViewById(R.id.fabCompartir);
        fabEstablecer = findViewById(R.id.fabEstablecer);

        String imagen = getIntent().getStringExtra("Imagen");
        String Nombre = getIntent().getStringExtra("Nombre");
        String Vista = getIntent().getStringExtra("Vista");


        try{
            Picasso.get().load(imagen).placeholder(R.drawable.detalle_imagen).into(ImagenDetalle);
        }catch (Exception e){
            Picasso.get().load(R.drawable.detalle_imagen).into(ImagenDetalle);
        }

        NombreImagenDetalle.setText(Nombre);
        VistaDetalle.setText(Vista);

        fabDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED) {
                            String [] permiso = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permiso,CODIGO_DE_ALMACENAMIENTO11);
                    }else{
                        DescargarImagenes_11();
                    }
                }
               else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                        String [] permisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permisos,CODIGO_DE_ALMACENAMIENTO);
                   }else{
                        DescargarImagenes();
                    }
                }
                else{
                    DescargarImagenes();
                }
            }
        });

        fabCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CompartirImagen();
            }
        });

        fabEstablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EstablecerImagen();
            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

   private void DescargarImagenes_11() {
        bitmap = ((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
        OutputStream fos;
        String nombre_imagen = NombreImagenDetalle.getText().toString();
       String FechadeDescarga = new SimpleDateFormat("yyyy_MM_dd '_' HH:mm:ss",
               Locale.getDefault()).format(System.currentTimeMillis());


        try{
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,nombre_imagen + "_" + FechadeDescarga);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+
                    File.separator+"/Wallpaper Community/");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Objects.requireNonNull(fos);
            Toast.makeText(this, "Descarga Exitosa", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            Toast.makeText(this, "Error de descarga", Toast.LENGTH_SHORT).show();
        }

    }

    private void DescargarImagenes(){
      bitmap = ((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
      String FechadeDescarga = new SimpleDateFormat("'Fecha Descarga: ' yyyy_MM_dd 'Hora: ' HH:mm:ss",
              Locale.getDefault()).format(System.currentTimeMillis());

      File ruta = Environment.getExternalStorageDirectory();
      File NombreCarpeta = new File(ruta +"/Wallpaper Community/");
     NombreCarpeta.mkdir();

      String ObtenerNombreImagen = NombreImagenDetalle.getText().toString();
      String NombreImagen = ObtenerNombreImagen + " " + FechadeDescarga + ".JPEG";
      File file = new File(NombreCarpeta,NombreImagen);
        OutputStream outputStream;
        try{
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Descargada con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void CompartirImagen(){

        try {
            bitmap = ((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
            String NombreImagen = NombreImagenDetalle.getText().toString();

            File file = new File(getExternalCacheDir(),NombreImagen+".JPEG");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true,true);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
            intent.setType("image/jpeg");
            startActivity(Intent.createChooser(intent,"Compartir"));

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void EstablecerImagen(){

        bitmap = ((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());


        try{
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "Establecido con Exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==CODIGO_DE_ALMACENAMIENTO){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                DescargarImagenes();
            }else{
                Toast.makeText(this, "Active los permisos de almacenamiento", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == CODIGO_DE_ALMACENAMIENTO11){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                DescargarImagenes_11();
            }
            else{
                Toast.makeText(this, "Active los permisos", Toast.LENGTH_SHORT).show();
            }
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}