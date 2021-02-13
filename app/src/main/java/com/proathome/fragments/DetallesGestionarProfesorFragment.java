package com.proathome.fragments;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.servicios.WorkaroundMapFragment;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.servicios.estudiante.ServicioTaskEliminarSesion;
import com.proathome.servicios.profesor.ServicioTaskEliminarSesionProfesor;
import com.proathome.servicios.profesor.ServicioTaskFotoDetalles;
import com.proathome.utils.ComponentProfesor;
import com.proathome.utils.ComponentSesionesProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesGestionarProfesorFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "Gestión de clase";
    public static final int GESTION_PROFESOR = 5;
    private Unbinder mUnbinder;
    private static ComponentProfesor mInstance;
    public static ImageView foto;
    private int idSesion, tiempoPasar = 0, idEstudiante, idProfesor;
    private NestedScrollView mScrollView;
    private GoogleMap mMap;
    public static String fotoNombre;
    private double longitud = -99.13320799999, latitud = 19.4326077;
    @BindView(R.id.estudiante)
    TextView tvEstudiante;
    @BindView(R.id.descripcionTV)
    TextView tvDescripcion;
    @BindView(R.id.correoTV)
    TextView tvCorreo;
    @BindView(R.id.horario)
    TextView tvHorario;
    @BindView(R.id.lugar)
    TextView tvLugar;
    @BindView(R.id.tiempo)
    TextView tvTiempo;
    @BindView(R.id.fecha)
    TextView tvFecha;
    @BindView(R.id.nivel)
    TextView tvNivel;
    @BindView(R.id.observaciones)
    TextView observaciones;
    @BindView(R.id.tipoClase)
    TextView tvTipoClase;
    @BindView(R.id.cancelar)
    MaterialButton cancelar;

    public DetallesGestionarProfesorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_gestionar_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

        if (fila.moveToFirst())
            this.idProfesor = fila.getInt(0);
        else
            baseDeDatos.close();

        baseDeDatos.close();

        ComponentSesionesProfesor componentSesionesProfesor = new ComponentSesionesProfesor();
        Bundle bun = getArguments();
        foto = view.findViewById(R.id.foto);
        idSesion = bun.getInt("idClase");
        this.fotoNombre = bun.getString("foto");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        tvEstudiante.setText(bun.getString("estudiante"));
        tvDescripcion.setText(bun.getString("descripcion"));
        tvCorreo.setText(bun.getString("correo"));
        tvLugar.setText("Dirección: " + bun.getString("lugar"));
        tvFecha.setText("Fecha: " + bun.getString("fecha"));
        tiempoPasar = bun.getInt("tiempo");
        tvTiempo.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        tvNivel.setText("Nivel: " + componentSesionesProfesor.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque")));
        tvTipoClase.setText("Tipo de clase " + bun.getString("tipoClase"));
        tvHorario.setText("Horario: " + bun.getString("horario"));
        observaciones.setText("Observaciónes: " + bun.getString("observaciones"));
        idEstudiante = bun.getInt("idEstudiante");

        return view;
    }

    public void errorMsg(){
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.PROFESOR)
                .setTitleText("¡OH NO!")
                .setContentText("No podemos continuar sin el permiso de ubicación.")
                .setConfirmButton("OK", sweetAlertDialog -> {
                    getFragmentManager().beginTransaction().detach(this).commit();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsGestionarProfesor);
                mapFragment.getMapAsync(this);
            }
        }


        ServicioTaskFotoDetalles fotoDetalles = new ServicioTaskFotoDetalles(getContext(), this.fotoNombre, GESTION_PROFESOR);
        fotoDetalles.execute();
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

    public static ComponentProfesor getmInstance(int idClase, String tipoClase, String horario, String estudiante, String correo,
                                                 String descripcion, String lugar, int tiempo, String observaciones, double latitud,
                                                 double longitud, String actualizado, int idSeccion, int idNivel,
                                                 int idBloque, String fecha, String tipoPlan, String foto) {

        mInstance = new ComponentProfesor();
        mInstance.setIdClase(idClase);
        mInstance.setEstudiante(estudiante);
        mInstance.setCorreoEstudiante(correo);
        mInstance.setDescripcionEstudiante(descripcion);
        mInstance.setLugar(lugar);
        mInstance.setFotoEstudiante(foto);
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
        mInstance.setTipoPlan(tipoPlan);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);

        return mInstance;
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        ServicioTaskEliminarSesionProfesor  eliminar = new ServicioTaskEliminarSesionProfesor(getContext(), this.idSesion, this.idProfesor, Constants.SOLICITUD_ELIMINAR, this);
        eliminar.execute();
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
        mScrollView = getView().findViewById(R.id.scrollMapGestionProfesor); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsGestionarProfesor))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        agregarMarca(googleMap, latitud, longitud);
    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu clase."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

}