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
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.AdminSQLiteOpenHelperProfesor;
import com.proathome.controladores.CargarImagenTask;
import com.proathome.utils.Constants;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class inicioProfesor extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private TextView correoTV, nombreTV;
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
                R.id.nav_material_profesor, R.id.nav_cerrarSesion_Profesor)
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
        Cursor fila = baseDeDatos.rawQuery("SELECT nombre, correo, foto FROM sesionProfesor WHERE id = " + "1", null);

        if(fila.moveToFirst()){

            nombreTV.setText(fila.getString(0));
            correoTV.setText(fila.getString(1));
            CargarImagenTask cargarImagenTask = new CargarImagenTask(imageHttpAddress, fila.getString(2), Constants.FOTO_PERFIL_PROFESOR);
            cargarImagenTask.execute();

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.inicio_profesor, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

}
