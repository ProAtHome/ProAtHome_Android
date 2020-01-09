package com.proathome.ui.sesiones;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proathome.R;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.STRegistroSesionesEstudiante;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.utils.Constants;

public class SesionesFragment extends Fragment implements OnMapReadyCallback {

    private SesionesViewModel sesionesViewModel;
    private GoogleMap mMap;
    private Marker perth;
    private ScrollView mScrollView;
    private Button btnSolicitar;
    private EditText direccionET, horarioET, tiempoET, tipoET, nivelET, observacionesET;
    private double latitud, longitud;
    private String registrarSesionREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/agregarSesion";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sesionesViewModel = ViewModelProviders.of(this).get(SesionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sesiones, container, false);
        direccionET = (EditText)root.findViewById(R.id.text_direccionET);
        horarioET = (EditText)root.findViewById(R.id.text_horarioET);
        tiempoET = (EditText)root.findViewById(R.id.text_tiempoET);
        tipoET = (EditText)root.findViewById(R.id.text_tipoET);
        nivelET = (EditText)root.findViewById(R.id.text_nivelET);
        observacionesET = (EditText)root.findViewById(R.id.text_observacionesET);
        btnSolicitar = (Button)root.findViewById(R.id.btn_solicitar);
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!direccionET.getText().toString().trim().equalsIgnoreCase("") && !horarioET.getText().toString().trim().equalsIgnoreCase("") && !tiempoET.getText().toString().trim().equalsIgnoreCase("")
                    && !tipoET.getText().toString().trim().equalsIgnoreCase("") && !nivelET.getText().toString().trim().equalsIgnoreCase("") && !observacionesET.getText().toString().trim().equalsIgnoreCase("")){

                    String direccion = direccionET.getText().toString();
                    String horario = horarioET.getText().toString();
                    String tiempo = tiempoET.getText().toString();
                    String tipo = tipoET.getText().toString();
                    String nivel = nivelET.getText().toString();
                    String extras = observacionesET.getText().toString();
                    int idCliente = 0;

                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
                    SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                    Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

                    if(fila.moveToFirst()){

                        idCliente = fila.getInt(0);

                    }else{

                        System.out.println("No hay sesión.");
                        baseDeDatos.close();

                    }

                    STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(), registrarSesionREST, idCliente, horario, direccion, tiempo, nivel, extras, tipo, latitud, longitud);
                    registro.execute();
                    direccionET.setText("");
                    horarioET.setText("");
                    tiempoET.setText("");
                    tipoET.setText("");
                    nivelET.setText("");
                    observacionesET.setText("");
                    Toast.makeText(getContext(), "Revisa tu nueva clase en Inicio o en Gestión.", Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(getContext(), "Llena todos los campos.", Toast.LENGTH_LONG).show();

                }

            }

        });

        return root;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (mMap == null) {

            SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
            mapFragment.getMapAsync(this);

        }

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

            mScrollView = getView().findViewById(R.id.scrollMap); //parent scrollview in xml, give your scrollview id value
            ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.maps))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch()
                        {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });

            mMap.setMyLocationEnabled(true);

            agregarMarca(googleMap, 19.4326077, -99.13320799999);
        }

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        perth = mMap.addMarker(new MarkerOptions().position(ubicacion).title("Mueve el marcador para elegir el punto de encuentro.").draggable(true));
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
                System.out.println(perth.getPosition().latitude);
                System.out.println(perth.getPosition().longitude);
            }

        });

    }


}