package com.proathome.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DatosFiscalesFragment extends DialogFragment {

    public Unbinder mUnbinder;
    public static Spinner spTipoPersona;
    public static TextInputEditText etRazonSocial;
    public static TextInputEditText etRFC;
    public static Spinner spCFDI;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnActualizar)
    MaterialButton btnActualizar;

    public DatosFiscalesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_fiscales, container, false);
        ButterKnife.bind(this, view);

        spTipoPersona = view.findViewById(R.id.tipoPersona);
        etRazonSocial = view.findViewById(R.id.tvRazonSocial);
        etRFC = view.findViewById(R.id.tvRFC);
        spCFDI = view.findViewById(R.id.spCFDI);

        Bundle bundle = getArguments();
        if(bundle.getInt("tipoPerfil") == Constants.TIPO_USUARIO_ESTUDIANTE){
            toolbar.setBackgroundColor(getResources().getColor(R.color.color_primary));
            btnActualizar.setBackgroundColor(getResources().getColor(R.color.color_primary));
        }else if(bundle.getInt("tipoPerfil") == Constants.TIPO_USUARIO_PROFESOR){
            toolbar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
            btnActualizar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}