package com.proathome.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ControladorTomarSesion;
import com.proathome.servicios.cliente.ServicioTaskUpSesion;
import com.proathome.servicios.WorkaroundMapFragment;
import com.proathome.servicios.planes.ServicioTaskFechaServidor;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String linkAPIUpSesion = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/actualizarSesion";
    public static final String TAG = "Detalles de la Sesión";
    private int idSeccion, idNivel, idBloque, tiempo, idCliente;
    private String tipoPlanString;
    @BindView(R.id.tietProfesional)
    TextInputEditText profesionalET;
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
    @BindView(R.id.tipoPlan)
    TextView tipoPlan;
    //@BindView(R.id.horasDisponibles)
    //TextView horasDisponiblesTV;
    public NestedScrollView mScrollView;
    private Unbinder mUnbinder;
    private int idServicio = 0;
    private double longitud = -99.13320799999, latitud = 19.4326077;
    private String fechaSesion = null;
    public static String fechaServidor = null;

    public DetallesGestionarFragment() {

    }

    //TODO DETALLES: Ver si podremos actualizar las cosas y depende de que, si hay profesional asignado,
    // si estamos en plan, etc.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles_gestionar, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ServicioTaskFechaServidor fechaServidor = new ServicioTaskFechaServidor();
        fechaServidor.execute();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
        } else {
            baseDeDatos.close();
        }

        String[] datosHoras = new String[]{"0 HRS", "1 HRS", "2 HRS", "3 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                datosHoras);
        String[] datosMinutos = new String[]{"0 min", "5 min", "10 min", "15 min", "20 min", "25 min",
                "30 min", "35 min", "40 min", "45 min", "50 min", "55 min"};
        ArrayAdapter<String> adapterMinutos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                datosMinutos);
        String[] datosTipo = new String[]{"Personal", "Grupal"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                datosTipo);

//        horas.setAdapter(adapterHoras);
        //minutos.setAdapter(adapterMinutos);
        tipo.setAdapter(adapterTipo);

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");
        fechaET.setKeyListener(null);

        Bundle bun = getArguments();
        idServicio = bun.getInt("idServicio");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesionalET.setText(bun.getString("profesional"));
        lugarET.setText(bun.getString("lugar"));
        //horas.setSelection(posicionHoras(horasTexto(bun.getInt("tiempo"))));
        //minutos.setSelection(posicionMinutos(minutosTexto(bun.getInt("tiempo"))));
        observacionesET.setText(bun.getString("observaciones"));
        fechaET.setText(bun.getString("fecha"));
        fechaSesion = bun.getString("fecha");
        //secciones.setSelection(bun.getInt("idSeccion")-1);
        //niveles.setSelection(bun.getInt("idNivel")-1);
        //bloques.setSelection(bun.getInt("idBloque")-1);
        horas.setText(horasTexto(bun.getInt("tiempo")) + " " + minutosTexto(bun.getInt("tiempo")));
        tipo.setSelection(posicionTipo(bun.getString("tipoServicio")));
        horarioET.setText(bun.getString("horario"));
        tiempo = bun.getInt("tiempo");
        tipoPlan.setText("DENTRO DEL PLAN:" + bun.getString("tipoPlan"));
        tipoPlanString = bun.getString("tipoPlan");

        tomarSesion = new ControladorTomarSesion(getContext(), bun.getInt("idSeccion"),
                bun.getInt("idNivel"), bun.getInt("idBloque"));
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

