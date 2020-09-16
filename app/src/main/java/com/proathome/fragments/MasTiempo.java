package com.proathome.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.proathome.ClaseEstudiante;
import com.proathome.R;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskMasTiempo;
import com.proathome.controladores.clase.ServicioTaskSumarClaseRuta;
import com.proathome.controladores.estudiante.ServicioTaskPreOrden;
import com.proathome.utils.Constants;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MasTiempo extends DialogFragment {

    private Unbinder mUnbinder;
    private int idSesion, idEstudiante;
    private Activity activity;
    public static Context contexto = null;

    public MasTiempo() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mas_tiempo, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        this.idSesion = bundle.getInt("idSesion", 0);
        this.idEstudiante = bundle.getInt("idEstudiante", 0);
        /*Vamos a obtene los datos de la pre orden y usarlos para mostrar el cobro final*/
        ServicioTaskPreOrden preOrden = new ServicioTaskPreOrden(idEstudiante, idSesion, ServicioTaskPreOrden.PANTALLA_COBRO_FINAL);
        preOrden.execute();

        return view;
    }

    @OnClick({R.id.cincoMin, R.id.diezMin, R.id.quinceMin, R.id.veinteMin, R.id.veinticincoMin, R.id.treintaMin})
    public void OnClickTiempo(View view){
        /*Creamos los datos de vamos a pasar a la wea*/
        Bundle bundle = new Bundle();
        bundle.putInt("idSesion", idSesion);
        bundle.putInt("idEstudiante", idEstudiante);
        bundle.putInt("estatus", Constants.FINALIZAR_CLASE);
        bundle.putInt("fragment", DetallesFragment.ESTUDIANTE);
        bundle.putInt("idSeccion", ClaseEstudiante.idSeccion);
        bundle.putInt("idNivel", ClaseEstudiante.idNivel);
        bundle.putInt("idBloque", ClaseEstudiante.idBloque);
        bundle.putInt("tiempo", ClaseEstudiante.tiempo);
        bundle.putBoolean("sumar", ClaseEstudiante.sumar);
        /*Mostraremos el cobro final cuando nos de el Tiempo Extra*/
        CobroFinalFragment cobroFinalFragment = new CobroFinalFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.cincoMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 5);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
            case R.id.diezMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 10);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
            case R.id.quinceMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 15);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
            case R.id.veinteMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 20);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
            case R.id.veinticincoMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 25);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
            case R.id.treintaMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 30);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
        }

    }

    @OnClick(R.id.cancelar)
    public void onClick(View view){
        /*Vamos a obtene los datos de la pre orden y usarlos para mostrar el cobro final*/
        Bundle bundle = new Bundle();
        bundle.putInt("Pantalla", 1);
        bundle.putInt("idSesion", idSesion);
        bundle.putInt("idEstudiante", idEstudiante);
        bundle.putInt("estatus", Constants.FINALIZAR_CLASE);
        bundle.putInt("fragment", DetallesFragment.ESTUDIANTE);
        bundle.putInt("idSeccion", ClaseEstudiante.idSeccion);
        bundle.putInt("idNivel", ClaseEstudiante.idNivel);
        bundle.putInt("idBloque", ClaseEstudiante.idBloque);
        bundle.putInt("tiempo", ClaseEstudiante.tiempo);
        bundle.putInt("progresoTotal", 0);
        bundle.putBoolean("sumar", ClaseEstudiante.sumar);
        CobroFinalFragment cobroFinalFragment = new CobroFinalFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        cobroFinalFragment.setArguments(bundle);
        cobroFinalFragment.show(fragmentTransaction, "PreOrden");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}