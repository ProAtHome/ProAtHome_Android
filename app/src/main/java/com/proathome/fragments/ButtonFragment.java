package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.proathome.R;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonFragment extends Fragment {


    private static Component mInstance;
    public static final String TAG = "Detalles";

    public ButtonFragment() {
        // Required empty public constructor
    }

    public static Component getmInstance(String nivel, String tipoClase, String horario){

        mInstance = new Component();
        mInstance.setNivel("Nivel: " + nivel);
        mInstance.setTipoClase("Tipo de Clase: " + tipoClase);
        mInstance.setHorario("Horario: " + horario);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);
        return mInstance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_button, container, false);
    }

}
