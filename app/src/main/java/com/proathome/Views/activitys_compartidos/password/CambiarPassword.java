package com.proathome.Views.activitys_compartidos.password;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordPresenter;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordView;
import com.proathome.Presenters.activitys_compartidos.CambiarPasswordPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CambiarPassword extends AppCompatActivity implements CambiarPasswordView {

    private Unbinder mUnbinder;
    private String correo, token, codigo;
    private ProgressDialog progressDialog;
    private int tipoPerfil;
    private CambiarPasswordPresenter cambiarPasswordPresenter;

    @BindView(R.id.nuevaPassET_IS)
    TextInputEditText nuevaPass;
    @BindView(R.id.nuevaPassRepET_IS)
    TextInputEditText nuevaPassRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);
        mUnbinder = ButterKnife.bind(this);

        cambiarPasswordPresenter = new CambiarPasswordPresenterImpl(this);

        Intent intent = getIntent();
        this.correo = intent.getStringExtra("correo");
        this.token = intent.getStringExtra("token");
        this.codigo = intent.getStringExtra("codigo");
        this.tipoPerfil = intent.getIntExtra("tipoPerfil", 1);
        SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡RECUERDA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.", false, null, null);
    }

    @OnClick(R.id.guardarPassBTN)
    public void onClick(){
        String nueva = nuevaPass.getText().toString().trim();
        String nuevaRep = nuevaPassRep.getText().toString().trim();

        cambiarPasswordPresenter.guardarPass(nueva, nuevaRep, token, correo, codigo, tipoPerfil);
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
        progressDialog = ProgressDialog.show(this, "Validando", "Espere, por favor...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String mensaje) {
        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
    }

    @Override
    public void successPassword(String mensaje) {
        SweetAlert.showMsg(this, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", mensaje, true, "OK", ()->{
            finish();
        });
    }

}