package com.proathome.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.proathome.MatchSesionEstudiante;
import com.proathome.R;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.profesor.ServicioTaskObtenerSesiones;
import com.proathome.utils.Constants;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SweetAlert;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuscarSesionFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = "Nueva Sesión";
    private String linkAPISesiones = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/obtenerSesionesMovil";
    public static int RANGO_BUSQUEDA = 4000;
    private double latitud, longitud;
    public static GoogleMap mMap;
    public static List<Marker> perth = new ArrayList<>();
    public static Marker profesorPerth;
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
        LatLng ubicacion = new LatLng(19.4326077, -99.13320799999);
        profesorPerth = mMap.addMarker(new MarkerOptions().position(ubicacion)
                .title("Mueve el marcador para elegir el radio a buscar.").snippet("profesor").draggable(true));
        profesorPerth.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.profubb));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(ubicacion)
                .radius(RANGO_BUSQUEDA)
                .strokeColor(Color.BLUE)
                .fillColor(R.color.color_ubicacion));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(Marker marker) {
                latitud = profesorPerth.getPosition().latitude;
                longitud = profesorPerth.getPosition().longitude;
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
                latitud = profesorPerth.getPosition().latitude;
                longitud = profesorPerth.getPosition().longitude;
                LatLng ubicacion = new LatLng(latitud, longitud);
                circle.setCenter(ubicacion);
                for(Marker marcador: perth){
                    LatLng latLng = marcador.getPosition();
                    mostrarMarcadores(ubicacion, latLng, marcador);
                }
            }

        });

        ServicioTaskObtenerSesiones obtenerSesiones = new ServicioTaskObtenerSesiones(getContext(), this.linkAPISesiones);
        obtenerSesiones.execute();

        BuscarSesionFragment.mMap.setOnMarkerClickListener(marker -> {
            if(marker.getSnippet().equals("profesor")){
                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                        .setTitle("UBICACIÓN PROFESOR")
                        .setMessage(marker.getTitle())
                        .setNegativeButton("Aceptar", ((dialog, which) -> {

                        }))
                        .show();
            }else {
                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                        .setTitle("SESIÓN DE ESTUDIANTE")
                        .setMessage(marker.getTitle())
                        .setNegativeButton("Cerrar", (dialog, which) -> {

                        })
                        .setPositiveButton("Ver detalles", ((dialog, which) -> {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                showAlert();
                            }else{
                                Intent intent = new Intent(getContext(), MatchSesionEstudiante.class);
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
        BuscarSesionFragment.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));

    }

    private void showAlert() {
        PermisosUbicacion.showAlert(getContext(), SweetAlert.PROFESOR);
    }

    public static void mostrarMarcadores(LatLng profesor, LatLng marcador, Marker marker){
        double latProfesor = profesor.latitude;
        double longProfesor = profesor.longitude;
        double latMarcador = marcador.latitude;
        double longMarcador = marcador.longitude;

        double distancia = distanciaEntre(latProfesor, longProfesor, latMarcador, longMarcador);
        if(distancia <= RANGO_BUSQUEDA){
            marker.setVisible(true);
        }else{
            marker.setVisible(false);
        }
    }

    public static double distanciaEntre(double latProfesor, double longProfesor, double latMarcador, double longMarcador) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(latMarcador - latProfesor);
        double dLng = Math.toRadians(longMarcador - longProfesor);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(latProfesor)) * Math.cos(Math.toRadians(latMarcador)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distancia = (double) (earthRadius * c);

        return distancia;
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
