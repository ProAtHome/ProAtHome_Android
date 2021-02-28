package com.proathome.servicios.clase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;

public class TokenCardPagoClase extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String nombreTitular, tarjeta, cvv;
    private int mes, ano, idEstudiante;
    private Bundle bundle;
    private ProgressDialog progressDialog;

    public TokenCardPagoClase(Context contexto, String nombreTitular, String tarjeta, int mes, int ano,
                            String cvv, int idEstudiante){
        this.contexto = contexto;
        this.nombreTitular = nombreTitular;
        this.tarjeta = tarjeta;
        this.mes = mes;
        this.ano = ano;
        this.cvv = cvv;
        this.idEstudiante = idEstudiante;
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
                errorMsg(e.toString());
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                errorMsg(e.toString());
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                String idCard = operationResult.getResult().getId();
                //Cobro de Clase
                ServicioTaskCobroClase cobroClase = new ServicioTaskCobroClase(contexto, idCard, nombreTitular,
                        NuevaSesionFragment.correoEstudiante, Double.parseDouble(bundle.getString("costoTotal")), "Cargo ProAtHome - " + bundle.getString("sesion"),
                        bundle.getString("deviceID"), idEstudiante);
                cobroClase.setBundleSesion(bundle);
                cobroClase.execute();
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

    public void errorMsg(String mensaje){
        new SweetAlert(DetallesFragment.contexto, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
        progressDialog.dismiss();
    }
}
