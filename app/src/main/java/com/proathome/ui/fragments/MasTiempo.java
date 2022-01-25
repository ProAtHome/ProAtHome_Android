package com.proathome.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.sesiones.ServiciosSesion;
import com.proathome.ui.ServicioCliente;
import com.proathome.R;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.FechaActual;
import com.proathome.utils.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
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
        getPreOrden();

        return view;
    }

    private void getPreOrden(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject jsonObject = data.getJSONObject("mensaje");
                    CobroFinalFragment.nombreTitular = jsonObject.getString("nombreTitular");
                    CobroFinalFragment.mes = jsonObject.get("mes").toString();
                    CobroFinalFragment.ano = jsonObject.get("ano").toString();
                    CobroFinalFragment.metodoRegistrado = jsonObject.getString("tarjeta");
                    CobroFinalFragment.sesion = "Sesión: " + Component.getSeccion(jsonObject.getInt("idSeccion")) + " / " + Component.getNivel(jsonObject.getInt("idSeccion"), jsonObject.getInt("idNivel")) + " / " + Component.getBloque(jsonObject.getInt("idBloque"));
                    CobroFinalFragment.tiempo = "Tiempo: " + obtenerHorario(jsonObject.getInt("tiempo"));
                    CobroFinalFragment.nombreCliente = jsonObject.get("nombreCliente").toString();
                    CobroFinalFragment.correo = jsonObject.get("correo").toString();
                }else
                    Toast.makeText(getContext(), data.getString("mensaje"), Toast.LENGTH_LONG).show();
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_PRE_ORDEN + this.idCliente + "/" + this.idSesion + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
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
        ServiciosSesion.finalizar(idSesion, idCliente);
        sumarSevicioRuta();

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
            generarOrdenPago();
            //Finalizamos el servicio, sumamos la ruta.
            ServicioSesion.finalizarServicio();
            ServicioTaskSumarServicioRuta sumarServicioRuta = new ServicioTaskSumarServicioRuta(this.contexto, this.idSesion, this.idCliente, ServicioCliente.idSeccion, ServicioCliente.idNivel, ServicioCliente.idBloque, ServicioCliente.tiempo, ServicioCliente.sumar);
            sumarServicioRuta.execute();

            dismiss();

        }*/

    }

    private void generarOrdenPago() throws JSONException {
        JSONObject jsonDatos = new JSONObject();
        jsonDatos.put("costoServicio", 0);
        jsonDatos.put("costoTE", 0);
        jsonDatos.put("idCliente", this.idCliente);
        jsonDatos.put("idSesion", this.idSesion);
        jsonDatos.put("estatusPago", "Pagado");
        jsonDatos.put("tipoPlan", DetallesFragment.planSesion);

        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

        }, APIEndPoints.ORDEN_COMPRA_ACTUALIZAR_PAGO, WebServicesAPI.PUT, jsonDatos);
        webServicesAPI.execute();
    }

    private void sumarSevicioRuta(){
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("idCliente", this.idCliente);
            parametros.put("idSesion", this.idSesion);
            parametros.put("idSeccion", ServicioCliente.idSeccion);
            parametros.put("idNivel",  ServicioCliente.idNivel);
            parametros.put("idBloque", ServicioCliente.idBloque);
            parametros.put("horasA_sumar", ServicioCliente.tiempo);
            parametros.put("fecha_registro", FechaActual.getFechaActual());
            parametros.put("sumar", ServicioCliente.sumar);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                //TODO AQUI PODEMOS PONER UN ANUNCIO DE FIN DE RUTA CUANDO LA VAR ultimaSesion SEA TRUE.
                DetallesFragment.procedenciaFin = true;
            }, APIEndPoints.SUMAR_SERVICIO_RUTA, WebServicesAPI.POST, parametros);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}