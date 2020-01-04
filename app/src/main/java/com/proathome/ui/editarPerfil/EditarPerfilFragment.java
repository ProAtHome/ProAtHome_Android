package com.proathome.ui.editarPerfil;

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

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel editarPerfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editarPerfilViewModel =
                ViewModelProviders.of(this).get(EditarPerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        editarPerfilViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}