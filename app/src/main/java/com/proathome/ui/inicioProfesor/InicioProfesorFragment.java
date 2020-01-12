package com.proathome.ui.inicioProfesor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.proathome.R;

public class InicioProfesorFragment extends Fragment {

    private InicioProfesorViewModel inicioProfesorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioProfesorViewModel =
                ViewModelProviders.of(this).get(InicioProfesorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio_profesor, container, false);


        return root;
    }
}