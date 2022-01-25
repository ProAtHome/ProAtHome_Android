package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.proathome.utils.SharedPreferencesManager;
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
    private ProgressDialog progressDialog;

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
        }else
            SweetAlert.showMsg(LoginProfesional.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos.", true, "OK", ()->{});

    }//Fin método entrar.

    private void login(String correo, String pass){
        progressDialog = ProgressDialog.show(LoginProfesional.this, "Iniciando Sesión", "Por favor, espere...");
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            progressDialog.dismiss();
            try{
                if(response == null){
                    SweetAlert.showMsg(LoginProfesional.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrió un error inesperado, intenta de nuevo.", false, null, null);
                }else {
                    if(!response.equals("null")){
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")) {
                            if(jsonObject.getBoolean("verificado")){
                                /*
                                AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(this,
                                        "sesionProfesional", null, 1);
                                SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                                ContentValues registro = new ContentValues();
                                registro.put("id", 1);
                                registro.put("idProfesional", jsonObject.getInt("idProfesional"));
                                registro.put("correo" , correo);
                                registro.put("rangoServicio", jsonObject.getInt("rangoServicio"));
                                baseDeDatos.insert("sesionProfesional", null, registro);
                                baseDeDatos.close();*/

                                //TODO PRUBAS SHARED PREFERENCES
                                SharedPreferencesManager.getInstance(this).logout();
                                SharedPreferencesManager.getInstance(this).guardarSesionProfesional(jsonObject.getInt("idProfesional"), correo, jsonObject.getInt("rangoServicio"), jsonObject.getString("token"));

                                startActivity(new Intent(this, InicioProfesional.class));
                            }else
                                SweetAlert.showMsg(LoginProfesional.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Aún no verificas tu cuenta de correo electrónico.", false, null, null);
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
                        SweetAlert.showMsg(LoginProfesional.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Usuario no registrado o tus datos están incorrectos.", false, null, null);
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.INICIAR_SESION_PROFESIONAL + correo + "/" + pass, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
