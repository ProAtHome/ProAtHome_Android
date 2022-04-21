package com.proathome.Views.profesional;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalPresenter;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalView;
import com.proathome.Presenters.profesional.ServicioProfesionalPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.ServicioCliente;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import com.proathome.Views.fragments_compartidos.MaterialFragment;
import com.proathome.Utils.pojos.Component;
import com.proathome.Utils.Constants;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ServicioProfesional extends AppCompatActivity implements ServicioProfesionalView {

    private int idSesion = 0, idProfesional = 0;
    public static CountDownTimer countDownTimer, countDownTimerTE;
    public static boolean mTimerRunning, encurso = true, enpausa = true, inicio = true, terminado = true,
            encurso_TE = true, enpausa_TE = true, inicio_TE = true, terminado_TE = true, schedule = true, primeraVez;
    public static long mTimeLeftMillis, progresoSegundos, progreso;
    public static TextView temporizador;
    public static Timer timer, timer2, timerSchedule;
    public static MaterialButton pausa_start, terminar;
    public static TimerTask taskSchedule;
    public static int idSeccion, idNivel, idBloque;
    public TextView seccionTV, nivelTV, bloqueTV;
    private Unbinder mUnbinder;
    private ServicioProfesionalPresenter servicioProfesionalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_profesional);
        mUnbinder = ButterKnife.bind(this);

        servicioProfesionalPresenter = new ServicioProfesionalPresenterImpl(this);

        temporizador = findViewById(R.id.temporizador);
        pausa_start = findViewById(R.id.pausar);
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
            servicioProfesionalPresenter.validarServicioFinalizadoEnClase(idSesion, idProfesional, this);
        });

        servicioProfesionalPresenter.cambiarEstatusServicio(Constants.ESTATUS_ENCURSO, this.idSesion, this.idProfesional);

        pausa_start.setOnClickListener(v -> {
            if (mTimerRunning) {
                servicioProfesionalPresenter.cambiarEstatusServicio(Constants.ESTATUS_ENPAUSA, this.idSesion, this.idProfesional);
                pauseTimer();
                mTimerRunning = false;
                pausa_start.setText("Start");
                pausa_start.setIcon(this.getDrawable(R.drawable.play));
            } else {
                servicioProfesionalPresenter.cambiarEstatusServicio(Constants.ESTATUS_ENCURSO, this.idSesion, this.idProfesional);
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
                    servicioProfesionalPresenter.guardarProgreso(idSesion, idProfesional, (int) (ServicioProfesional.mTimeLeftMillis / 1000 / 60),
                                (int) (ServicioProfesional.mTimeLeftMillis / 1000 % 60), 2);
                });
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    servicioProfesionalPresenter.guardarProgreso(idSesion, idProfesional, (int) (ServicioProfesional.mTimeLeftMillis / 1000 / 60),
                                (int) (ServicioProfesional.mTimeLeftMillis / 1000 % 60), 1);
                });
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                handler2.post(() -> {
                    //TODO  VALIDAR CONEXION A INTERNET
                    if(NetworkValidate.validate(ServicioProfesional.this))
                        servicioProfesionalPresenter.validarSesionDisponible(getApplicationContext(), idSesion, idProfesional, DetallesSesionProfesionalFragment.PROFESIONAL, ServicioProfesional.this);
                    else{
                        Toast.makeText(ServicioProfesional.this, "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();
                        finish();
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

    @OnClick(R.id.material)
    public void onClick(){
        Bundle bundle = new Bundle();
        String idPDF = idSeccion + "_" + idNivel + "_" + idBloque + ".pdf";
        bundle.putString("idPDF", idPDF);
        MaterialFragment nueva = new MaterialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        nueva.setArguments(bundle);
        nueva.show(transaction, "Material Didáctico");
    }

    public static void startSchedule(){
        if(timerSchedule != null)
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
    public void showError(String mensaje) {
        SweetAlert.showMsg(ServicioProfesional.this, SweetAlert.WARNING_TYPE, "¡ESPERA!", mensaje, false, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if(countDownTimer != null)
            countDownTimer.cancel();
        timer.cancel();
        timer2.cancel();
        timerSchedule.cancel();
        servicioProfesionalPresenter.cambiarDisponibilidadProfesional(idSesion, idProfesional, false);
    }

}