package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.proathome.controladores.ServicioTaskClase;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.utils.Constants;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ClaseProfesor extends AppCompatActivity {

    private int idSesion = 0, idProfesor = 0;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    public static int START_TIME_IN_MILLIS = 0;
    private long mTimeLeftMillis = START_TIME_IN_MILLIS, progresoSegundos = 0, progreso = 0;
    TextView temporizador;
    public static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_profesor);
        temporizador = findViewById(R.id.temporizador);

        idProfesor = getIntent().getIntExtra("idProfesor", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ServicioTaskClase servicioTaskClase = new ServicioTaskClase(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.VERIFICAR_DISPONIBLIDAD, 0);
                            servicioTaskClase.execute();
                            servicioTaskClase = null;
                            servicioTaskClase = new ServicioTaskClase(getApplicationContext(), idSesion, idProfesor, DetallesSesionProfesorFragment.PROFESOR, Constants.GUARDAR_PROGRESO, (int)progreso, (int)progresoSegundos);
                            servicioTaskClase.execute();
                            servicioTaskClase = null;
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 1000);
        //PETICION CADA X SEGUNDOS.

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
        System.out.println((mTimeLeftMillis / 1000 / 60) + "" + (mTimeLeftMillis / 1000 % 60));
        progresoSegundos = (mTimeLeftMillis / 1000 % 60);
        progreso = (mTimeLeftMillis / 1000 / 60);
        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d", horas, minutos, segundos);

        //System.out.println(time);
        temporizador.setText(time);
    }

    public void pauseTimer(){

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
    }

}