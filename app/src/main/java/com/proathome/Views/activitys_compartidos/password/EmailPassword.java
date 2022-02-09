package com.proathome.Views.activitys_compartidos.password;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmailPassword extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int tipoPerfil;
    private String urlApi;
    private ProgressDialog progressDialog;
    @BindView(R.id.correoCodigoET_IS)
    TextInputEditText correoCodigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        mUnbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        this.tipoPerfil = intent.getIntExtra("tipoPerfil", 1);
    }

    @OnClick(R.id.enviarCodigoBTN)
    public void enviarCodigo(){
        String correo = correoCodigo.getText().toString();
        if(!correo.trim().equals("")) {
            Pattern pattern = Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher mather = pattern.matcher(correo);

            if(mather.find()) {

                if(this.tipoPerfil == Constants.TIPO_CLIENTE)
                    this.urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?getCodigo=true&correo=" + correo;
                else if(this.tipoPerfil == Constants.TIPO_PROFESIONAL)
                    this.urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?getCodigoPro=true&correo=" + correo;

                progressDialog = ProgressDialog.show(this, "Enviando código", "Por favor, espere...");
                WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        if(jsonObject.getBoolean("respuesta")){
                            if(jsonObject.getBoolean("valido")){
                                String token = jsonObject.getString("mensaje");

                                SweetAlert.showMsg(this, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", "Email enviado, revisa tus correos no deseados.",
                                        true, "OK", ()->{
                                            Intent intent = new Intent(this, CodigoEmail.class);
                                            intent.putExtra("tipoPerfil", this.tipoPerfil);
                                            intent.putExtra("token", token);
                                            intent.putExtra("correo", correo.trim());
                                            startActivity(intent);
                                            finish();
                                        });
                            }else
                                SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                        }else
                            SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }, this.urlApi, WebServicesAPI.GET, null);
                enviarCodigo.execute();
            }else
                SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡ESPERA!", "Ingresa un correo electronico válido.", false, null, null);
        }else
            SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡ESPERA!", "Ingresa tu correo.", false, null, null);

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