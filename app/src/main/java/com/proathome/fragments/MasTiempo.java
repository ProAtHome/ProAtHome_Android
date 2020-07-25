package com.proathome.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proathome.ClaseEstudiante;
import com.proathome.ClaseProfesor;
import com.proathome.R;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskMasTiempo;
import com.proathome.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MasTiempo extends DialogFragment {

    private Unbinder mUnbinder;
    private int idSesion, idEstudiante;
    private Activity activity;

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

        return view;
    }

    @OnClick({R.id.cincoMin, R.id.diezMin, R.id.quinceMin, R.id.veinteMin, R.id.veinticincoMin, R.id.treintaMin})
    public void OnClickTiempo(View view){
        ServicioTaskMasTiempo masTiempo;
        switch (view.getId()){
            case R.id.cincoMin:
                masTiempo = new ServicioTaskMasTiempo(getContext(), this.idSesion, this.idEstudiante, 5);
                masTiempo.execute();
                dismiss();
                break;
            case R.id.diezMin:
                masTiempo = new ServicioTaskMasTiempo(getContext(), this.idSesion, this.idEstudiante, 10);
                masTiempo.execute();
                dismiss();
                break;
            case R.id.quinceMin:
                masTiempo = new ServicioTaskMasTiempo(getContext(), this.idSesion, this.idEstudiante, 15);
                masTiempo.execute();
                dismiss();
                break;
            case R.id.veinteMin:
                masTiempo = new ServicioTaskMasTiempo(getContext(), this.idSesion, this.idEstudiante, 20);
                masTiempo.execute();
                dismiss();
                break;
            case R.id.veinticincoMin:
                masTiempo = new ServicioTaskMasTiempo(getContext(), this.idSesion, this.idEstudiante, 25);
                masTiempo.execute();
                dismiss();
                break;
            case R.id.treintaMin:
                masTiempo = new ServicioTaskMasTiempo(getContext(), this.idSesion, this.idEstudiante, 30);
                masTiempo.execute();
                dismiss();
                break;
        }

    }

    @OnClick(R.id.cancelar)
    public void onClick(View view){
        ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(getContext(), this.idSesion, this.idEstudiante, Constants.FINALIZAR_CLASE, DetallesFragment.ESTUDIANTE);
        finalizarClase.execute();
        dismiss();
        Constants.fragmentActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}