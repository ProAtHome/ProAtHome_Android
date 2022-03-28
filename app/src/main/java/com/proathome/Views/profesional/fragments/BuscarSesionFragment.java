package com.proathome.Views.profesional.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.Toast;

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
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionPresenter;
import com.proathome.Interfaces.profesional.BuscarSesionFragment.BuscarSesionView;
import com.proathome.Presenters.profesional.BuscarSesionPresenterImpl;
import com.proathome.Utils.pojos.Component;
import com.proathome.Views.profesional.MatchSesionCliente;
import com.proathome.R;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.WorkaroundMapFragment;
import com.proathome.Utils.PermisosUbicacion;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuscarSesionFragment extends DialogFragment implements OnMapReadyCallback, BuscarSesionView {

    public static final String TAG = "Nueva Sesión";
    public static int RANGO_BUSQUEDA = 4000;
    private int idProfesional, rangoServicio;
    private double latitud, longitud;
    public static GoogleMap mMap;
    public static List<Marker> perth = new ArrayList<>();
    public static Marker profesionalPerth;
    private ScrollView mScrollView;
    private BuscarSesionPresenter buscarSesionPresenter;
    private Location location;
    private LatLng ubicacion;
    private Unbinder mUnbinder;
    private String fechaActual;
    private JSONObject servicios, serviciosPendientes;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public BuscarSesionFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);

        buscarSesionPresenter = new BuscarSesionPresenterImpl(this);
        cargarPerfil();
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
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null)
            ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
        else{
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null)
                ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
            else ubicacion = new LatLng(19.432608, -99.133208);
        }
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
                ubicacion = new LatLng(latitud, longitud);
                circle.setCenter(ubicacion);
                for(Marker marcador: perth){
                    LatLng latLng = marcador.getPosition();
                    mostrarMarcadores(latLng, marcador);
                }
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latitud = profesionalPerth.getPosition().latitude;
                longitud = profesionalPerth.getPosition().longitude;
                ubicacion = new LatLng(latitud, longitud);
                circle.setCenter(ubicacion);
                for(Marker marcador: perth){
                    LatLng latLng = marcador.getPosition();
                    mostrarMarcadores(latLng, marcador);
                }
            }

        });

        buscarSesionPresenter.getSesiones(getContext());
        mMap.setOnMarkerClickListener(marker -> {
            if(marker.getSnippet().equals("profesional")){
                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                        .setTitle("UBICACIÓN PROFESIONAL")
                        .setMessage(marker.getTitle())
                        .setNegativeButton("Aceptar", ((dialog, which) -> {

                        }))
                        .show();
            }else {
                Toast.makeText(getContext(), "Validar fecha contra agenda", Toast.LENGTH_LONG).show();

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
                                intent.putExtra("serviciosPendientes", serviciosPendientes.toString());
                                intent.putExtra("serviciosDisponibles", servicios.toString());
                                intent.putExtra("fechaActual", fechaActual);
                                startActivity(intent);
                            }
                        }))
                        .show();
            }

            return true;
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    private void showAlert() {
        PermisosUbicacion.showAlert(getContext(), SweetAlert.PROFESIONAL);
    }

    @Override
    public void mostrarMarcadores(LatLng marcador, Marker marker){
        double latProfesional = ubicacion.latitude;
        double longProfesional = ubicacion.longitude;
        double latMarcador = marcador.latitude;
        double longMarcador = marcador.longitude;

        double distancia = distanciaEntre(latProfesional, longProfesional, latMarcador, longMarcador);
        if(distancia <= RANGO_BUSQUEDA)
            marker.setVisible(true);
        else
            marker.setVisible(false);
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
        this.idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();
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

    @Override
    public void setInfoServiciosDisponibles(JSONObject servicios, String fechaActual) {
        this.servicios = servicios;
        this.fechaActual = fechaActual;
    }

    @Override
    public void setInfoServiciosPendientes(JSONObject serviciosPendientes) {
        this.serviciosPendientes = serviciosPendientes;
    }

    @Override
    public void addMarker(JSONObject object, LatLng ubicacion) {
        try{
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(ubicacion).title("Sesion de: " + object.getString("nombre") +
                            "\n" + "Fecha: " + object.getString("fecha") +
                            "\n" + "Nivel: " + Component.getSeccion(object.getInt("idSeccion")) +
                            "/" + Component.getNivel(object.getInt("idSeccion"),
                            object.getInt("idNivel")) + "\n" + "TIPO DE PLAN: " + object.getString("tipoPlan")).snippet(String.valueOf(
                            object.getInt("idSesion"))));
            if(!object.getString("tipoPlan").equalsIgnoreCase("PARTICULAR") && !object.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN"))
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.profplan));
            perth.add(marker);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
