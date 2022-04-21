package com.proathome.Views.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClientePresenter;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClienteView;
import com.proathome.Presenters.cliente.ServicioClientePresenterImpl;
import com.proathome.R;
import com.proathome.Servicios.sesiones.ServiciosSesion;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.fragments_compartidos.MaterialFragment;
import com.proathome.Utils.pojos.Component;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ServicioCliente extends AppCompatActivity implements ServicioClienteView{

    private Unbinder mUnbinder;
    private int idSesion = 0, idCliente = 0;
    public static CountDownTimer countDownTimer;
    public static boolean mTimerRunning, sumar, encurso = true, enpausa = true, inicio = true,
            terminado = true, encurso_TE = true, enpausa_TE = true, inicio_TE = true, terminado_TE = true;
    public static long mTimeLeftMillis = 0;
    public static TextView temporizador;
    public static Timer timer;
    public static MaterialButton terminar;
    public static int idSeccion, idNivel, idBloque, tiempo;
    public TextView seccionTV, nivelTV, bloqueTV;
    private ServicioClientePresenter servicioClientePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_cliente);
        mUnbinder = ButterKnife.bind(this);

        servicioClientePresenter = new ServicioClientePresenterImpl(this);

        temporizador = findViewById(R.id.temporizador);
        terminar = findViewById(R.id.terminar);
        seccionTV = findViewById(R.id.seccion);
        nivelTV = findViewById(R.id.nivel);
        bloqueTV = findViewById(R.id.bloque);

        terminar.setOnClickListener(v ->{
            finish();
        });

        idCliente = getIntent().getIntExtra("idCliente", 0);
        idSesion = getIntent().getIntExtra("idSesion", 0);
        idSeccion = getIntent().getIntExtra("idSeccion", 0);
        idNivel = getIntent().getIntExtra("idNivel", 0);
        idBloque = getIntent().getIntExtra("idBloque", 0);
        sumar = getIntent().getBooleanExtra("sumar", true);
        tiempo = getIntent().getIntExtra("tiempo", 0);

        seccionTV.setText("Servicio - " + Component.getSeccion(idSeccion));
        nivelTV.setText(Component.getNivel(idSeccion, idNivel));
        bloqueTV.setText(Component.getBloque(idBloque));

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    if(NetworkValidate.validate(ServicioCliente.this))
                        servicioClientePresenter.servicioDisponible(getApplicationContext(), idSesion, idCliente, DetallesFragment.CLIENTE, ServicioCliente.this);
                    else{
                        Toast.makeText(ServicioCliente.this, "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        };

        timer.schedule(task, 0, 3000);

    }

    @OnClick(R.id.material)
    public void onClick(View view){
        Bundle bundle = new Bundle();
        String idPDF = idSeccion + "_" + idNivel + "_" + idBloque + ".pdf";
        bundle.putString("idPDF", idPDF);
        MaterialFragment nueva = new MaterialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        nueva.setArguments(bundle);
        nueva.show(transaction, "Material Didáctico");
    }

    public static FragmentTransaction obtenerFargment(FragmentActivity activity){
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        return fragmentTransaction;
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
        Log.d("TAGDESTROY", "OnDestroy");
        if(countDownTimer != null)
            countDownTimer.cancel();
        timer.cancel();
        servicioClientePresenter.cambiarDisponibilidadCliente(idSesion, idCliente, false);
        mUnbinder.unbind();
    }

}