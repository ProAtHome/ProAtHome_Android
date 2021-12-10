package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.proathome.servicios.WorkaroundMapFragment;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.servicios.profesional.ServicioTaskInfoSesion;
import com.proathome.servicios.profesional.ServicioTaskMatchSesion;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MatchSesionCliente extends AppCompatActivity implements OnMapReadyCallback {

    private String linkInfoSesion = Constants.IP + "/ProAtHome/apiProAtHome/profesional/informacionSesionMatch";
    private String linkAPIMatch = Constants.IP + "/ProAtHome/apiProAtHome/profesional/matchSesion";
    private String linkFoto = Constants.IP_80 + "/assets/img/fotoPerfil/";
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

        ServicioTaskInfoSesion sesion = new ServicioTaskInfoSesion(this, this.linkInfoSesion, this.linkFoto, this.idSesion);
        sesion.execute();

        matchBTN.setOnClickListener(v -> {
            if (matchBTN.isEnabled()) {
                ServicioTaskMatchSesion match = new ServicioTaskMatchSesion(this, this.linkAPIMatch, this.idSesion, this.idProfesional);
                match.execute();
                finish();
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("¡Espera!")
                        .setConfirmButton("OK", sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                        })
                        .setContentText("Ya tiene un profesional asigando.")
                        .show();
            }
        });

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
