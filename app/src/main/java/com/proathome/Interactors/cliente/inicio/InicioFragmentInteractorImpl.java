package com.proathome.Interactors.cliente.inicio;

import android.content.Context;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentInteractor;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.SharedPreferencesManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioFragmentInteractorImpl implements InicioFragmentInteractor {

    private InicioFragmentPresenter inicioFragmentPresenter;

    public InicioFragmentInteractorImpl(InicioFragmentPresenter inicioFragmentPresenter){
        this.inicioFragmentPresenter = inicioFragmentPresenter;
    }

    @Override
    public void getSesiones(int idCliente, Context context) {
        inicioFragmentPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            inicioFragmentPresenter.hideProgress();
            if(response != null){
                try{
                    //iniciarProcesoRuta();

                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        JSONArray jsonArray = jsonObject.getJSONArray("sesiones");

                        if(jsonArray.length() == 0)
                            inicioFragmentPresenter.setLottieVisible();

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            inicioFragmentPresenter.setAdapter(object);
                        }
                    }else
                        inicioFragmentPresenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    inicioFragmentPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                inicioFragmentPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_SESIONES_CLIENTE + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
