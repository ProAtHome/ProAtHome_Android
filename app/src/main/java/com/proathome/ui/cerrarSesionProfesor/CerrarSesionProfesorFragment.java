package com.proathome.ui.cerrarSesionProfesor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.proathome.R;

public class CerrarSesionProfesorFragment extends Fragment {

    private CerrarSesionProfesorViewModel cerrarSesionProfesorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cerrarSesionProfesorViewModel = ViewModelProviders.of(this).get(CerrarSesionProfesorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cerrar_sesion_profesor, container, false);

        return root;
    }
}