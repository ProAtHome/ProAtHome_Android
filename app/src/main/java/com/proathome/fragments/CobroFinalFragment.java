package com.proathome.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.proathome.ClaseEstudiante;
import com.proathome.R;
import com.proathome.controladores.TabuladorCosto;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskMasTiempo;
import com.proathome.controladores.clase.ServicioTaskSumarClaseRuta;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CobroFinalFragment extends DialogFragment {

    private Unbinder mUnbinder;
    @BindView(R.id.entendido)
    MaterialButton entendido;
    @BindView(R.id.regresar)
    MaterialButton regresar;
    @BindView(R.id.tarjeta)
    TextView tvTarjeta;
    @BindView(R.id.tvSesion)
    TextView tvSesion;
    @BindView(R.id.tvTiempo)
    TextView tvTiempo;
    @BindView(R.id.tvTiempoExtra)
    TextView tvTiempoExtra;
    @BindView(R.id.tvCostoTotal)
    TextView tvCostoTotal;
    public static String metodoRegistrado, sesion, tiempo;
    private int idSesion = 0, idEstudiante = 0, pantalla = 0, progresoTotal = 0;
    public static final int PANTALLA_CANCELAR = 1, PANTALLA_TE = 2;

    public CobroFinalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cobro_final, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        /*Obtenemos los datos para manejar el cobro final*/
        Bundle bundle = getArguments();
        idSesion = bundle.getInt("idSesion");
        idEstudiante = bundle.getInt("idEstudiante");
        pantalla = bundle.getInt("Pantalla");
        progresoTotal = bundle.getInt("progresoTotal");
        System.out.println(metodoRegistrado);
        tvTarjeta.setText(metodoRegistrado);
        tvSesion.setText(sesion);
        tvTiempo.setText(tiempo);
        tvTiempoExtra.setText("Tiempo Extra: " + obtenerHorario(progresoTotal));
        double costoTotal = (double) TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, ClaseEstudiante.tiempo, TabuladorCosto.PARTICULAR) + TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, progresoTotal, TabuladorCosto.PARTICULAR);
        tvCostoTotal.setText("Total: " + String.format("%.2f", costoTotal) + " MXN");

        return view;
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    @OnClick({R.id.entendido, R.id.regresar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.entendido:
                if(pantalla == CobroFinalFragment.PANTALLA_CANCELAR){//Si venimos de el boton de CANCELAR en Más Tiempo.
                    //FInalizamos la clase, sumamos la ruta y obtenemos el token de el celular para realizar el cobro.
                    ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(getContext(), idSesion, idEstudiante, Constants.FINALIZAR_CLASE, DetallesFragment.ESTUDIANTE);
                    finalizarClase.execute();
                    ServicioTaskSumarClaseRuta sumarClaseRuta = new ServicioTaskSumarClaseRuta(getContext(), idSesion, idEstudiante, ClaseEstudiante.idSeccion, ClaseEstudiante.idNivel, ClaseEstudiante.idBloque, ClaseEstudiante.tiempo, ClaseEstudiante.sumar);
                    sumarClaseRuta.execute();
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.contexto_DISPONIBILIDAD_PROGRESO);
                    String idCardSesion = "idCard" + idSesion;
                    String idCard = myPreferences.getString(idCardSesion, "Sin valor");
                    System.out.println(idCard);
                    //TODO Cobro tentativo con OpenPay al cancelar el tiempo EXTRA.
                    Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "IdCard: " + idCard, Toast.LENGTH_LONG).show();
                    Constants.fragmentActivity.finish();
                    dismiss();
                }else if(pantalla == CobroFinalFragment.PANTALLA_TE){//Si venimos de algún Tiempo Extra en Más Tiempo.
                    //Generamos el tiempo extra y la vida sigue.
                    ServicioTaskMasTiempo masTiempo = new ServicioTaskMasTiempo(getContext(), idSesion, idEstudiante, progresoTotal);
                    masTiempo.execute();
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.contexto_DISPONIBILIDAD_PROGRESO);
                    String idCardSesion = "idCard" + idSesion;
                    String idCard = myPreferences.getString(idCardSesion, "Sin valor");
                    System.out.println(idCard);
                    Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "IdCard: " + idCard, Toast.LENGTH_LONG).show();
                    dismiss();
                }
                break;
            case R.id.regresar:
                if(pantalla == CobroFinalFragment.PANTALLA_CANCELAR){//Si venimos de el boton de CANCELAR en Más Tiempo.
                    dismiss();
                }else if(pantalla == CobroFinalFragment.PANTALLA_TE){//Si venimos de algún Tiempo Extra en Más Tiempo.
                    //Volvemos a mostrar los Tiempos extras disponibles.
                    MasTiempo masTiempo = new MasTiempo();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                    bundle.putInt("idEstudiante", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    masTiempo.setArguments(bundle);
                    masTiempo.show(fragmentTransaction, "Tiempo Extra");
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}