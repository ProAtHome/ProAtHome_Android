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
import com.proathome.controladores.ServicioTaskCambiarEstatusClase;
import com.proathome.controladores.ServicioTaskClaseDisponible;
import com.proathome.controladores.ServicioTaskSincronizarClases;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.MaterialFragment;
import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.utils.Constants;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ClaseEstudiante extends AppCompatActivity {

    private int idSesion = 0, idEstudiante = 0;
    public static CountDownTimer countDownTimer;
    public static boolean mTimerRunning, encurso = true, enpausa = true, inicio = true;
    public static long mTimeLeftMillis = 0;
    public static TextView temporizador;
    public static Timer timer;
    public static MaterialButton pausa_start;
    private FloatingActionButton material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_estudiante);
        temporizador = findViewById(R.id.temporizador);
        pausa_start = findViewById(R.id.pausar);
        material = findViewById(R.id.material);

        material.setOnClickListener(v -> {
            MaterialFragment nueva = new MaterialFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            nueva.show(transaction, "Material Didáctico");
        });

        idEstudiante = getIntent().getIntExtra("idEstudiante", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ServicioTaskClaseDisponible servicioTaskClaseDisponible = new ServicioTaskClaseDisponible(getApplicationContext(), idSesion, idEstudiante, DetallesFragment.ESTUDIANTE, ClaseEstudiante.this);
                            servicioTaskClaseDisponible.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 3000);
        //PETICION CADA X SEGUNDOS.

        //TODO Verificamos que sigan con disponibilidad
        //TODO Validar el estatus y el progreso, junto con la info de la sesión.
        //TODO GUARDAMOS PROGRESO
        //TODO TIMER TASK CON LO ANTERIOR

        //TODO Cambiamos a ENCURSO
        //startTimer();
        //updateCountDownText();

    }

    public static void startTimer(){
        countDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    public static void updateCountDownText(){
        int horas = (int) mTimeLeftMillis / (60 * 60 * 1000) % 24;
        int minutos = (int) mTimeLeftMillis / (60 * 1000) % 60;
        int segundos = (int) (mTimeLeftMillis / 1000) % 60;
        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d", horas, minutos, segundos);
        //Toast.makeText(this, "Progreso en reloj: " + minutos + " " + segundos, Toast.LENGTH_LONG).show();
        //System.out.println(time);
        temporizador.setText(time);
    }

    public static void pauseTimer(){
        if(countDownTimer != null)
            countDownTimer.cancel();
        //TODO Cambiamos a ENPAUSA
        //TODO GUARDAMOS PROGRESO
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
        ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getApplicationContext(), idSesion, idEstudiante, DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, false);
        sincronizarClases.execute();
        //TODO Cambiamos a ENPAUSA
        //TODO GUARDAMOS PROGRESO
        //TODO Cambiar disponibilidad a FALSE
    }
}