package com.proathome.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ControladorTomarSesion;
import com.proathome.controladores.estudiante.STRegistroSesionesEstudiante;
import com.proathome.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NuevaSesionFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = "Nueva Sesión";
    private GoogleMap mMap;
    private Marker perth;
    private ScrollView mScrollView;
    private double latitud, longitud;
    private String registrarSesionREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/agregarSesion";
    private Unbinder mUnbinder;
    @BindView(R.id.text_direccionET)
    TextInputEditText direccionET;
    @BindView(R.id.text_horarioET)
    TextInputEditText horarioET;
    @BindView(R.id.text_observacionesET)
    TextInputEditText observacionesET;
    @BindView(R.id.btnSolicitar)
    MaterialButton btnSolicitar;
    @BindView(R.id.horas)
    Spinner horas;
    @BindView(R.id.minutos)
    Spinner minutos;
    @BindView(R.id.tipo)
    Spinner tipo;
    @BindView(R.id.secciones)
    Spinner secciones;
    @BindView(R.id.niveles)
    Spinner niveles;
    @BindView(R.id.bloques)
    Spinner bloques;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public NuevaSesionFragment() {

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
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nueva_sesion, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ControladorTomarSesion tomarSesion = new ControladorTomarSesion(getContext(), Constants.INTERMEDIO, Constants.INTERMEDIO_5, Constants.BLOQUE6_INTERMEDIO5);

        String[] datosHoras = new String[]{"0 HRS", "1 HRS", "2 HRS", "3 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosHoras);
        String[] datosMinutos = new String[]{"0 min", "5 min", "10 min", "15 min", "20 min", "25 min", "30 min", "35 min", "40 min", "45 min", "50 min", "55 min"};
        ArrayAdapter<String> adapterMinutos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosMinutos);
        String[] datosTipo = new String[]{"Personal", "Grupal"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosTipo);
        String[] datosSecciones = new String[]{"Básico"};
        horas.setAdapter(adapterHoras);
        minutos.setAdapter(adapterMinutos);
        tipo.setAdapter(adapterTipo);
        secciones.setAdapter(tomarSesion.obtenerSecciones());
        secciones.setSelection(2-1);
        niveles.setAdapter(tomarSesion.obtenerNiveles());
        niveles.setSelection(5-1);
        bloques.setAdapter(tomarSesion.obtenerBloques());
        bloques.setSelection(6-1);

        secciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(tomarSesion.getSeccion() == Constants.BASICO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){

                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){

                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){

                    }
                }else if(tomarSesion.getSeccion() == Constants.INTERMEDIO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                            Toast.makeText(getContext(), "Obtener niveles y bloques de Básico.", Toast.LENGTH_LONG).show();
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                            Toast.makeText(getContext(), "Obtener niveles y bloques de Intermedio.", Toast.LENGTH_LONG).show();
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                            Toast.makeText(getContext(), "Obtener niveles y bloques de Avanzado.", Toast.LENGTH_LONG).show();
                    }
                }else if(tomarSesion.getSeccion() == Constants.AVANZADO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){

                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){

                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){

                    }
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");

        btnSolicitar.setOnClickListener(v -> {

            if (!direccionET.getText().toString().trim().equalsIgnoreCase("") && !horarioET.getText().toString().trim().equalsIgnoreCase("")
                      && !observacionesET.getText().toString().trim().equalsIgnoreCase("")) {

                String direccion = direccionET.getText().toString();
                String horario = horarioET.getText().toString();
                String extras = observacionesET.getText().toString();
                int idCliente = 0;

                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
                SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

                if (fila.moveToFirst()) {
                    idCliente = fila.getInt(0);
                } else {
                    baseDeDatos.close();
                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                String strDate = mdformat.format(calendar.getTime());
                STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(), registrarSesionREST, idCliente, horario, direccion, "tiempo", "nivel", extras, "tipo", latitud, longitud, strDate);
                registro.execute();
                direccionET.setText("");
                horarioET.setText("");
                observacionesET.setText("");
                Toast.makeText(getContext(), "Revisa tu nueva clase en Inicio o en Gestión.", Toast.LENGTH_LONG).show();
                dismiss();

            } else {

                Toast.makeText(getContext(), "Llena todos los campos.", Toast.LENGTH_LONG).show();

            }


        });

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

        mScrollView = getView().findViewById(R.id.scrollMap); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        mMap.setMyLocationEnabled(true);

        agregarMarca(googleMap, 19.4326077, -99.13320799999);

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
            }

        });

    }

    @OnClick(R.id.elegirHorario)
    public void onChooserClicked(){

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
        adapter.add("01:00 HRS");
        adapter.add("02:00 HRS");
        adapter.add("03:00 HRS");
        adapter.add("04:00 HRS");
        adapter.add("05:00 HRS");
        adapter.add("06:00 HRS");
        adapter.add("07:00 HRS");
        adapter.add("08:00 HRS");
        adapter.add("09:00 HRS");
        adapter.add("10:00 HRS");
        adapter.add("11:00 HRS");
        adapter.add("12:00 HRS");
        adapter.add("13:00 HRS");
        adapter.add("14:00 HRS");
        adapter.add("15:00 HRS");
        adapter.add("16:00 HRS");
        adapter.add("17:00 HRS");
        adapter.add("18:00 HRS");
        adapter.add("19:00 HRS");
        adapter.add("20:00 HRS");
        adapter.add("21:00 HRS");
        adapter.add("22:00 HRS");
        adapter.add("23:00 HRS");
        adapter.add("00:00 HRS");

        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Elige el horario de tu preferencia para comenzar.")
                .setAdapter(adapter, (dialog, which) ->{
                    horarioET.setText(adapter.getItem(which));
                })
                .show();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

}
