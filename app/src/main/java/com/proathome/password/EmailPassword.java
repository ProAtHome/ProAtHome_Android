package com.proathome.password;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

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
                    this.urlApi = Constants.IP_80 + "/assets/lib/Reestablecimiento.php?getCodigo=true&correo=" + correo;
                else if(this.tipoPerfil == Constants.TIPO_PROFESIONAL)
                    this.urlApi = Constants.IP_80 + "/assets/lib/Reestablecimiento.php?getCodigoPro=true&correo=" + correo;

                WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        if(jsonObject.getBoolean("respuesta")){
                            if(jsonObject.getBoolean("valido")){
                                String token = jsonObject.getString("mensaje");
                                System.out.println(token);
                                new SweetAlert(this, SweetAlert.SUCCESS_TYPE, SweetAlert.CLIENTE)
                                        .setTitleText("¡GENIAL!")
                                        .setContentText("Email enviado, revisa tus correos no deseados.")
                                        .setConfirmButton("OK", listener ->{
                                            Intent intent = new Intent(this, CodigoEmail.class);
                                            intent.putExtra("tipoPerfil", this.tipoPerfil);
                                            intent.putExtra("token", token);
                                            intent.putExtra("correo", correo);
                                            startActivity(intent);
                                            listener.dismiss();
                                            finish();
                                        })
                                        .show();
                            }else
                                msgAlert("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.CLIENTE, SweetAlert.ERROR_TYPE);
                        }else
                            msgAlert("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.CLIENTE, SweetAlert.ERROR_TYPE);
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }, this.urlApi, WebServicesAPI.GET, null);
                enviarCodigo.execute();
            }else
                msgAlert("¡ESPERA!", "Ingresa un correo electronico válido.", SweetAlert.CLIENTE, SweetAlert.WARNING_TYPE);
        }else
            msgAlert("¡ESPERA!", "Ingresa tu correo.", SweetAlert.CLIENTE, SweetAlert.WARNING_TYPE);

    }

    public void msgAlert(String titulo, String mensaje, int tipoPerfil, int tipoMsg){
        new SweetAlert(this, tipoMsg, tipoPerfil)
        .setTitleText(titulo)
        .setContentText(mensaje)
        .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}