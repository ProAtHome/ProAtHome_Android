package com.proathome.ui.fragments;

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
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.WorkaroundMapFragment;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.utils.ComponentProfesional;
import com.proathome.utils.ComponentSesionesProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesGestionarProfesionalFragment extends Fragment implements OnMapReadyCallback {

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

        this.idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();

        ComponentSesionesProfesional componentSesionesProfesional = new ComponentSesionesProfesional();
        Bundle bun = getArguments();
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

    public void errorMsg(){
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

        setImageBitmap(fotoNombre);
    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            fotoPerfil.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
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
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        solicitudEliminarSesion();
    }

    private void cancelarSesion(){
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", this.idProfesional);
            parametrosPUT.put("idServicio", this.idSesion);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", jsonObject.getString("mensaje"),
                                true, "OK", ()->{
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                                    getActivity().finish();
                                });
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.CANCELAR_SERVICIO, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void solicitudEliminarSesion(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                    if(mensaje.getBoolean("eliminar") && mensaje.getBoolean("multa")){
                        //Mostrar que se puede eliminar pero con multa.
                        showAlert("Si cancelas esta servicio serás acreedor de una multa, ya que para cancelar libremente deberá ser 3 HRS antes.");
                    }else if(mensaje.getBoolean("eliminar") && !mensaje.getBoolean("multa")){
                        //Eliminar sin multa.
                        showAlert("¿Deseas cancelar el servicio?");
                    }else if(!mensaje.getBoolean("eliminar")){
                        //No se puede eliminar
                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "No se puede cancelar el servicio a menos de 24 HRS de esta.", false, null, null);
                    }
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.SOLICITUD_ELIMINAR_SESION_PROFESIONAL  + this.idSesion + "/" + this.idProfesional, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void showAlert(String mensaje) {
        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Cancelar Servicio")
                .setMessage(mensaje)
                .setPositiveButton("Cancelar Servicio", (dialog, which) -> {
                    cancelarSesion();
                })
                .setNegativeButton("Cerrar", (dialog, which) -> {
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
        mScrollView = getView().findViewById(R.id.scrollMapGestionProfesional); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsGestionarProfesional))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        agregarMarca(googleMap, latitud, longitud);
    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu servicio."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

}