/*
        secciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(tomarSesion.getSeccion() == Constants.BASICO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                        if(!basicoVisto){

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

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 2")){
                        if(tomarSesion.getNivel() == Constants.BASICO_2){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 3")){
                        if(tomarSesion.getNivel() == Constants.BASICO_3){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 4")){
                        if(tomarSesion.getNivel() == Constants.BASICO_4){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 5")){
                        if(tomarSesion.getNivel() == Constants.BASICO_5){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_5));
                        }
                    }
                }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 1")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_1){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 2")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_2){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 3")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_3){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 4")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_4){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 5")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_5){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_5));
                        }
                    }
                }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 1")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 2")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 3")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 4")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){

                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 5")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){

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

                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 2")){
                    if(tomarSesion.getBloque() == 2){

                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 3")){
                    if(tomarSesion.getBloque() == 3){

                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 4")){
                    if(tomarSesion.getBloque() == 4){

                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 5")){
                    if(tomarSesion.getBloque() == 5){

                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 6")){
                    if(tomarSesion.getBloque() == 6){

                    }else{
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                    }
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 7")){
                    if(tomarSesion.getBloque() == 7){

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
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.mapsDetallesG);
                mapFragment.getMapAsync(this);
            }
        }
    }

    private void showAlert() {
        new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos el permiso de ubicación para ofrecerte una mejor experiencia.")
                .setPositiveButton("Dar permiso", (dialog, which) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    errorMsg();
                })
                .setOnCancelListener(dialog -> {
                    errorMsg();
                })
                .show();
    }

    public void errorMsg() {
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.CLIENTE)
                .setTitleText("¡OH NO!")
                .setContentText("No podemos continuar sin el permiso de ubicación.")
                .setConfirmButton("OK", sweetAlertDialog -> {
                    getFragmentManager().beginTransaction().detach(this).commit();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    public static Component getmInstance(int idServicio, String tipoServicio, String horario, String profesional,
                                         String lugar, int tiempo, String observaciones, double latitud,
                                         double longitud, String actualizado, int idSeccion, int idNivel,
                                         int idBloque, String fecha, String tipoPlan) {

        mInstance = new Component();
        mInstance.setIdServicio(idServicio);
        mInstance.setProfesional(profesional);
        mInstance.setLugar(lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones(observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setFecha(fecha);
        mInstance.setTipoServicio(tipoServicio);
        mInstance.setHorario(horario);
        mInstance.setActualizado(actualizado);
        mInstance.setTipoPlan(tipoPlan);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);

        return mInstance;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = getActivity().findViewById(R.id.content_scroll);
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesG))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        agregarMarca(googleMap, latitud, longitud);

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        perth = mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será aplicado el servicio.").draggable(true));
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
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,
                (arg0, year, month, day_of_month) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, (month));
            calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            fechaET.setText(sdf.format(calendar.getTime()));
            cambioFecha = true;
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        //Si estamos en plan activo, ajustar rango de fecha hasta fin de expiración
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaFin = null;
        try {
            fechaFin = sdf.parse(SesionesFragment.FECHA_FIN);
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        if(!SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR") && !SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR_PLAN"))
            dialog.getDatePicker().setMaxDate(fechaFin.getTime());

        calendar.add(Calendar.YEAR, 0);
        dialog.show();

    }

    @OnClick(R.id.elegirHorario)
    public void onChooserClicked(){

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
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
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                String strDate =  mdformat.format(calendar.getTime());

                if(cambioFecha){
                    ServicioTaskUpSesion upSesion = new ServicioTaskUpSesion(getContext(),
                            this.linkAPIUpSesion, this.idServicio, horarioET.getText().toString(),
                                lugarET.getText().toString(), tiempo,
                            tipo.getSelectedItem().toString(), observacionesET.getText().toString(),
                                this.latitud, this.longitud, strDate, idSeccion, idNivel, idBloque,
                                    fechaET.getText().toString(), true);
                    upSesion.execute();
                }else{
                    ServicioTaskUpSesion upSesion = new ServicioTaskUpSesion(getContext(),
                            this.linkAPIUpSesion, this.idServicio, horarioET.getText().toString(),
                                lugarET.getText().toString(), tiempo,
                            tipo.getSelectedItem().toString(), observacionesET.getText().toString(),
                                this.latitud, this.longitud, strDate, idSeccion, idNivel, idBloque,
                                    fechaET.getText().toString(),false);
                    upSesion.execute();
                }

                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().finish();
                break;
            case R.id.btnEliminarSesion:
                //Verificar que sea 24 hrs antes
//TODO DETALLES: Verificar que no tenga profesional asignado o preguntarle a NERIQUE.
                Calendar calendarHoy = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaHoy = sdf.format(calendarHoy.getTime());

                try{
                    Date fechaActual = sdf.parse(fechaHoy);
                    Date fechaSesionFin = sdf.parse(fechaSesion);
                    Date fechaServidor = sdf.parse(DetallesGestionarFragment.fechaServidor);
                    if(fechaActual.equals(fechaServidor)){
                        if(fechaActual.equals(fechaSesionFin)){
                            errorServicioMsg("La sesión sólo podía ser eliminada el " +
                                    "día anterior al servicio.");
                        }else if(fechaActual.before(fechaSesionFin)){
                            eliminarSesion();
                            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                            getActivity().finish();
                        }

                    }else{
                        errorServicioMsg("Fecha del dispositivo erronea.");
                    }
                }catch(ParseException | JSONException ex){
                    ex.printStackTrace();
                }
                break;
        }

    }

    private void eliminarSesion() throws JSONException {
        JSONObject jsonData = new JSONObject();
        jsonData.put("idSesion", this.idServicio);
        jsonData.put("idCliente", this.idCliente);
        jsonData.put("tipoPlan", this.tipoPlan);
        jsonData.put("horas", this.horas);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            new SweetAlert(SesionesFragment.contexto, SweetAlert.WARNING_TYPE, SweetAlert.CLIENTE)
                    .setTitleText("¡AVISO!")
                    .setContentText(response)
                    .show();
        }, APIEndPoints.ELIMINAR_SESION, WebServicesAPI.POST, jsonData);
        webServicesAPI.execute();
    }

    public void errorServicioMsg(String mensaje){
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.CLIENTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
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
