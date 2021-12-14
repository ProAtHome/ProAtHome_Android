package com.proathome.servicios.api.openpay;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.servicios.ServicioTaskCobro;
import com.proathome.ui.fragments.DatosBancoPlanFragment;
import com.proathome.utils.Constants;
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
                ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(contexto, DatosBancoPlanFragment.deviceIdString, DatosBancoPlanFragment.idSesion, DatosBancoPlanFragment.idCliente, operationResult.getResult().getId(), DatosBancoPlanFragment.costoTotal, ServicioTaskCobro.ENTENDIDO_TE);
                servicioTaskCobro.execute();
            }
        });

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
