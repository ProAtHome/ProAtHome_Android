package com.proathome.Interactors.profesional;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BuscarSesionInteractorImpl implements BuscarSesionInteractor {

    private BuscarSesionPresenter buscarSesionPresenter;

    public BuscarSesionInteractorImpl(BuscarSesionPresenter buscarSesionPresenter){
        this.buscarSesionPresenter = buscarSesionPresenter;
    }

    @Override
    public void getSesiones(Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            //Log.d("TAG1", response);
            if(response != null){
                JSONObject data = new JSONObject(response);
                JSONObject mensajeData = data.getJSONObject("mensaje");
                String fechaActual = mensajeData.getString("fechaActual");
                JSONObject servicios = mensajeData.getJSONObject("servicios");
                JSONObject serviciosPendientes = mensajeData.getJSONObject("serviciosPendientes");
                Log.d("TAGS", serviciosPendientes.toString());
                buscarSesionPresenter.setInfoServiciosDisponibles(servicios, fechaActual);
                buscarSesionPresenter.setInfoServiciosPendientes(serviciosPendientes);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter fechaFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //AGENDA HOY
                    JSONArray hoy = servicios.getJSONArray(fechaFormat.format(LocalDateTime.now()));
                    addMarkers(hoy);
                    //AGENDA DIA 1
                    JSONArray dia1 = servicios.getJSONArray(fechaFormat.format(LocalDateTime.now().plusDays(1)));
                    addMarkers(dia1);
                    //AGENDA DIA 2
                    JSONArray dia2 = servicios.getJSONArray(fechaFormat.format(LocalDateTime.now().plusDays(2)));
                    addMarkers(dia2);
                    //AGENDA DIA 3
                    JSONArray dia3 = servicios.getJSONArray(fechaFormat.format(LocalDateTime.now().plusDays(3)));
                    addMarkers(dia3);
                    //AGENDA DIA 4
                    JSONArray dia4 = servicios.getJSONArray(fechaFormat.format(LocalDateTime.now().plusDays(4)));
                    addMarkers(dia4);
                }

                for(Marker marcador: BuscarSesionFragment.perth){
                    LatLng latLng = marcador.getPosition();
                    buscarSesionPresenter.mostrarMarcadores(latLng, marcador);
                }
            }
        }, APIEndPoints.BUSCAR_SESIONES + SharedPreferencesManager.getInstance(context).getRangoServicioProfeisonal() +
                "/" + SharedPreferencesManager.getInstance(context).getIDProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
        /*
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
        webServicesAPI.execute();*/
    }

    private void addMarkers(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject object = jsonArray.getJSONObject(i);
            LatLng ubicacion = new LatLng(object.getDouble("latitud"), object.getDouble("longitud"));
            //ANADIR MARCADORES AL PERTH
            buscarSesionPresenter.addMarker(object, ubicacion);
        }
    }

}
