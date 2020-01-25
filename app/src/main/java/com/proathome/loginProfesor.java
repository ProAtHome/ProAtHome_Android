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
import com.proathome.controladores.AdminSQLiteOpenHelperProfesor;
import com.proathome.controladores.ServicioTaskLoginEstudiante;
import com.proathome.controladores.ServicioTaskLoginProfesor;
import com.proathome.utils.Constants;

public class loginProfesor extends AppCompatActivity {

    private Intent intent;
    private TextInputEditText correoET, contraET;
    private final String iniciarSesionProfesorREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/sesionProfesor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profesor);
        correoET =  findViewById(R.id.correoET_ISP);
        contraET =  findViewById(R.id.contraET_ISP);

        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(this, "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT nombre, correo, foto FROM sesionProfesor WHERE id = " + "1", null);

        if(fila.moveToFirst()){

            intent = new Intent(this, inicioProfesor.class);
            startActivity(intent);
            baseDeDatos.close();
            finish();

        }else{

            baseDeDatos.close();

        }

    }

    public void soyCliente(View view){

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }//Fin método soyProfesor.

    public void registrarse(View view){

        intent = new Intent(this, registrarseProfesor.class);
        startActivity(intent);
        finish();

    }//Fin método registrarse.

    public void entrar(View view){

        if(!correoET.getText().toString().trim().equalsIgnoreCase("") && !contraET.getText().toString().trim().equalsIgnoreCase("")){

            String correo = String.valueOf(correoET.getText());
            String contrasena = String.valueOf(contraET.getText());

            if(correo.equals("admin") && contrasena.equals("admin")){

                intent = new Intent(this, inicioProfesor.class);
                startActivity(intent);

            }else{

                ServicioTaskLoginProfesor servicio = new ServicioTaskLoginProfesor(this, iniciarSesionProfesorREST, correo, contrasena);
                servicio.execute();

            }

        }else{

            Toast.makeText(this, "Llena todos los campos.", Toast.LENGTH_LONG).show();

        }

    }//Fin método entrar.

}
