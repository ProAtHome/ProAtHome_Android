package com.proathome.servicios.api.openpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
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
    private ProgressDialog progressDialog;

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
        progressDialog = ProgressDialog.show(contexto, "Validando Tarjeta", "Por favor, espere...");
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
                progressDialog.dismiss();
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                progressDialog.dismiss();
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
                    ProgressDialog progressDialog = ProgressDialog.show(contexto, "Generando Cobro", "Por favor, espere...");
                    WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                        progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("respuesta")){
                                //Actualizar la orden de pago con el costo del TE
                                actualizarPagoTE();
                                //generarOrdenPago();

                                //Generamos el tiempo extra y la vida sigue.
                                activarTiempoExtra();
                            }else {//Mostramos el error.
                                SweetAlert.showMsg(contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), true, "OK", ()->{
                                    //Preguntamos si desea más tiempo Extra.
                                    MasTiempo masTiempo = new MasTiempo();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                                    bundle.putInt("idCliente", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                                    masTiempo.setArguments(bundle);
                                    masTiempo.show(ServicioCliente.obtenerFargment(Constants.fragmentActivity), "Tiempo Extra");
                                });
                            }
                    }, APIEndPoints.COBROS, WebServicesAPI.POST, parametrosPost);
                    webServicesAPI.execute();
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });

        return resultado;
    }

    private void activarTiempoExtra(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            Toast.makeText(contexto, "Tiempo Extra en Curso.", Toast.LENGTH_SHORT).show();
        }, APIEndPoints.ACTIVAR_TIEMPO_EXTRA + DatosBancoPlanFragment.idSesion + "/" + DatosBancoPlanFragment.idCliente + "/" + CobroFinalFragment.progresoTotal, WebServicesAPI.PUT, new JSONObject());
        webServicesAPI.execute();
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
