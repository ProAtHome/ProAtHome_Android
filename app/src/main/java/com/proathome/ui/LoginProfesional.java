package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.ui.password.EmailPassword;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginProfesional extends AppCompatActivity {

    private Intent intent;
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
            login(correo, contrasena);
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

    private void login(String correo, String pass){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response == null){
                    errorMsg("Ocurrió un error inesperado, intenta de nuevo.");
                }else {
                    if(!response.equals("null")){
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")) {
                            if(jsonObject.getBoolean("verificado")){
                                AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(this,
                                        "sesionProfesional", null, 1);
                                SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                                ContentValues registro = new ContentValues();
                                registro.put("id", 1);
                                registro.put("idProfesional", jsonObject.getInt("idProfesional"));
                                registro.put("correo" , correo);
                                registro.put("rangoServicio", jsonObject.getInt("rangoServicio"));
                                baseDeDatos.insert("sesionProfesional", null, registro);
                                baseDeDatos.close();

                                startActivity(new Intent(this, InicioProfesional.class));
                            }else
                                errorMsg("Aún no verificas tu cuenta de correo electrónico.");
                        }else if(jsonObject.getString("estado").equalsIgnoreCase("documentacion") ||
                                jsonObject.getString("estado").equalsIgnoreCase("cita") ||
                                jsonObject.getString("estado").equalsIgnoreCase("registro")){
                                    startActivity(new Intent(this, PasosActivarCuenta.class));
                        }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO")){
                            Bundle bundle = new Bundle();
                            bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_PROFESIONAL);
                            Intent intent = new Intent(this, PerfilBloqueado.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }else
                        errorMsg("Usuario no registrado o tus datos están incorrectos.");
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.INICIAR_SESION_PROFESIONAL + correo + "/" + pass, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }


    public void errorMsg(String mensaje){
        new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
