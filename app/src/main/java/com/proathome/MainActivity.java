package com.proathome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.servicios.ServicioTaskSQL;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.servicios.estudiante.ServicioTaskLoginEstudiante;
import com.proathome.servicios.profesor.AdminSQLiteOpenHelperProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private final String iniciarSesionREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/sesionCliente";
    private final String latidoSQL = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/admin/latidoSQL";
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

        ServicioTaskSQL servicioTaskSQL = new ServicioTaskSQL(this, this.latidoSQL);
        servicioTaskSQL.execute();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        baseDeDatos.delete("sesion", "id=1", null);
        baseDeDatos.close();

        AdminSQLiteOpenHelperProfesor admin2 = new AdminSQLiteOpenHelperProfesor(this, "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos2 = admin2.getWritableDatabase();
        baseDeDatos2.delete("sesionProfesor", "id=1", null);
        baseDeDatos2.close();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.ESTUDIANTE);
        }else {

            /*
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

            }*/

        }

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    public void soyProfesor(View view){
        intent = new Intent(this, loginProfesor.class);
        startActivity(intent);
        finish();
    }

    public void registrarse(View view){
        intent = new Intent(this, registrarseEstudiante.class);
        startActivity(intent);
        finish();
    }

    public void entrar(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.ESTUDIANTE);
        }else{
            if(!correoET.getText().toString().trim().equalsIgnoreCase("") && !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){
                String correo = String.valueOf(correoET.getText());
                String contrasena = String.valueOf(contrasenaET.getText());
                ServicioTaskLoginEstudiante servicio = new ServicioTaskLoginEstudiante(this, iniciarSesionREST, correo, contrasena);
                servicio.execute();
            }else{
                new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                        .setTitleText("¡ERROR!")
                        .setContentText("Llena todos los campos.")
                        .show();
            }
        }
    }//Fin método entrar.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
