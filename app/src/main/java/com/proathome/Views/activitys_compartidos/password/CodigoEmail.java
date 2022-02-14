package com.proathome.Views.activitys_compartidos.password;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailPresenter;
import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailView;
import com.proathome.Presenters.activitys_compartidos.CodigoEmailPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class CodigoEmail extends AppCompatActivity implements CodigoEmailView {

    private Unbinder mUnbinder;
    private String token, correo;
    private ProgressDialog progressDialog;
    private int tipoPerfil;
    private CodigoEmailPresenter codigoEmailPresenter;

    @BindView(R.id.d1ET_IS)
    TextInputEditText d1;
    @BindView(R.id.d2ET_IS)
    TextInputEditText d2;
    @BindView(R.id.d3ET_IS)
    TextInputEditText d3;
    @BindView(R.id.d4ET_IS)
    TextInputEditText d4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_email);
        mUnbinder = ButterKnife.bind(this);

        codigoEmailPresenter = new CodigoEmailPresenterImpl(this);

        token = getIntent().getStringExtra("token");
        correo = getIntent().getStringExtra("correo");
        tipoPerfil = getIntent().getIntExtra("tipoPerfil", 1);
    }

    @OnTextChanged(R.id.d1ET_IS)
    public void onTextChanged(){
        d2.requestFocus();
    }

    @OnTextChanged(R.id.d2ET_IS)
    public void onTextChanged2(){
        d3.requestFocus();
    }

    @OnTextChanged(R.id.d3ET_IS)
    public void onTextChanged3(){
        d4.requestFocus();
    }

    @OnClick(R.id.validarCodigoBTN)
    public void onClick(){
        String d1S = d1.getText().toString();
        String d2S = d2.getText().toString();
        String d3S = d3.getText().toString();
        String d4S = d4.getText().toString();

        codigoEmailPresenter.validarCodigo(d1S, d2S, d3S, d4S, tipoPerfil, correo, token);
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
    public void showError(String error) {
        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void successCode(String codigo) {
        Intent intent = new Intent(this, CambiarPassword.class);
        intent.putExtra("tipoPerfil", this.tipoPerfil);
        intent.putExtra("correo", this.correo);
        intent.putExtra("token", this.token);
        intent.putExtra("codigo", codigo);
        startActivity(intent);
        finish();
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