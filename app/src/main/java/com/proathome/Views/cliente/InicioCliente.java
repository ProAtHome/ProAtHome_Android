package com.proathome.Views.cliente;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.proathome.Interfaces.cliente.Inicio.InicioPresenter;
import com.proathome.Interfaces.cliente.Inicio.InicioView;
import com.proathome.Presenters.cliente.inicio.InicioPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import de.hdodenhof.circleimageview.CircleImageView;

public class InicioCliente extends AppCompatActivity implements InicioView {

    private AppBarConfiguration mAppBarConfiguration;
    public static TextView correoTV, nombreTV, tipoPlan, monedero, tvCerrarSesion,
                            tvTerminosCondiciones, tvContacto;
    private CircleImageView fotoPerfil;
    public static String planActivo;
    public static AppCompatActivity appCompatActivity;
    private InicioPresenter inicioPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicioPresenter = new InicioPresenterImpl(this);

        appCompatActivity = InicioCliente.this;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        nombreTV = view.findViewById(R.id.nombreClienteTV);
        correoTV = view.findViewById(R.id.correoClienteTV);
        tipoPlan = view.findViewById(R.id.tipoPlan);
        monedero = view.findViewById(R.id.monedero);
        fotoPerfil = view.findViewById(R.id.fotoIV);
        tvCerrarSesion = navigationView.findViewById(R.id.tv_cerrar_sesion);
        tvTerminosCondiciones = navigationView.findViewById(R.id.tv_tyc);
        tvContacto = navigationView.findViewById(R.id.tv_contactanos);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_editarPerfil, R.id.nav_sesiones,
                R.id.nav_ruta, R.id.nav_ayuda, R.id.nav_privacidad)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        tvCerrarSesion.setOnClickListener(view1 -> {
            SharedPreferencesManager.getInstance(this).logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        tvContacto.setOnClickListener(view1 -> {
            Uri uri = Uri.parse("https://www.proathome.com.mx/ayuda/contacto");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        tvTerminosCondiciones.setOnClickListener(view1 -> {
            Uri uri = Uri.parse("https://www.proathome.com.mx/T&C/T&C-Cliente.pdf");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        /*TODO FLUJO_PLANES: Verificar si el perfil debe ser Bloqueado o no.
            ->Si no está bloqueado entonces obtenemos el PLAN ACTUAL y el Monedero.
            (Dentro de la función de cargarPerfil)*/
        inicioPresenter.validarTokenSesion(SharedPreferencesManager.getInstance(this).getIDCliente(), InicioCliente.this);
    }


    /*  TODO POR EL MOMENTO YA NO USAMOS LA VALIDACION DE PAGOS EN DEUDA
    private void checkBloquearPerfil(){
        WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
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
                }else{
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!",
                            "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.",
                            false, null ,null);
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.BLOQUEAR_PERFIL + "/" + idCliente + "/" +  SharedPreferencesManager.getInstance(InicioCliente.this).getTokenCliente(),  WebServicesAPI.GET, null);
        bloquearPerfil.execute();
    }*/

    /*
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
                        ServiciosCliente.validarPlan(this.idCliente, this);
                        setImageBitmap(jsonObject.getString("foto"));
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error en el perfil, intente ingresar más tarde.", false, null, null);
            }else
                SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error del servidor, intente ingresar más tarde.", false, null, null);
        }, APIEndPoints.GET_PERFIL_CLIENTE + this.idCliente + "/" + SharedPreferencesManager.getInstance(InicioCliente.this).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if(NetworkValidate.validate(InicioCliente.this))
            inicioPresenter.cargarPerfil(SharedPreferencesManager.getInstance(this).getIDCliente(), InicioCliente.this);
        else
            Toast.makeText(InicioCliente.this, "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void setFoto(Bitmap bitmap) {
        if(bitmap != null && fotoPerfil != null)
            fotoPerfil.setImageBitmap(bitmap);
    }

    @Override
    public void toMainActivity() {
        SweetAlert.showMsg(this, SweetAlert.NORMAL_TYPE, "OH NO!", "Tu sesión expiró, vuelve a iniciar sesión.", true, "VAMOS", ()->{
            SharedPreferencesManager.getInstance(InicioCliente.this).logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void setInfoPerfil(JSONObject jsonObject) {
        try {
            nombreTV.setText(jsonObject.getString("nombre"));
            correoTV.setText(jsonObject.getString("correo"));
            tipoPlan.setText("PLAN ACTUAL: " + (jsonObject.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : jsonObject.getString("tipoPlan")));
            monedero.setText("HORAS DISPONIBLES:                      " + obtenerHorario(jsonObject.getInt("monedero")));
            planActivo = jsonObject.getString("tipoPlan");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    public static String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

}
