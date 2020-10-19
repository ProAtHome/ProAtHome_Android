package com.proathome.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.SincronizarClase;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskSincronizarClases;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.profesor.ServicioTaskFotoDetalles;
import com.proathome.controladores.valoracion.ServicioValidarValoracion;
import com.proathome.utils.ComponentSesionesProfesor;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesSesionProfesorFragment extends Fragment implements OnMapReadyCallback {

    private static ComponentSesionesProfesor mInstance;
    public static final String TAG = "Detalles de Clase";
    public static int PROFESOR = 2, idEstudiante;
    public static int idSesion = 0, idProfesor = 0;
    private int tiempoPasar = 0;
    private GoogleMap mMap;
    private ScrollView mScrollView;
    private Unbinder mUnbinder;
    private double longitud = -99.13320799999, latitud = 19.4326077;
    public static ImageView foto;
    public static String fotoNombre;
    public static boolean procedenciaFin = false;
    @BindView(R.id.nombreTV)
    TextView nombreTV;
    @BindView(R.id.descripcionTV)
    TextView descripcionTV;
    @BindView(R.id.correoTV)
    TextView correoTV;
    @BindView(R.id.direccionTV)
    TextView direccionTV;
    @BindView(R.id.tiempoTV)
    TextView tiempoTV;
    @BindView(R.id.nivelTV)
    TextView nivelTV;
    @BindView(R.id.tipoTV)
    TextView tipoTV;
    @BindView(R.id.horarioTV)
    TextView horarioTV;
    @BindView(R.id.observacionesTV)
    TextView observacionesTV;
    public static MaterialButton iniciar;
    public static Fragment fragmentDetallesProf;

    public DetallesSesionProfesorFragment() {

    }

    public static ComponentSesionesProfesor getmInstance(int idClase, String nombreEstudiante, String descripcion, String correo, String foto, String tipoClase, String horario, String profesor, String lugar, int tiempo, String observaciones, double latitud, double longitud, int idSeccion, int idNivel, int idBloque, int idEstudiante){

        mInstance = new ComponentSesionesProfesor();
        mInstance.setIdClase(idClase);
        mInstance.setNombreEstudiante(nombreEstudiante);
        mInstance.setDescripcion(descripcion);
        mInstance.setCorreo(correo);
        mInstance.setFoto(foto);
        mInstance.setProfesor("Profesor Asignado: " + profesor);
        mInstance.setLugar("Lugar - Dirección: " + lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones("Observaciones: " + observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setTipoClase("Tipo de Clase: " + tipoClase);
        mInstance.setHorario("Horario: " + horario);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setIdEstudiante(idEstudiante);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.STATIC);
        return mInstance;

    }

    @Override
    public void onResume() {
        super.onResume();
        ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.CAMBIAR_DISPONIBILIDAD, false);
        sincronizarClases.execute();
        ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(getContext(), idSesion, idProfesor, Constants.VALIDAR_CLASE_FINALIZADA_AMBOS_PERFILES, DetallesSesionProfesorFragment.PROFESOR);
        finalizarClase.execute();
        if(procedenciaFin){
            ServicioValidarValoracion validarValoracion = new ServicioValidarValoracion(idSesion, idEstudiante, ServicioValidarValoracion.PROCEDENCIA_PROFESOR);
            validarValoracion.execute();
            procedenciaFin = false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles_sesion_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bun = getArguments();
        ComponentSesionesProfesor componentSesionesProfesor = new ComponentSesionesProfesor();
        foto = view.findViewById(R.id.foto);
        iniciar = view.findViewById(R.id.iniciar);

        fragmentDetallesProf = DetallesSesionProfesorFragment.this;

        idSesion = bun.getInt("idClase");
        this.fotoNombre = bun.getString("foto");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        nombreTV.setText(bun.getString("nombreEstudiante"));
        descripcionTV.setText(bun.getString("descripcion"));
        correoTV.setText(bun.getString("correo"));
        direccionTV.setText(bun.getString("lugar"));
        tiempoPasar = bun.getInt("tiempo");
        tiempoTV.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        nivelTV.setText("Nivel: " + componentSesionesProfesor.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque")));
        tipoTV.setText(bun.getString("tipoClase"));
        horarioTV.setText(bun.getString("horario"));
        observacionesTV.setText(bun.getString("observaciones"));
        idEstudiante = bun.getInt("idEstudiante");
        Toast.makeText(getContext(), "IdEst: " + bun.getInt("idEstudiante"), Toast.LENGTH_LONG).show();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            idProfesor = fila.getInt(0);
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

        iniciar.setOnClickListener(v ->{
            ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.CAMBIAR_DISPONIBILIDAD, true);
            sincronizarClases.execute();

            Intent intent = new Intent(getContext(), SincronizarClase.class);
            intent.putExtra("perfil", PROFESOR);
            intent.putExtra("idSesion", idSesion);
            intent.putExtra("idPerfil", idProfesor);
            intent.putExtra("tiempo", tiempoPasar);
            intent.putExtra("idSeccion", bun.getInt("idSeccion"));
            intent.putExtra("idNivel", bun.getInt("idNivel"));
            intent.putExtra("idBloque", bun.getInt("idBloque"));
            startActivity(intent);
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        if (mMap == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            }else{
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesSesionProfesor);
                mapFragment.getMapAsync(this);
            }
        }

        ServicioTaskFotoDetalles fotoDetalles = new ServicioTaskFotoDetalles(getContext(), this.fotoNombre, PROFESOR);
        fotoDetalles.execute();

    }

    @OnClick(R.id.perfilEstudianteCard)
    public void onClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", PerfilFragment.PERFIL_ESTUDIANTE);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        PerfilFragment perfilFragment = new PerfilFragment();
        perfilFragment.setArguments(bundle);
        perfilFragment.show(fragmentTransaction, "Perfil - Estudiante");
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
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = getView().findViewById(R.id.scrollMapDetallesProfesor); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesSesionProfesor))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        mMap.setMyLocationEnabled(true);
        agregarMarca(googleMap, latitud, longitud);

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu clase."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
