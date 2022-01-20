package com.proathome.ui.password;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class CodigoEmail extends AppCompatActivity {

    private Unbinder mUnbinder;
    private String token, correo, urlApi;
    private ProgressDialog progressDialog;
    private int tipoPerfil;
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
        Intent intent = getIntent();
        this.token = intent.getStringExtra("token");
        this.correo = intent.getStringExtra("correo");
        this.tipoPerfil = intent.getIntExtra("tipoPerfil", 1);
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

        if(!d1S.trim().equals("") && !d2S.trim().equals("") && !d3S.trim().equals("") && !d4S.trim().equals("")){
            String codigo = d1S + d2S + d3S + d4S;

            if(this.tipoPerfil == Constants.TIPO_CLIENTE)
                this.urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?validar=true&correo=" + this.correo + "&codigo=" +  codigo + "&token=" + this.token;
            else if(this.tipoPerfil == Constants.TIPO_PROFESIONAL)
                this.urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?validarPro=true&correo=" + this.correo + "&codigo=" +  codigo + "&token=" + this.token;


            progressDialog = ProgressDialog.show(this, "Validando", "Espere, por favor...");
           WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
               progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if(jsonObject.getBoolean("respuesta")) {
                        if(jsonObject.getBoolean("valido")) {
                            Intent intent = new Intent(this, CambiarPassword.class);
                            intent.putExtra("tipoPerfil", this.tipoPerfil);
                            intent.putExtra("correo", this.correo);
                            intent.putExtra("token", this.token);
                            intent.putExtra("codigo", codigo);
                            startActivity(intent);
                            finish();
                        }else
                            SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                    }else
                        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, this.urlApi, WebServicesAPI.GET, null);
            enviarCodigo.execute();
        }else
            SweetAlert.showMsg(this,SweetAlert.WARNING_TYPE, "¡ESPERA!", "Ingresa el codigo completo.", false, null, null);
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