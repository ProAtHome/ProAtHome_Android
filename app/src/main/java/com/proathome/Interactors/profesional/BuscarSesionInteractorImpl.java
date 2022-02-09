package com.proathome.Interactors.profesional;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionInteractor;
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.profesional.fragments.BuscarSesionFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuscarSesionInteractorImpl implements BuscarSesionInteractor {

    private BuscarSesionPresenter buscarSesionPresenter;

    public BuscarSesionInteractorImpl(BuscarSesionPresenter buscarSesionPresenter){
        this.buscarSesionPresenter = buscarSesionPresenter;
    }

    @Override
    public void getSesiones(Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response == null)
                SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error del servidor, intente de nuevo más tarde.", false, null, null);
            else{
                if(!response.equals("null")){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            LatLng ubicacion = new LatLng(object.getDouble("latitud"), object.getDouble("longitud"));
                            //ANADIR MARCADORES AL PERTH
                            buscarSesionPresenter.addMarker(object, ubicacion);

                        }

                        for(Marker marcador: BuscarSesionFragment.perth){
                            LatLng latLng = marcador.getPosition();
                            buscarSesionPresenter.mostrarMarcadores(latLng, marcador);
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    SweetAlert.showMsg(context, SweetAlert.WARNING_TYPE, "¡ERROR!", "Sin Sesiones disponibles.", false, null, null);
            }
        }, APIEndPoints.BUSCAR_SESIONES + SharedPreferencesManager.getInstance(context).getRangoServicioProfeisonal(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
