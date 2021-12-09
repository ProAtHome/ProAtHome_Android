package com.proathome.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.proathome.ServicioCliente;
import com.proathome.R;
import com.proathome.servicios.servicio.ServicioTaskFinalizarServicio;
import com.proathome.servicios.servicio.ServicioTaskSumarServicioRuta;
import com.proathome.servicios.cliente.ServicioTaskPreOrden;
import com.proathome.utils.Constants;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MasTiempo extends DialogFragment {

    private Unbinder mUnbinder;
    private int idSesion, idCliente;
    public static Context contexto = null;

    public MasTiempo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mas_tiempo, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        this.idSesion = bundle.getInt("idSesion", 0);
        this.idCliente = bundle.getInt("idCliente", 0);
        /*Vamos a obtene los datos de la pre orden y usarlos para mostrar el cobro final*/
        ServicioTaskPreOrden preOrden = new ServicioTaskPreOrden(idCliente, idSesion, ServicioTaskPreOrden.PANTALLA_COBRO_FINAL);
        preOrden.execute();

        return view;
    }

    @OnClick({R.id.quinceMin, R.id.treintaMin})
    public void OnClickTiempo(View view){
        /*Creamos los datos de vamos a pasar a la wea*/
        Bundle bundle = new Bundle();
        bundle.putInt("idSesion", idSesion);
        bundle.putInt("idCliente", idCliente);
        bundle.putInt("estatus", Constants.FINALIZAR_SERVICIO);
        bundle.putInt("fragment", DetallesFragment.CLIENTE);
        bundle.putInt("idSeccion", ServicioCliente.idSeccion);
        bundle.putInt("idNivel", ServicioCliente.idNivel);
        bundle.putInt("idBloque", ServicioCliente.idBloque);
        bundle.putInt("tiempo", ServicioCliente.tiempo);
        bundle.putBoolean("sumar", ServicioCliente.sumar);
        /*Mostraremos el cobro final cuando nos de el Tiempo Extra*/
        CobroFinalFragment cobroFinalFragment = new CobroFinalFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.quinceMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 15);
                bundle.putString("tipoPlan", DetallesFragment.planSesion);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
            case R.id.treintaMin:
                bundle.putInt("Pantalla", 2);
                bundle.putInt("progresoTotal", 30);
                bundle.putString("tipoPlan", DetallesFragment.planSesion);
                cobroFinalFragment.setArguments(bundle);
                cobroFinalFragment.show(fragmentTransaction, "PreOrden");
                dismiss();
                break;
        }

    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        /*Vamos a obtene los datos de la pre orden y usarlos para mostrar el cobro final*/
        //TODO FLUJO_EJECUTAR_PLAN:  Cobramos sólo el TE elegido, crear otro MODAL Cobro Final MODO PLAN,
        // y poner las funciones de finalización de servicio que están en ServicioTaskCobro, etc.
        //Finalizamos el servicio, sumamos la ruta.
        ServicioTaskFinalizarServicio finalizarServicio = new ServicioTaskFinalizarServicio(this.contexto, this.idSesion, this.idCliente, Constants.FINALIZAR_SERVICIO, DetallesFragment.CLIENTE);
        finalizarServicio.execute();
        ServicioTaskSumarServicioRuta sumarServicioRuta = new ServicioTaskSumarServicioRuta(this.contexto, this.idSesion, this.idCliente, ServicioCliente.idSeccion, ServicioCliente.idNivel, ServicioCliente.idBloque, ServicioCliente.tiempo, ServicioCliente.sumar);
        sumarServicioRuta.execute();

        dismiss();
        /*
        if(DetallesFragment.planSesion.equalsIgnoreCase("PARTICULAR")){
            Bundle bundle = new Bundle();
            bundle.putInt("Pantalla", 1);
            bundle.putInt("idSesion", idSesion);
            bundle.putInt("idCliente", idCliente);
            bundle.putInt("estatus", Constants.FINALIZAR_SERVICIO);
            bundle.putInt("fragment", DetallesFragment.CLIENTE);
            bundle.putInt("idSeccion", ServicioCliente.idSeccion);
            bundle.putInt("idNivel", ServicioCliente.idNivel);
            bundle.putInt("idBloque", ServicioCliente.idBloque);
            bundle.putInt("tiempo", ServicioCliente.tiempo);
            bundle.putInt("progresoTotal", 0);
            bundle.putString("tipoPlan", DetallesFragment.planSesion);
            bundle.putBoolean("sumar", ServicioCliente.sumar);
            CobroFinalFragment cobroFinalFragment = new CobroFinalFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            cobroFinalFragment.setArguments(bundle);
            cobroFinalFragment.show(fragmentTransaction, "PreOrden");
            dismiss();
        }else{
            //Actualizar la orden de pago con estatusPago = Pagado.
            ServicioTaskOrdenPago ordenPago = new ServicioTaskOrdenPago(this.idCliente, this.idSesion, 0, 0, DetallesFragment.planSesion, "Pagado", ServicioTaskOrdenPago.SOLICITUD_COBRO_TE);
            ordenPago.execute();
            //Finalizamos el servicio, sumamos la ruta.
            ServicioTaskFinalizarServicio finalizarServicio = new ServicioTaskFinalizarServicio(this.contexto, this.idSesion, this.idCliente, Constants.FINALIZAR_SERVICIO, DetallesFragment.CLIENTE);
            finalizarServicio.execute();
            ServicioTaskSumarServicioRuta sumarServicioRuta = new ServicioTaskSumarServicioRuta(this.contexto, this.idSesion, this.idCliente, ServicioCliente.idSeccion, ServicioCliente.idNivel, ServicioCliente.idBloque, ServicioCliente.tiempo, ServicioCliente.sumar);
            sumarServicioRuta.execute();

            dismiss();

        }*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}