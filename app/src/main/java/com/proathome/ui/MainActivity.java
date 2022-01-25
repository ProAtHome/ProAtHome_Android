package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.ui.password.EmailPassword;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.correoET_IS)
    TextInputEditText correoET;
    @BindView(R.id.contraET_IS)
    TextInputEditText contrasenaET;
    private Unbinder mUnbinder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.CLIENTE);
        }else{
            //EVALUAR SESION EXISTENTE DE CLIENTE
            if(SharedPreferencesManager.getInstance(this).getIDCliente() != 0){
                startActivity(new Intent(this, InicioCliente.class));
                finish();
            }else if(SharedPreferencesManager.getInstance(this).getIDProfesional() != 0){
                startActivity(new Intent(this, InicioProfesional.class));
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            Log.d("TAG1", "Latido SQL");
        }, APIEndPoints.LATIDO_SQL, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @OnClick({R.id.tvOlvideContra, R.id.soyProfesionalBTN, R.id.registrarseBTN, R.id.entrarBTN})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvOlvideContra:
                tvOlvidePass();
                break;
            case R.id.soyProfesionalBTN:
                soyProfesional();
                break;
            case R.id.registrarseBTN:
                registrarse();
                break;
            case R.id.entrarBTN:
                entrar();
                break;
        }
    }

    public void tvOlvidePass(){
        Intent intent = new Intent(this, EmailPassword.class);
        intent.putExtra("tipoPerfil", Constants.TIPO_CLIENTE);
        startActivity(intent);
    }

    public void soyProfesional(){
        startActivity(new Intent(this, LoginProfesional.class));
        finish();
    }

    public void registrarse(){
        startActivity(new Intent(this, RegistrarseCliente.class));
        finish();
    }

    public void entrar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.CLIENTE);
        }else{
            if(!correoET.getText().toString().trim().equalsIgnoreCase("") && !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){
                String correo = correoET.getText().toString().trim();
                String contrasena = contrasenaET.getText().toString().trim();

                progressDialog = ProgressDialog.show(MainActivity.this, "Iniciando Sesión", "Por favor, espere ...");
                WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                    progressDialog.dismiss();
                    try{
                        if(response == null){
                            SweetAlert.showMsg(MainActivity.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrió un error inesperado, intenta de nuevo.", false, null, null);
                        }else {
                            if(!response.equals("null")){
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println(jsonObject);
                                if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")){
                                    if(jsonObject.getBoolean("verificado")){
                                        /*
                                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion",
                                                null, 1);
                                        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                                        ContentValues registro = new ContentValues();
                                        registro.put("id", 1);
                                        registro.put("idCliente", jsonObject.getInt("idCliente"));
                                        registro.put("correo", correo);
                                        baseDeDatos.insert("sesion", null, registro);
                                        baseDeDatos.close();*/

                                        //TODO PRUBAS SHARED PREFERENCES
                                        SharedPreferencesManager.getInstance(this).logout();
                                        SharedPreferencesManager.getInstance(this).guardarSesionCliente(jsonObject.getInt("idCliente"), correo, jsonObject.getString("token"));

                                        startActivity(new Intent(this, InicioCliente.class));
                                    }else
                                        SweetAlert.showMsg(MainActivity.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Aún no verificas tu cuenta de correo electrónico.", false, null, null);
                                }else if(jsonObject.getString("estado").equalsIgnoreCase("DOCUMENTACION") ||
                                        jsonObject.getString("estado").equalsIgnoreCase("REGISTRO")){
                                    startActivity(new Intent(this, PasosActivarCuentaCliente.class));
                                }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO")){
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_CLIENTE);
                                    Intent intent = new Intent(this, PerfilBloqueado.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }

                            }else
                                SweetAlert.showMsg(MainActivity.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Usuario no registrado o tus datos están incorrectos.", false, null, null);
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }, APIEndPoints.INICIAR_SESION_CLIENTE + "/" + correo + "/" + contrasena, WebServicesAPI.GET, null);
                webServicesAPI.execute();
            }else
                SweetAlert.showMsg(MainActivity.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos.", false, null, null);
        }
    }//Fin método entrar.

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
