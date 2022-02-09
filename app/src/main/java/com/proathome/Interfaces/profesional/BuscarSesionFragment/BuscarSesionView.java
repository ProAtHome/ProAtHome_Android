package com.proathome.Interfaces.profesional.BuscarSesionFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONObject;

public interface BuscarSesionView {

    void addMarker(JSONObject jsonObject, LatLng ubicacion);

    void mostrarMarcadores(LatLng latLng, Marker marker);

}
