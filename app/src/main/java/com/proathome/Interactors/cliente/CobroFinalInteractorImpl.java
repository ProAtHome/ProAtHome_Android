package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.CobroFinal.CobroFinalInteractor;
import com.proathome.Interfaces.cliente.CobroFinal.CobroFinalPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONException;
import org.json.JSONObject;

public class CobroFinalInteractorImpl implements CobroFinalInteractor {

    private CobroFinalPresenter cobroFinalPresenter;

    public CobroFinalInteractorImpl(CobroFinalPresenter cobroFinalPresenter){
        this.cobroFinalPresenter = cobroFinalPresenter;
    }

    @Override
    public void getPreOrden(int idCliente, int idSesion, String token) {
        cobroFinalPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            cobroFinalPresenter.hideProgress();
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        cobroFinalPresenter.setDatosPreOrden(jsonObject);
                    }else
                        cobroFinalPresenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    cobroFinalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                cobroFinalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_PRE_ORDEN + idCliente + "/" + idSesion + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
