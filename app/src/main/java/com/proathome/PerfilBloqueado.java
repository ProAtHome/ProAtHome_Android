package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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
        if(bundle.getInt("tipoPerfil") == Constants.TIPO_USUARIO_ESTUDIANTE){
            tvBloqueado.setTextColor(getResources().getColor(R.color.color_primary));
            tvDescripcion.setText("Tu pefil ha sido bloquedo debido a la acumulación de quejas por" +
                    " parte de tus profesores, deberás ponerte en contacto con nosotros para poder activar tu cuenta nuevamente.");
        }else if(bundle.getInt("tipoPerfil") == Constants.TIPO_USUARIO_PROFESOR){
            tvBloqueado.setTextColor(getResources().getColor(R.color.color_secondary));
            tvDescripcion.setText("Tu pefil ha sido bloquedo debido a la acumulación de quejas por" +
                    " parte de tus estudiantes, deberás ponerte en contacto con nosotros para poder activar tu cuenta nuevamente.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}