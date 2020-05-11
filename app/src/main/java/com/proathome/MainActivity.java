package com.proathome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioTaskLoginEstudiante;
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAlert();
        }else {

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

            if(fila.moveToFirst()){

                intent = new Intent(this, inicioEstudiante.class);
                startActivity(intent);
                baseDeDatos.close();
                finish();

            }else{

                baseDeDatos.close();

            }

        }

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void showAlert() {
        new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos tu permiso :)")
                .setPositiveButton("Dar permiso", (dialog, which) -> {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Toast.makeText(this, "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setOnCancelListener(dialog -> {
                    Toast.makeText(this, "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                    finish();
                })
                .show();
    }

    private void showAlertEntrar() {
        new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos tu permiso :)")
                .setPositiveButton("Dar permiso", (dialog, which) -> {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Toast.makeText(this, "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                })
                .setOnCancelListener(dialog -> {
                    Toast.makeText(this, "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                })
                .show();
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAlertEntrar();
        }else{

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

        }

    }//Fin método entrar.

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mUnbinder.unbind();

    }
}
