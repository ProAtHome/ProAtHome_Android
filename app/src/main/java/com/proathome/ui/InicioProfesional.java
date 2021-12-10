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
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.servicios.profesional.ServicioTaskPerfilProfesional;
import com.proathome.utils.Constants;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class InicioProfesional extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    private String imageHttpAddress = Constants.IP_80 +
            "/assets/img/fotoPerfil/";
    private String linkRESTCargarPerfil = Constants.IP +
            "/ProAtHome/apiProAtHome/profesional/perfilProfesional";
    public static TextView correoTV, nombreTV;
    private int idProfesional = 0;
    public static ImageView foto;

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
        foto = view.findViewById(R.id.fotoProfesionalIV);
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
            ServicioTaskPerfilProfesional perfilProfesional = new ServicioTaskPerfilProfesional(this, linkRESTCargarPerfil,
                    this.imageHttpAddress, this.idProfesional, Constants.INFO_PERFIL);
            perfilProfesional.execute();
            baseDeDatos.close();
        }else{
            baseDeDatos.close();
        }

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
