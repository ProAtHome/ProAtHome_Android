package com.proathome.Views.activitys_compartidos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.proathome.R;
import com.proathome.Servicios.sesiones.ServiciosSesion;
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

public class SincronizarServicio extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.cancelar)
    MaterialButton cancelar;
    @BindView(R.id.esperando)
    TextView esperando;
    public static Timer timer;
    private int tipoPerfil = 0;
    private int idSesion = 0;
    private int idPerfil = 0;
    public static int tiempo = 0;
    public static int idSeccion = 0;
    public static int idNivel = 0;
    public static int idBloque = 0;
    public static boolean sumar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar_servicio);
        mUnbinder = ButterKnife.bind(this);

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
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (tipoPerfil == DetallesFragment.CLIENTE)
                                ServiciosSesion.verificarDisponibilidadProfesional(idSesion, idPerfil, getApplicationContext());
                            else if(tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL)
                                ServiciosSesion.verificarDisponibilidadCliente(idSesion, idPerfil, getApplicationContext());
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
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
            ServiciosSesion.cambiarDisponibilidadCliente(idSesion, idPerfil, false);
        else if(tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL)
            ServiciosSesion.cambiarDisponibilidadProfesional(idSesion, idPerfil, false);
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
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        timer.cancel();
    }

}