package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.fragments.DetallesSesionProfesionalFragment;
import com.proathome.servicios.servicio.ServicioTaskCambiarEstatusServicio;
import com.proathome.servicios.servicio.ServicioTaskSesionDisponible;
import com.proathome.servicios.servicio.ServicioTaskFinalizarServicio;
import com.proathome.servicios.servicio.ServicioTaskGuardarProgreso;
import com.proathome.servicios.servicio.ServicioTaskSincronizarServicios;
import com.proathome.fragments.MaterialFragment;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ServicioProfesional extends AppCompatActivity {

    private int idSesion = 0, idProfesional = 0;
    public static CountDownTimer countDownTimer, countDownTimerTE;
    public static boolean mTimerRunning, encurso = true, enpausa = true, inicio = true, terminado = true,
            encurso_TE = true, enpausa_TE = true, inicio_TE = true, terminado_TE = true, schedule = true, primeraVez;
    public static long mTimeLeftMillis, progresoSegundos, progreso;
    public static TextView temporizador;
    public static Timer timer, timer2, timerSchedule;
    public static MaterialButton pausa_start, terminar;
    private FloatingActionButton material;
    public static TimerTask taskSchedule;
    public static int idSeccion, idNivel, idBloque;
    public TextView seccionTV, nivelTV, bloqueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_profesional);
        temporizador = findViewById(R.id.temporizador);
        pausa_start = findViewById(R.id.pausar);
        material = findViewById(R.id.material);
        terminar = findViewById(R.id.terminar);
        seccionTV = findViewById(R.id.seccion);
        nivelTV = findViewById(R.id.nivel);
        bloqueTV = findViewById(R.id.bloque);

        idProfesional = getIntent().getIntExtra("idProfesional", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);
        idSeccion = getIntent().getIntExtra("idSeccion", 0);
        idNivel = getIntent().getIntExtra("idNivel", 0);
        idBloque = getIntent().getIntExtra("idBloque", 0);

        seccionTV.setText("Servicio - " + Component.getSeccion(idSeccion));
        nivelTV.setText(Component.getNivel(idSeccion, idNivel));
        bloqueTV.setText(Component.getBloque(idBloque));

        terminar.setOnClickListener(v -> {
            ServicioTaskFinalizarServicio finalizarServicio = new ServicioTaskFinalizarServicio(this,
                    idSesion, idProfesional, Constants.VALIDAR_SERVICIO_FINALIZADA, DetallesSesionProfesionalFragment.PROFESIONAL);
            finalizarServicio.execute();
        });

        material.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            String idPDF = idSeccion + "_" + idNivel + "_" + idBloque + ".pdf";
            bundle.putString("idPDF", idPDF);
            MaterialFragment nueva = new MaterialFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            nueva.setArguments(bundle);
            nueva.show(transaction, "Material Didáctico");
        });


        ServicioTaskCambiarEstatusServicio servicioTaskCambiarEstatusServicio1 =
                new ServicioTaskCambiarEstatusServicio(getApplicationContext(), idSesion, idProfesional,
                        DetallesSesionProfesionalFragment.PROFESIONAL, Constants.ESTATUS_ENCURSO);
        servicioTaskCambiarEstatusServicio1.execute();

        pausa_start.setOnClickListener(v -> {
            if (mTimerRunning) {
                ServicioTaskCambiarEstatusServicio servicioTaskCambiarEstatus =
                        new ServicioTaskCambiarEstatusServicio(getApplicationContext(), idSesion, idProfesional,
                                DetallesSesionProfesionalFragment.PROFESIONAL, Constants.ESTATUS_ENPAUSA);
                servicioTaskCambiarEstatus.execute();
                pauseTimer();
                mTimerRunning = false;
                pausa_start.setText("Start");
                pausa_start.setIcon(this.getDrawable(R.drawable.play));
            } else {
                ServicioTaskCambiarEstatusServicio servicioTaskCambiarEstatusServicio =
                        new ServicioTaskCambiarEstatusServicio(getApplicationContext(), idSesion, idProfesional,
                                DetallesSesionProfesionalFragment.PROFESIONAL, Constants.ESTATUS_ENCURSO);
                servicioTaskCambiarEstatusServicio.execute();
                startTimer();
                pausa_start.setText("Pausar");
                pausa_start.setIcon(this.getDrawable(R.drawable.pause));
            }
        });

        final Handler handler = new Handler();
        final Handler handler2 = new Handler();
        final Handler handler3 = new Handler();
        timer = new Timer();
        timer2 = new Timer();
        timerSchedule = new Timer();

        taskSchedule = new TimerTask() {
            @Override
            public void run() {
                handler3.post(() -> {
                    try {
                        ServicioTaskGuardarProgreso servicioTaskGuardarProgreso =
                                new ServicioTaskGuardarProgreso(getApplicationContext(), idSesion,
                                        idProfesional, (int) (ServicioProfesional.mTimeLeftMillis / 1000 / 60),
                                            (int) (ServicioProfesional.mTimeLeftMillis / 1000 % 60), 2);
                        servicioTaskGuardarProgreso.execute();
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        ServicioTaskGuardarProgreso servicioTaskGuardarProgreso =
                                new ServicioTaskGuardarProgreso(getApplicationContext(), idSesion,
                                        idProfesional, (int) (ServicioProfesional.mTimeLeftMillis / 1000 / 60),
                                            (int) (ServicioProfesional.mTimeLeftMillis / 1000 % 60), 1);
                        servicioTaskGuardarProgreso.execute();
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                handler2.post(() -> {
                    try {
                        ServicioTaskSesionDisponible servicioTaskSesionDisponible =
                                new ServicioTaskSesionDisponible(getApplicationContext(), idSesion, idProfesional,
                                        DetallesSesionProfesionalFragment.PROFESIONAL, ServicioProfesional.this);
                        servicioTaskSesionDisponible.execute();
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }
        };

        timer2.schedule(task2,0, 3000);
        timer.schedule(task, 0, 3000);
        //PETICION CADA X SEGUNDOS.

        startTimer();
        updateCountDownText();
        primeraVez = true;

    }

    public static void startSchedule(){
        timerSchedule.schedule(taskSchedule, 0, 3000);
    }

    public static void startTimer(){
        countDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish()
            {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    public static void updateCountDownText(){
        int horas = (int) mTimeLeftMillis / (60 * 60 * 1000) % 24;
        int minutos = (int) mTimeLeftMillis / (60 * 1000) % 60;
        int segundos = (int) (mTimeLeftMillis / 1000) % 60;
        progresoSegundos = (mTimeLeftMillis / 1000 % 60);
        progreso = (mTimeLeftMillis / 1000 / 60);
        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d", horas, minutos, segundos);
        temporizador.setText(time);
    }

    public static void pauseTimer(){
        if(countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null)
            countDownTimer.cancel();
        timer.cancel();
        timer2.cancel();
        timerSchedule.cancel();
        ServicioTaskSincronizarServicios sincronizarServicios = new ServicioTaskSincronizarServicios(getApplicationContext(), idSesion, idProfesional, DetallesSesionProfesionalFragment.PROFESIONAL, Constants.CAMBIAR_DISPONIBILIDAD, false);
        sincronizarServicios.execute();
    }

}