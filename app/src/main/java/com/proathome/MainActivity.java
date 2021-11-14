package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.password.EmailPassword;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.profesor.AdminSQLiteOpenHelperProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        baseDeDatos.delete("sesion", "id=1", null);
        baseDeDatos.close();

        AdminSQLiteOpenHelperProfesor admin2 = new AdminSQLiteOpenHelperProfesor(this, "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos2 = admin2.getWritableDatabase();
        baseDeDatos2.delete("sesionProfesor", "id=1", null);
        baseDeDatos2.close();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.ESTUDIANTE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            Log.d("TAG1", "Latido SQL");
        }, APIEndPoints.LATIDO_SQL, this, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @OnClick({R.id.tvOlvideContra, R.id.soyProfesorBTN, R.id.registrarseBTN, R.id.entrarBTN})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvOlvideContra:
                tvOlvidePass();
                break;
            case R.id.soyProfesorBTN:
                soyProfesor();
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
        intent.putExtra("tipoPerfil", Constants.TIPO_ESTUDIANTE);
        startActivity(intent);
    }

    public void soyProfesor(){
        startActivity(new Intent(this, loginProfesor.class));
        finish();
    }

    public void registrarse(){
        startActivity(new Intent(this, RegistrarseEstudiante.class));
        finish();
    }

    public void entrar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.ESTUDIANTE);
        }else{
            if(!correoET.getText().toString().trim().equalsIgnoreCase("") && !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){
                String correo = String.valueOf(correoET.getText());
                String contrasena = String.valueOf(contrasenaET.getText());

                WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                    try{
                        if(response == null){
                            errorMsg("Ocurrió un error inesperado, intenta de nuevo.");
                        }else {
                            if(!response.equals("null")){
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println(jsonObject);
                                if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")){
                                    if(jsonObject.getBoolean("verificado")){
                                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion",
                                                null, 1);
                                        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                                        ContentValues registro = new ContentValues();
                                        registro.put("id", 1);
                                        registro.put("idEstudiante", jsonObject.getInt("idCliente"));
                                        registro.put("correo", correo);
                                        baseDeDatos.insert("sesion", null, registro);
                                        baseDeDatos.close();

                                        startActivity(new Intent(this, InicioEstudiante.class));
                                    }else
                                        errorMsg("Aún no verificas tu cuenta de correo electrónico.");
                                }else if(jsonObject.getString("estado").equalsIgnoreCase("DOCUMENTACION") ||
                                        jsonObject.getString("estado").equalsIgnoreCase("REGISTRO")){
                                    startActivity(new Intent(this, PasosActivarCuentaEstudiante.class));
                                }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO")){
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_ESTUDIANTE);
                                    Intent intent = new Intent(this, PerfilBloqueado.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }

                            }else{
                                errorMsg("Usuario no registrado o tus datos están incorrectos.");
                            }
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }, APIEndPoints.INICIAR_SESION_ESTUDIANTE + "/" + correo + "/" + contrasena, this, WebServicesAPI.GET, null);
                webServicesAPI.execute();
            }else{
                new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                        .setTitleText("¡ERROR!")
                        .setContentText("Llena todos los campos.")
                        .show();
            }
        }
    }//Fin método entrar.

    public void errorMsg(String mensaje){
        new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
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
