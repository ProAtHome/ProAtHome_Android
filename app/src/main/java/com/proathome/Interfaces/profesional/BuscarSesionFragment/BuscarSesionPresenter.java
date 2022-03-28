package com.proathome.Interfaces.profesional.BuscarSesionFragment;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONObject;

public interface BuscarSesionPresenter {

    void setInfoServiciosDisponibles(JSONObject servicios, String fechaActual);

    void setInfoServiciosPendientes(JSONObject serviciosPendientes);

    void getSesiones(Context context);

    void addMarker(JSONObject jsonObject, LatLng ubicacion);

    void mostrarMarcadores(LatLng latLng, Marker marker);

}
