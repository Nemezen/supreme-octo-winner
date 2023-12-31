package com.example.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE=1;
    public static Path path= new Path();
    public static Paint paint_brush =new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText nombre=findViewById(R.id.editText);
        EditText empleado=findViewById(R.id.editText2);
        Spinner puesto=findViewById(R.id.spinner);
        Button btn_enviar=findViewById(R.id.enviar);
        Button btn_borrar=findViewById(R.id.borrar);
        SignaturePad singature= findViewById(R.id.firma);
        View vista=findViewById(R.id.firma);
        Spinner puestos=findViewById(R.id.spinner);
        String objetos[]= new String[]{"Seleccione","Contador","Cocinero","Recursos Humanos","Sistemas","Administrativo","Marketing"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, objetos);
        puestos.setAdapter(adapter);
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                    && (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
                    String nombre_empleado= String.valueOf(nombre.getText());
                    String no_empleado= String.valueOf(empleado.getText());
                    String puesto_empleado= puesto.getSelectedItem().toString();
                    System.out.println(no_empleado);
                    System.out.println(nombre_empleado);
                    System.out.println(puesto_empleado);
                    if (!singature.isEmpty() && !nombre_empleado.isEmpty() && !no_empleado.isEmpty())
                    {

                        Bitmap bitmap=Bitmap.createBitmap(vista.getWidth(),vista.getHeight(),Bitmap.Config.RGBA_F16);
                        Canvas canvas=new Canvas(bitmap);
                        vista.draw(canvas);
                        File file = new File(Environment.getExternalStorageDirectory(),"/Download/"+no_empleado+".png");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.flush();
                            ostream.close();
                            Toast.makeText(MainActivity.this,"Imagen guardada en "+file,Toast.LENGTH_SHORT).show();
                            singature.clearView();
                            System.out.println(file.toString());
                            enviar(nombre_empleado,puesto_empleado,no_empleado);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(getClass().getSimpleName(),"error",e);
                        }
                    }else Toast.makeText(MainActivity.this,"Favor de llenar todos los campos.",Toast.LENGTH_LONG).show();
                } else if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)){
                    requestReadPermission();
                } else requestWritePermission();
            }
        });
        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                singature.clear();
                nombre.setText("");
                empleado.setText("");
                puestos.setSelection(0);
            }
        });
    }



    private void enviar(String nombre, String puesto, String empleado) throws IOException {
        /*File file= new File(Environment.getExternalStorageDirectory(),"/Download/"+empleado+".png");
        String fileName = file.getAbsolutePath();
        Bitmap original = BitmapFactory.decodeStream(getAssets().open(String.valueOf(file)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.PNG, 100, out);
        String foto=original.toString();*/
        String url="https://script.google.com/macros/s/AKfycbwj-mR9s_2EBAYWCIzu9j9VckAXQ2ctAcbLWd_3w5ZbFo23w4upxQd0HKyevoFiaHCs9Q/exec";
        url=url+"action=create&nombre="+nombre+"&puesto="+puesto+"&noempleado="+empleado+"&firma";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(stringRequest);


    }
    private void requestReadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Si no aceptas, te mocha la cabeza el mencho 💀")
                    .setPositiveButton("Fierro viejo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    private void requestWritePermission() {
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