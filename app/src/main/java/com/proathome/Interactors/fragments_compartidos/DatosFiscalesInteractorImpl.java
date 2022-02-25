package com.proathome.Interactors.fragments_compartidos;

import android.content.Context;
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesInteractor;
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import org.json.JSONException;
import org.json.JSONObject;

public class DatosFiscalesInteractorImpl implements DatosFiscalesInteractor {

    private DatosFiscalesPresenter datosFiscalesPresenter;

    public DatosFiscalesInteractorImpl(DatosFiscalesPresenter datosFiscalesPresenter){
        this.datosFiscalesPresenter = datosFiscalesPresenter;
    }

    @Override
    public void getDatosFiscales(int tipoPerfil, int idUsuario, Context context) {
        String apiDatos = tipoPerfil == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.GET_DATOS_FISCALES_CLIENTE + idUsuario
                + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente() : APIEndPoints.GET_DATOS_FISCALES_PROFESIONAL +
                idUsuario + "/" + SharedPreferencesManager.getInstance(context).getTokenProfesional();

        datosFiscalesPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            datosFiscalesPresenter.hideProgress();
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject datos = jsonObject.getJSONObject("mensaje");
                        if(datos.getBoolean("existe"))
                            datosFiscalesPresenter.setInfoDatos(datos);
                    }else
                        datosFiscalesPresenter.showError(jsonObject.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    datosFiscalesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                datosFiscalesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, apiDatos, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void upDatosFiscales(int tipoPerfil, int idUsuario, String tipoPersona, String razonSocial, String rfc, String cfdi) {
        JSONObject jsonDatos = new JSONObject();
        try{
            if(tipoPerfil == Constants.TIPO_USUARIO_CLIENTE)
                jsonDatos.put("idCliente", idUsuario);
            else if(tipoPerfil == Constants.TIPO_USUARIO_PROFESIONAL)
                jsonDatos.put("idProfesional", idUsuario);
            jsonDatos.put("tipoPersona", tipoPersona);
            jsonDatos.put("razonSocial", razonSocial);
            jsonDatos.put("rfc", rfc);
            jsonDatos.put("cfdi", cfdi);

            datosFiscalesPresenter.showProgress();
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                datosFiscalesPresenter.hideProgress();
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("respuesta"))
                            datosFiscalesPresenter.updateSuccess(jsonObject.getString("mensaje"));
                        else
                            datosFiscalesPresenter.showError(jsonObject.getString("mensaje"));
                    }catch (JSONException ex){
                        ex.printStackTrace();
                        datosFiscalesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    datosFiscalesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, tipoPerfil == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.UPDATE_DATOS_FISCALES_CLIENTE : APIEndPoints.UPDATE_DATOS_FISCALES_PROFESIONAL, WebServicesAPI.POST, jsonDatos);
            webServicesAPI.execute();
        }catch (JSONException ex){
            ex.printStackTrace();
            datosFiscalesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

}
