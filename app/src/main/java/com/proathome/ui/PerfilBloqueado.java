package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.proathome.R;
import com.proathome.utils.Constants;

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

        Bundle bundle = getIntent().getExtras();
        if(bundle.getInt("tipoPerfil") == Constants.TIPO_USUARIO_CLIENTE)
            tvBloqueado.setTextColor(getResources().getColor(R.color.color_primary));
        else if(bundle.getInt("tipoPerfil") == Constants.TIPO_USUARIO_PROFESIONAL)
            tvBloqueado.setTextColor(getResources().getColor(R.color.color_secondary));

            tvDescripcion.setText("Lo sentimos, este perfil esta temporalmente bloqueado, comunícate a soporte para atender tu situación y pronto reactivar tu cuenta.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}