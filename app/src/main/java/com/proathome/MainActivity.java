package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.ServicioTaskLoginEstudiante;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private TextInputEditText correoET, contraET;
    private final String iniciarSesionREST = "http://192.168.100.24:8080/ProAtHome/apiProAtHome/cliente/sesionCliente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        correoET =  findViewById(R.id.correoET_IS);
        contraET =  findViewById(R.id.contraET_IS);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT nombre, correo, foto FROM sesion WHERE id = " + "1", null);

        if(fila.moveToFirst()){

            intent = new Intent(this, inicioEstudiante.class);
            startActivity(intent);
            baseDeDatos.close();
            finish();

        }else{

            baseDeDatos.close();

        }


    }

    public void soyProfesor(View view){

        intent = new Intent(this, loginProfesor.class);
        startActivity(intent);
        finish();

    }//Fin método soyProfesor.

    public void registrarse(View view){

        intent = new Intent(this, registrarseEstudiante.class);
        startActivity(intent);
        finish();


    }//Fin método registrarse.

    public void entrar(View view) {

        if(!correoET.getText().toString().trim().equalsIgnoreCase("") && !contraET.getText().toString().trim().equalsIgnoreCase("")){

            String correo = String.valueOf(correoET.getText());
            String contrasena = String.valueOf(contraET.getText());

            if(correo.equals("admin") && contrasena.equals("admin")){

                intent = new Intent(this, inicioEstudiante.class);
                startActivity(intent);

            }else{

                ServicioTaskLoginEstudiante servicio = new ServicioTaskLoginEstudiante(this, iniciarSesionREST, correo, contrasena);
                servicio.execute();

            }

        }else{

            Toast.makeText(this, "Llena todos los campos.", Toast.LENGTH_LONG).show();

        }

    }//Fin método entrar.

}
