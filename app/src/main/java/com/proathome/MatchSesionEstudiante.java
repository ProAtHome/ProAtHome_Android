package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
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
import com.proathome.controladores.WorkaroundMapFragment;
import com.proathome.controladores.profesor.ServicioTaskInfoSesion;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MatchSesionEstudiante extends AppCompatActivity implements OnMapReadyCallback {

    private String linkInfoSesion = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/informacionSesionMatch";
    private String linkFoto = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private Unbinder mUnbinder;
    private GoogleMap mMap;
    private ScrollView mScrollView;
    public static TextView nombreTV, correoTV, descripcionTV;
    public static AppCompatImageView imageView;
    private double longitud, latitud;
    @BindView(R.id.matchBTN)
    MaterialButton matchBTN;
    @BindView(R.id.seguirBuscandoBTN)
    MaterialButton seguirBuscandoBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_sesion_estudiante);
        mUnbinder = ButterKnife.bind(this);
        int idSesion = Integer.parseInt(getIntent().getStringExtra("idSesion"));
        latitud = getIntent().getDoubleExtra("latitud", 19.4326077);
        longitud = getIntent().getDoubleExtra("longitud", -99.13320799999);

        if (mMap == null) {

            SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsDetallesMatch);
            mapFragment.getMapAsync(this);

        }

        nombreTV = findViewById(R.id.nombreTV);
        imageView = findViewById(R.id.fotoEstudiante);
        correoTV = findViewById(R.id.correoTV);
        descripcionTV = findViewById(R.id.descripcionTV);
        ServicioTaskInfoSesion sesion = new ServicioTaskInfoSesion(this, this.linkInfoSesion, this.linkFoto, idSesion);
        sesion.execute();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mScrollView = findViewById(R.id.scrollMatch); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsDetallesMatch))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        agregarMarca(googleMap, latitud, longitud);

    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){

        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será la clase con el estudiante."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));

    }

    @OnClick({R.id.matchBTN, R.id.seguirBuscandoBTN})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.matchBTN:
                break;
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
