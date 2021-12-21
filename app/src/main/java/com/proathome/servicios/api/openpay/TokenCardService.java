package com.proathome.servicios.api.openpay;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.servicio.ServicioTaskMasTiempo;
import com.proathome.ui.ServicioCliente;
import com.proathome.ui.fragments.CobroFinalFragment;
import com.proathome.ui.fragments.DatosBancoPlanFragment;
import com.proathome.ui.fragments.MasTiempo;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;

public class TokenCardService extends AsyncTask<Void, Void, String> {

    private String nombreTitular, tarjeta, cvv;
    private int mes, ano;
    private Context contexto;

    public TokenCardService(Context contexto, String nombreTitular, String tarjeta, int mes, int ano, String cvv){
        this.contexto = contexto;
        this.nombreTitular = nombreTitular;
        this.tarjeta = tarjeta;
        this.mes = mes;
        this.ano = ano;
        this.cvv = cvv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        Card card = new Card();
        card.holderName(this.nombreTitular);
        card.cardNumber(this.tarjeta);
        card.expirationMonth(this.mes);
        card.expirationYear(this.ano);
        card.cvv2(this.cvv);

        Constants.openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {

            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {

            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                separadoresPersonalizados.setDecimalSeparator('.');
                DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
                JSONObject parametrosPost= new JSONObject();

                try{
                    parametrosPost.put("idCard", operationResult.getResult().getId());
                    parametrosPost.put("nombreCliente", CobroFinalFragment.nombreCliente);
                    parametrosPost.put("correo", CobroFinalFragment.correo);
                    parametrosPost.put("cobro", formato1.format(DatosBancoPlanFragment.costoTotal));
                    parametrosPost.put("descripcion", "Cargo ProAtHome - " + CobroFinalFragment.sesion);
                    parametrosPost.put("deviceId", DatosBancoPlanFragment.deviceIdString);
                    WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("respuesta")){
                                //Actualizar la orden de pago con el costo del TE
                                actualizarPagoTE();
                                //generarOrdenPago();

                                //Generamos el tiempo extra y la vida sigue.
                                ServicioTaskMasTiempo masTiempo = new ServicioTaskMasTiempo(contexto, DatosBancoPlanFragment.idSesion,
                                        DatosBancoPlanFragment.idCliente, CobroFinalFragment.progresoTotal);
                                masTiempo.execute();
                            }else//Mostramos el error.
                                showMsg("¡ERROR!",jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
                    }, APIEndPoints.COBROS, WebServicesAPI.POST, parametrosPost);
                    webServicesAPI.execute();
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });

        return resultado;
    }

    /*
    private void generarOrdenPago() throws JSONException {
        JSONObject jsonDatos = new JSONObject();
        if(DetallesFragment.planSesion.equalsIgnoreCase("PARTICULAR"))
            jsonDatos.put("costoServicio", TabuladorCosto.getCosto(ServicioCliente.idSeccion, ServicioCliente.tiempo, TabuladorCosto.PARTICULAR));
        else
            jsonDatos.put("costoServicio", 0);
        jsonDatos.put("costoTE", TabuladorCosto.getCosto(ServicioCliente.idSeccion, CobroFinalFragment.progresoTotal, TabuladorCosto.PARTICULAR));
        jsonDatos.put("idCliente", this.idCliente);
        jsonDatos.put("idSesion", this.idSesion);
        jsonDatos.put("estatusPago", "Pagado");
        jsonDatos.put("tipoPlan", DetallesFragment.planSesion);

        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            
        }, APIEndPoints.GENERAR_ORDEN_PAGO, WebServicesAPI.PUT, );
    }*/

    private void actualizarPagoTE() throws JSONException {
        JSONObject parametrosPost= new JSONObject();
        parametrosPost.put("cobro", DatosBancoPlanFragment.costoTotal);
        parametrosPost.put("idSesion", DatosBancoPlanFragment.idSesion);

        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            Toast.makeText(this.contexto, "Pago de Tiempo Extra actualizado.", Toast.LENGTH_SHORT).show();
        }, APIEndPoints.ACTUALIZAR_PAGO_TE, WebServicesAPI.PUT, parametrosPost);
        webServicesAPI.execute();
    }

    public void showMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    //Preguntamos si desea más tiempo Extra.
                    MasTiempo masTiempo = new MasTiempo();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                    bundle.putInt("idCliente", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                    masTiempo.setArguments(bundle);
                    masTiempo.show(ServicioCliente.obtenerFargment(Constants.fragmentActivity), "Tiempo Extra");
                })
                .show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
