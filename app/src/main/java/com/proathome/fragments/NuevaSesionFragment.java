package com.proathome.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ControladorTomarSesion;
import com.proathome.controladores.estudiante.STRegistroSesionesEstudiante;
import com.proathome.controladores.estudiante.ServicioTaskSesionActual;
import com.proathome.utils.Constants;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private String registrarSesionREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/agregarSesion";
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
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
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
            ServicioTaskSesionActual servicioTaskSesionActual = new ServicioTaskSesionActual(getContext(), idCliente);
            servicioTaskSesionActual.execute();
        } else {
            baseDeDatos.close();
        }

        baseDeDatos.close();

        String[] datosHoras = new String[]{"0 HRS", "1 HRS", "2 HRS", "3 HRS"};
        ArrayAdapter<String> adapterHoras = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosHoras);
        String[] datosMinutos = new String[]{"0 min", "5 min", "10 min", "15 min", "20 min", "25 min", "30 min", "35 min", "40 min", "45 min", "50 min", "55 min"};
        ArrayAdapter<String> adapterMinutos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosMinutos);
        String[] datosTipo = new String[]{"Personal", "Grupal"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datosTipo);

        horas.setAdapter(adapterHoras);
        minutos.setAdapter(adapterMinutos);
        tipo.setAdapter(adapterTipo);

        horarioET.setKeyListener(null);
        horarioET.setText("13:00 HRS");
        fechaET.setKeyListener(null);
        fechaET.setText("");

        btnSolicitar.setOnClickListener(v -> {

            if (!direccionET.getText().toString().trim().equalsIgnoreCase("") && !observacionesET.getText().toString().trim().equalsIgnoreCase("") && !fechaET.getText().toString().trim().equalsIgnoreCase("")) {

                if(obtenerMinutosHorario() == 0){
                    Toast.makeText(getContext(), "Elige el tiempo de la sesión.", Toast.LENGTH_LONG).show();
                }else{
                    if(rutaRecomendada){
                        int minutosRestantes = minutosEstablecidos - minutosAnteriores;
                        if(obtenerMinutosHorario() <= minutosRestantes){
                            String direccion = direccionET.getText().toString();
                            String extras = observacionesET.getText().toString();

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                            String strDate = mdformat.format(calendar.getTime());
                            STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(), registrarSesionREST, idCliente, horarioET.getText().toString(), direccion, obtenerMinutosHorario(), secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1, extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate, fechaET.getText().toString(), true);
                            registro.execute();
                            direccionET.setText("");
                            horarioET.setText("");
                            observacionesET.setText("");
                            Toast.makeText(getContext(), "Revisa tu nueva clase en Inicio o en Gestión.", Toast.LENGTH_LONG).show();
                            dismiss();
                        }else{
                            Toast.makeText(getContext(), "Elige un tiempo de sesión a corde a el tiempo faltante.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        String direccion = direccionET.getText().toString();
                        String extras = observacionesET.getText().toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                        String strDate = mdformat.format(calendar.getTime());
                        STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(getContext(), registrarSesionREST, idCliente, horarioET.getText().toString(), direccion, obtenerMinutosHorario(), secciones.getSelectedItemPosition()+1, niveles.getSelectedItemPosition()+1, bloques.getSelectedItemPosition()+1, extras, tipo.getSelectedItem().toString(), latitud, longitud, strDate, fechaET.getText().toString(), false);
                        registro.execute();
                        direccionET.setText("");
                        horarioET.setText("");
                        observacionesET.setText("");
                        Toast.makeText(getContext(), "Revisa tu nueva clase en Inicio o en Gestión.", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }

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

    public int obtenerMinutosHorario(){
        int horasInt = 0;
        int minutosInt = 0;

        if(horas.getSelectedItem().toString().equalsIgnoreCase("0 HRS"))
            horasInt = 0;
        else if(horas.getSelectedItem().toString().equalsIgnoreCase("1 HRS"))
            horasInt = 60;
        else if(horas.getSelectedItem().toString().equalsIgnoreCase("2 HRS"))
            horasInt = 120;
        else if(horas.getSelectedItem().toString().equalsIgnoreCase("3 HRS"))
            horasInt = 180;

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
