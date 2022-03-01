package com.proathome.Views.activitys_compartidos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioPresenter;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioView;
import com.proathome.Presenters.activitys_compartidos.SincronizarServicioPresenterImpl;
import com.proathome.R;
import com.proathome.Views.cliente.ServicioCliente;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.profesional.ServicioProfesional;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SincronizarServicio extends AppCompatActivity implements SincronizarServicioView {

    @BindView(R.id.cancelar)
    MaterialButton cancelar;
    @BindView(R.id.esperando)
    TextView esperando;

    private Unbinder mUnbinder;
    private Timer timer;
    public static int tipoPerfil = 0, idSesion = 0, idPerfil = 0, tiempo = 0,
            idSeccion = 0, idNivel = 0, idBloque = 0;
    public static boolean sumar;
    private SincronizarServicioPresenter sincronizarServicioPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar_servicio);
        mUnbinder = ButterKnife.bind(this);

        sincronizarServicioPresenter = new SincronizarServicioPresenterImpl(this);

        tipoPerfil = getIntent().getIntExtra("perfil", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);
        idPerfil = getIntent().getIntExtra("idPerfil", 0);
        tiempo = getIntent().getIntExtra("tiempo", 0);
        idSeccion = getIntent().getIntExtra("idSeccion", 0);
        idNivel = getIntent().getIntExtra("idNivel", 0);
        idBloque = getIntent().getIntExtra("idBloque", 0);
        sumar = getIntent().getBooleanExtra("sumar", true);

        if(tipoPerfil == DetallesFragment.CLIENTE)
            esperando.setText("Buscando la conexión del profesional...");
        else if(tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL)
            esperando.setText("Buscando la conexión del cliente...");

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        if (tipoPerfil == DetallesFragment.CLIENTE)
                            sincronizarServicioPresenter.verificarDisponibilidadProfesional(idSesion, idPerfil, getApplicationContext());
                        else if(tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL)
                            sincronizarServicioPresenter.verificarDisponibilidadCliente(idSesion, idPerfil, getApplicationContext());
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }
        };

        timer.schedule(task, 0, 2000);
        //PETICION CADA X SEGUNDOS.
    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        if (tipoPerfil == DetallesFragment.CLIENTE)
            sincronizarServicioPresenter.cambiarDisponibilidadCliente(idSesion, idPerfil, false);
        else if(tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL)
            sincronizarServicioPresenter.cambiarDisponibilidadProfesional(idSesion, idPerfil, false);
        finish();
        //CAMBIAR A FALSE EN BD.
    }

    @Override
    protected void onPause() {
        super.onPause();
        ServicioCliente.enpausa = true;
        ServicioCliente.encurso = true;
        ServicioCliente.terminado = true;
        ServicioProfesional.enpausa = true;
        ServicioProfesional.encurso = true;
        ServicioProfesional.terminado = true;
        ServicioCliente.enpausa_TE= true;
        ServicioCliente.encurso_TE = true;
        ServicioCliente.terminado_TE = true;
        ServicioProfesional.enpausa_TE = true;
        ServicioProfesional.encurso_TE = true;
        ServicioProfesional.terminado_TE = true;
        ServicioProfesional.schedule = true;
        finish();
    }

    @Override
    public void cancelTime() {
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        timer.cancel();
    }

}