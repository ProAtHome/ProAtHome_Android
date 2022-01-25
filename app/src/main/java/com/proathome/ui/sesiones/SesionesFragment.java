package com.proathome.ui.sesiones;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.proathome.ui.fragments.DetallesGestionarFragment;
import com.proathome.ui.fragments.NuevaSesionFragment;
import com.proathome.ui.fragments.PlanesFragment;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesFragment extends Fragment {

    public static ComponentAdapterGestionar myAdapter;
    private Unbinder mUnbinder;
    private int idCliente = 0;
    public static boolean SESIONES_PAGADAS_FINALIZADAS = false, PLAN_ACTIVO = false, disponibilidad;
    public static String PLAN = "";
    public static String FECHA_INICIO = "", FECHA_FIN = "";
    public static int MONEDERO = 0, horasDisponibles;
    public static LottieAnimationView lottieAnimationView;
    public static Context contexto;
    private ProgressDialog progressDialog;
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
        getSesiones();
        configAdapter();
        configRecyclerView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sesiones, container, false);
        lottieAnimationView = root.findViewById(R.id.animation_viewSesiones);
        mUnbinder = ButterKnife.bind(this, root);
        this.idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();

        SesionesFragment.contexto = getContext();
        webServiceDisponibilidad();

        return root;

    }

    private void iniciarProcesoRuta() throws JSONException {
        JSONObject parametros = new JSONObject();
        parametros.put("idCliente", this.idCliente);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

        }, APIEndPoints.INICIAR_PROCESO_RUTA, WebServicesAPI.POST, parametros);
        webServicesAPI.execute();
    }

    private void getSesiones(){
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere por favor...");
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    iniciarProcesoRuta();

                    if(!response.equals("null")){
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("sesiones");

                        if(jsonArray.length() == 0)
                            lottieAnimationView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            //TODO FLUJO_PLANES: Nota - Si el servicio está finalizada no se puede eliminar ni editar (No mostrar en Gestión)
                            if(!object.getBoolean("finalizado")){
                                myAdapter.add(DetallesGestionarFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                                        object.getString("profesional"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                        object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                                        object.getString("fecha"), object.getString("tipoPlan")));
                            }
                        }
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡AVISO!", "Usuario sin servicios disponibles.", false, null, null);
                }catch(JSONException ex){
                    ex.printStackTrace();
                    progressDialog.dismiss();
                }
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Error del servidor, intente de nuevo más tarde.", false, null, null);
            sesionesPagadas();
        }, APIEndPoints.GET_SESIONES_CLIENTE  + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void sesionesPagadas(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            JSONObject data = new JSONObject(response);
            if(data.getBoolean("respuesta")){
                JSONObject jsonObject = data.getJSONObject("mensaje");
                //TODO FLUJO_PLANES_EJECUTAR: Posible cambio de algortimo para obtener plan_activo, verificar la fecha de inicio si es distinto a PARTICULAR.
                PLAN_ACTIVO = jsonObject.getBoolean("plan_activo");
                SESIONES_PAGADAS_FINALIZADAS = jsonObject.getBoolean("sesiones_pagadas_finalizadas");
                progressDialog.dismiss();
            }else
                Toast.makeText(getContext(), data.getString("mensaje"), Toast.LENGTH_LONG).show();
        }, APIEndPoints.SESIONES_PAGADAS_Y_FINALIZADAS + this.idCliente + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void webServiceDisponibilidad(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject jsonObject = data.getJSONObject("mensaje");
                    if(jsonObject.getBoolean("disponibilidad")){
                        SesionesFragment.disponibilidad = true;
                        SesionesFragment.horasDisponibles = jsonObject.getInt("horasDisponibles");
                    }else
                        SesionesFragment.disponibilidad = false;
                }else{
                    SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ERROR!", data.getString("mensaje"), true, "OK", ()->{
                        validarPlan_Monedero();
                    });
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.DISPONIBILIDAD_SERVICIO + idCliente + "/" +  SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
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
                    if((SesionesFragment.horasDisponibles / 60) == 1){
                        SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡AVISO!", "Sólo puedes crear una servicio con la hora faltante del bloque de la ruta de Aprendizaje Actual.", true, "OK", ()->{
                            validarPlan_Monedero();
                        });
                    }else
                        validarPlan_Monedero();
                }else{
                    NuevaSesionFragment.disponibilidad = false;
                    SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ESPERA!", "El bloque actual tiene todas las horas ocupadas por servicios, termina tus servicios actuales y regresa por más.", false, null, null);
                }
                break;
            case R.id.fabActualizar:
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }

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
            SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ESPERA!", "Tienes un plan activo pero ya no tienes tiempo disponible," +
                    " elimina una sesión o espera a que finalicen los servicios que creaste.", false, null, null);
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
       /*
       SweetAlert.showMsg(getContext(), SweetAlert.NORMAL_TYPE, "Creación de servicios con PLAN: " + plan, "", true, "OK", ()->{

       });*/
        NuevaSesionFragment nueva = new NuevaSesionFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        nueva.show(transaction, NuevaSesionFragment.TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}