package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import com.proathome.R;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.utils.Component;
import com.proathome.utils.WorkaroundMapFragment;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MatchSesionCliente extends AppCompatActivity implements OnMapReadyCallback {

    private Unbinder mUnbinder;
    private GoogleMap mMap;
    private ScrollView mScrollView;
    public static TextView nombreTV, correoTV, descripcionTV, direccionTV, tiempoTV, nivelTV, tipoServicioTV, observacionesTV, horarioTV;
    public static AppCompatImageView imageView;
    private double longitud, latitud;
    public static int idProfesionalSesion;
    public int idProfesional, idSesion;
    public static MaterialButton matchBTN;
    @BindView(R.id.seguirBuscandoBTN)
    MaterialButton seguirBuscandoBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_sesion_cliente);
        mUnbinder = ButterKnife.bind(this);

        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(this, "sesionProfesional", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor cursor = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);

        if (cursor.moveToFirst()) {
            this.idProfesional = cursor.getInt(0);
            idProfesionalSesion = this.idProfesional;
        } else {
            baseDeDatos.close();
        }

        baseDeDatos.close();

        this.idSesion = Integer.parseInt(getIntent().getStringExtra("idSesion"));
        latitud = getIntent().getDoubleExtra("latitud", 19.4326077);
        longitud = getIntent().getDoubleExtra("longitud", -99.13320799999);

        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsDetallesMatch);
            mapFragment.getMapAsync(this);
        }

        matchBTN = findViewById(R.id.matchBTN);
        nombreTV = findViewById(R.id.nombreTV);
        imageView = findViewById(R.id.fotoCliente);
        correoTV = findViewById(R.id.correoTV);
        descripcionTV = findViewById(R.id.descripcionTV);
        direccionTV = findViewById(R.id.direccionTV);
        tiempoTV = findViewById(R.id.tiempoTV);
        nivelTV = findViewById(R.id.nivelTV);
        tipoServicioTV = findViewById(R.id.tipoTV);
        observacionesTV = findViewById(R.id.observacionesTV);
        horarioTV = findViewById(R.id.horarioTV);

        getInfoSesion();

        matchBTN.setOnClickListener(v -> {
            if (!matchBTN.isEnabled()) {
                SweetAlert.showMsg(this,  SweetAlert.ERROR_TYPE, "¡Espera!", "Ya tiene un profesional asigando.",
                        true, "OK", ()->{
                        });
            }else
                matchSesion();
        });

    }

    private void getInfoSesion(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            Log.d("TAG1", response);
            if(response == null){
                SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error, ocurrió un problema con el servidor.", false, null, null);
            }else {
                if(!response.equals("null")){
                    JSONObject jsonObject = new JSONObject(response);

                    if(this.idProfesionalSesion == jsonObject.getInt("idProfesional") || jsonObject.getInt("idProfesional") != 0)
                        matchBTN.setVisibility(View.INVISIBLE);
                    else
                        matchBTN.setVisibility(View.VISIBLE);

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
                    setImageBitmap(jsonObject.getString("foto"));
                }else
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Sin datos, ocurrió un error.", false, null, null);
            }
        }, APIEndPoints.INFO_SESION_MATCH  + this.idSesion, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            imageView.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

    private void matchSesion(){
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", this.idProfesional);
            parametrosPUT.put("idSesion", this.idSesion);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    SweetAlert.showMsg(this, SweetAlert.SUCCESS_TYPE, "¡GENIAL", response,
                            true, "OK", ()->{
                                finish();
                            });
                }else{
                    SweetAlert.showMsg(this,  SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrió un error inesperado",
                            true, "OK", ()->{
                                finish();
                            });
                }
            }, APIEndPoints.MATCH_SESION, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        agregarMarca(googleMap, latitud, longitud);

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será el servicio con el cliente."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    @OnClick(R.id.seguirBuscandoBTN)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.seguirBuscandoBTN:
                finish();
                break;
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
    }

}
