package com.proathome.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.proathome.controladores.estudiante.ControladorTomarSesion;
import com.proathome.controladores.estudiante.ServicioTaskEliminarSesion;
import com.proathome.controladores.estudiante.ServicioTaskUpSesion;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesGestionarFragment extends Fragment implements OnMapReadyCallback {

    private static Component mInstance;
    public static boolean basicoVisto, intermedioVisto, avanzadoVisto;
    public static ControladorTomarSesion tomarSesion;
    private boolean cambioFecha;
    private GoogleMap mMap;
    private Marker perth;
    private String linkAPIEliminarSesion = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/eliminarSesion";
    private String linkAPIUpSesion = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/actualizarSesion";
    public static final String TAG = "Detalles de la Sesión";
    private int idSeccion, idNivel, idBloque, tiempo;
    @BindView(R.id.tietProfesor)
    TextInputEditText profesorET;
    @BindView(R.id.tietHorario)
    TextInputEditText horarioET;
    @BindView(R.id.tietLugar)
    TextInputEditText lugarET;
    @BindView(R.id.textHoras)
    TextView horas;
    //@BindView(R.id.minutos)
    //Spinner minutos;
    @BindView(R.id.secciones)
    TextView secciones;
    @BindView(R.id.niveles)
    TextView niveles;
    @BindView(R.id.bloques)
    TextView bloques;
    @BindView(R.id.tietObservaciones)
    TextInputEditText observacionesET;
    @BindView(R.id.tipo)
    Spinner tipo;
    @BindView(R.id.tietFecha)
    TextInputEditText fechaET;
    @BindView(R.id.btnActualizarSesion)
    MaterialButton btnActualizar;
    @BindView(R.id.btnEliminarSesion)
    MaterialButton btnEliminar;
    //@BindView(R.id.horasDisponibles)
    //TextView horasDisponiblesTV;
    public NestedScrollView mScrollView;
    private Unbinder mUnbinder;
    private int idClase = 0;
    private double longitud = -99.13320799999, latitud = 19.4326077;

    public DetallesGestionarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles_gestionar, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        String[] datosHoras = new String[]{"0 HRS", "1 HRS", "2 HRS", "3 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosHoras);
        String[] datosMinutos = new String[]{"0 min", "5 min", "10 min", "15 min", "20 min", "25 min", "30 min", "35 min", "40 min", "45 min", "50 min", "55 min"};
        ArrayAdapter<String> adapterMinutos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosMinutos);
        String[] datosTipo = new String[]{"Personal", "Grupal"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosTipo);

//        horas.setAdapter(adapterHoras);
        //minutos.setAdapter(adapterMinutos);
        tipo.setAdapter(adapterTipo);

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");
        fechaET.setKeyListener(null);

        Bundle bun = getArguments();
        idClase = bun.getInt("idClase");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesorET.setText(bun.getString("profesor"));
        lugarET.setText(bun.getString("lugar"));
        //horas.setSelection(posicionHoras(horasTexto(bun.getInt("tiempo"))));
        //minutos.setSelection(posicionMinutos(minutosTexto(bun.getInt("tiempo"))));
        observacionesET.setText(bun.getString("observaciones"));
        fechaET.setText(bun.getString("fecha"));
        //secciones.setSelection(bun.getInt("idSeccion")-1);
        //niveles.setSelection(bun.getInt("idNivel")-1);
        //bloques.setSelection(bun.getInt("idBloque")-1);
        horas.setText(horasTexto(bun.getInt("tiempo")) + " " + minutosTexto(bun.getInt("tiempo")));
        tipo.setSelection(posicionTipo(bun.getString("tipoClase")));
        horarioET.setText(bun.getString("horario"));
        tiempo = bun.getInt("tiempo");

        tomarSesion = new ControladorTomarSesion(getContext(), bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque"));
        secciones.setText(Component.getSeccion(bun.getInt("idSeccion")));
        niveles.setText(Component.getNivel(bun.getInt("idSeccion"), bun.getInt("idNivel")));
        bloques.setText(Component.getBloque(bun.getInt("idBloque")));
        idSeccion = bun.getInt("idSeccion");
        idNivel = bun.getInt("idNivel");
        idBloque = bun.getInt("idBloque");
        //secciones.setAdapter(tomarSesion.obtenerSecciones());
        //secciones.setSelection(bun.getInt("idSeccion")-1);
        //niveles.setAdapter(tomarSesion.obtenerNiveles());
        //niveles.setSelection(bun.getInt("idNivel")-1);
        //bloques.setAdapter(tomarSesion.obtenerBloques());
        //bloques.setSelection(bun.getInt("idBloque")-1);
        System.out.println(bun.getInt("idSeccion") + " " + bun.getInt("idNivel") + " " + bun.getInt("idBloque"));
/*
        secciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(tomarSesion.getSeccion() == Constants.BASICO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                        if(!basicoVisto){
                            Toast.makeText(getContext(), "Valores de REST", Toast.LENGTH_LONG).show();
                            basicoVisto = true;
                        }else{
                            niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.BASICO));
                        }
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                        niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                        niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.AVANZADO));
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(tomarSesion.getSeccion() == Constants.INTERMEDIO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                        niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.BASICO));
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                        if(!intermedioVisto){
                            Toast.makeText(getContext(), "Valores de REST", Toast.LENGTH_LONG).show();
                            intermedioVisto = true;
                        }else{
                            niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
                        }
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                        niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.AVANZADO));
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(tomarSesion.getSeccion() == Constants.AVANZADO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                        niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.BASICO));
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                        niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                        if(!avanzadoVisto){
                            Toast.makeText(getContext(), "Valores de REST", Toast.LENGTH_LONG).show();
                            avanzadoVisto = true;
                        }else{
                            niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.AVANZADO));
                        }
                    }
                }

                if(tomarSesion.validarSesionCorrecta(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque"), secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1)){
                    horasDisponiblesTV.setText("*Sesión registrada previamente.*");
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        niveles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")) {
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 1")){
                        if(tomarSesion.getNivel() == Constants.BASICO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 2")){
                        if(tomarSesion.getNivel() == Constants.BASICO_2){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 3")){
                        if(tomarSesion.getNivel() == Constants.BASICO_3){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 4")){
                        if(tomarSesion.getNivel() == Constants.BASICO_4){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 5")){
                        if(tomarSesion.getNivel() == Constants.BASICO_5){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_5));
                        }
                    }
                }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 1")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 2")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_2){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 3")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_3){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 4")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_4){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 5")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_5){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_5));
                        }
                    }
                }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 1")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 2")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 3")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 4")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 5")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){
                            Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_5));
                        }
                    }
                }

                if(tomarSesion.validarSesionCorrecta(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque"), secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1)){
                    horasDisponiblesTV.setText("*Sesión registrada previamente.*");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bloques.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 1")){
                    if(tomarSesion.getBloque() == 1){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 2")){
                    if(tomarSesion.getBloque() == 2){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 3")){
                    if(tomarSesion.getBloque() == 3){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 4")){
                    if(tomarSesion.getBloque() == 4){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 5")){
                    if(tomarSesion.getBloque() == 5){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 6")){
                    if(tomarSesion.getBloque() == 6){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 7")){
                    if(tomarSesion.getBloque() == 7){
                        Toast.makeText(getContext(), "Valores Normales", Toast.LENGTH_LONG);
                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }

                if(tomarSesion.validarSesionCorrecta(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque"), secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1)){
                    horasDisponiblesTV.setText("*Sesión registrada previamente.*");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mMap == null) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            }else{

                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesG);
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

    public static Component getmInstance(int idClase, String tipoClase, String horario, String profesor, String lugar, int tiempo, String observaciones, double latitud, double longitud, String actualizado, int idSeccion, int idNivel, int idBloque, String fecha){

        mInstance = new Component();
        mInstance.setIdClase(idClase);
        mInstance.setProfesor(profesor);
        mInstance.setLugar(lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones(observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setFecha(fecha);
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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mScrollView = getActivity().findViewById(R.id.content_scroll); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesG))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        mMap.setMyLocationEnabled(true);

        agregarMarca(googleMap, latitud, longitud);

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

    @OnClick(R.id.elegirFecha)
    public void elegirFecha(){

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, (month));
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                fechaET.setText(sdf.format(calendar.getTime()));
                cambioFecha = true;
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());//used to hide previous date,month and year
        calendar.add(Calendar.YEAR, 0);
        dialog.show();

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

    public String horasTexto(int minutos){
        String horas = String.valueOf(minutos/60);

        return horas + " HRS";
    }

    public String minutosTexto(int minutos){
        String minutosT = String.valueOf(minutos%60);

        return minutosT + " min";
    }

    public int posicionTipo(String tipo){
        int posicion = 0;

        if(tipo.equalsIgnoreCase("Personal"))
            posicion = 0;
        else  if(tipo.equalsIgnoreCase("Grupal"))
            posicion = 1;

        return posicion;
    }

    public int posicionHoras(String horas){
        int posicion = 0;
        if(horas.equalsIgnoreCase("0 HRS"))
            posicion = 0;
        else if(horas.equalsIgnoreCase("1 HRS"))
            posicion = 1;
        else if(horas.equalsIgnoreCase("2 HRS"))
            posicion = 2;
        else if(horas.equalsIgnoreCase("3 HRS"))
            posicion = 3;

        return posicion;
    }

    public int posicionMinutos(String minutos){
        int posicion = 0;
        if(minutos.equalsIgnoreCase("0 min"))
            posicion = 0;
        else if(minutos.equalsIgnoreCase("5 min"))
            posicion = 1;
        else if(minutos.equalsIgnoreCase("10 min"))
            posicion = 2;
        else if(minutos.equalsIgnoreCase("15 min"))
            posicion = 3;
        else if(minutos.equalsIgnoreCase("20 min"))
            posicion = 4;
        else if(minutos.equalsIgnoreCase("25 min"))
            posicion = 5;
        else if(minutos.equalsIgnoreCase("30 min"))
            posicion = 6;
        else if(minutos.equalsIgnoreCase("35 min"))
            posicion = 7;
        else if(minutos.equalsIgnoreCase("40 min"))
            posicion = 8;
        else if(minutos.equalsIgnoreCase("45 min"))
            posicion = 9;
        else if(minutos.equalsIgnoreCase("50 min"))
            posicion = 10;
        else if(minutos.equalsIgnoreCase("55 min"))
            posicion = 11;

        return posicion;
    }

    @OnClick({R.id.btnActualizarSesion, R.id.btnEliminarSesion})
    public void onClicked(View view) {

        switch (view.getId()){

            case R.id.btnActualizarSesion:
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                String strDate =  mdformat.format(calendar.getTime());

                if(cambioFecha){
                    ServicioTaskUpSesion upSesion = new ServicioTaskUpSesion(getContext(), this.linkAPIUpSesion, this.idClase, horarioET.getText().toString(), lugarET.getText().toString(), tiempo,
                            tipo.getSelectedItem().toString(), observacionesET.getText().toString(), this.latitud, this.longitud, strDate, idSeccion, idNivel, idBloque, fechaET.getText().toString(), true);
                    upSesion.execute();
                }else{
                    ServicioTaskUpSesion upSesion = new ServicioTaskUpSesion(getContext(), this.linkAPIUpSesion, this.idClase, horarioET.getText().toString(), lugarET.getText().toString(), tiempo,
                            tipo.getSelectedItem().toString(), observacionesET.getText().toString(), this.latitud, this.longitud, strDate, idSeccion, idNivel, idBloque, fechaET.getText().toString(),false);
                    upSesion.execute();
                }

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

    public int obtenerMinutosHorario(){
        int horasInt = 0;
        int minutosInt = 0;

        /*
        if(horas.getSelectedItem().toString().equalsIgnoreCase("0 HRS"))
            horasInt = 0;
        else if(horas.getSelectedItem().toString().equalsIgnoreCase("1 HRS"))
            horasInt = 60;
        else if(horas.getSelectedItem().toString().equalsIgnoreCase("2 HRS"))
            horasInt = 120;
        else if(horas.getSelectedItem().toString().equalsIgnoreCase("3 HRS"))
            horasInt = 180;*/
/*
        if(minutos.getSelectedItem().toString().equalsIgnoreCase("0 min"))
            minutosInt = 0;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("5 min"))
            minutosInt = 5;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("10 min"))
            minutosInt = 10;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("15 min"))
            minutosInt = 15;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("20 min"))
            minutosInt = 20;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("25 min"))
            minutosInt = 25;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("30 min"))
            minutosInt = 30;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("35 min"))
            minutosInt = 35;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("40 min"))
            minutosInt = 40;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("45 min"))
            minutosInt = 45;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("50 min"))
            minutosInt = 50;
        else if(minutos.getSelectedItem().toString().equalsIgnoreCase("55 min"))
            minutosInt = 55;*/

        return horasInt + minutosInt;
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
        tomarSesion = null;
        //secciones.setOnItemSelectedListener(null);
        //secciones.setAdapter(null);
        //niveles.setOnItemSelectedListener(null);
        //niveles.setAdapter(null);
        //bloques.setOnItemSelectedListener(null);
        //bloques.setAdapter(null);
        avanzadoVisto = false;
        intermedioVisto = false;
        basicoVisto = false;
        mUnbinder.unbind();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapsDetallesG));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

    }
}
