package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.proathome.controladores.ServicioTaskSincronizarClases;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SincronizarClase extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.cancelar)
    MaterialButton cancelar;
    @BindView(R.id.esperando)
    TextView esperando;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar_clase);
        mUnbinder = ButterKnife.bind(this);

        int tipoPerfil = getIntent().getIntExtra("perfil", 0);
        int idSesion = getIntent().getIntExtra("idSesion", 0);
        int idPerfil = getIntent().getIntExtra("idPerfil", 0);

        if(tipoPerfil == DetallesFragment.ESTUDIANTE){
            cancelar.setBackgroundColor(getResources().getColor(R.color.colorPersonalDark));
            esperando.setTextColor(getResources().getColor(R.color.colorPersonalDark));
            esperando.setText("Buscando la conexión del profesor...");
        }else if(tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
            cancelar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
            esperando.setTextColor(getResources().getColor(R.color.color_secondary));
            esperando.setText("Buscando la conexión del estudiante...");
        }

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (tipoPerfil == DetallesFragment.ESTUDIANTE){
                                ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idPerfil, DetallesFragment.ESTUDIANTE, Constants.VERIFICAR_DISPONIBLIDAD, true);
                                sincronizarClases.execute();
                            }else if(tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                                ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idPerfil, DetallesSesionProfesorFragment.PROFESOR, Constants.VERIFICAR_DISPONIBLIDAD, true);
                                sincronizarClases.execute();
                            }
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 3000);
        //PETICION CADA X SEGUNDOS.
    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        finish();
        //CAMBIAR A FALSE EN BD.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        timer.cancel();
    }
}