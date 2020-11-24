package com.proathome.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
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
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ControladorTomarSesion;
import com.proathome.controladores.estudiante.STRegistroSesionesEstudiante;
import com.proathome.controladores.estudiante.ServicioTaskSesionActual;
import com.proathome.controladores.planes.ServicioTaskValidarPlan;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NuevaSesionFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = "Nueva Sesión";
    public static boolean basicoVisto, intermedioVisto, avanzadoVisto;
    private int idCliente = 0;
    private GoogleMap mMap;
    private Marker perth;
    private ScrollView mScrollView;
    private double latitud, longitud;
    private String registrarSesionREST = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/agregarSesion";
    private Unbinder mUnbinder;
    public static ControladorTomarSesion tomarSesion;
    @BindView(R.id.text_direccionET)
    TextInputEditText direccionET;
    @BindView(R.id.text_horarioET)
    TextInputEditText horarioET;
    @BindView(R.id.text_fechaET)
    TextInputEditText fechaET;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static Spinner secciones;
    public static Spinner niveles;
    public static Spinner bloques;
    public static TextView horasDisponiblesTV;
    public static int minutosAnteriores = 0;
    public static int minutosEstablecidos = 0;
    public static int nuevoMonedero = 0;
    public static boolean rutaRecomendada = false;

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
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nueva_sesion, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        secciones = view.findViewById(R.id.secciones);
        niveles = view.findViewById(R.id.niveles);
        bloques = view.findViewById(R.id.bloques);
        horasDisponiblesTV = view.findViewById(R.id.horasDisponibles);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
            ServicioTaskSesionActual servicioTaskSesionActual =
                    new ServicioTaskSesionActual(getContext(), idCliente, ServicioTaskSesionActual.NUEVA_SESION_FRAGMENT);
            servicioTaskSesionActual.execute();
        } else {
            baseDeDatos.close();
        }

        ServicioTaskValidarPlan validarPlan = new ServicioTaskValidarPlan(getContext(), idCliente);
        validarPlan.execute();

        baseDeDatos.close();

        String[] datosHoras = new String[]{"0 HRS", "1 HRS", "2 HRS", "3 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosHoras);
        String[] datosMinutos = new String[]{"0 min", "5 min", "10 min", "15 min", "20 min", "25 min",
                "30 min", "35 min", "40 min", "45 min", "50 min", "55 min"};
        ArrayAdapter<String> adapterMinutos = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosMinutos);
        String[] datosTipo = new String[]{"Personal", "Grupal"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosTipo);

        horas.setAdapter(adapterHoras);
        minutos.setAdapter(adapterMinutos);
        tipo.setAdapter(adapterTipo);

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");
        fechaET.setKeyListener(null);
        fechaET.setText("");

        btnSolicitar.setOnClickListener(v -> {

            if (!direccionET.getText().toString().trim().equalsIgnoreCase("") &&
                    !observacionesET.getText().toString().trim().equalsIgnoreCase("") &&
                        !fechaET.getText().toString().trim().equalsIgnoreCase("")) {

                if (obtenerMinutosHorario() == 0) {
                    new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                            .setTitleText("¡ERROR!")
                            .setContentText("Elige el tiempo de duración de la clase.")
                            .show();
                } else {
                    String direccion = direccionET.getText().toString();
                    String extras = observacionesET.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                    String strDate = mdformat.format(calendar.getTime());
                    if (rutaRecomendada) {
                        /*TODO FLUJO_EJECUTAR_PLAN: Validaciones correspondientes en el FragmentNuevaSesion
                           (Verificar que el tiempo sea acorde a las horas diponibles si hay PLAN activo)
                           -> descontamos horas del monedero.
                            En la tabla sesiones agregar campo tipoPlan.*/
                        int minutosRestantes = minutosEstablecidos - minutosAnteriores;
                        if (obtenerMinutosHorario() <= minutosRestantes) {

                            if (SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR")) {
                                STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(),
                                        registrarSesionREST, idCliente, horarioET.getText().toString(), direccion,
                                            obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1,
                                                niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1,
                                                    extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate,
                                                        fechaET.getText().toString(), true, SesionesFragment.PLAN);
                                registro.execute();
                                direccionET.setText("");
                                horarioET.setText("");
                                observacionesET.setText("");
                                succesAlert();
                            } else {
                                if (obtenerMinutosHorario() <= SesionesFragment.MONEDERO) {//Eliminar horas de monedero
                                    //TODO FLUJO_EJECUTAR_PLAN: En esta peticion restamos en el monedero el tiempo elegido y verificamos después la vigencia del PLAN en el servidor.
                                    nuevoMonedero = SesionesFragment.MONEDERO - obtenerMinutosHorario();

                                    STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(),
                                            registrarSesionREST, idCliente, horarioET.getText().toString(), direccion,
                                                obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1,
                                                    niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1,
                                                        extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate,
                                                            fechaET.getText().toString(), true, SesionesFragment.PLAN);
                                    registro.execute();
                                    direccionET.setText("");
                                    horarioET.setText("");
                                    observacionesET.setText("");
                                    succesAlert();
                                } else {
                                    new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                                            .setTitleText("¡ERROR!")
                                            .setContentText("Elige un tiempo de clase a corde" +
                                                    " a tus horas disponibles de tu plan activo.")
                                            .show();
                                }
                            }

                        } else {
                            new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                                    .setTitleText("¡ERROR!")
                                    .setContentText("Elige un tiempo de clase a corde a el tiempo" +
                                            " faltante del bloque en curso.")
                                    .show();
                        }
                    } else {
                        /*TODO FLUJO_EJECUTAR_PLAN: Validaciones correspondientes en el FragmentNuevaSesion
                            (Verificar que el tiempo sea acorde a las horas diponibles si hay PLAN activo)
                            -> descontamos horas del monedero.
                            En la tabla sesiones agregar campo tipoPlan.*/

                        if (SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR")) {

                            STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(),
                                    registrarSesionREST, idCliente, horarioET.getText().toString(), direccion,
                                        obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1,
                                        niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1,
                                                    extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate,
                                                        fechaET.getText().toString(), false, SesionesFragment.PLAN);
                            registro.execute();
                            direccionET.setText("");
                            horarioET.setText("");
                            observacionesET.setText("");
                            succesAlert();
                        } else {
                            if (obtenerMinutosHorario() <= SesionesFragment.MONEDERO) {//Eliminar horas de monedero
                                //TODO FLUJO_EJECUTAR_PLAN: En esta peticion restamos en el monedero el tiempo elegido y verificamos después la vigencia del PLAN en el servidor.
                                nuevoMonedero = SesionesFragment.MONEDERO - obtenerMinutosHorario();

                                STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(),
                                        registrarSesionREST, idCliente, horarioET.getText().toString(), direccion,
                                            obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1,
                                            niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1,
                                                        extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate,
                                                            fechaET.getText().toString(), false, SesionesFragment.PLAN);
                                registro.execute();
                                direccionET.setText("");
                                horarioET.setText("");
                                observacionesET.setText("");
                                succesAlert();
                            } else {
                                new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                                        .setTitleText("¡ERROR!")
                                        .setContentText("Elige un tiempo de clase a corde" +
                                                " a tus horas disponibles de tu plan activo.")
                                        .show();
                            }
                        }
                    }
                }

            } else {
                new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                        .setTitleText("¡ERROR!")
                        .setContentText("Llena todos los campos.")
                        .show();
            }


        });

        toolbar.setTitle("Nueva Sesión");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {

            dismiss();

        });

        return view;

    }

    public void succesAlert(){
        new SweetAlert(getContext(), SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡GENIAL!")
                .setContentText("Revisa tu nueva clase en Inicio o en Gestión de Sesiones.")
                .setConfirmButton("¡VAMOS!", sweetAlertDialog -> {
                    dismiss();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    public int obtenerMinutosHorario() {
        int horasInt = 0;
        int minutosInt = 0;

        if (horas.getSelectedItem().toString().equalsIgnoreCase("0 HRS"))
            horasInt = 0;
        else if (horas.getSelectedItem().toString().equalsIgnoreCase("1 HRS"))
            horasInt = 60;
        else if (horas.getSelectedItem().toString().equalsIgnoreCase("2 HRS"))
            horasInt = 120;
        else if (horas.getSelectedItem().toString().equalsIgnoreCase("3 HRS"))
            horasInt = 180;

        if (minutos.getSelectedItem().toString().equalsIgnoreCase("0 min"))
            minutosInt = 0;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("5 min"))
            minutosInt = 5;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("10 min"))
            minutosInt = 10;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("15 min"))
            minutosInt = 15;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("20 min"))
            minutosInt = 20;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("25 min"))
            minutosInt = 25;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("30 min"))
            minutosInt = 30;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("35 min"))
            minutosInt = 35;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("40 min"))
            minutosInt = 40;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("45 min"))
            minutosInt = 45;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("50 min"))
            minutosInt = 50;
        else if (minutos.getSelectedItem().toString().equalsIgnoreCase("55 min"))
            minutosInt = 55;

        return horasInt + minutosInt;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mScrollView = getView().findViewById(R.id.scrollMap); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        agregarMarca(googleMap, 19.4326077, -99.13320799999);

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        perth = mMap.addMarker(new MarkerOptions().position(ubicacion).title("Mueve el marcador para elegir" +
                " el punto de encuentro.").draggable(true));
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
    public void elegirFecha() {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,
                (arg0, year, month, day_of_month) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, (month));
                    //Un día después.
                    calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                    String myFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    fechaET.setText(sdf.format(calendar.getTime()));
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        //Si estamos en plan activo, ajustar rango de fecha hasta fin de expiración
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaFin = null;
        try {
            if(!SesionesFragment.FECHA_FIN.equalsIgnoreCase("null"))
                fechaFin = sdf.parse(SesionesFragment.FECHA_FIN);
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        if(!SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR"))
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

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();
        tomarSesion = null;
        secciones.setOnItemSelectedListener(null);
        secciones.setAdapter(null);
        niveles.setOnItemSelectedListener(null);
        niveles.setAdapter(null);
        bloques.setOnItemSelectedListener(null);
        bloques.setAdapter(null);
        avanzadoVisto = false;
        intermedioVisto = false;
        basicoVisto = false;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

}
