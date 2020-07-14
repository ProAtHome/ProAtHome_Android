package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.proathome.controladores.ServicioTaskClase;
import com.proathome.fragments.DetallesFragment;
import com.proathome.utils.Constants;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ClaseEstudiante extends AppCompatActivity {

    private int idSesion = 0, idEstudiante = 0;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    public static int START_TIME_IN_MILLIS = 0;
    private long mTimeLeftMillis = START_TIME_IN_MILLIS;
    TextView temporizador;
    public static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_estudiante);
        temporizador = findViewById(R.id.temporizador);

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
                            ServicioTaskClase servicioTaskClase = new ServicioTaskClase(getApplicationContext(), idSesion, idEstudiante, DetallesFragment.ESTUDIANTE, Constants.VERIFICAR_DISPONIBLIDAD, 0);
                            servicioTaskClase.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 2000);
        //PETICION CADA X SEGUNDOS.

        //TODO Verificamos que sigan con disponibilidad
        //TODO Validar el estatus y el progreso, junto con la info de la sesión.
        //TODO GUARDAMOS PROGRESO
        //TODO TIMER TASK CON LO ANTERIOR

        //TODO Cambiamos a ENCURSO
        startTimer();
        updateCountDownText();

    }

    public void startTimer(){
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

    public void updateCountDownText(){
        int horas = (int) mTimeLeftMillis / (60 * 60 * 1000) % 24;
        int minutos = (int) mTimeLeftMillis / (60 * 1000) % 60;
        int segundos = (int) (mTimeLeftMillis / 1000) % 60;
        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d", horas, minutos, segundos);

        System.out.println(time);
        temporizador.setText(time);
    }

    public void pauseTimer(){
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
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        timer.cancel();
        //TODO Cambiamos a ENPAUSA
        //TODO GUARDAMOS PROGRESO
        //TODO Cambiar disponibilidad a FALSE
    }
}