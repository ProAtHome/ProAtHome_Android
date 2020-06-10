package com.proathome.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.proathome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentRutaGenerada extends DialogFragment {

    private Unbinder mUnbinder;
    public static MaterialButton ruta;
    public static TextView nivel;
    @BindView(R.id.rutainicio)
    MaterialButton rutainicio;

    public FragmentRutaGenerada() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ruta_generada, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ruta = view.findViewById(R.id.ruta);
        nivel = view.findViewById(R.id.nivel);
        rutainicio.setOnClickListener(v ->{
            dismiss();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}