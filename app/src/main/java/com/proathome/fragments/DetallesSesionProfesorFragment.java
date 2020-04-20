package com.proathome.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.proathome.R;
import com.proathome.utils.ComponentSesionesProfesor;
import com.proathome.utils.Constants;

import java.time.chrono.MinguoChronology;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetallesSesionProfesorFragment extends Fragment {

    private static ComponentSesionesProfesor mInstance;
    public static final String TAG = "Detalles de Clase";
    private Unbinder mUnbinder;

    public DetallesSesionProfesorFragment() {

    }

    public static ComponentSesionesProfesor getmInstance(int idClase, String nombreEstudiante, String descripcion, String correo, String foto, String nivel, String tipoClase, String horario, String profesor, String lugar, String tiempo, String observaciones, double latitud, double longitud){

        mInstance = new ComponentSesionesProfesor();
        mInstance.setIdClase(idClase);
        mInstance.setNombreEstudiante("Estudiante: " + nombreEstudiante);
        mInstance.setDescripcion("Descripcion: " + nombreEstudiante);
        mInstance.setCorreo("Correo: " + correo);
        mInstance.setFoto(foto);
        mInstance.setProfesor("Profesor Asignado: " + profesor);
        mInstance.setLugar("Lugar - Dirección: " + lugar);
        mInstance.setTiempo("Tiempo de la clase: " + tiempo);
        mInstance.setObservaciones("Observaciones: " + observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setNivel("Nivel: " + nivel);
        mInstance.setTipoClase("Tipo de Clase: " + tipoClase);
        mInstance.setHorario("Horario: " + horario);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.STATIC);
        return mInstance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles_sesion_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bun = getArguments();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }

}
