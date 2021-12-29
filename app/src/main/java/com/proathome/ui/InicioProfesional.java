package com.proathome.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.utils.SweetAlert;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioProfesional extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    public static TextView correoTV, nombreTV;
    private int idProfesional = 0;
    public ImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_profesional);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        cargarPerfil();

    }

    public void cargarPerfil(){

        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(this, "sesionProfesional",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            this.idProfesional = fila.getInt(0);
            getDatosPerfil();
            baseDeDatos.close();
        }else{
            baseDeDatos.close();
        }

    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            fotoPerfil.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

    private void getDatosPerfil(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if (!response.equals("null")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        nombreTV.setText(jsonObject.getString("nombre"));
                        correoTV.setText(jsonObject.getString("correo"));
                        setImageBitmap(jsonObject.getString("foto"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } else
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error en el perfil, intente ingresar más tarde.", false, null, null);
            }else
                SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error del servidor, intente ingresar más tarde.", false, null, null);
        }, APIEndPoints.GET_PERFIL_PROFESIONAL + this.idProfesional, WebServicesAPI.GET,  null);
        webServicesAPI.execute();
    }

    public void cerrarSesion(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesionProfesional",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        baseDeDatos.delete("sesionProfesional", "id=1", null);
        baseDeDatos.close();

        intent = new Intent(this, LoginProfesional.class);
        startActivity(intent);
        finish();

    }//Fin método cerrarSesion.

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        cargarPerfil();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}
