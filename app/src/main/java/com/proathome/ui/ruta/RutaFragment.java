package com.proathome.ui.ruta;

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

public class RutaFragment extends Fragment {

    private RutaViewModel rutaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rutaViewModel =
                ViewModelProviders.of(this).get(RutaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ruta, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        rutaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}