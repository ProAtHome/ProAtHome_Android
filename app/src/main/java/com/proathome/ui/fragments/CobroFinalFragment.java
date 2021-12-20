package com.proathome.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.proathome.ui.ServicioCliente;
import com.proathome.R;
import com.proathome.servicios.cliente.TabuladorCosto;
import com.proathome.servicios.cliente.ServicioTaskPreOrden;
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
//    @BindView(R.id.tarjeta)
  //  TextView tvTarjeta;
    @BindView(R.id.tvSesion)
    TextView tvSesion;
    //@BindView(R.id.tvTiempo)
    //TextView tvTiempo;
    @BindView(R.id.tvTiempoExtra)
    TextView tvTiempoExtra;
    @BindView(R.id.tvCostoTotal)
    TextView tvCostoTotal;
    @BindView(R.id.tvTipoPlan)
    TextView tvTipoPlan;
    public static String metodoRegistrado, sesion, tiempo, deviceIdString, nombreCliente, correo, tipoPlan;
    //Datos tarjeta para modo PLAN.
    public static String nombreTitular, tarjeta, mes, ano;
    public static double costoTotal = 0.0;
    public static int idSesion = 0, idCliente = 0, pantalla = 0, progresoTotal = 0;
    public static final int PANTALLA_CANCELAR = 1, PANTALLA_TE = 2;
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
        idCliente = bundle.getInt("idCliente");
        pantalla = bundle.getInt("Pantalla");
        progresoTotal = bundle.getInt("progresoTotal");
        //tvTarjeta.setText(metodoRegistrado);
        tvSesion.setText(sesion);
        //tvTiempo.setText(tiempo);
        tvTipoPlan.setText("PLAN DEL SERVICIO: " + bundle.getString("tipoPlan"));
        tipoPlan = bundle.getString("tipoPlan");
        tvTiempoExtra.setText("Tiempo Extra: " + obtenerHorario(progresoTotal));
        //if(bundle.getString("tipoPlan").equalsIgnoreCase("PARTICULAR"))
          //  costoTotal = (double) TabuladorCosto.getCosto(ServicioCliente.idSeccion, ServicioCliente.tiempo, TabuladorCosto.PARTICULAR) + TabuladorCosto.getCosto(ServicioCliente.idSeccion, progresoTotal, TabuladorCosto.PARTICULAR);
        //else
        costoTotal = (double) TabuladorCosto.getCosto(ServicioCliente.idSeccion, progresoTotal, TabuladorCosto.PARTICULAR);
        tvCostoTotal.setText("Total: " + String.format("%.2f", costoTotal) + " MXN");

        ServicioTaskPreOrden preOrden = new ServicioTaskPreOrden(idCliente, idSesion, ServicioTaskPreOrden.PANTALLA_COBRO_FINAL);
        preOrden.execute();

        return view;
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    @OnClick({R.id.entendido, R.id.regresar})
    public void onClick(View view){
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.entendido:
            /*    if(pantalla == CobroFinalFragment.PANTALLA_CANCELAR){//Si venimos de el boton de CANCELAR en Más Tiempo.
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.contexto_DISPONIBILIDAD_PROGRESO);
                    String idCardSesion = "idCard" + idSesion;
                    idCard = myPreferences.getString(idCardSesion, "Sin valor");

                    ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(getContext(), deviceIdString, idSesion, idCliente, idCard, costoTotal, ServicioTaskCobro.ENTENDIDO_CANCELAR);
                    servicioTaskCobro.execute();
                    DetallesFragment.procedenciaFin = true;
                    dismiss();
                    //TODO FLUJO_EVALUACION: Mostrar modal de evaluación;
                }else if(pantalla == CobroFinalFragment.PANTALLA_TE){ */ //Si venimos de algún Tiempo Extra en Más Tiempo.
                 //   SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.contexto_DISPONIBILIDAD_PROGRESO);
                   // String idCardSesion = "idCard" + idSesion;
                    //idCard = myPreferences.getString(idCardSesion, "Sin valor");

                    //Cobro tentativo con OpenPay al elegir el tiempo EXTRA.
                    //Validar si estamos en tipoPlan.
               /*     if(tipoPlan.equalsIgnoreCase("PARTICULAR")){
                        ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(getContext(), deviceIdString, idSesion, idCliente, idCard, costoTotal, ServicioTaskCobro.ENTENDIDO_TE);
                        servicioTaskCobro.execute();
                    }else{*/
                        //Crear nuevo token de Tarjeta.
                        //TODO FLUJO_EJECUTAR_PLAN: Validar metodo de pago.

                bundle.putInt("procedencia", DatosBancoPlanFragment.PROCEDENCIA_PAGO_PLAN);
                bundle.putString("deviceIdString", deviceIdString);
                bundle.putInt("idSesion", idSesion);
                bundle.putInt("idCliente", idCliente);
                bundle.putDouble("costoTotal", costoTotal);
                DatosBancoPlanFragment bancoPlanFragment = new DatosBancoPlanFragment();
                bancoPlanFragment.setArguments(bundle);
                bancoPlanFragment.show(fragmentTransaction, "Validar datos");
                   // }

                dismiss();
                //}
                break;
            case R.id.regresar:
                //if(pantalla == CobroFinalFragment.PANTALLA_CANCELAR){//Si venimos de el boton de CANCELAR en Más Tiempo.
                  //  dismiss();
                //}else if(pantalla == CobroFinalFragment.PANTALLA_TE){//Si venimos de algún Tiempo Extra en Más Tiempo.
                    //Volvemos a mostrar los Tiempos extras disponibles.
                MasTiempo masTiempo = new MasTiempo();
 //               Bundle bundle = new Bundle();
                bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                bundle.putInt("idCliente", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);

                masTiempo.setArguments(bundle);
                masTiempo.show(fragmentTransaction, "Tiempo Extra");
                dismiss();
                //}
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}