package com.proathome;

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
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.profesor.AdminSQLiteOpenHelperProfesor;
import com.proathome.controladores.profesor.ServicioTaskPerfilProfesor;
import com.proathome.utils.Constants;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class inicioProfesor extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private String linkRESTCargarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/perfilProfesor";
    public static TextView correoTV, nombreTV;
    private int idProfesor = 0;
    public static ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_profesor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        correoTV = view.findViewById(R.id.correoProfesorTV);
        nombreTV = view.findViewById(R.id.nombreProfesorTV);
        foto = view.findViewById(R.id.fotoProfesorIV);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio_profesor, R.id.nav_editarPerfil_profesor, R.id.nav_sesiones_profesor,
                R.id.nav_material_profesor, R.id.nav_cerrarSesion_Profesor, R.id.nav_ayudaProfesor)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        cargarPerfil();

    }

    public void cargarPerfil(){

        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(this, "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            this.idProfesor = fila.getInt(0);
            ServicioTaskPerfilProfesor perfilProfesor = new ServicioTaskPerfilProfesor(this, linkRESTCargarPerfil, this.imageHttpAddress, this.idProfesor, Constants.INFO_PERFIL);
            perfilProfesor.execute();

            baseDeDatos.close();

        }else{

            baseDeDatos.close();

        }

    }

    public void cerrarSesion(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        baseDeDatos.delete("sesionProfesor", "id=1", null);
        baseDeDatos.close();

        intent = new Intent(this, loginProfesor.class);
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
