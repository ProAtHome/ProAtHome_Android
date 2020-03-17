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
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private final String iniciarSesionREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/sesionCliente";
    @BindView(R.id.correoET_IS)
    TextInputEditText correoET;
    @BindView(R.id.contraET_IS)
    TextInputEditText contrasenaET;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

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

        if(!correoET.getText().toString().trim().equalsIgnoreCase("") && !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){

            String correo = String.valueOf(correoET.getText());
            String contrasena = String.valueOf(contrasenaET.getText());

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

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mUnbinder.unbind();

    }
}
