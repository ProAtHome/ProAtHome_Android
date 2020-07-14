package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import java.util.Locale;

public class ClaseEstudiante extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private static final int START_TIME_IN_MILLIS = SincronizarClase.tiempo * 60 * 1000;
    private long mTimeLeftMillis = START_TIME_IN_MILLIS;
    TextView temporizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_estudiante);
        temporizador = findViewById(R.id.temporizador);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        //TODO Cambiamos a ENPAUSA
        //TODO GUARDAMOS PROGRESO
        //TODO Cambiar disponibilidad a FALSE
    }
}