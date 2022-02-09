package com.proathome.Views.activitys_compartidos.password;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CambiarPassword extends AppCompatActivity {

    private Unbinder mUnbinder;
    private String correo, token, codigo, urlApi;
    private ProgressDialog progressDialog;
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
        SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡RECUERDA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.", false, null, null);
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
                            this.urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?guardar=true";
                        else if(this.tipoPerfil == Constants.TIPO_PROFESIONAL)
                            this.urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?guardarPro=true";

                        progressDialog = ProgressDialog.show(this, "Validando", "Espere, por favor...");
                        WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                            progressDialog.dismiss();
                            try{
                                JSONObject respuesta = new JSONObject(output);
                                if(respuesta.getBoolean("respuesta"))
                                    informeCorreo(respuesta);
                                else
                                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", respuesta.getString("mensaje"), false, null, null);
                            }catch (JSONException ex){
                                ex.printStackTrace();
                            }
                        }, this.urlApi, WebServicesAPI.PUT, jsonObject);
                        enviarCodigo.execute();
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡ERROR!", "Las contraseñas no coinciden.", false, null, null);
            }else
                SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡ESPERA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.", false, null, null);
        }else
            SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡ESPERA!", "Escribe tu nueva contraseña.", false, null, null);
    }

    private void informeCorreo(JSONObject respuesta) throws JSONException {
        JSONObject informe = new JSONObject();
        informe.put("titulo", "Reestablecimiento de Contraseña");
        informe.put("correo", this.correo);
        //OBTENEMOS INFO DE DISPOSITIVO
        String dispositivo = Build.DEVICE + " " + Build.MODEL + " " + Build.VERSION.BASE_OS;
        informe.put("dispositivo", dispositivo);
        //OBTENEMOS FECHA Y HORA
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        informe.put("datetime", currentDateTimeString);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            SweetAlert.showMsg(this, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", respuesta.getString("mensaje"), true, "OK", ()->{
                finish();
            });
        }, Constants.IP_80 + "/assets/lib/Restablecimiento.php?informeCorreo=true", WebServicesAPI.POST, informe);
        webServicesAPI.execute();
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