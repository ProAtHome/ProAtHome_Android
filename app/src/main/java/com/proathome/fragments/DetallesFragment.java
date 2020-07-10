package com.proathome.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetallesFragment extends Fragment implements OnMapReadyCallback {

    private static Component mInstance;
    private GoogleMap mMap;
    public static final String TAG = "Detalles";
    @BindView(R.id.profesor)
    public TextView profesor;
    @BindView(R.id.horario)
    public TextView horario;
    @BindView(R.id.lugar)
    public TextView lugar;
    @BindView(R.id.tiempo)
    public TextView tiempo;
    @BindView(R.id.nivel)
    public TextView nivel;
    @BindView(R.id.observaciones)
    public TextView observaciones;
    @BindView(R.id.tipoClase)
    public TextView tipoClase;
    private ScrollView mScrollView;
    private Unbinder mUnbinder;
    private double longitud = -99.13320799999, latitud = 19.4326077;

    public DetallesFragment() {

    }

    public static Component getmInstance(int idClase, String tipoClase, String horario, String profesor, String lugar, int tiempo, String observaciones, double latitud, double longitud, int idSeccion, int idNivel, int idBloque, String fecha){

        mInstance = new Component();
        mInstance.setIdClase(idClase);
        mInstance.setProfesor("Profesor Asignado: " + profesor);
        mInstance.setLugar("Lugar - Dirección: " + lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones("Observaciones: " + observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setFecha(fecha);
        mInstance.setTipoClase("Tipo de Clase: " + tipoClase);
        mInstance.setHorario("Horario: " + horario);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);
        return mInstance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles, container, false);
        Component component = new Component();
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bun = getArguments();
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesor.setText(bun.getString("profesor"));
        lugar.setText(bun.getString("lugar"));
        tiempo.setText(bun.getString("tiempo"));
        observaciones.setText(bun.getString("observaciones"));
        nivel.setText(component.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque")));
        tipoClase.setText(bun.getString("tipoClase"));
        horario.setText(bun.getString("horario"));

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (mMap == null) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            }else{
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetalles);
                mapFragment.getMapAsync(this);
            }

        }

    }

    private void showAlert() {
        new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos tu permiso :)")
                .setPositiveButton("Dar permiso", (dialog, which) -> {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Toast.makeText(getContext(), "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().detach(this).commit();
                })
                .setOnCancelListener(dialog -> {
                    Toast.makeText(getContext(), "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().detach(this).commit();
                })
                .show();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mScrollView = getView().findViewById(R.id.scrollMapDetalles); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetalles))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        mMap.setMyLocationEnabled(true);

        agregarMarca(googleMap, latitud, longitud);

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu clase."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));

    }

}
