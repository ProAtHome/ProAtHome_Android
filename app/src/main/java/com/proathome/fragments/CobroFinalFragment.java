package com.proathome.fragments;

import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.proathome.ClaseEstudiante;
import com.proathome.R;
import com.proathome.controladores.ServicioTaskCobro;
import com.proathome.controladores.TabuladorCosto;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskMasTiempo;
import com.proathome.controladores.clase.ServicioTaskSumarClaseRuta;
import com.proathome.utils.Constants;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.openpay.client.core.OpenpayAPI;

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
    public static String metodoRegistrado, sesion, tiempo, deviceIdString, nombreEstudiante, correo;
    public static double costoTotal = 0.0;
    public static int idSesion = 0, idEstudiante = 0, pantalla = 0, progresoTotal = 0;
    public static final int PANTALLA_CANCELAR = 1, PANTALLA_TE = 2;
    private String linkCobro = "http://" + Constants.IP + "/ProAtHome/assets/lib/Cargo.php";
    public Context context = null;
    public static String idCard = null;

    public CobroFinalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);

        deviceIdString = Constants.openpay.getDeviceCollectorDefaultImpl().setup(getActivity());
        context = getContext();
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
        tvTarjeta.setText(metodoRegistrado);
        tvSesion.setText(sesion);
        tvTiempo.setText(tiempo);
        tvTiempoExtra.setText("Tiempo Extra: " + obtenerHorario(progresoTotal));
        costoTotal = (double) TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, ClaseEstudiante.tiempo, TabuladorCosto.PARTICULAR) + TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, progresoTotal, TabuladorCosto.PARTICULAR);
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
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.contexto_DISPONIBILIDAD_PROGRESO);
                    String idCardSesion = "idCard" + idSesion;
                    idCard = myPreferences.getString(idCardSesion, "Sin valor");

                    //TODO Cobro tentativo con OpenPay al cancelar el tiempo EXTRA.
                    if(idCard.equalsIgnoreCase("Sin valor")){
                        //TODO obtenerToken de BD.
                        ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(getContext(), deviceIdString, idSesion, idEstudiante, idCard, costoTotal, ServicioTaskCobro.TOKEN_BD, ServicioTaskCobro.ENTENDIDO_CANCELAR);
                        servicioTaskCobro.execute();
                    }else{
                        ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(getContext(), deviceIdString, idSesion, idEstudiante, idCard, costoTotal, ServicioTaskCobro.TOKEN_PHONE, ServicioTaskCobro.ENTENDIDO_CANCELAR);
                        servicioTaskCobro.execute();
                    }
                    dismiss();
                }else if(pantalla == CobroFinalFragment.PANTALLA_TE){//Si venimos de algún Tiempo Extra en Más Tiempo.
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.contexto_DISPONIBILIDAD_PROGRESO);
                    String idCardSesion = "idCard" + idSesion;
                    idCard = myPreferences.getString(idCardSesion, "Sin valor");

                    //TODO Cobro tentativo con OpenPay al elegir el tiempo EXTRA.
                    if(idCard.equalsIgnoreCase("Sin valor")){
                        //TODO obtenerToken de BD.
                        ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(getContext(), deviceIdString, idSesion, idEstudiante, idCard, costoTotal, ServicioTaskCobro.TOKEN_BD, ServicioTaskCobro.ENTENDIDO_TE);
                        servicioTaskCobro.execute();
                    }else{
                        ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(getContext(), deviceIdString, idSesion, idEstudiante, idCard, costoTotal, ServicioTaskCobro.TOKEN_PHONE, ServicioTaskCobro.ENTENDIDO_TE);
                        servicioTaskCobro.execute();
                    }
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