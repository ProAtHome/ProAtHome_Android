package com.proathome.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.MatchSesionCliente;
import com.proathome.R;
import com.proathome.utils.Component;
import com.proathome.utils.WorkaroundMapFragment;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuscarSesionFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = "Nueva Sesión";
    public static Context contexto;
    public static int RANGO_BUSQUEDA = 4000;
    private int idProfesional, rangoServicio;
    private double latitud, longitud;
    public static GoogleMap mMap;
    public static List<Marker> perth = new ArrayList<>();
    public static Marker profesionalPerth;
    private ScrollView mScrollView;
    private Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public BuscarSesionFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        contexto = getContext();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buscar_sesion, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        toolbar.setTitle("Nueva Sesión");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        cargarPerfil();

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mScrollView = getView().findViewById(R.id.scrollMapProf);
        ((WorkaroundMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng ubicacion = new LatLng(19.4326077, -99.13320799999);
        profesionalPerth = mMap.addMarker(new MarkerOptions().position(ubicacion)
                .title("Mueve el marcador para elegir el radio a buscar.").snippet("profesional").draggable(true));
        profesionalPerth.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.profubb));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(ubicacion)
                .radius(RANGO_BUSQUEDA)
                .strokeColor(Color.BLUE)
                .fillColor(R.color.color_ubicacion));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(Marker marker) {
                latitud = profesionalPerth.getPosition().latitude;
                longitud = profesionalPerth.getPosition().longitude;
                LatLng ubicacion = new LatLng(latitud, longitud);
                circle.setCenter(ubicacion);
                for(Marker marcador: perth){
                    LatLng latLng = marcador.getPosition();
                    mostrarMarcadores(ubicacion, latLng, marcador);
                }
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latitud = profesionalPerth.getPosition().latitude;
                longitud = profesionalPerth.getPosition().longitude;
                LatLng ubicacion = new LatLng(latitud, longitud);
                circle.setCenter(ubicacion);
                for(Marker marcador: perth){
                    LatLng latLng = marcador.getPosition();
                    mostrarMarcadores(ubicacion, latLng, marcador);
                }
            }

        });

        getSesiones();

        BuscarSesionFragment.mMap.setOnMarkerClickListener(marker -> {
            if(marker.getSnippet().equals("profesional")){
                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                        .setTitle("UBICACIÓN PROFESIONAL")
                        .setMessage(marker.getTitle())
                        .setNegativeButton("Aceptar", ((dialog, which) -> {

                        }))
                        .show();
            }else {
                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                        .setTitle("SESIÓN DE CLIENTE")
                        .setMessage(marker.getTitle())
                        .setNegativeButton("Cerrar", (dialog, which) -> {

                        })
                        .setPositiveButton("Ver detalles", ((dialog, which) -> {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                showAlert();
                            }else{
                                Intent intent = new Intent(getContext(), MatchSesionCliente.class);
                                intent.putExtra("idSesion", marker.getSnippet());
                                intent.putExtra("latitud", marker.getPosition().latitude);
                                intent.putExtra("longitud", marker.getPosition().longitude);
                                startActivity(intent);
                            }
                        }))
                        .show();
            }

            return true;
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));

    }

    private void getSesiones(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response == null){
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Error del servidor, intente de nuevo más tarde.", false, null, null);
            }else{
                if(!response.equals("null")){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            LatLng ubicacion = new LatLng(object.getDouble("latitud"), object.getDouble("longitud"));
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(ubicacion).title("Sesion de: " + object.getString("nombre") +
                                            "\n" + "Nivel: " + Component.getSeccion(object.getInt("idSeccion")) +
                                            "/" + Component.getNivel(object.getInt("idSeccion"),
                                            object.getInt("idNivel")) + "\n" + "TIPO DE PLAN: " + object.getString("tipoPlan")).snippet(String.valueOf(
                                            object.getInt("idSesion"))));
                            if(!object.getString("tipoPlan").equalsIgnoreCase("PARTICULAR") && !object.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN"))
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.profplan));
                            perth.add(marker);
                        }
                        LatLng ubicacion = new LatLng(19.4326077, -99.13320799999);
                        for(Marker marcador: BuscarSesionFragment.perth){
                            LatLng latLng = marcador.getPosition();
                            mostrarMarcadores(ubicacion, latLng, marcador);
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ERROR!", "Sin Sesiones disponibles.", false, null, null);
            }
        }, APIEndPoints.BUSCAR_SESIONES + this.rangoServicio, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void showAlert() {
        PermisosUbicacion.showAlert(getContext(), SweetAlert.PROFESIONAL);
    }

    public static void mostrarMarcadores(LatLng profesional, LatLng marcador, Marker marker){
        double latProfesional = profesional.latitude;
        double longProfesional = profesional.longitude;
        double latMarcador = marcador.latitude;
        double longMarcador = marcador.longitude;

        double distancia = distanciaEntre(latProfesional, longProfesional, latMarcador, longMarcador);
        if(distancia <= RANGO_BUSQUEDA){
            marker.setVisible(true);
        }else{
            marker.setVisible(false);
        }
    }

    public static double distanciaEntre(double latProfesional, double longProfesional, double latMarcador, double longMarcador) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(latMarcador - latProfesional);
        double dLng = Math.toRadians(longMarcador - longProfesional);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(latProfesional)) * Math.cos(Math.toRadians(latMarcador)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distancia = (double) (earthRadius * c);

        return distancia;
    }

    public void cargarPerfil(){

        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(getContext(), "sesionProfesional",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional, rangoServicio FROM sesionProfesional WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            //Recorremos el cursor hasta que no haya más registros
            do {
                this.idProfesional = fila.getInt(0);
                this.rangoServicio = fila.getInt(1);
            } while(fila.moveToNext());
            baseDeDatos.close();
        }else{
            baseDeDatos.close();
        }

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();
        mMap = null;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

}
