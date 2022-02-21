package com.proathome.Interactors.cliente;

import android.view.View;
import com.proathome.Interfaces.cliente.Ayuda.AyudaInteractor;
import com.proathome.Interfaces.cliente.Ayuda.AyudaPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AyudaInteractorImpl implements AyudaInteractor {

    private AyudaPresenter ayudaPresenter;

    public AyudaInteractorImpl(AyudaPresenter ayudaPresenter){
        this.ayudaPresenter = ayudaPresenter;
    }

    @Override
    public void obtenerTickets(int idCliente, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONArray jsonArray = data.getJSONArray("mensaje");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getBoolean("sinTickets")){
                                ayudaPresenter.setVisibilityLottie(View.VISIBLE);
                            } else{
                                ayudaPresenter.setVisibilityLottie(View.INVISIBLE);
                                ayudaPresenter.setAdapterTickets(jsonObject);
                            }
                        }
                    }else
                        ayudaPresenter.showError("Error al obtener tickets.");
                }else
                    ayudaPresenter.showError("Ocurrió un error inseperado, intenta nuevamente.");
            }catch (JSONException ex){
                ex.printStackTrace();
                ayudaPresenter.showError("Ocurrió un error inseperado, intenta nuevamente.");
            }
        }, APIEndPoints.GET_TICKETS_CLIENTE + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
