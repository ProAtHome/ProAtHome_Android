package com.proathome.Interactors.profesional;

import android.view.View;
import com.proathome.Interfaces.profesional.Sesiones.SesionesInteractor;
import com.proathome.Interfaces.profesional.Sesiones.SesionesPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SesionesInteractorImpl implements SesionesInteractor {

    private SesionesPresenter sesionesPresenter;

    public SesionesInteractorImpl(SesionesPresenter sesionesPresenter){
        this.sesionesPresenter = sesionesPresenter;
    }

    @Override
    public void getSesiones(int idProfesional, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    try{
                        JSONArray jsonArray = data.getJSONArray("mensaje");

                        if(jsonArray.length() == 0)
                            sesionesPresenter.setVisibilityLottie(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++){
                            sesionesPresenter.setVisibilityLottie(View.INVISIBLE);
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(!object.getBoolean("finalizado"))
                                sesionesPresenter.setAdapterSesiones(object);
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        sesionesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    sesionesPresenter.showError(data.getString("mensaje"));
            }else
                sesionesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_SESIONES_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
