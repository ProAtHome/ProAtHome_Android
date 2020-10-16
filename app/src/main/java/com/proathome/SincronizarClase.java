package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.proathome.controladores.clase.ServicioTaskSincronizarClases;
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
        setContentView(R.layout.activity_sincronizar_clase);
        mUnbinder = ButterKnife.bind(this);

        tipoPerfil = getIntent().getIntExtra("perfil", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);
        idPerfil = getIntent().getIntExtra("idPerfil", 0);
        tiempo = getIntent().getIntExtra("tiempo", 0);
        idSeccion = getIntent().getIntExtra("idSeccion", 0);
        idNivel = getIntent().getIntExtra("idNivel", 0);
        idBloque = getIntent().getIntExtra("idBloque", 0);
        sumar = getIntent().getBooleanExtra("sumar", true);

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
                                sincronizarClases = null;
                            }else if(tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                                ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idPerfil, DetallesSesionProfesorFragment.PROFESOR, Constants.VERIFICAR_DISPONIBLIDAD, true);
                                sincronizarClases.execute();
                                sincronizarClases = null;
                            }
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
        finish();
        if (tipoPerfil == DetallesFragment.ESTUDIANTE){
            ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idPerfil, DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, false);
            sincronizarClases.execute();
        }else if(tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
            ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idPerfil, DetallesSesionProfesorFragment.PROFESOR, Constants.CAMBIAR_DISPONIBILIDAD, false);
            sincronizarClases.execute();
        }
        //CAMBIAR A FALSE EN BD.
    }

    @Override
    protected void onPause() {
        super.onPause();
        ClaseEstudiante.enpausa = true;
        ClaseEstudiante.encurso = true;
        ClaseEstudiante.terminado = true;
        ClaseProfesor.enpausa = true;
        ClaseProfesor.encurso = true;
        ClaseProfesor.terminado = true;
        ClaseEstudiante.enpausa_TE= true;
        ClaseEstudiante.encurso_TE = true;
        ClaseEstudiante.terminado_TE = true;
        ClaseProfesor.enpausa_TE = true;
        ClaseProfesor.encurso_TE = true;
        ClaseProfesor.terminado_TE = true;
        ClaseProfesor.schedule = true;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        timer.cancel();
    }

}