package com.example.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE=1;
    public static Path path= new Path();
    public static Paint paint_brush =new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_abrirsitio1=findViewById(R.id.act1);
        Button btn_enviar=findViewById(R.id.enviar);
        Button btn_borrar=findViewById(R.id.borrar);
        SignaturePad singature= findViewById(R.id.firma);
        View besitos=findViewById(R.id.firma);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    Bitmap bitmap=Bitmap.createBitmap(besitos.getWidth(),besitos.getHeight(),Bitmap.Config.RGBA_F16);
                    Canvas canvas=new Canvas(bitmap);
                    besitos.draw(canvas);
                    File file = new File(Environment.getExternalStorageDirectory(),"/Download/signature.png");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                        ostream.flush();
                        ostream.close();
                        Toast.makeText(MainActivity.this,"Imagen guardada en "+file,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(getClass().getSimpleName(),"error",e);
                    }


                } else {
                    requestStoragePermission();
                }
            }
        });
        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singature.clear();
            }
        });
        btn_abrirsitio1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String sitio1="https://sites.google.com/view/empleadin/p%C3%A1gina-principal";
                try {
                    // Crea un Intent para abrir un navegador web externo
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sitio1));

                    // Verifica si hay aplicaciones que pueden manejar el Intent
                    if (intent.resolveActivity(getPackageManager()) == null)
                        Log.e("MiApp", "No se encontró una aplicación para manejar el Internet.");
                    else {
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    // Captura y registra cualquier excepción que pueda ocurrir
                    e.printStackTrace();
                    Log.e("MiApp", "Error al abrir el navegador: " + e.getMessage());
                }
            }
        });

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Si no aceptas, te mocha la cabeza el mencho 💀")
                    .setPositiveButton("Fierro viejo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);


                        }
                    })
                    .setNegativeButton("Vivo a mi modo 😘", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,"Concedido",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this,"Denegado",Toast.LENGTH_SHORT).show();

            }
        }
    }



}