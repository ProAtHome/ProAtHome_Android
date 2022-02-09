package com.proathome.Views.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityPresenter;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityView;
import com.proathome.Presenters.cliente.MainActivityPresenterImpl;
import com.proathome.R;
import com.proathome.Views.profesional.LoginProfesional;
import com.proathome.Views.activitys_compartidos.password.EmailPassword;
import com.proathome.Utils.Constants;
import com.proathome.Utils.PermisosUbicacion;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    @BindView(R.id.correoET_IS)
    TextInputEditText correoET;
    @BindView(R.id.contraET_IS)
    TextInputEditText contrasenaET;

    private Unbinder mUnbinder;
    private ProgressDialog progressDialog;
    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mainActivityPresenter = new MainActivityPresenterImpl(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            PermisosUbicacion.showAlert(this, MainActivity.this, SweetAlert.CLIENTE);
        else
            mainActivityPresenter.sesionExistente(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityPresenter.latidoSQL();
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

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(MainActivity.this, "Iniciando Sesión", "Por favor, espere ...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showErrorLogin(String error) {
        SweetAlert.showMsg(MainActivity.this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void finishActivity() {
        finish();
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
                mainActivityPresenter.login(correo, contrasena, MainActivity.this);
            }else
                SweetAlert.showMsg(MainActivity.this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos.", false, null, null);
        }
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
