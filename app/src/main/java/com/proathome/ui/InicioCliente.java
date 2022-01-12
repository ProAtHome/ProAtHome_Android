package com.proathome.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.proathome.R;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.ui.editarPerfil.EditarPerfilFragment;
import com.proathome.ui.fragments.PagoPendienteFragment;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.PlanesFragment;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioCliente extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private Intent intent;
    public static TextView correoTV, nombreTV, tipoPlan, monedero;
    private int idCliente = 0;
    public static ImageView fotoPerfil;
    public static String planActivo;
    public static AppCompatActivity appCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appCompatActivity = InicioCliente.this;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        nombreTV = view.findViewById(R.id.nombreClienteTV);
        correoTV = view.findViewById(R.id.correoClienteTV);
        tipoPlan = view.findViewById(R.id.tipoPlan);
        monedero = view.findViewById(R.id.monedero);
        fotoPerfil = view.findViewById(R.id.fotoIV);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_editarPerfil, R.id.nav_sesiones,
                R.id.nav_ruta, R.id.nav_cerrarSesion, R.id.nav_ayuda, R.id.nav_privacidad)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*TODO FLUJO_PLANES: Verificar si el perfil debe ser Bloqueado o no.
            ->Si no está bloqueado entonces obtenemos el PLAN ACTUAL y el Monedero.
            (Dentro de la función de cargarPerfil)*/
        cargarPerfil();

    }

    public void cargarPerfil(){
            this.idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
            //TODO FLUJO_PLANES: Crear PLAN al iniciar sesión si no existe registro en la BD.
            /*TODO FLUJO_PANES: Cargamos la info de PLAN, MONEDERO Y PERFIL.*/
            DetallesFragment.procedenciaFin = false;
            checkBloquearPerfil();
            getDatosPerfil();
    }

    private void checkBloquearPerfil(){
        WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONObject jsonObject = new JSONObject(response);
                    PagoPendienteFragment pagoPendienteFragment = new PagoPendienteFragment();
                    Bundle bundle = new Bundle();
                    if(jsonObject.getBoolean("bloquear")){
                        FragmentTransaction fragmentTransaction = null;
                        bundle.putDouble("deuda", jsonObject.getDouble("deuda"));
                        bundle.putString("sesion", Component.getSeccion(jsonObject.getInt("idSeccion")) +
                                " / " + Component.getNivel(jsonObject.getInt("idSeccion"),
                                jsonObject.getInt("idNivel")) + " / " + jsonObject.getInt("idBloque"));
                        bundle.putString("lugar", jsonObject.getString("lugar"));
                        bundle.putString("nombre", jsonObject.getString("nombre"));
                        bundle.putString("correo", jsonObject.getString("correo"));
                        bundle.putInt("idSesion", jsonObject.getInt("idSesion"));
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        pagoPendienteFragment.setArguments(bundle);
                        pagoPendienteFragment.show(fragmentTransaction, "Pago pendiente");
                    }
                }else{
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!",
                            "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.",
                            false, null ,null);
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.BLOQUEAR_PERFIL + "/" + idCliente,  WebServicesAPI.GET, null);
        bloquearPerfil.execute();
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
                if(!response.equals("null")){
                    try{
                        Log.d("TAG1", response);
                        JSONObject jsonObject = new JSONObject(response);
                        nombreTV.setText(jsonObject.getString("nombre"));
                        correoTV.setText(jsonObject.getString("correo"));
                        PlanesFragment.nombreCliente = jsonObject.getString("nombre");
                        PlanesFragment.correoCliente = jsonObject.getString("correo");
                    /*TODO FLUJO_EJECUTAR_PLAN: Verificar si hay PLAN distinto a PARTICULAR
                        -Si, entonces, verificar la expiracion y el monedero (En el servidor verificamos el monedero y  la expiración,
                            y ahí decidimos si finalizamos el plan activo, en cualquier caso regresamos un mensaje validando o avisando que valió verga.
                        -No, entonces, todo sigue el flujo.*/
                        ServiciosCliente.validarPlan(this.idCliente, this);
                        setImageBitmap(jsonObject.getString("foto"));
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }

                }else
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error en el perfil, intente ingresar más tarde.", false, null, null);
            }else
                SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error del servidor, intente ingresar más tarde.", false, null, null);
        }, APIEndPoints.GET_PERFIL_CLIENTE + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        cargarPerfil();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void cerrarSesion(View view){
/*
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sesion", null,
                1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        baseDeDatos.delete("sesion", "id=1", null);
        baseDeDatos.close();*/

        SharedPreferencesManager.getInstance(this).logout();

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }//Fin método cerrarSesion.

}
