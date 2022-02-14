package com.proathome.Views.activitys_compartidos.password;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordPresenter;
import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordView;
import com.proathome.Presenters.activitys_compartidos.EmailPasswordPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmailPassword extends AppCompatActivity implements EmailPasswordView {

    private Unbinder mUnbinder;
    private int tipoPerfil;
    private ProgressDialog progressDialog;
    private EmailPasswordPresenter emailPasswordPresenter;

    @BindView(R.id.correoCodigoET_IS)
    TextInputEditText correoCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        mUnbinder = ButterKnife.bind(this);

        emailPasswordPresenter = new EmailPasswordPresenterImpl(this);

        tipoPerfil = getIntent().getIntExtra("tipoPerfil", 1);
    }

    @OnClick(R.id.enviarCodigoBTN)
    public void enviarCodigo(){
        String correo = correoCodigo.getText().toString();
        emailPasswordPresenter.enviarCodigo(correo, tipoPerfil);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "Enviando código", "Por favor, espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void successEmail(String token, String correo, String mensaje) {
        SweetAlert.showMsg(this, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", mensaje,
                true, "OK", ()->{
                    Intent intent = new Intent(this, CodigoEmail.class);
                    intent.putExtra("tipoPerfil", tipoPerfil);
                    intent.putExtra("token", token);
                    intent.putExtra("correo", correo.trim());
                    startActivity(intent);
                    finish();
                });
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
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