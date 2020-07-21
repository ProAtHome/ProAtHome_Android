package com.proathome.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proathome.R;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
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