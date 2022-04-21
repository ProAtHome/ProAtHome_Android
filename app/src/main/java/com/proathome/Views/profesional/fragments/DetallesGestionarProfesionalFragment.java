package com.proathome.Views.profesional.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarPresenter;
import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarView;
import com.proathome.Presenters.profesional.DetallesGestionarPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.WorkaroundMapFragment;
import com.proathome.Utils.pojos.ComponentProfesional;
import com.proathome.Utils.pojos.ComponentSesionesProfesional;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesGestionarProfesionalFragment extends Fragment implements OnMapReadyCallback, DetallesGestionarView {

    public static final String TAG = "Gestión del Servicio";
    public static final int GESTION_PROFESIONAL = 5;
    private Unbinder mUnbinder;
    private static ComponentProfesional mInstance;
    public static ImageView fotoPerfil;
    private int idSesion, tiempoPasar = 0, idCliente, idProfesional;
    private NestedScrollView mScrollView;
    private GoogleMap mMap;
    public static String fotoNombre;
    private double longitud = -99.13320799999, latitud = 19.4326077;
    private DetallesGestionarPresenter detallesGestionarPresenter;
    private ProgressDialog progressDialog;
    private Bundle bun;

    @BindView(R.id.cliente)
    TextView tvCliente;
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
    @BindView(R.id.tipoServicio)
    TextView tvTipoServicio;
    @BindView(R.id.cancelar)
    MaterialButton cancelar;

    public DetallesGestionarProfesionalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_gestionar_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        detallesGestionarPresenter = new DetallesGestionarPresenterImpl(this);
        this.idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();

        ComponentSesionesProfesional componentSesionesProfesional = new ComponentSesionesProfesional();
        bun = getArguments();
        fotoPerfil = view.findViewById(R.id.foto);
        idSesion = bun.getInt("idServicio");
        this.fotoNombre = bun.getString("foto");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        tvCliente.setText(bun.getString("cliente"));
        tvDescripcion.setText(bun.getString("descripcion"));
        tvCorreo.setText(bun.getString("correo"));
        tvLugar.setText("Dirección: " + bun.getString("lugar"));
        tvFecha.setText("Fecha: " + bun.getString("fecha"));
        tiempoPasar = bun.getInt("tiempo");
        tvTiempo.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        tvNivel.setText("Nivel: " + componentSesionesProfesional.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque")));
        tvTipoServicio.setText("Tipo de servicio " + bun.getString("tipoServicio"));
        tvHorario.setText("Horario: " + bun.getString("horario"));
        observaciones.setText("Observaciónes: " + bun.getString("observaciones"));
        idCliente = bun.getInt("idCliente");

        return view;
    }

    public void errorMsgGps(){
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡OH NO!", "No podemos continuar sin el permiso de ubicación.",
                true, "OK", ()->{
                    getFragmentManager().beginTransaction().detach(this).commit();
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsGestionarProfesional);
                mapFragment.getMapAsync(this);
            }
        }

        detallesGestionarPresenter.getFotoPerfil(fotoNombre);
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
                    errorMsgGps();
                })
                .setOnCancelListener(dialog -> {
                    errorMsgGps();
                })
                .show();
    }

    public static ComponentProfesional getmInstance(int idServicio, String tipoServicio, String horario, String cliente, String correo,
                                                    String descripcion, String lugar, int tiempo, String observaciones, double latitud,
                                                    double longitud, String actualizado, int idSeccion, int idNivel,
                                                    int idBloque, String fecha, String tipoPlan, String foto) {

        mInstance = new ComponentProfesional();
        mInstance.setIdServicio(idServicio);
        mInstance.setCliente(cliente);
        mInstance.setCorreoCliente(correo);
        mInstance.setDescripcionCliente(descripcion);
        mInstance.setLugar(lugar);
        mInstance.setFotoCliente(foto);
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

    public String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        detallesGestionarPresenter.solicitudEliminarSesion(this.idProfesional, this.idSesion, SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());
    }

    @Override
    public void showAlertCancel(String mensaje) {
        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Cancelar Servicio")
                .setMessage(mensaje)
                .setPositiveButton("Cancelar Servicio", (dialog, which) -> {
                    detallesGestionarPresenter.cancelarSesion(this.idProfesional, this.idSesion);
                })
                .setNegativeButton("Cerrar", (dialog, which) -> {
                })
                .show();
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        fotoPerfil.setImageBitmap(bitmap);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = getView().findViewById(R.id.scrollMapGestionProfesional); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsGestionarProfesional))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        agregarMarca(latitud, longitud);
    }

    public void agregarMarca(double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu servicio."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    @Override
    public void showError(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
    }

    @Override
    public void successCancel(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", mensaje,
                true, "OK", ()->{
                    //NOTIFICAR CLIENTE
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("tipo", "LIBERACION");
                        jsonObject.put("correoProfesional", SharedPreferencesManager.getInstance(getContext()).getCorreoProfesional());
                        jsonObject.put("correoCliente", bun.getString("correo"));
                        jsonObject.put("fechaServicio", bun.getString("fecha"));
                        jsonObject.put("horaServicio", bun.getString("horario"));
                        detallesGestionarPresenter.notificarCliente(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}