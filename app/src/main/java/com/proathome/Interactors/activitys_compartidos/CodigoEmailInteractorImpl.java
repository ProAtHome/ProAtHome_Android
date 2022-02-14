package com.proathome.Interactors.activitys_compartidos;

import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailInteractor;
import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailPresenter;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class CodigoEmailInteractorImpl implements CodigoEmailInteractor {

    private CodigoEmailPresenter codigoEmailPresenter;
    private String urlApi;

    public CodigoEmailInteractorImpl(CodigoEmailPresenter codigoEmailPresenter){
        this.codigoEmailPresenter = codigoEmailPresenter;
    }

    @Override
    public void validarCodigo(String d1, String d2, String d3, String d4, int tipoPerfil, String correo, String token) {
        if(!d1.trim().equals("") && !d2.trim().equals("") && !d3.trim().equals("") && !d4.trim().equals("")){
            String codigo = d1 + d2 + d3 + d4;

            if(tipoPerfil == Constants.TIPO_CLIENTE)
                urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?validar=true&correo=" + correo + "&codigo=" +  codigo + "&token=" + token;
            else if(tipoPerfil == Constants.TIPO_PROFESIONAL)
                urlApi = Constants.IP_80 + "/assets/lib/Restablecimiento.php?validarPro=true&correo=" + correo + "&codigo=" +  codigo + "&token=" + token;

            codigoEmailPresenter.showProgress();
            WebServicesAPI enviarCodigo = new WebServicesAPI(output -> {
                codigoEmailPresenter.hideProgress();
                if(output != null){
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        if(jsonObject.getBoolean("respuesta")) {
                            if(jsonObject.getBoolean("valido"))
                                codigoEmailPresenter.successCode(codigo);
                            else
                                codigoEmailPresenter.showError(jsonObject.getString("mensaje"));
                        }else
                            codigoEmailPresenter.showError(jsonObject.getString("mensaje"));
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        codigoEmailPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    codigoEmailPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, this.urlApi, WebServicesAPI.GET, null);
            enviarCodigo.execute();
        }else
            codigoEmailPresenter.showError("Ingresa el codigo completo.");
    }

}
