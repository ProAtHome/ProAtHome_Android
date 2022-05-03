package com.proathome.Views.cliente.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionPresenter;
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionView;
import com.proathome.Presenters.cliente.NuevaSesionFrPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.WorkaroundMapFragment;
import com.proathome.Servicios.cliente.ControladorTomarSesion;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;
import com.proathome.Utils.pojos.Component;
import com.proathome.Utils.SweetAlert;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NuevaSesionFragment extends DialogFragment implements OnMapReadyCallback, NuevaSesionView {

    private boolean primeraVezI1 = true, primeraVezI2 = true, primeraVezI3 = true, primeraVezI4 = true, primeraVezI5 = true;
    private boolean primeraVezA1 = true, primeraVezA2 = true, primeraVezA3 = true, primeraVezA4 = true, primeraVezA5 = true;
    private boolean primeraVezB1 = true, primeraVezB2 = true, primeraVezB3 = true, primeraVezB4 = true, primeraVezB5 = true;
    public static final String TAG = "Nueva Sesión";
    private String[] datosHoras = null, datosTipo = new String[]{"Personal", "Grupal"}, datosPersonas = new String[]{"1"};
    public static boolean basicoVisto, intermedioVisto, avanzadoVisto, rutaFinalizada, disponibilidad, banco;
    public static int idCliente = 0, horasDisponibles = 0;
    private int seccion, nivel, bloque, minutos_horas;
    private GoogleMap mMap;
    private Marker perth;
    private ScrollView mScrollView;
    private double latitud, longitud;
    public static String correoCliente;
    public static DialogFragment dialogFragment;
    public static ControladorTomarSesion tomarSesion;
    private String nombreTitular = null, tarjeta = null, mes = null, ano = null;
    private NuevaSesionPresenter nuevaSesionPresenter;
    private Unbinder mUnbinder;
    private SupportMapFragment mapFragment;
    private ProgressDialog progressDialog;
    public static boolean clickSolicitar = false;

    @BindView(R.id.text_direccionET)
    TextInputEditText direccionET;
    @BindView(R.id.text_horarioET)
    TextInputEditText horarioET;
    @BindView(R.id.text_fechaET)
    TextInputEditText fechaET;
    @BindView(R.id.text_observacionesET)
    TextInputEditText observacionesET;
    @BindView(R.id.horas)
    Spinner horas;
    @BindView(R.id.tipo)
    Spinner tipo;
    @BindView(R.id.personas)
    Spinner personas;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.secciones)
    Spinner secciones;
    @BindView(R.id.niveles)
    Spinner niveles;
    @BindView(R.id.bloques)
    Spinner bloques;
    @BindView(R.id.horasDisponibles)
    TextView horasDisponiblesTV;
    @BindView(R.id.btnSolicitar)
    MaterialButton btnSolicitar;

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
        nuevaSesionPresenter = new NuevaSesionFrPresenterImpl(this);
        showProgress();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap == null) {
                mapFragment = (WorkaroundMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(R.id.mapNueva);
             if(mapFragment != null)
                mapFragment.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG11", "Entro");
        View view = inflater.inflate(R.layout.fragment_nueva_sesion, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        dialogFragment = this;

        this.idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        this.correoCliente = SharedPreferencesManager.getInstance(getContext()).getCorreoCliente();

        Bundle bundle = getArguments();
        //DATOS BANCO
        PreOrdenServicio.nombreTitular = bundle.getString("nombreTitular");
        PreOrdenServicio.tarjeta = bundle.getString("tarjeta");
        PreOrdenServicio.mes = bundle.getString("mes");
        PreOrdenServicio.ano = bundle.getString("ano");
        banco = bundle.getBoolean("banco");
        //DATOS RUTA
        seccion = bundle.getInt("idSeccion");
        nivel = bundle.getInt("idNivel");
        bloque = bundle.getInt("idBloque");
        minutos_horas = bundle.getInt("horas");
        rutaFinalizada = bundle.getBoolean("rutaFinalizada");

        setAdapters(seccion, nivel, bloque, minutos_horas);
        setSeccionesListener(seccion, nivel, bloque, minutos_horas);
        setNivelesListener(seccion, nivel, bloque, minutos_horas);
        setBloquesListener(seccion, nivel, bloque, minutos_horas);

        if(rutaFinalizada){
            SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE,"¡AVISO!", "La Ruta de Aprendizaje fue finalizada, pero puedes repasar tus lecciones.", false, null, null);
            horasDisponiblesTV.setVisibility(View.INVISIBLE);
        }

        //nuevaSesionPresenter.getSesionActual(idCliente, getContext());
        //nuevaSesionPresenter.validarPlan(idCliente, getContext());
        //Servicio para validar que ya tenemos datos bancarios registrados para lanzar la preOrden.
        //nuevaSesionPresenter.validarBanco(idCliente, getContext());

        setAdaptersInfoGeneral();
        listenerPersonas();

        toolbar.setTitle("Nueva Sesión");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideProgress();
    }

    public void setAdaptersInfoGeneral(){
        if((horasDisponibles / 60) == 1)
            datosHoras = new String[]{"1 HRS"};
        else
            datosHoras = new String[]{"1 HRS", "2 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosHoras);
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosTipo);
        ArrayAdapter<String> adapterPersonas = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosPersonas);

        horas.setAdapter(adapterHoras);
        tipo.setAdapter(adapterTipo);
        personas.setAdapter(adapterPersonas);

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");
        fechaET.setKeyListener(null);
        fechaET.setText("");
    }

    @Override
    public void validacionPlanes_Ruta(){
        String direccion = direccionET.getText().toString();
        String extras = observacionesET.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String strDate = mdformat.format(calendar.getTime());
        if (rutaRecomendada) {
                        /*TODO FLUJO_EJECUTAR_PLAN: Validaciones correspondientes en el FragmentNuevaSesion
                           (Verificar que el tiempo sea acorde a las horas diponibles si hay PLAN activo)
                           -> descontamos horas del monedero*/
            int minutosRestantes = minutosEstablecidos - minutosAnteriores;
            if (obtenerMinutosHorario() <= minutosRestantes) {
                if (SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR")) {
                    registroDeServicio(idCliente, horarioET.getText().toString(), direccion, obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1,
                            niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1, extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate,
                            fechaET.getText().toString(), true, SesionesFragment.PLAN, Integer.parseInt(personas.getSelectedItem().toString()));
                }else {
                    if (obtenerMinutosHorario() <= SesionesFragment.MONEDERO) {//Eliminar horas de monedero
                        //TODO FLUJO_EJECUTAR_PLAN: En esta peticion restamos en el monedero el tiempo elegido y verificamos después la vigencia del PLAN en el servidor.
                        nuevoMonedero = SesionesFragment.MONEDERO - obtenerMinutosHorario();
                        registroDeServicio(idCliente, horarioET.getText().toString(), direccion,
                                obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1, niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1,
                                extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate, fechaET.getText().toString(), true,
                                SesionesFragment.PLAN, Integer.parseInt(personas.getSelectedItem().toString()));
                    } else {
                        String mensaje = null;
                        if(SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR_PLAN"))
                            mensaje = "Elige un tiempo de servicio a corde a tus horas disponibles agregadas a tu perfil.";
                        else
                            mensaje = "Elige un tiempo de servicio a corde a tus horas disponibles de tu plan activo.";

                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
                    }
                }

            } else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Elige un tiempo de servicio a corde a el tiempo faltante del bloque en curso.", false, null, null);
        } else {
            /*TODO FLUJO_EJECUTAR_PLAN: Validaciones correspondientes en el FragmentNuevaSesion
                (Verificar que el tiempo sea acorde a las horas diponibles si hay PLAN activo)
                -> descontamos horas del monedero.
                En la tabla sesiones agregar campo tipoPlan.*/
            if (SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR")) {
                registroDeServicio(idCliente, horarioET.getText().toString(), direccion,
                        obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1, niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1,
                        extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate, fechaET.getText().toString(), false,
                        SesionesFragment.PLAN, Integer.parseInt(personas.getSelectedItem().toString()));
            }else {
                if (obtenerMinutosHorario() <= SesionesFragment.MONEDERO) {//Eliminar horas de monedero
                    //TODO FLUJO_EJECUTAR_PLAN: En esta peticion restamos en el monedero el tiempo elegido y verificamos después la vigencia del PLAN en el servidor.
                    nuevoMonedero = SesionesFragment.MONEDERO - obtenerMinutosHorario();
                    registroDeServicio(idCliente, horarioET.getText().toString(), direccion, obtenerMinutosHorario(), secciones.getSelectedItemPosition() + 1,
                            niveles.getSelectedItemPosition() + 1, bloques.getSelectedItemPosition() + 1, extras, tipo.getSelectedItem().toString(),
                            latitud, longitud, strDate, fechaET.getText().toString(), false, SesionesFragment.PLAN, Integer.parseInt(personas.getSelectedItem().toString()));
                } else {
                    String mensaje = null;
                    if(SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR_PLAN"))
                        mensaje = "Finaliza los servicios que compraste con tu monedero para seguir comprando.";
                    else
                        mensaje = "Elige un tiempo de servicio a corde a tus horas disponibles de tu plan activo.";

                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
                }
            }
        }
    }

    @Override
    public void showProgress() {
        if(getContext() != null)
            progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    public void registroDeServicio(int idCliente, String horario, String lugar,
                                int tiempo, int idSeccion, int idNivel, int idBloque, String extras, String tipoServicio,
                                double latitud, double longitud, String actualizado, String fecha, boolean sumar, String tipoPlan, int personas){
        /* TODO Validar si hay pago o no dependiendo el PLAN*/
        /*TODO FLUJO_EJECUTAR_PLAN: Servicio en modo PLAN activo?
                    Si, entonces, Al iniciar el servicio no mostramos Pre Orden ya que está pagado.*/
        if (!InicioCliente.planActivo.equals("PARTICULAR")) {
            //Guardamos la info de PAGO
            Bundle bundle = getBundleSesion(idCliente, horario, lugar, tiempo, idSeccion, idNivel, idBloque, extras, tipoServicio, latitud, longitud, actualizado,
                    fecha, sumar, tipoPlan, personas);
            guardarPago(bundle, "PLAN - " + SesionesFragment.PLAN);
        }else{
            btnSolicitar.setEnabled(false);
            PreOrdenServicio.sesion = "Sesión: " + Component.getSeccion(secciones.getSelectedItemPosition() + 1) + " / "
                    + Component.getNivel(secciones.getSelectedItemPosition() + 1, niveles.getSelectedItemPosition() + 1) + " / "
                    + Component.getBloque(bloques.getSelectedItemPosition() + 1);
            PreOrdenServicio.tiempo = "Tiempo: " + obtenerHorario(obtenerMinutosHorario());
            PreOrdenServicio.tiempoPasar = obtenerMinutosHorario();
            PreOrdenServicio.idSeccion = (secciones.getSelectedItemPosition() + 1);
            //Pre Orden ya que no esta pagada.
            Bundle bundle = getBundleSesion(idCliente, horario, lugar, tiempo, idSeccion, idNivel, idBloque, extras, tipoServicio, latitud, longitud, actualizado,
                    fecha, sumar, tipoPlan, personas);
            PreOrdenServicio preOrdenServicio = new PreOrdenServicio();
            preOrdenServicio.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            preOrdenServicio.show(fragmentTransaction, "PreOrden");
            btnSolicitar.setEnabled(true);
        }
    }

    private void guardarPago(Bundle bundle, String token){
        nuevaSesionPresenter.guardarPago(idCliente, token, bundle, rutaFinalizada, nuevoMonedero, getContext());
    }

    public Bundle getBundleSesion(int idCliente, String horario, String lugar,
                                  int tiempo, int idSeccion, int idNivel, int idBloque, String extras, String tipoServicio,
                                  double latitud, double longitud, String actualizado, String fecha, boolean sumar, String tipoPlan, int personas){
        Bundle bundle = new Bundle();
        bundle.putInt("idCliente", idCliente);
        bundle.putString("horario", horario);
        bundle.putString("lugar", lugar);
        bundle.putInt("tiempo", tiempo);
        bundle.putInt("idSeccion", idSeccion);
        bundle.putInt("idNivel", idNivel);
        bundle.putInt("idBloque", idBloque);
        bundle.putString("extras", extras);
        bundle.putString("tipoServicio", tipoServicio);
        bundle.putDouble("latitud", latitud);
        bundle.putDouble("longitud", longitud);
        bundle.putString("actualizado", actualizado);
        bundle.putString("fecha", fecha);
        bundle.putBoolean("sumar", sumar);
        bundle.putString("tipoPlan", tipoPlan);
        bundle.putInt("personas", personas);

        return bundle;
    }

    public void listenerPersonas(){
        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] datosPersonas = null;
                if(tipo.getSelectedItem().toString().equals("Personal"))
                    datosPersonas = new String[]{"1"};
                else if(tipo.getSelectedItem().toString().equals("Grupal"))
                    datosPersonas = new String[]{"1", "2", "3", "4", "5"};

                ArrayAdapter<String> adapterPersonas = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, datosPersonas);
                personas.setAdapter(adapterPersonas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

    public int obtenerMinutosHorario() {
        int horasInt = 0;

        if (horas.getSelectedItem().toString().equalsIgnoreCase("0 HRS"))
            horasInt = 0;
        else if (horas.getSelectedItem().toString().equalsIgnoreCase("1 HRS"))
            horasInt = 60;
        else if (horas.getSelectedItem().toString().equalsIgnoreCase("2 HRS"))
            horasInt = 120;
        else if (horas.getSelectedItem().toString().equalsIgnoreCase("3 HRS"))
            horasInt = 180;

        return horasInt;
    }

    public void agregarMarca(double lat, double longi){
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

    @OnClick(R.id.btnSolicitar)
    public void solicitar(){
        if (!direccionET.getText().toString().trim().equalsIgnoreCase("") &&
                !observacionesET.getText().toString().trim().equalsIgnoreCase("") &&
                !fechaET.getText().toString().trim().equalsIgnoreCase("")) {
            if (obtenerMinutosHorario() != 0) {
                if(banco){
                    //VALIDAR HORA Y FECHA DEL SERVICIO
                    if(!clickSolicitar){
                        nuevaSesionPresenter.validarEmpalme(SharedPreferencesManager.getInstance(getContext()).getIDCliente(), fechaET.getText().toString(), horarioET.getText().toString());
                        clickSolicitar = true;
                    }
                } else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡AVISO!","Sin datos bancarios.", false, null, null);
            } else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Elige el tiempo de duración de el servicio.", false, null, null);
        } else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos.", false, null, null);
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
            else//Fecha fin de 2023 aprox
                fechaFin = sdf.parse("2022-12-31");
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        //if(!SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR") && !SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR_PLAN"))
          //  dialog.getDatePicker().setMaxDate(fechaFin.getTime());
        //else
        dialog.getDatePicker().setMaxDate(fechaFin.getTime());

        calendar.add(Calendar.YEAR, 0);
        dialog.show();
    }

    @OnClick(R.id.elegirHorario)
    public void onChooserClicked(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
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

        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Elige el horario de tu preferencia para comenzar.")
                .setAdapter(adapter, (dialog, which) ->{
                    horarioET.setText(adapter.getItem(which));
                })
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = getView().findViewById(R.id.scrollMapNueva); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapNueva))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null)
            agregarMarca(location.getLatitude(), location.getLongitude());
        else{
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null)
                agregarMarca(location.getLatitude(), location.getLongitude());
            else agregarMarca(19.432608, -99.133208);
        }
    }

    @Override
    public void setAdapters(int seccion, int nivel, int bloque, int minutos_horas) {
        minutosAnteriores = minutos_horas;
        tomarSesion = new ControladorTomarSesion(getContext(), seccion, nivel, bloque);
        minutosEstablecidos = tomarSesion.validarHorasRestantes(seccion, nivel, bloque);

        secciones.setAdapter(tomarSesion.obtenerSecciones());
        secciones.setSelection(seccion-1);
        niveles.setAdapter(tomarSesion.obtenerNiveles());
        niveles.setSelection(nivel-1);
        bloques.setAdapter(tomarSesion.obtenerBloques());
        bloques.setSelection(bloque-1);
    }

    @Override
    public void setSeccionesListener(int seccion, int nivel, int bloque, int minutos_horas) {
        secciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(tomarSesion.getSeccion() == Constants.BASICO){
                    if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                        if(!basicoVisto)
                            basicoVisto = true;
                        else
                            niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.BASICO));
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
                        if(!intermedioVisto)
                            intermedioVisto = true;
                        else
                            niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
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
                        if(!avanzadoVisto)
                            avanzadoVisto = true;
                        else
                            niveles.setAdapter(tomarSesion.obtenerNiveles(Constants.AVANZADO));
                    }
                }

                if(tomarSesion.validarSesionCorrecta(seccion, nivel, bloque, secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1)){
                    horasDisponiblesTV.setText("*Tienes " + tomarSesion.minutosAHRS(minutos_horas) + " / " +  tomarSesion.minutosAHRS(tomarSesion.validarHorasRestantes(seccion, nivel, bloque)) + " de avance en el actual bloque*");
                    rutaRecomendada = true;
                }else
                    rutaRecomendada = false;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public void setNivelesListener(int seccion, int nivel, int bloque, int minutos_horas) {
        niveles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")) {
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 1")){
                        if(tomarSesion.getNivel() == Constants.BASICO_1){
                            if(!primeraVezB1)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_1));
                            primeraVezB1 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 2")){
                        if(tomarSesion.getNivel() == Constants.BASICO_2){
                            if(!primeraVezB2)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_2));
                            primeraVezB2 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 3")){
                        if(tomarSesion.getNivel() == Constants.BASICO_3){
                            if(!primeraVezB3)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_3));
                            primeraVezB3 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 4")){
                        if(tomarSesion.getNivel() == Constants.BASICO_4){
                            if(!primeraVezB4)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_4));
                            primeraVezB4 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 5")){
                        if(tomarSesion.getNivel() == Constants.BASICO_5){
                            if(!primeraVezB5)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_5));
                            primeraVezB5 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_5));
                        }
                    }
                }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 1")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_1){
                            if(!primeraVezI1)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_1));
                            primeraVezI1 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 2")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_2){
                            if(!primeraVezI2)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_2));
                            primeraVezI2 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 3")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_3){
                            if(!primeraVezI3)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_3));
                            primeraVezI3 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 4")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_4){
                            if(!primeraVezI4)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_4));
                            primeraVezI4 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 5")){
                        if(tomarSesion.getNivel() == Constants.INTERMEDIO_5){
                            if(!primeraVezI5)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_5));
                            primeraVezI5 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_5));
                        }
                    }
                }else if(secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                    if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 1")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_1){
                            if(!primeraVezA1)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_1));
                            primeraVezA1 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_1));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 2")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_2){
                            if(!primeraVezA2)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_2));
                            primeraVezA2 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_2));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 3")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_3){
                            if(!primeraVezA3)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_3));
                            primeraVezA3 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_3));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 4")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_4){
                            if(!primeraVezA4)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_4));
                            primeraVezA4 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_4));
                        }
                    }else if(niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 5")){
                        if(tomarSesion.getNivel() == Constants.AVANZADO_5){
                            if(!primeraVezA5)
                                bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_5));
                            primeraVezA5 = false;
                        }else {
                            horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                            bloques.setAdapter(tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_5));
                        }
                    }
                }

                if(tomarSesion.validarSesionCorrecta(seccion, nivel, bloque, secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1)){
                    horasDisponiblesTV.setText("*Tienes " + tomarSesion.minutosAHRS(minutos_horas) + " / " +  tomarSesion.minutosAHRS(tomarSesion.validarHorasRestantes(seccion, nivel, bloque)) + " de avance en el actual bloque*");
                    rutaRecomendada = true;
                }else{
                    rutaRecomendada = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setBloquesListener(int seccion, int nivel, int bloque, int minutos_horas) {
        bloques.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 1")){
                    if(tomarSesion.getBloque() != 1)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 2")){
                    if(tomarSesion.getBloque() != 2)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 3")){
                    if(tomarSesion.getBloque() != 3)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 4")){
                    if(tomarSesion.getBloque() != 4)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 5")){
                    if(tomarSesion.getBloque() != 5)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 6")){
                    if(tomarSesion.getBloque() != 6)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }else if(bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 7")){
                    if(tomarSesion.getBloque() != 7)
                        horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                }

                if(tomarSesion.validarSesionCorrecta(seccion, nivel, bloque, secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1)){
                    horasDisponiblesTV.setText("*Tienes " + tomarSesion.minutosAHRS(minutos_horas) + " / " +  tomarSesion.minutosAHRS(tomarSesion.validarHorasRestantes(seccion, nivel, bloque)) + " de avance en el actual bloque*");
                    rutaRecomendada = true;
                }else
                    rutaRecomendada = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setBanco(boolean valor) {
        banco = valor;
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void finishFragment() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        tomarSesion = null;
        avanzadoVisto = false;
        intermedioVisto = false;
        basicoVisto = false;
        if(mapFragment != null)
            mapFragment = null;
        if(secciones != null){
            secciones.setOnItemSelectedListener(null);
            secciones.setAdapter(null);
        }
        if(niveles != null){
            niveles.setOnItemSelectedListener(null);
            niveles.setAdapter(null);
        }
        if(bloques != null){
            bloques.setOnItemSelectedListener(null);
            bloques.setAdapter(null);
        }

        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }

        SesionesFragment.click = false;

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapNueva));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        SesionesFragment.fragment.getFragmentManager().beginTransaction().detach(SesionesFragment.fragment).attach(SesionesFragment.fragment).commit();
    }

}
