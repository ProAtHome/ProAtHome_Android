package com.proathome.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.estudiante.ServicioTaskEliminarSesion;
import com.proathome.controladores.estudiante.ServicioTaskUpSesion;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesGestionarFragment extends Fragment implements OnMapReadyCallback {

    private static Component mInstance;
    private GoogleMap mMap;
    private Marker perth;
    private String linkAPIEliminarSesion = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/eliminarSesion";
    private String linkAPIUpSesion = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/actualizarSesion";
    public static final String TAG = "Detalles de la Sesión";
    @BindView(R.id.tietProfesor)
    TextInputEditText profesorET;
    @BindView(R.id.tietHorario)
    TextInputEditText horarioET;
    @BindView(R.id.tietLugar)
    TextInputEditText lugarET;
    @BindView(R.id.tietTiempo)
    TextInputEditText tiempoET;
    @BindView(R.id.tietNivel)
    TextInputEditText nivelET;
    @BindView(R.id.tietObservaciones)
    TextInputEditText observacionesET;
    @BindView(R.id.tietTipo)
    TextInputEditText tipoClaseET;
    @BindView(R.id.btnActualizarSesion)
    MaterialButton btnActualizar;
    @BindView(R.id.btnEliminarSesion)
    MaterialButton btnEliminar;
    public ScrollView mScrollView;
    private Unbinder mUnbinder;
    private int idClase = 0;
    private double longitud = -99.13320799999, latitud = 19.4326077;

    public DetallesGestionarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles_gestionar, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bun = getArguments();
        idClase = bun.getInt("idClase");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesorET.setText(bun.getString("profesor"));
        lugarET.setText(bun.getString("lugar"));
        tiempoET.setText(bun.getString("tiempo"));
        observacionesET.setText(bun.getString("observaciones"));
        nivelET.setText(bun.getString("nivel"));
        tipoClaseET.setText(bun.getString("tipoClase"));
        horarioET.setText(bun.getString("horario"));

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mMap == null) {

            SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesG);
            mapFragment.getMapAsync(this);

        }

    }

    public static Component getmInstance(int idClase, String nivel, String tipoClase, String horario, String profesor, String lugar, String tiempo, String observaciones, double latitud, double longitud, String actualizado){

        mInstance = new Component();
        mInstance.setIdClase(idClase);
        mInstance.setProfesor(profesor);
        mInstance.setLugar(lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones(observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setNivel(nivel);
        mInstance.setTipoClase(tipoClase);
        mInstance.setHorario(horario);
        mInstance.setActualizado(actualizado);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);
        return mInstance;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            Log.e("Location", "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;

        }else{

            Log.i("Location", "Permisos necesarios OK!.");
            LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            mScrollView = getView().findViewById(R.id.scrollMapGestionar); //parent scrollview in xml, give your scrollview id value
            ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesG))
                    .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

            mMap.setMyLocationEnabled(true);


            agregarMarca(googleMap, latitud, longitud);
        }

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        perth = mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu clase.").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
        latitud = perth.getPosition().latitude;
        longitud = perth.getPosition().longitude;

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latitud = perth.getPosition().latitude;
                longitud = perth.getPosition().longitude;
            }

        });
    }

    @OnClick({R.id.btnActualizarSesion, R.id.btnEliminarSesion})
    public void onClicked(View view) {

        switch (view.getId()){

            case R.id.btnActualizarSesion:
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                String strDate =  mdformat.format(calendar.getTime());
                ServicioTaskUpSesion upSesion = new ServicioTaskUpSesion(getContext(), this.linkAPIUpSesion, this.idClase, horarioET.getText().toString(), lugarET.getText().toString(), tiempoET.getText().toString(),
                        nivelET.getText().toString(), tipoClaseET.getText().toString(), observacionesET.getText().toString(), this.latitud, this.longitud, strDate);
                upSesion.execute();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().finish();
                break;
            case R.id.btnEliminarSesion:
                ServicioTaskEliminarSesion eliminarSesion = new ServicioTaskEliminarSesion(getContext(), this.linkAPIEliminarSesion, this.idClase);
                eliminarSesion.execute();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().finish();
                break;

        }

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapsDetallesG));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

    }
}
