package com.proathome.password;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.password.ServicioEnviarCodigo;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CodigoEmail extends AppCompatActivity {

    private Unbinder mUnbinder;
    private String token, correo, urlApi;
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

    @OnClick(R.id.validarCodigoBTN)
    public void onClick(){
        String d1S = d1.getText().toString();
        String d2S = d2.getText().toString();
        String d3S = d3.getText().toString();
        String d4S = d4.getText().toString();

        if(!d1S.trim().equals("") && !d2S.trim().equals("") && !d3S.trim().equals("") && !d4S.trim().equals("")){
            String codigo = d1S + d2S + d3S + d4S;

            if(this.tipoPerfil == Constants.TIPO_ESTUDIANTE)
                this.urlApi = Constants.IP_80 + "/assets/lib/Reestablecimiento.php?validar=true&correo=" + this.correo + "&codigo=" +  codigo + "&token=" + this.token;
            else if(this.tipoPerfil == Constants.TIPO_PROFESOR)
                this.urlApi = Constants.IP_80 + "/assets/lib/Reestablecimiento.php?validarPro=true&correo=" + this.correo + "&codigo=" +  codigo + "&token=" + this.token;

            ServicioEnviarCodigo enviarCodigo = new ServicioEnviarCodigo(output -> {
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
                            msgAlert("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ESTUDIANTE, SweetAlert.ERROR_TYPE);
                    }else
                        msgAlert("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ESTUDIANTE, SweetAlert.ERROR_TYPE);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, this.urlApi, this, ServicioEnviarCodigo.GET, null);
            enviarCodigo.execute();
        }else
            msgAlert("¡ESPERA!", "Ingresa el codigo completo.", SweetAlert.ESTUDIANTE, SweetAlert.WARNING_TYPE);
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