package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.controladores.profesor.ServicioTaskLoginProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class loginProfesor extends AppCompatActivity {

    private Intent intent;
    private final String iniciarSesionProfesorREST = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/sesionProfesor";
    @BindView(R.id.correoET_ISP)
    TextInputEditText correoET;
    @BindView(R.id.contraET_ISP)
    TextInputEditText contrasenaET;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profesor);
        mUnbinder = ButterKnife.bind(this);

        /*
        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(this, "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            intent = new Intent(this, inicioProfesor.class);
            startActivity(intent);
            baseDeDatos.close();
            finish();

        }else{

            baseDeDatos.close();

        }*/

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

        if(!correoET.getText().toString().trim().equalsIgnoreCase("") &&
                !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){
            String correo = String.valueOf(correoET.getText());
            String contrasena = String.valueOf(contrasenaET.getText());
            ServicioTaskLoginProfesor servicio = new ServicioTaskLoginProfesor(this, iniciarSesionProfesorREST,
                    correo, contrasena);
            servicio.execute();
        }else{
            new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.PROFESOR)
                    .setTitleText("¡Error!")
                    .setConfirmButton("OK", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .setContentText("Llena todos los campos.")
                    .show();
        }

    }//Fin método entrar.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
