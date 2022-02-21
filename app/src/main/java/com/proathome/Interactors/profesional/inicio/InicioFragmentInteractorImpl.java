package com.proathome.Interactors.profesional.inicio;

import com.proathome.Interfaces.profesional.Inicio.InicioFragmentInteractor;
import com.proathome.Interfaces.profesional.Inicio.InicioFragmentPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioFragmentInteractorImpl implements InicioFragmentInteractor {

    private InicioFragmentPresenter inicioFragmentPresenter;

    public InicioFragmentInteractorImpl(InicioFragmentPresenter inicioFragmentPresenter){
        this.inicioFragmentPresenter = inicioFragmentPresenter;
    }

    @Override
    public void getSesiones(int idProfesional, String token) {
        inicioFragmentPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            inicioFragmentPresenter.hideProgress();
            if(response != null){
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    try{
                        JSONArray jsonArray = data.getJSONArray("mensaje");

                        if(jsonArray.length() == 0)
                            inicioFragmentPresenter.setLottieVisible();

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            inicioFragmentPresenter.setAdapterSesiones(object);
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        inicioFragmentPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    inicioFragmentPresenter.showError(data.getString("mensaje"));
            }else
                inicioFragmentPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_SESIONES_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
