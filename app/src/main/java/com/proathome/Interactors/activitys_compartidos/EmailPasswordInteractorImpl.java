package com.proathome.Interactors.activitys_compartidos;

import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordInteractor;
import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordPresenter;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPasswordInteractorImpl implements EmailPasswordInteractor {

    private EmailPasswordPresenter emailPasswordPresenter;
    private String urlApi;

    public EmailPasswordInteractorImpl(EmailPasswordPresenter emailPasswordPresenter){
        this.emailPasswordPresenter = emailPasswordPresenter;
    }

    @Override
    public void enviarCodigo(String correo, int tipoPerfil) {
        if(!correo.trim().equals("")) {
            Pattern pattern = Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher mather = pattern.matcher(correo);

            if(mather.find()) {
                if(tipoPerfil == Constants.TIPO_CLIENTE)
                    urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?getCodigo=true&correo=" + correo;
                else if(tipoPerfil == Constants.TIPO_PROFESIONAL)
                    urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?getCodigoPro=true&correo=" + correo;

                emailPasswordPresenter.showProgress();
                WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                    emailPasswordPresenter.hideProgress();
                    if(output != null){
                        try {
                            JSONObject jsonObject = new JSONObject(output);
                            if(jsonObject.getBoolean("respuesta")){
                                if(jsonObject.getBoolean("valido")){
                                    String token = jsonObject.getString("mensaje");
                                    emailPasswordPresenter.successEmail(token, correo, "Email enviado, revisa tus correos no deseados.");
                                }else
                                    emailPasswordPresenter.showError(jsonObject.getString("mensaje"));
                            }else
                                emailPasswordPresenter.showError(jsonObject.getString("mensaje"));
                        }catch (JSONException ex){
                            emailPasswordPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                            ex.printStackTrace();
                        }
                    }else
                        emailPasswordPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }, this.urlApi, WebServicesAPI.GET, null);
                enviarCodigo.execute();
            }else
                emailPasswordPresenter.showError("Ingresa un correo electronico válido.");
        }else
            emailPasswordPresenter.showError("Ingresa tu correo.");
    }

}
