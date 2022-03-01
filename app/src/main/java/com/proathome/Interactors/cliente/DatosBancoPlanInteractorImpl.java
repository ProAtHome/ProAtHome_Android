package com.proathome.Interactors.cliente;

import android.content.Context;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanInteractor;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.openpay.TokenCardService;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.fragments.DatosBancoPlanFragment;
import com.proathome.Views.cliente.fragments.PagoPendienteFragment;
import org.json.JSONException;
import org.json.JSONObject;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import mx.openpay.android.validation.CardValidator;

public class DatosBancoPlanInteractorImpl implements DatosBancoPlanInteractor {

    private DatosBancoPlanPresenter datosBancoPlanPresenter;

    public DatosBancoPlanInteractorImpl(DatosBancoPlanPresenter datosBancoPlanPresenter){
        this.datosBancoPlanPresenter = datosBancoPlanPresenter;
    }

    @Override
    public void validarDatos(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                             int procedencia, Context context, JSONObject datosPago, int idSesion) {
        if(!nombreTitular.trim().equalsIgnoreCase("") && !tarjeta.trim().equalsIgnoreCase("") &&
                !mes.trim().equalsIgnoreCase("") && !ano.trim().equalsIgnoreCase("") && !cvv.trim().equalsIgnoreCase("")){
            if(CardValidator.validateHolderName(nombreTitular)){
                if(CardValidator.validateNumber(tarjeta)){
                    if(CardValidator.validateExpiryDate(Integer.parseInt(mes), Integer.parseInt(ano))){
                        if(CardValidator.validateCVV(cvv,tarjeta)){
                            /*Vamos a crear un perro y poderoso token de tarjeta*/
                            if(procedencia == DatosBancoPlanFragment.PROCEDENCIA_PAGO_PENDIENTE){
                                datosBancoPlanPresenter.setEstatusButtonValidarDatos(false);
                                /*Vamos a crear un perro y poderoso token de tarjeta*/
                                Card card = new Card();
                                card.holderName(nombreTitular);
                                card.cardNumber(tarjeta);
                                card.expirationMonth(Integer.parseInt(mes));
                                card.expirationYear(Integer.parseInt(ano));
                                card.cvv2(cvv);
                                pagar(card, datosPago, idSesion);
                            }else if(procedencia == DatosBancoPlanFragment.PROCEDENCIA_PAGO_PLAN){
                                TokenCardService tokenCard = new TokenCardService(context, nombreTitular, tarjeta, Integer.parseInt(mes), Integer.parseInt(ano), cvv);
                                tokenCard.execute();
                                datosBancoPlanPresenter.cerrarFragment();
                            }
                        }else{
                            datosBancoPlanPresenter.showError("CVV no válido.");
                            datosBancoPlanPresenter.setEstatusButtonValidarDatos(true);
                        }
                    }else{
                        datosBancoPlanPresenter.showError("Fecha de expiración no válida.");
                        datosBancoPlanPresenter.setEstatusButtonValidarDatos(true);
                    }
                }else{
                    datosBancoPlanPresenter.showError("Tarjeta no válida.");
                    datosBancoPlanPresenter.setEstatusButtonValidarDatos(true);
                }
            }else{
                datosBancoPlanPresenter.showError("Nombre del titular no válido.");
                datosBancoPlanPresenter.setEstatusButtonValidarDatos(true);
            }
        }else {
            datosBancoPlanPresenter.showError("Llena todos los campos correctamente.");
            datosBancoPlanPresenter.setEstatusButtonValidarDatos(true);
        }
    }

    private void pagar(Card card, JSONObject datosPago, int idSesion){
        datosBancoPlanPresenter.showProgress();
        Constants.openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {
                datosBancoPlanPresenter.hideProgress();
                datosBancoPlanPresenter.showError(e.getMessage());
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                datosBancoPlanPresenter.hideProgress();
                datosBancoPlanPresenter.showError(e.getMessage());
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                datosBancoPlanPresenter.hideProgress();
                try {
                    datosPago.put("idCard", operationResult.getResult().getId());
                    WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                        if(response != null){
                            if(response.equalsIgnoreCase(  "")){
                                //Actualizar Pago en Pagos
                                saldarDeuda(idSesion);
                                datosBancoPlanPresenter.successOperation("Cobro correcto.", "¡GENIAL!", SweetAlert.SUCCESS_TYPE);
                            }else
                                datosBancoPlanPresenter.successOperation("Error en el cobro - " + response,"¡ERROR!", SweetAlert.ERROR_TYPE);
                        }else
                            datosBancoPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }, APIEndPoints.COBROS, WebServicesAPI.POST, datosPago);
                    webServicesAPI.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                    datosBancoPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }
        });
    }


    private void saldarDeuda(int idSesion) throws JSONException {
        JSONObject parametrosPUT = new JSONObject();
        parametrosPUT.put("idSesion", idSesion);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            PagoPendienteFragment.pagoPendiente.dismiss();
        }, APIEndPoints.SALDAR_DEUDA, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();
    }

}
