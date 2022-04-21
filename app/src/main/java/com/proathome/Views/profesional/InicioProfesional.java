package com.proathome.Views.profesional;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.proathome.Interfaces.profesional.Inicio.InicioPresenter;
import com.proathome.Interfaces.profesional.Inicio.InicioView;
import com.proathome.Presenters.profesional.inicio.InicioPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.MainActivity;
import com.proathome.Views.cliente.ServicioCliente;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class InicioProfesional extends AppCompatActivity implements InicioView {

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    public static TextView correoTV, nombreTV;
    public ImageView fotoPerfil;
    private InicioPresenter inicioPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_profesional);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicioPresenter = new InicioPresenterImpl(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        correoTV = view.findViewById(R.id.correoProfesionalTV);
        nombreTV = view.findViewById(R.id.nombreProfesionalTV);
        fotoPerfil = view.findViewById(R.id.fotoProfesionalIV);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio_profesional, R.id.nav_editarPerfil_profesional, R.id.nav_sesiones_profesional,
                R.id.nav_material_profesional, R.id.nav_cerrarSesion_Profesional, R.id.nav_ayudaProfesional,
                R.id.nav_privacidadProfesional)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        inicioPresenter.validarTokenSesion(SharedPreferencesManager.getInstance(InicioProfesional.this).getIDProfesional(), SharedPreferencesManager.getInstance(InicioProfesional.this).getTokenProfesional());
    }

    public void cerrarSesion(View view){
        SharedPreferencesManager.getInstance(this).logout();
        intent = new Intent(this, LoginProfesional.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if(NetworkValidate.validate(InicioProfesional.this))
            inicioPresenter.cargarPerfil(SharedPreferencesManager.getInstance(InicioProfesional.this).getIDProfesional(), SharedPreferencesManager.getInstance(InicioProfesional.this).getTokenProfesional());
        else
            Toast.makeText(InicioProfesional.this, "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void setFoto(Bitmap bitmap) {
        fotoPerfil.setImageBitmap(bitmap);
    }

    @Override
    public void sesionExpirada() {
        SweetAlert.showMsg(this, SweetAlert.NORMAL_TYPE, "OH NO!", "Tu sesión expiró, vuelve a iniciar sesión.", true, "VAMOS", ()->{
            SharedPreferencesManager.getInstance(this).logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void setInfoPerfil(JSONObject jsonObject) {
        try {
            nombreTV.setText(jsonObject.getString("nombre"));
            correoTV.setText(jsonObject.getString("correo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
