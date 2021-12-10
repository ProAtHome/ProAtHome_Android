package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.ui.password.EmailPassword;
import com.proathome.servicios.profesional.ServicioTaskLoginProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginProfesional extends AppCompatActivity {

    private Intent intent;
    private final String iniciarSesionProfesionalREST = Constants.IP +
            "/ProAtHome/apiProAtHome/profesional/sesionProfesional";
    @BindView(R.id.correoET_ISP)
    TextInputEditText correoET;
    @BindView(R.id.contraET_ISP)
    TextInputEditText contrasenaET;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profesional);
        mUnbinder = ButterKnife.bind(this);

        /*
        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(this, "sesionProfesional", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            intent = new Intent(this, inicioProfesional.class);
            startActivity(intent);
            baseDeDatos.close();
            finish();

        }else{

            baseDeDatos.close();

        }*/

    }

    @OnClick(R.id.tvOlvideContraPro)
    public void onClick(){
        Intent intent = new Intent(this, EmailPassword.class);
        intent.putExtra("tipoPerfil", Constants.TIPO_PROFESIONAL);
        startActivity(intent);
    }

    public void soyCliente(View view){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }//Fin método soyProfesional.

    public void registrarse(View view){
        intent = new Intent(this, RegistrarseProfesional.class);
        startActivity(intent);
        finish();
    }//Fin método registrarse.

    public void entrar(View view){

        if(!correoET.getText().toString().trim().equalsIgnoreCase("") &&
                !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){
            String correo = String.valueOf(correoET.getText()).trim();
            String contrasena = String.valueOf(contrasenaET.getText()).trim();
            ServicioTaskLoginProfesional servicio = new ServicioTaskLoginProfesional(this, iniciarSesionProfesionalREST,
                    correo, contrasena);
            servicio.execute();
        }else{
            new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
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
