package com.proathome.Interactors.profesional;

import android.view.View;
import com.proathome.Interfaces.profesional.Ayuda.AyudaInteractor;
import com.proathome.Interfaces.profesional.Ayuda.AyudaPresenter;
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
    public void obtenerTickets(int idProfesional, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
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
                        ayudaPresenter.showError(data.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    ayudaPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                ayudaPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_TICKETS_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
