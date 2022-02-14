package com.proathome.Interactors.activitys_compartidos;

import android.os.Build;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordInteractor;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordPresenter;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class CambiarPasswordInteractorImpl implements CambiarPasswordInteractor {

    private CambiarPasswordPresenter cambiarPasswordPresenter;
    private String urlApi;

    public CambiarPasswordInteractorImpl(CambiarPasswordPresenter cambiarPasswordPresenter){
        this.cambiarPasswordPresenter = cambiarPasswordPresenter;
    }

    @Override
    public void guardarPass(String nuevaPass, String nuevaPassRep, String token, String correo, String codigo, int tipoPerfil) {
        if(!nuevaPass.equals("") && !nuevaPassRep.equals("")){
            //Validamos numero, minuscula, mayuscula,
            if(nuevaPass.matches(".*\\d.*") && nuevaPass.matches(".*[a-z].*") && nuevaPass.matches(".*[A-Z].*") && nuevaPass.length() >= 8){
                if(nuevaPass.equals(nuevaPassRep)){
                    try{
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("contrasena1", nuevaPass);
                        jsonObject.put("contrasena2", nuevaPassRep);
                        jsonObject.put("token", token);
                        jsonObject.put("correo", correo);
                        jsonObject.put("codigo", codigo);

                        if(tipoPerfil == Constants.TIPO_CLIENTE)
                            urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?guardar=true";
                        else if(tipoPerfil == Constants.TIPO_PROFESIONAL)
                            urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?guardarPro=true";

                        cambiarPasswordPresenter.showProgress();
                        WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                            cambiarPasswordPresenter.hideProgress();
                            if(output != null){
                                try{
                                    JSONObject respuesta = new JSONObject(output);
                                    if(respuesta.getBoolean("respuesta"))
                                        informeCorreo(respuesta, correo);
                                    else
                                        cambiarPasswordPresenter.showError(respuesta.getString("mensaje"));
                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                }
                            }else
                                cambiarPasswordPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                        }, this.urlApi, WebServicesAPI.PUT, jsonObject);
                        enviarCodigo.execute();
                    }catch (JSONException ex){
                        ex.printStackTrace();
                        cambiarPasswordPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    cambiarPasswordPresenter.showError("Las contraseñas no coinciden.");
            }else
                cambiarPasswordPresenter.showError("La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.");
        }else
            cambiarPasswordPresenter.showError("Escribe tu nueva contraseña.");
    }

    private void informeCorreo(JSONObject respuesta, String correo) throws JSONException {
        JSONObject informe = new JSONObject();
        informe.put("titulo", "Reestablecimiento de Contraseña");
        informe.put("correo", correo);
        //OBTENEMOS INFO DE DISPOSITIVO
        String dispositivo = Build.DEVICE + " " + Build.MODEL + " " + Build.VERSION.BASE_OS;
        informe.put("dispositivo", dispositivo);
        //OBTENEMOS FECHA Y HORA
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        informe.put("datetime", currentDateTimeString);

        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            cambiarPasswordPresenter.successPassword(respuesta.getString("mensaje"));
        }, Constants.IP_80 + "/assets/lib/Restablecimiento.php?informeCorreo=true", WebServicesAPI.POST, informe);
        webServicesAPI.execute();
    }

}
