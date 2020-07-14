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
import com.proathome.controladores.ServicioTaskSincronizarClases;
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioTaskPerfilEstudiante;
import com.proathome.controladores.profesor.ServicioTaskFotoDetalles;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesFragment extends Fragment implements OnMapReadyCallback {

    private static Component mInstance;
    private GoogleMap mMap;
    public static final String TAG = "Detalles";
    public static int ESTUDIANTE = 1;
    private String fotoNombre;
    private int idSesion = 0;
    private int idEstudiante = 0;
    private int tiempoPasar = 0;
    public static ImageView foto;
    @BindView(R.id.profesor)
    public TextView profesor;
    @BindView(R.id.horario)
    public TextView horario;
    @BindView(R.id.lugar)
    public TextView lugar;
    @BindView(R.id.tiempo)
    public TextView tiempo;
    @BindView(R.id.nivel)
    public TextView nivel;
    @BindView(R.id.observaciones)
    public TextView observaciones;
    @BindView(R.id.tipoClase)
    public TextView tipoClase;
    @BindView(R.id.descripcionTV)
    TextView descripcionProfesor;
    @BindView(R.id.correoTV)
    TextView correoProfesor;
    @BindView(R.id.iniciar)
    MaterialButton iniciar;
    private ScrollView mScrollView;
    private Unbinder mUnbinder;
    private double longitud = -99.13320799999, latitud = 19.4326077;

    public DetallesFragment() {

    }

    public static Component getmInstance(int idClase, String tipoClase, String horario, String profesor, String lugar, int tiempo, String observaciones, double latitud, double longitud, int idSeccion, int idNivel, int idBloque, String fecha, String fotoProfesor, String descripcionProfesor, String correoProfesor){

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
        mInstance.setFotoProfesor(fotoProfesor);
        mInstance.setDescripcionProfesor(descripcionProfesor);
        mInstance.setCorreoProfesor(correoProfesor);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);
        return mInstance;

    }

    @Override
    public void onResume() {
        super.onResume();
        ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getContext(), idSesion, idEstudiante, DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, false);
        sincronizarClases.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles, container, false);
        Component component = new Component();
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bun = getArguments();
        foto = view.findViewById(R.id.foto);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            idEstudiante = fila.getInt(0);
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();


        System.out.println(bun.getString("fotoProfesor"));
        if(bun.getString("fotoProfesor").equalsIgnoreCase("Sin foto")) {
            iniciar.setVisibility(View.INVISIBLE);
        }else {
            iniciar.setVisibility(View.VISIBLE);
        }
        if(bun.getString("descripcionProfesor").equalsIgnoreCase("Sin descripcion"))
            descripcionProfesor.setVisibility(View.INVISIBLE);
        else
            descripcionProfesor.setVisibility(View.VISIBLE);
        if(bun.getString("correoProfesor").equalsIgnoreCase("Sin correo"))
            correoProfesor.setVisibility(View.INVISIBLE);
        else
            correoProfesor.setVisibility(View.VISIBLE);

        idSesion = bun.getInt("idClase");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesor.setText(bun.getString("profesor"));
        lugar.setText("Dirección: " + bun.getString("lugar"));
        tiempo.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        tiempoPasar = bun.getInt("tiempo");
        observaciones.setText("Observaciones: " + bun.getString("observaciones"));
        nivel.setText("Nivel: " + component.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque")));
        tipoClase.setText("Tipo de Clase: " + bun.getString("tipoClase"));
        horario.setText("Horario: " + bun.getString("horario"));
        descripcionProfesor.setText(bun.getString("descripcionProfesor"));
        correoProfesor.setText(bun.getString("correoProfesor"));
        fotoNombre = bun.getString("fotoProfesor");

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (mMap == null) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            }else{
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetalles);
                mapFragment.getMapAsync(this);
            }

        }

        ServicioTaskFotoDetalles fotoDetalles = new ServicioTaskFotoDetalles(getContext(), this.fotoNombre, ESTUDIANTE);
        if(!fotoNombre.equalsIgnoreCase("Sin foto"))
            fotoDetalles.execute();

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
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mScrollView = getView().findViewById(R.id.scrollMapDetalles); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetalles))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        mMap.setMyLocationEnabled(true);

        agregarMarca(googleMap, latitud, longitud);

    }

    @OnClick(R.id.iniciar)
    public void onClicked(){

        ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getContext(), idSesion, idEstudiante, DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, true);
        sincronizarClases.execute();

        Intent intent = new Intent(getContext(), SincronizarClase.class);
        intent.putExtra("perfil", ESTUDIANTE);
        intent.putExtra("idSesion", idSesion);
        intent.putExtra("idPerfil", idEstudiante);
        intent.putExtra("tiempo", tiempoPasar);
        startActivity(intent);

    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu clase."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));

    }

}
