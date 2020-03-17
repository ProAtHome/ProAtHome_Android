package com.proathome.ui.sesionesProfesor;

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

public class SesionesProfesorFragment extends Fragment {

    private SesionesProfesorViewModel sesionesProfesorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sesionesProfesorViewModel =
                ViewModelProviders.of(this).get(SesionesProfesorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sesiones_profesor, container, false);
        final TextView textView = root.findViewById(R.id.text_sesiones);
        sesionesProfesorViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}