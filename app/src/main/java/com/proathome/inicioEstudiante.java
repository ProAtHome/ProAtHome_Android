package com.proathome;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.CargarImagenTask;
import com.proathome.controladores.ServicioTaskPerfilEstudiante;
import com.proathome.utils.Constants;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class inicioEstudiante extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    private String linkRESTCargarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/perfilCliente";
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    public static TextView correoTV, nombreTV;
    private int idEstudiante = 0;
    public static String fotoStatic = "";
    public static ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_estudiante);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        nombreTV = view.findViewById(R.id.nombreEstudianteTV);
        correoTV = view.findViewById(R.id.correoEstudianteTV);
        foto = view.findViewById(R.id.fotoIV);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_editarPerfil, R.id.nav_sesiones,
                R.id.nav_ruta, R.id.nav_cerrarSesion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        cargarPerfil();

    }

    public void cargarPerfil(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            this.idEstudiante = fila.getInt(0);
            ServicioTaskPerfilEstudiante perfilEstudiante = new ServicioTaskPerfilEstudiante(this, linkRESTCargarPerfil, this.imageHttpAddress, this.idEstudiante, Constants.INFO_PERFIL);
            perfilEstudiante.execute();

        }else{

            baseDeDatos.close();

        }

        baseDeDatos.close();

    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        cargarPerfil();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();

    }

    public void cerrarSesion(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        baseDeDatos.delete("sesion", "id=1", null);
        baseDeDatos.close();

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }//Fin método cerrarSesion.

}
