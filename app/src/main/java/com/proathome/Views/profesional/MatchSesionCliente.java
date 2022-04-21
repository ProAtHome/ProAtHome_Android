package com.proathome.Views.profesional;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionPresenter;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionView;
import com.proathome.Presenters.profesional.MatchSesionPresenterImpl;
import com.proathome.R;
import com.proathome.Servicios.cliente.ServiciosCliente;
import com.proathome.Utils.pojos.Component;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.WorkaroundMapFragment;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MatchSesionCliente extends AppCompatActivity implements OnMapReadyCallback, MatchSesionView {

    private Unbinder mUnbinder;
    private GoogleMap mMap;
    private ScrollView mScrollView;
    private double longitud, latitud;
    public int idProfesional, idSesion;
    private ProgressDialog progressDialog;
    private MatchSesionPresenter matchSesionPresenter;
    private JSONObject servicios, serviciosPendientes;
    private String fechaActual, fechaServicio, horarioServicio, correoCliente;

    @BindView(R.id.seguirBuscandoBTN)
    MaterialButton seguirBuscandoBTN;
    @BindView(R.id.matchBTN)
    MaterialButton matchBTN;
    @BindView(R.id.nombreTV)
    TextView nombreTV;
    @BindView(R.id.correoTV)
    TextView correoTV;
    @BindView(R.id.descripcionTV)
    TextView descripcionTV;
    @BindView(R.id.direccionTV)
    TextView direccionTV;
    @BindView(R.id.tiempoTV)
    TextView tiempoTV;
    @BindView(R.id.nivelTV)
    TextView nivelTV;
    @BindView(R.id.tipoTV)
    TextView tipoServicioTV;
    @BindView(R.id.observacionesTV)
    TextView observacionesTV;
    @BindView(R.id.horarioTV)
    TextView horarioTV;
    @BindView(R.id.fotoCliente)
    AppCompatImageView imageView;
    @BindView(R.id.fechaTV)
    TextView fechaTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_sesion_cliente);
        mUnbinder = ButterKnife.bind(this);

        matchSesionPresenter = new MatchSesionPresenterImpl(this);

        idProfesional = SharedPreferencesManager.getInstance(this).getIDProfesional();
        idSesion = Integer.parseInt(getIntent().getStringExtra("idSesion"));
        latitud = getIntent().getDoubleExtra("latitud", 19.4326077);
        longitud = getIntent().getDoubleExtra("longitud", -99.13320799999);
        fechaActual = getIntent().getStringExtra("fechaActual");
        try {
            servicios = new JSONObject(getIntent().getStringExtra("serviciosDisponibles"));
            serviciosPendientes = new JSONObject(getIntent().getStringExtra("serviciosPendientes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TAGS", servicios.toString());
        Log.d("TAGS", serviciosPendientes.toString());

        matchSesionPresenter.getInfoSesion(idSesion);
    }

    public void agregarMarca(double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será el servicio con el cliente."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    public void btnMatch(){
        if (!matchBTN.isEnabled()) {
            SweetAlert.showMsg(this,  SweetAlert.ERROR_TYPE, "¡Espera!", "Ya tiene un profesional asigando.",
                    true, "OK", ()->{
                    });
        }else
            matchSesionPresenter.matchSesion(idProfesional, idSesion, servicios, serviciosPendientes,
                    fechaActual, fechaServicio, horarioServicio, SharedPreferencesManager.getInstance(MatchSesionCliente.this).getRangoServicioProfeisonal());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsDetallesMatch);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(MatchSesionCliente.this, "Cargando", "Por favor, espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mScrollView = findViewById(R.id.scrollMatch); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsDetallesMatch))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        agregarMarca(latitud, longitud);
    }

    @OnClick({R.id.seguirBuscandoBTN, R.id.matchBTN})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.seguirBuscandoBTN:
                finish();
                break;
            case R.id.matchBTN:
                btnMatch();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mMap = null;
        Fragment fragment = (getSupportFragmentManager().findFragmentById(R.id.mapsDetallesMatch));
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(MatchSesionCliente.this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void setInfoSesion(JSONObject jsonObject) {
        try {
            if(this.idProfesional == jsonObject.getInt("idProfesional") || jsonObject.getInt("idProfesional") != 0)
                matchBTN.setVisibility(View.INVISIBLE);
            else
                matchBTN.setVisibility(View.VISIBLE);

            fechaServicio = jsonObject.getString("fecha");
            horarioServicio = jsonObject.getString("horario");
            correoCliente = jsonObject.getString("correo");
            fechaTV.setText("Fecha: " + jsonObject.getString("fecha"));
            nombreTV.setText(jsonObject.getString("nombre"));
            correoTV.setText(jsonObject.getString("correo"));
            descripcionTV.setText(jsonObject.getString("descripcion"));
            direccionTV.setText("Dirección: " + jsonObject.getString("lugar"));
            tiempoTV.setText("Tiempo de la Sesión: " + ServiciosCliente.obtenerHorario(jsonObject.getInt("tiempo")));
            nivelTV.setText("Nivel: " + Component.getSeccion(jsonObject.getInt("idSeccion")) +
                    "/" + Component.getNivel(jsonObject.getInt("idSeccion"), jsonObject.getInt("idNivel")) + "/" +
                    Component.getBloque(jsonObject.getInt("idBloque")));
            tipoServicioTV.setText("Tipo de servicio: " + jsonObject.getString("tipoServicio"));
            observacionesTV.setText("Observaciones: " + jsonObject.getString("extras"));
            horarioTV.setText("Horario: " + jsonObject.getString("horario"));
            matchSesionPresenter.getImage(jsonObject.getString("foto"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void successMatch(String mensaje) {
        SweetAlert.showMsg(MatchSesionCliente.this, SweetAlert.SUCCESS_TYPE, "¡GENIAL", mensaje,
                true, "OK", ()->{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("tipo", "MATCH");
                        jsonObject.put("correoProfesional", SharedPreferencesManager.getInstance(MatchSesionCliente.this).getCorreoProfesional());
                        jsonObject.put("correoCliente", correoCliente);
                        jsonObject.put("fechaServicio", fechaServicio);
                        jsonObject.put("horaServicio", horarioServicio);
                        matchSesionPresenter.notificarCliente(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
    }

}
