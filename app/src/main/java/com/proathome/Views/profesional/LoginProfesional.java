package com.proathome.Views.profesional;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.profesional.Login.LoginProfesionalPresenter;
import com.proathome.Interfaces.profesional.Login.LoginProfesionalView;
import com.proathome.Presenters.profesional.LoginProfesionalPresenterImpl;
import com.proathome.R;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Views.activitys_compartidos.PerfilBloqueado;
import com.proathome.Views.cliente.MainActivity;
import com.proathome.Views.activitys_compartidos.password.EmailPassword;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginProfesional extends AppCompatActivity implements LoginProfesionalView {

    @BindView(R.id.correoET_ISP)
    TextInputEditText correoET;
    @BindView(R.id.contraET_ISP)
    TextInputEditText contrasenaET;

    private Unbinder mUnbinder;
    private ProgressDialog progressDialog;
    private LoginProfesionalPresenter loginProfesionalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profesional);
        mUnbinder = ButterKnife.bind(this);

        loginProfesionalPresenter = new LoginProfesionalPresenterImpl(this);
    }

    @OnClick(R.id.tvOlvideContraPro)
    public void onClick(){
        Intent intent = new Intent(this, EmailPassword.class);
        intent.putExtra("tipoPerfil", Constants.TIPO_PROFESIONAL);
        startActivity(intent);
    }

    @OnClick(R.id.soyClienteBTN)
    public void soyCliente(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.registrarseBTN)
    public void registrarse(){
        startActivity(new Intent(this, RegistrarseProfesional.class));
        finish();
    }

    @OnClick(R.id.entrarBTNP)
    public void entrar(){
        if(NetworkValidate.validate(LoginProfesional.this)){
            if(!correoET.getText().toString().trim().equalsIgnoreCase("") &&
                    !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){
                String correo = String.valueOf(correoET.getText()).trim();
                String contrasena = String.valueOf(contrasenaET.getText()).trim();
                loginProfesionalPresenter.login(correo, contrasena);
            }else
                SweetAlert.showMsg(LoginProfesional.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos.", true, "OK", ()->{});
        }else
            Toast.makeText(LoginProfesional.this, "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();
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

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(LoginProfesional.this, "Iniciando Sesión", "Por favor, espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showErrorLogin(String error) {
        SweetAlert.showMsg(LoginProfesional.this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void toInicio(JSONObject jsonObject, String correo) {
        try {
            SharedPreferencesManager.getInstance(this).logout();
            SharedPreferencesManager.getInstance(this).guardarSesionProfesional(jsonObject.getInt("idProfesional"), correo, jsonObject.getInt("rangoServicio"), jsonObject.getString("token"));
            startActivity(new Intent(this, InicioProfesional.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toActivarCuenta() {
        startActivity(new Intent(this, PasosActivarCuenta.class));
    }

    @Override
    public void toBloquearPerfil() {
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_PROFESIONAL);
        Intent intent = new Intent(this, PerfilBloqueado.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
