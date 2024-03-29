package com.proathome.Views.activitys_compartidos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.proathome.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PerfilBloqueado extends AppCompatActivity {

    @BindView(R.id.desc)
    TextView tvDescripcion;
    @BindView(R.id.tvPerfilBloqueado)
    TextView tvBloqueado;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_bloqueado);
        mUnbinder = ButterKnife.bind(this);

        tvDescripcion.setText("Lo sentimos, este perfil esta temporalmente bloqueado, comunícate a soporte para atender tu situación y pronto reactivar tu cuenta.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}