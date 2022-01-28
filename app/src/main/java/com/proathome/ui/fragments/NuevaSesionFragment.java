package com.proathome.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.ui.InicioCliente;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.WorkaroundMapFragment;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ControladorTomarSesion;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Component;
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

public class NuevaSesionFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = "Nueva Sesión";
    public static boolean basicoVisto, intermedioVisto, avanzadoVisto, rutaFinalizada;
    public static int idCliente = 0, horasDisponibles = 0;
    private GoogleMap mMap;
    private Marker perth;
    private ScrollView mScrollView;
    private double latitud, longitud;
    public static boolean banco = false, disponibilidad;
    public static String planSesion, correoCliente;
    public static DialogFragment dialogFragment;
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
    @BindView(R.id.tipo)
    Spinner tipo;
    @BindView(R.id.personas)
    Spinner personas;
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
                    .getSupportFragmentManager().findFragmentById(R.id.mapNueva);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_sesion, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        dialogFragment = this;

        secciones = view.findViewById(R.id.secciones);
        niveles = view.findViewById(R.id.niveles);
        bloques = view.findViewById(R.id.bloques);
        horasDisponiblesTV = view.findViewById(R.id.horasDisponibles);

        this.idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        this.correoCliente = SharedPreferencesManager.getInstance(getContext()).getCorreoCliente();
        ServiciosCliente.getSesionActual(ServiciosCliente.NUEVA_SESION_FRAGMENT, idCliente, getContext());

        ServiciosCliente.validarPlan(idCliente, getContext());
        //Servicio para validar que ya tenemos datos bancarios registrados para lanzar la preOrden.
        validarBanco();
        /*Datos de pre Orden listos para ser lanzados :)
        getPreOrden();*/

        String[] datosHoras = null;
        if((NuevaSesionFragment.horasDisponibles / 60) == 1)
            datosHoras = new String[]{"1 HRS"};
        else
            datosHoras = new String[]{"1 HRS", "2 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosHoras);
        String[] datosTipo = new String[]{"Personal", "Grupal"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosTipo);
        String[] datosPersonas = new String[]{"1"};
        ArrayAdapter<String> adapterPersonas = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosPersonas);

        horas.setAdapter(adapterHoras);
        tipo.setAdapter(adapterTipo);
        personas.setAdapter(adapterPersonas);

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");
        fechaET.setKeyListener(null);
        fechaET.setText("");

        btnSolicitar.setOnClickListener(v -> {
            if (!direccionET.getText().toString().trim().equalsIgnoreCase("") &&
                    !observacionesET.getText().toString().trim().equalsIgnoreCase("") &&
                        !fechaET.getText().toString().trim().equalsIgnoreCase("")) {
                if (obtenerMinutosHorario() != 0) {
                    if(banco)
                        validacionPlanes_Ruta();
                    else
                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡AVISO!","Sin datos bancarios.", false, null, null);
                } else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Elige el tiempo de duración de el servicio.", false, null, null);
            } else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos.", false, null, null);
        });

        toolbar.setTitle("Nueva Sesión");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        listenerPersonas();

        return view;
    }

    private void validarBanco(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                    if(mensaje.getBoolean("existe")){
                        DetallesFragment.banco = true;
                        banco = true;
                        //Datos bancarios Pre Orden.
                        PreOrdenServicio.nombreTitular = mensaje.getString("nombreTitular");
                        PreOrdenServicio.tarjeta = mensaje.get("tarjeta").toString();
                        PreOrdenServicio.mes = mensaje.get("mes").toString();
                        PreOrdenServicio.ano = mensaje.get("ano").toString();
                    }else{
                        banco = false;
                        DetallesFragment.banco = false;
                    }
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.get("mensaje").toString(), false, null, null);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_DATOS_BANCO_CLIENTE + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    /*
    private void getPreOrden(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                PreOrdenServicio.nombreTitular = jsonObject.getString("nombreTitular");
                PreOrdenServicio.tarjeta = jsonObject.get("tarjeta").toString();
                PreOrdenServicio.mes = jsonObject.get("mes").toString();
                PreOrdenServicio.ano = jsonObject.get("ano").toString();
                PreOrdenServicio.sesion = "Sesión: " + Component.getSeccion(jsonObject.getInt("idSeccion")) + " / " + Component.getNivel(jsonObject.getInt("idSeccion"), jsonObject.getInt("idNivel")) + " / " + Component.getBloque(jsonObject.getInt("idBloque"));
                PreOrdenServicio.tiempo = "Tiempo: " + obtenerHorario(jsonObject.getInt("tiempo"));
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_PRE_ORDEN + this.idCliente + "/" + this.idSesion, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

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
                        mensaje = "Elige un tiempo de servicio a corde a tus horas disponibles agregadas a tu perfil.";
                    else
                        mensaje = "Elige un tiempo de servicio a corde a tus horas disponibles de tu plan activo.";

                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
                }
            }
        }
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
        }
    }

    private void guardarPago(Bundle bundle, String token){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("costoServicio", 0.0);
            jsonObject.put("costoTE", 0.0);
            jsonObject.put("estatusPago", "Pagado");
            jsonObject.put("idCliente", this.idCliente);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                registrarSesion(bundle, token);
            }, APIEndPoints.GUARDAR_TOKEN_PAGO, WebServicesAPI.PUT, jsonObject);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registrarSesion(Bundle bundle, String token){
        JSONObject parametrosPOST = new JSONObject();
        try {
            parametrosPOST.put("idCliente", this.idCliente);
            parametrosPOST.put("horario", bundle.getString("horario"));
            parametrosPOST.put("lugar", bundle.getString("lugar"));
            parametrosPOST.put("tiempo", bundle.getInt("tiempo"));
            parametrosPOST.put("idSeccion", bundle.getInt("idSeccion"));
            parametrosPOST.put("idNivel", bundle.getInt("idNivel"));
            parametrosPOST.put("idBloque", bundle.getInt("idBloque"));
            parametrosPOST.put("extras", bundle.getString("extras"));
            parametrosPOST.put("tipoServicio", bundle.getString("tipoServicio"));
            parametrosPOST.put("latitud", bundle.getDouble("latitud"));
            parametrosPOST.put("longitud", bundle.getDouble("longitud"));
            parametrosPOST.put("actualizado", bundle.getString("actualizado"));
            parametrosPOST.put("fecha",  bundle.getString("fecha"));
            //SI LA RUTA YA ESTÁ FINALZADA NO IMPORTA LO QUE VENGA ES FALSE EL SUMAR.
            parametrosPOST.put("sumar", NuevaSesionFragment.rutaFinalizada ? false : bundle.getBoolean("sumar"));
            parametrosPOST.put("tipoPlan", bundle.getString("tipoPlan"));
            parametrosPOST.put("personas", bundle.getInt("personas"));
            parametrosPOST.put("token", token);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta"))
                    actualizarMonedero(jsonObject);
                else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
            }, APIEndPoints.REGISTRAR_SESION_CLIENTE, WebServicesAPI.POST, parametrosPOST);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarMonedero(JSONObject jsonObject) throws JSONException {
        JSONObject parametrosPUT= new JSONObject();
        parametrosPUT.put("idCliente", idCliente);
        parametrosPUT.put("nuevoMonedero", NuevaSesionFragment.nuevoMonedero);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            ServiciosCliente.validarPlan(idCliente, getContext());
        }, APIEndPoints.ACTUALIZAR_MONEDERO, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();
        NuevaSesionFragment.nuevoMonedero = 0;
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", jsonObject.getString("mensaje"),
                true, "¡VAMOS!", ()->{
                    dismiss();
                });
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
        Log.d("TAG1", location.getLatitude() + " " + location.getLongitude());

        agregarMarca(googleMap, location.getLatitude(), location.getLongitude());
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
        if(!SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR") && !SesionesFragment.PLAN.equalsIgnoreCase("PARTICULAR_PLAN"))
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
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapNueva));
        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

}
