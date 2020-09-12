package com.proathome.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesBloque extends DialogFragment {

    private Unbinder mUnbinder;
    @BindView(R.id.tvContenido)
    TextView tvContenido;
    @BindView(R.id.tvContenidoTitle)
    TextView tvContenidoTitle;
    @BindView(R.id.tvHoras)
    TextView tvHoras;
    @BindView(R.id.animation_view)
    LottieAnimationView lottieAnimationView;

    public DetallesBloque() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_bloque, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        tvContenido.setText(bundle.getString("Contenido"));
        tvContenidoTitle.setText("Contenido del Bloque " + bundle.getString("Bloque") + ":");
        tvHoras.setText("Horas del Bloque: " + bundle.getString("Horas"));
        if(bundle.get("Nivel").equals("Basico"))
            lottieAnimationView.setAnimation(R.raw.bus);
        else if(bundle.get("Nivel").equals("Intermedio"))
            lottieAnimationView.setAnimation(R.raw.busthree);
        else if(bundle.get("Nivel").equals("Avanzado"))
            lottieAnimationView.setAnimation(R.raw.bustwo);

        return view;
    }

    @OnClick(R.id.btnCerrar)
    public void onClick(View view){
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
