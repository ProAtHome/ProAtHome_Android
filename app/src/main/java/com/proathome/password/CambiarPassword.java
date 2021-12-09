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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CambiarPassword extends AppCompatActivity {

    private Unbinder mUnbinder;
    private String correo, token, codigo, urlApi;
    private int tipoPerfil;
    @BindView(R.id.nuevaPassET_IS)
    TextInputEditText nuevaPass;
    @BindView(R.id.nuevaPassRepET_IS)
    TextInputEditText nuevaPassRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        this.correo = intent.getStringExtra("correo");
        this.token = intent.getStringExtra("token");
        this.codigo = intent.getStringExtra("codigo");
        this.tipoPerfil = intent.getIntExtra("tipoPerfil", 1);
        msgAlert("¡RECUERDA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.",
                SweetAlert.CLIENTE, SweetAlert.WARNING_TYPE);
    }

    @OnClick(R.id.guardarPassBTN)
    public void onClick(){
        String nueva = nuevaPass.getText().toString().trim();
        String nuevaRep = nuevaPassRep.getText().toString().trim();

        if(!nueva.equals("") && !nuevaRep.equals("")){
            //Validamos numero, minuscula, mayuscula,
            if(nueva.matches(".*\\d.*") && nueva.matches(".*[a-z].*") && nueva.matches(".*[A-Z].*") && nueva.length() >= 8){
                if(nueva.equals(nuevaRep)){
                    try{
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("contrasena1", nueva);
                        jsonObject.put("contrasena2", nuevaRep);
                        jsonObject.put("token", this.token);
                        jsonObject.put("correo", this.correo);
                        jsonObject.put("codigo", this.codigo);

                        if(this.tipoPerfil == Constants.TIPO_CLIENTE)
                            this.urlApi = Constants.IP_80 + "/assets/lib/Reestablecimiento.php?guardar=true";
                        else if(this.tipoPerfil == Constants.TIPO_PROFESIONAL)
                            this.urlApi = Constants.IP_80 + "/assets/lib/Reestablecimiento.php?guardarPro=true";

                        WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                            try{
                                JSONObject respuesta = new JSONObject(output);
                                if(respuesta.getBoolean("respuesta")){
                                    new SweetAlert(this, SweetAlert.SUCCESS_TYPE, SweetAlert.CLIENTE)
                                            .setTitleText("¡GENIAL!")
                                            .setContentText(respuesta.getString("mensaje"))
                                            .setConfirmButton("OK", listener ->{
                                                listener.dismiss();
                                                finish();
                                            }).show();
                                }else
                                    msgAlert("¡ERROR!", respuesta.getString("mensaje"), SweetAlert.CLIENTE, SweetAlert.ERROR_TYPE);
                            }catch (JSONException ex){
                                ex.printStackTrace();
                            }
                        }, this.urlApi, WebServicesAPI.PUT, jsonObject);
                        enviarCodigo.execute();
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    msgAlert("¡ERROR!", "Las contraseñas no coinciden.", SweetAlert.CLIENTE, SweetAlert.WARNING_TYPE);
            }else
                msgAlert("¡ESPERA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.", SweetAlert.CLIENTE, SweetAlert.WARNING_TYPE);
        }else
            msgAlert("¡ESPERA!", "Escribe tu nueva contraseña.", SweetAlert.CLIENTE, SweetAlert.WARNING_TYPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void msgAlert(String titulo, String mensaje, int tipoPerfil, int tipoMsg){
        new SweetAlert(this, tipoMsg, tipoPerfil)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }
}