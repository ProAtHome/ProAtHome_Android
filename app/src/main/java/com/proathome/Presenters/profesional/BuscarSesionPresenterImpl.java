package com.proathome.Presenters.profesional;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.proathome.Interactors.profesional.BuscarSesionInteractorImpl;
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionInteractor;
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionPresenter;
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionView;
import org.json.JSONObject;

public class BuscarSesionPresenterImpl implements BuscarSesionPresenter {

    private BuscarSesionView buscarSesionView;
    private BuscarSesionInteractor buscarSesionInteractor;

    public BuscarSesionPresenterImpl(BuscarSesionView buscarSesionView){
        this.buscarSesionView = buscarSesionView;
        buscarSesionInteractor = new BuscarSesionInteractorImpl(this);
    }

    @Override
    public void getSesiones(Context context) {
        buscarSesionInteractor.getSesiones(context);
    }

    @Override
    public void addMarker(JSONObject jsonObject, LatLng ubicacion) {
        if(buscarSesionView != null)
            buscarSesionView.addMarker(jsonObject, ubicacion);
    }

    @Override
    public void mostrarMarcadores(LatLng latLng, Marker marker) {
        if(buscarSesionView != null)
            buscarSesionView.mostrarMarcadores(latLng, marker);
    }

}
