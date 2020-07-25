package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.controladores.clase.ServicioTaskCambiarEstatusClase;
import com.proathome.controladores.clase.ServicioTaskClaseDisponible;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskGuardarProgreso;
import com.proathome.controladores.clase.ServicioTaskSincronizarClases;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.fragments.MaterialFragment;
import com.proathome.utils.Constants;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ClaseProfesor extends AppCompatActivity {

    private int idSesion = 0, idProfesor = 0;
    public static CountDownTimer countDownTimer, countDownTimerTE;
    public static boolean mTimerRunning, encurso = true, enpausa = true, inicio = true, terminado = true, encurso_TE = true, enpausa_TE = true, inicio_TE = true, terminado_TE = true, schedule = true, primeraVez;
    public static long mTimeLeftMillis = 0, progresoSegundos = 0, progreso = 0;
    public static TextView temporizador;
    public static Timer timer, timer2, timerSchedule;
    public static MaterialButton pausa_start, terminar;
    private FloatingActionButton material;
    public static TimerTask taskSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_profesor);
        temporizador = findViewById(R.id.temporizador);
        pausa_start = findViewById(R.id.pausar);
        material = findViewById(R.id.material);
        terminar = findViewById(R.id.terminar);

        idProfesor = getIntent().getIntExtra("idProfesor", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);

        terminar.setOnClickListener(v -> {
            ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(this, idSesion, idProfesor, Constants.VALIDAR_CLASE_FINALIZADA, DetallesSesionProfesorFragment.PROFESOR);
            finalizarClase.execute();
        });

        material.setOnClickListener(v -> {
            MaterialFragment nueva = new MaterialFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            nueva.show(transaction, "Material Didáctico");
        });


        ServicioTaskCambiarEstatusClase servicioTaskCambiarEstatusClase1 = new ServicioTaskCambiarEstatusClase(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.ESTATUS_ENCURSO);
        servicioTaskCambiarEstatusClase1.execute();

        pausa_start.setOnClickListener(v -> {
            if (mTimerRunning) {
                ServicioTaskCambiarEstatusClase servicioTaskCambiarEstatus = new ServicioTaskCambiarEstatusClase(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.ESTATUS_ENPAUSA);
                servicioTaskCambiarEstatus.execute();
                pauseTimer();
                mTimerRunning = false;
                pausa_start.setText("Start");
                pausa_start.setIcon(this.getDrawable(R.drawable.play));
            } else {
                ServicioTaskCambiarEstatusClase servicioTaskCambiarEstatusClase = new ServicioTaskCambiarEstatusClase(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.ESTATUS_ENCURSO);
                servicioTaskCambiarEstatusClase.execute();
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
                handler3.post(new Runnable() {
                    public void run() {
                        try {
                            ServicioTaskGuardarProgreso servicioTaskGuardarProgreso = new ServicioTaskGuardarProgreso(getApplicationContext(), idSesion, idProfesor, (int) (ClaseProfesor.mTimeLeftMillis / 1000 / 60), (int) (ClaseProfesor.mTimeLeftMillis / 1000 % 60), 2);
                            servicioTaskGuardarProgreso.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ServicioTaskGuardarProgreso servicioTaskGuardarProgreso = new ServicioTaskGuardarProgreso(getApplicationContext(), idSesion, idProfesor, (int) (ClaseProfesor.mTimeLeftMillis / 1000 / 60), (int) (ClaseProfesor.mTimeLeftMillis / 1000 % 60), 1);
                            servicioTaskGuardarProgreso.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                handler2.post(new Runnable() {
                    public void run() {
                        try {
                            ServicioTaskClaseDisponible servicioTaskClaseDisponible = new ServicioTaskClaseDisponible(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, ClaseProfesor.this);
                            servicioTaskClaseDisponible.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
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
                System.out.println("PARADIOOOOOOOOOOOOOOOOOOOOOOOO");
            }
        }.start();

        mTimerRunning = true;
    }

    public static void startTimerTE(){
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
                System.out.println("PARADIOOOOOOOOOOOOOOOOOOOOOOOO");
            }
        }.start();

        mTimerRunning = true;
    }

    public static void updateCountDownText(){
        int horas = (int) mTimeLeftMillis / (60 * 60 * 1000) % 24;
        int minutos = (int) mTimeLeftMillis / (60 * 1000) % 60;
        int segundos = (int) (mTimeLeftMillis / 1000) % 60;
        //System.out.println((mTimeLeftMillis / 1000 / 60) + "" + (mTimeLeftMillis / 1000 % 60));
        progresoSegundos = (mTimeLeftMillis / 1000 % 60);
        progreso = (mTimeLeftMillis / 1000 / 60);
        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d", horas, minutos, segundos);

        //System.out.println(time);
        temporizador.setText(time);
    }

    public static void pauseTimer(){
        if(countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO Cambiamos a ENPAUSA
        //TODO GUARDAMOS PROGRESO
        //TODO Cambiar disponibilidad a FALSE
        //TODO finish()
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
        ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.CAMBIAR_DISPONIBILIDAD, false);
        sincronizarClases.execute();
    }

}