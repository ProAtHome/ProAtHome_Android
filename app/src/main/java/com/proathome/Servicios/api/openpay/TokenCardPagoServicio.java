package com.proathome.Servicios.api.openpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.proathome.Servicios.sesiones.ServiciosSesion;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Views.cliente.fragments.NuevaSesionFragment;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.fragments.PreOrdenServicio;

import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;

public class TokenCardPagoServicio extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String nombreTitular, tarjeta, cvv;
    private int mes, ano, idCliente;
    private Bundle bundle;
    private ProgressDialog progressDialog;

    public TokenCardPagoServicio(Context contexto, String nombreTitular, String tarjeta, int mes, int ano,
                                 String cvv, int idCliente){
        this.contexto = contexto;
        this.nombreTitular = nombreTitular;
        this.tarjeta = tarjeta;
        this.mes = mes;
        this.ano = ano;
        this.cvv = cvv;
        this.idCliente = idCliente;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Validando tarjeta", "Por favor espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        Card card = new Card();
        card.holderName(this.nombreTitular);
        card.cardNumber(this.tarjeta);
        card.expirationMonth(this.mes);
        card.expirationYear(this.ano);
        card.cvv2(this.cvv);

        Constants.openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {
                SweetAlert.showMsg(contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", e.getMessage(), false, null, null);
                progressDialog.dismiss();
                PreOrdenServicio.clickComprar = false;
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                SweetAlert.showMsg(contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", e.getMessage(), false, null, null);
                progressDialog.dismiss();
                PreOrdenServicio.clickComprar = false;
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                if(NetworkValidate.validate(contexto)){
                    String idCard = operationResult.getResult().getId();
                    //Cobro de Servicio
                    ServiciosSesion.cobroServicio(idCard, nombreTitular, NuevaSesionFragment.correoCliente,
                            bundle.getDouble("costoTotal"), "Cargo ProAtHome - " + bundle.getString("sesion"),
                            bundle.getString("deviceID"), idCliente, bundle, contexto);

                }else{
                    PreOrdenServicio.clickComprar = false;
                    Toast.makeText(contexto, "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });

        return null;
    }

    public void setBundleSesion(Bundle bundle){
        this.bundle = bundle;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
