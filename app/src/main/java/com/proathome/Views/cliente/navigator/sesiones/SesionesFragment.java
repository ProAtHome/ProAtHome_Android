package com.proathome.Views.cliente.navigator.sesiones;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import com.proathome.Interfaces.cliente.Sesiones.SesionesPresenter;
import com.proathome.Interfaces.cliente.Sesiones.SesionesView;
import com.proathome.Presenters.cliente.SesionesPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterGestionar;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.cliente.fragments.DetallesGestionarFragment;
import com.proathome.Views.cliente.fragments.NuevaSesionFragment;
import com.proathome.Views.cliente.fragments.PlanesFragment;
import com.proathome.Utils.PermisosUbicacion;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesFragment extends Fragment implements SesionesView {

    public static ComponentAdapterGestionar myAdapter;
    private Unbinder mUnbinder;
    private String nombreTitular = null, tarjeta = null, mes = null, ano = null;
    public static boolean SESIONES_PAGADAS_FINALIZADAS = false, PLAN_ACTIVO = false, disponibilidad, banco, rutaFinalizada;
    public static String PLAN = "";
    public static String FECHA_INICIO = "", FECHA_FIN = "";
    public static int MONEDERO = 0, horasDisponibles;
    public static LottieAnimationView lottieAnimationView;
    public static Context contexto;
    private SesionesPresenter sesionesPresenter;
    public static Fragment fragment;
    private int seccion, nivel, bloque, minutos_horas;
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
        sesionesPresenter = new SesionesPresenterImpl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sesionesPresenter.getInfoInicioSesiones(SharedPreferencesManager.getInstance(getContext()).getIDCliente(), getContext());
        sesionesPresenter.getSesiones(SharedPreferencesManager.getInstance(getContext()).getIDCliente(), getContext());
        configAdapter();
        configRecyclerView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sesiones, container, false);
        lottieAnimationView = root.findViewById(R.id.animation_viewSesiones);
        mUnbinder = ButterKnife.bind(this, root);

        SesionesFragment.contexto = getContext();
        fragment = this;
        //sesionesPresenter.getDisponibilidadServicio(idCliente, getContext());

        return root;
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
                if(disponibilidad){
                    NuevaSesionFragment.disponibilidad = true;
                    NuevaSesionFragment.horasDisponibles = SesionesFragment.horasDisponibles;
                    if((horasDisponibles / 60) == 1){
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
                        toNuevaSesion();
                    }else{
                        Bundle bundle = new Bundle();
                        //TODO PASAR INFO BANCO
                        bundle.putString("nombreTitular", nombreTitular);
                        bundle.putString("tarjeta", tarjeta);
                        bundle.putString("mes", mes);
                        bundle.putString("ano", ano);
                        bundle.putBoolean("banco", banco);
                        //TODO PASAR INFO RUTA ACTUAL
                        bundle.putInt("idSeccion", seccion);
                        bundle.putInt("idNivel", nivel);
                        bundle.putInt("idBloque", bloque);
                        bundle.putInt("horas", minutos_horas);
                        bundle.putBoolean("rutaFinalizada", rutaFinalizada);

                        PlanesFragment planesFragment = new PlanesFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        planesFragment.setArguments(bundle);
                        planesFragment.show(transaction, "Planes Disponibles");
                    }
                }else
                    toNuevaSesion();
            }
        }else if(SesionesFragment.PLAN_ACTIVO && SesionesFragment.MONEDERO == 0){
            SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ESPERA!", "Tienes un plan activo pero ya no tienes tiempo disponible, o solicitaste un servicio con saldo de tu monedero previamente," +
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
                        toNuevaSesion();
                    }else{
                        Bundle bundle = new Bundle();
                        //TODO PASAR INFO BANCO
                        bundle.putString("nombreTitular", nombreTitular);
                        bundle.putString("tarjeta", tarjeta);
                        bundle.putString("mes", mes);
                        bundle.putString("ano", ano);
                        bundle.putBoolean("banco", banco);
                        //TODO PASAR INFO RUTA ACTUAL
                        bundle.putInt("idSeccion", seccion);
                        bundle.putInt("idNivel", nivel);
                        bundle.putInt("idBloque", bloque);
                        bundle.putInt("horas", minutos_horas);
                        bundle.putBoolean("rutaFinalizada", rutaFinalizada);

                        PlanesFragment planesFragment = new PlanesFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        planesFragment.setArguments(bundle);
                        planesFragment.show(transaction, "Planes Disponibles");
                    }
                }else
                    toNuevaSesion();
            }
        }
    }

    private void toNuevaSesion() {
        Bundle bundle = new Bundle();
        //TODO PASAR INFO BANCO
        bundle.putString("nombreTitular", nombreTitular);
        bundle.putString("tarjeta", tarjeta);
        bundle.putString("mes", mes);
        bundle.putString("ano", ano);
        bundle.putBoolean("banco", banco);
        //TODO PASAR INFO RUTA ACTUAL
        bundle.putInt("idSeccion", seccion);
        bundle.putInt("idNivel", nivel);
        bundle.putInt("idBloque", bloque);
        bundle.putInt("horas", minutos_horas);
        bundle.putBoolean("rutaFinalizada", rutaFinalizada);

        Log.d("TAGFRAG", bundle.toString());

        NuevaSesionFragment nueva = new NuevaSesionFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        nueva.setArguments(bundle);
        nueva.show(transaction, NuevaSesionFragment.TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere por favor...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void setVisibilityLottie() {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setMyAdapter(JSONObject object) {
        try {
            myAdapter.add(DetallesGestionarFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                    object.getString("profesional"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                    object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                    object.getString("fecha"), object.getString("tipoPlan")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void setEstatusSesionesPagadas(boolean planActivo, boolean sesionesPagadas) {
        PLAN_ACTIVO = planActivo;
        SESIONES_PAGADAS_FINALIZADAS = sesionesPagadas;
    }

    @Override
    public void setDisponibilidadEstatus(boolean disponibilidad, int horas) {
        this.disponibilidad = disponibilidad;
        if(horas != 0)
            horasDisponibles = horas;
    }

    @Override
    public void setInfoPlan(JSONObject jsonObject) {
        try {
            PLAN =  jsonObject.getString("tipoPlan");
            MONEDERO = jsonObject.getInt("monedero");
            FECHA_INICIO = jsonObject.getString("fechaInicio");
            FECHA_FIN = jsonObject.getString("fechaFin");
            InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (jsonObject.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : jsonObject.getString("tipoPlan")));
            InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " + obtenerHorario(jsonObject.getInt("monedero")));
            InicioCliente.planActivo = jsonObject.getString("tipoPlan");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRutaActual(JSONObject jsonObject) {
        try {
            seccion = jsonObject.getInt("idSeccion");
            nivel = jsonObject.getInt("idNivel");
            bloque = jsonObject.getInt("idBloque");
            minutos_horas = jsonObject.getInt("horas");

            if(jsonObject.getBoolean("rutaFinalizada"))
                rutaFinalizada = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDatosBanco(JSONObject jsonObject) {
        try {
            if(jsonObject.getBoolean("existe")){
                DetallesFragment.banco = true;
                banco = true;
                //Datos bancarios Pre Orden.
                nombreTitular = jsonObject.getString("nombreTitular");
                tarjeta = jsonObject.get("tarjeta").toString();
                mes = jsonObject.get("mes").toString();
                ano = jsonObject.get("ano").toString();
            }else{
                banco = false;
                DetallesFragment.banco = false;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

}