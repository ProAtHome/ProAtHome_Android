package com.proathome.ui.sesiones;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterGestionar;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ServicioTaskSesionesCliente;
import com.proathome.ui.fragments.NuevaSesionFragment;
import com.proathome.ui.fragments.PlanesFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesFragment extends Fragment {

    public static ComponentAdapterGestionar myAdapter;
    private String serviciosHttpAddress = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    private ServicioTaskSesionesCliente sesionesTask;
    private Unbinder mUnbinder;
    private int idCliente = 0;
    public static boolean SESIONES_PAGADAS_FINALIZADAS = false, PLAN_ACTIVO = false, disponibilidad;
    public static String PLAN = "";
    public static String FECHA_INICIO = "", FECHA_FIN = "";
    public static int MONEDERO = 0, horasDisponibles;
    public static LottieAnimationView lottieAnimationView;
    public static Context contexto;
    @BindView(R.id.recyclerGestionar)
    RecyclerView recyclerView;
    @BindView(R.id.fabNuevaSesion)
    FloatingActionButton fabNuevaSesion;
    @BindView(R.id.fabActualizar)
    FloatingActionButton fabActualizar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sesionesTask = new ServicioTaskSesionesCliente(getContext(), serviciosHttpAddress, this.idCliente,
                Constants.SESIONES_GESTIONAR);
        sesionesTask.execute();
        configAdapter();
        configRecyclerView();
        sesionesPagadas();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sesiones, container, false);
        lottieAnimationView = root.findViewById(R.id.animation_viewSesiones);
        mUnbinder = ButterKnife.bind(this, root);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        SesionesFragment.contexto = getContext();

        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);
        if (fila.moveToFirst()) {
            this.idCliente = fila.getInt(0);
            baseDeDatos.close();
        } else {
            baseDeDatos.close();
        }

        webServiceDisponibilidad();
        sesionesPagadas();

        return root;

    }

    private void sesionesPagadas(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            JSONObject jsonObject = new JSONObject(response);
            //TODO FLUJO_PLANES_EJECUTAR: Posible cambio de algortimo para obtener plan_activo, verificar la fecha de inicio si es distinto a PARTICULAR.
            PLAN_ACTIVO = jsonObject.getBoolean("plan_activo");
            SESIONES_PAGADAS_FINALIZADAS = jsonObject.getBoolean("sesiones_pagadas_finalizadas");
        }, APIEndPoints.SESIONES_PAGADAS_Y_FINALIZADAS + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void webServiceDisponibilidad(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    if(jsonObject.getBoolean("disponibilidad")){
                        SesionesFragment.disponibilidad = true;
                        SesionesFragment.horasDisponibles = jsonObject.getInt("horasDisponibles");
                    }else
                        SesionesFragment.disponibilidad = false;
                }else
                    msg("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.DISPONIBILIDAD_SERVICIO + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterGestionar(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    private void showAlert() {
        PermisosUbicacion.showAlert(getContext(), SweetAlert.CLIENTE);
    }

    @OnClick({R.id.fabNuevaSesion, R.id.fabActualizar})
    public void onClicked(View view){

        switch (view.getId()){
            case R.id.fabNuevaSesion:
                //Validar si puedes crear otra sesion dependiendo las horas del bloque.
                if(SesionesFragment.disponibilidad){
                    NuevaSesionFragment.disponibilidad = true;
                    NuevaSesionFragment.horasDisponibles = SesionesFragment.horasDisponibles;
                    if((SesionesFragment.horasDisponibles / 60) == 1)
                        msg("¡AVISO!", "Sólo puedes crear una servicio con la hora faltante del bloque de la ruta de Aprendizaje Actual.", SweetAlert.WARNING_TYPE);
                    else
                        validarPlan_Monedero();
                }else{
                    NuevaSesionFragment.disponibilidad = false;
                    msgError("¡ESPERA!", "El bloque actual tiene todas las horas ocupadas por servicios, termina tus servicios actuales y regresa por más.", SweetAlert.WARNING_TYPE);
                }
                break;
            case R.id.fabActualizar:
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }

    }

    public void msgError(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public void msg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    validarPlan_Monedero();
                })
                .show();
    }

    public void validarPlan_Monedero(){
        /*TODO FLUJO_PLANES: Verificar que tengamos más de X sesiones PAGADAS Y FINALIZADAS (Antes de dar click) y guardar en Constante.
                   -> SI, ENTONES, ¿Hay un plan distinto a PARTICULAR activo? -> SI, ENTONCES, No mostramos los planes.
                                                                              -> NO, ENTONCES, Mostramos MODAL con PLANES.
                   -> NO, ENTONCES  Creamos Servicio con PLAN -> PARTICULAR (Normal). */
        if(SesionesFragment.PLAN_ACTIVO && SesionesFragment.MONEDERO > 0){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                if(SesionesFragment.SESIONES_PAGADAS_FINALIZADAS){
                    if(SesionesFragment.PLAN_ACTIVO){
                        //TODO FLUJO_EJECUTAR_PLAN: Pasar por Bundle el tipo de PLAN en nuevaSesionFragment.
                        tipoPlanMsg(SesionesFragment.PLAN);
                    }else{
                        PlanesFragment planesFragment = new PlanesFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        planesFragment.show(transaction, "Planes Disponibles");
                    }
                }else{
                    if(SesionesFragment.PLAN_ACTIVO){
                        tipoPlanMsg(SesionesFragment.PLAN);
                    }else{
                        tipoPlanMsg("PARTICULAR");
                    }
                }
            }
        }else if(SesionesFragment.PLAN_ACTIVO && SesionesFragment.MONEDERO == 0){
            new SweetAlert(getContext(), SweetAlert.WARNING_TYPE, SweetAlert.CLIENTE)
                    .setTitleText("¡ESPERA!")
                    .setContentText("Tienes un plan activo pero ya no tienes tiempo disponible," +
                            " elimina una sesión o espera a que finalicen los servicios que creaste.")
                    .show();
        }else{
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                if(SesionesFragment.SESIONES_PAGADAS_FINALIZADAS){
                    if(SesionesFragment.PLAN_ACTIVO){
                        //TODO FLUJO_EJECUTAR_PLAN: Pasar por Bundle el tipo de PLAN en nuevaSesionFragment.
                        tipoPlanMsg(SesionesFragment.PLAN);
                    }else{
                        PlanesFragment planesFragment = new PlanesFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        planesFragment.show(transaction, "Planes Disponibles");
                    }
                }else{
                    if(SesionesFragment.PLAN_ACTIVO){
                        tipoPlanMsg(SesionesFragment.PLAN);
                    }else{
                        tipoPlanMsg("PARTICULAR");
                    }
                }
            }
        }
    }

    private void tipoPlanMsg(String plan) {
       /* new SweetAlert(getContext(), SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                .setTitleText("Creación de servicios con PLAN: " + plan)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();*/
                    NuevaSesionFragment nueva = new NuevaSesionFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    nueva.show(transaction, NuevaSesionFragment.TAG);
                /*})
                .show();*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}