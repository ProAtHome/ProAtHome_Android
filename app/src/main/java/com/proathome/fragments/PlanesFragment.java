package com.proathome.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlanesFragment extends DialogFragment {

    private Unbinder mUnbinder;
    public static final int PLAN_SEMANAL = 1, PLAN_MENSUAL = 2, PLAN_BIMESTRAL = 3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnCancelar)
    MaterialButton btnCancelar;
    @BindView(R.id.card1)
    MaterialCardView card1;
    @BindView(R.id.card2)
    MaterialCardView card2;
    @BindView(R.id.card3)
    MaterialCardView card3;

    public PlanesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        toolbar.setTitle("Promociones Disponibles");
        toolbar.setTitleTextColor(Color.WHITE);

        card1.setOnClickListener(v -> {
            verPromo("PLAN SEMANAL", "Descripción de plan Semanal.", PlanesFragment.PLAN_SEMANAL);
        });

        card2.setOnClickListener(v -> {
            verPromo("PLAN MENSUAL", "Descripción de plan Mensual.", PlanesFragment.PLAN_MENSUAL);
        });

        card3.setOnClickListener(v -> {
            verPromo("PLAN BIMESTRAL", "Descripción de plan Bimestral.", PlanesFragment.PLAN_BIMESTRAL);
        });

        return view;
    }

    public void verPromo(String titulo, String mensaje, int plan){
        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Comprar", (dialog, which) -> {

                    if(plan == PlanesFragment.PLAN_SEMANAL){

                    }else if(plan == PlanesFragment.PLAN_MENSUAL){

                    }else if(plan == PlanesFragment.PLAN_BIMESTRAL){

                    }

                })
                .setNegativeButton("Regresar", (dialog, which) -> {
                })
                .setOnCancelListener(dialog -> {
                })
                .show();
    }

    @OnClick(R.id.btnCancelar)
    public void OnClick(){
        //TODO FLUJO_PLANES: Aquí iremos por nueva Clase PARTICULAR.
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}