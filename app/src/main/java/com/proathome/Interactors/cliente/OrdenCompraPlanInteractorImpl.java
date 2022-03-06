package com.proathome.Interactors.cliente;

import android.util.Log;

import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanInteractor;
import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.fragments.OrdenCompraPlanFragment;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;
import org.json.JSONException;
import org.json.JSONObject;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import mx.openpay.android.validation.CardValidator;

public class OrdenCompraPlanInteractorImpl implements OrdenCompraPlanInteractor {

    private OrdenCompraPlanPresenter ordenCompraPlanPresenter;

    public OrdenCompraPlanInteractorImpl(OrdenCompraPlanPresenter ordenCompraPlanPresenter){
        this.ordenCompraPlanPresenter = ordenCompraPlanPresenter;
    }

    @Override
    public void comprar(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                        int idCliente, String token, JSONObject jsonDatosPago) {
        /*Checamos los campos de la perra tarjeta*/
        if(!nombreTitular.equalsIgnoreCase("") && !tarjeta.equalsIgnoreCase("") &&
                !mes.equalsIgnoreCase("") && !ano.equalsIgnoreCase("") && !cvv.equalsIgnoreCase("")){
            if(CardValidator.validateHolderName(nombreTitular)){
                if(CardValidator.validateNumber(tarjeta)){
                    if(CardValidator.validateExpiryDate(Integer.parseInt(mes), Integer.parseInt(ano))){
                        if(CardValidator.validateCVV(cvv, tarjeta)){
                            ordenCompraPlanPresenter.showProgress();
                            ordenCompraPlanPresenter.btnComprarEnabled(false);
                            /*Vamos a crear un perro y poderoso token de tarjeta*/
                            Card card = new Card();
                            card.holderName(nombreTitular);
                            card.cardNumber(tarjeta);
                            card.expirationMonth(Integer.parseInt(mes));
                            card.expirationYear(Integer.parseInt(ano));
                            card.cvv2(cvv);
                            pagar(card, idCliente, token, jsonDatosPago);
                        }else
                            ordenCompraPlanPresenter.showError("CVV no válido.");
                    }else
                        ordenCompraPlanPresenter.showError("Fecha de expiración no válida.");
                }else
                    ordenCompraPlanPresenter.showError("Tarjeta no válida.");
            }else
                ordenCompraPlanPresenter.showError("Nombre del titular no válido.");
        }else
            ordenCompraPlanPresenter.showError("Llena todos los campos correctamente.");
    }

    public void pagar(Card card, int idCliente, String token, JSONObject jsonDatosPago){
        Constants.openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {
                ordenCompraPlanPresenter.hideProgress();
                ordenCompraPlanPresenter.showError(e.toString());
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                ordenCompraPlanPresenter.hideProgress();
                ordenCompraPlanPresenter.showError(e.toString());
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                try{
                    jsonDatosPago.put("idCard",  operationResult.getResult().getId());
                    WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                        ordenCompraPlanPresenter.hideProgress();
                        if(response != null){

                                Log.d("TAGORDEN", response);
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getBoolean("respuesta")){
                                    generarPlan(idCliente, token);
                                    ordenCompraPlanPresenter.successPlan();
                                }else
                                    ordenCompraPlanPresenter.errorPlan(jsonObject.getString("mensaje"));
                        }else
                            ordenCompraPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }, APIEndPoints.COBROS, WebServicesAPI.POST, jsonDatosPago);
                    webServicesAPI.execute();
                }catch(JSONException ex){
                    ex.printStackTrace();
                    ordenCompraPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }
        });
    }

    private void generarPlan(int idCliente, String token) throws JSONException {
        JSONObject parametrosPost= new JSONObject();
        parametrosPost.put("tipoPlan", OrdenCompraPlanFragment.tipoPlan);
        parametrosPost.put("fechaInicio", OrdenCompraPlanFragment.fechaIn);
        parametrosPost.put("fechaFin", OrdenCompraPlanFragment.fechaFi);
        parametrosPost.put("monedero", OrdenCompraPlanFragment.monedero);
        parametrosPost.put("idCliente", idCliente);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            sesionesPagadas(idCliente, token);
            validarPlan(idCliente, token);
        }, APIEndPoints.GENERAR_PLAN, WebServicesAPI.POST, parametrosPost);
        webServicesAPI.execute();
    }

    public void validarPlan(int idCliente, String token){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject jsonDatos = new JSONObject(response);
                    if(!jsonDatos.getBoolean("respuesta")){
                        ordenCompraPlanPresenter.validarPlanError("Error al obtener la información.");
                    }else{
                        JSONObject body = jsonDatos.getJSONObject("mensaje");
                        SesionesFragment.PLAN =  body.getString("tipoPlan");
                        SesionesFragment.MONEDERO = body.getInt("monedero");
                        SesionesFragment.FECHA_INICIO = body.getString("fechaInicio");
                        SesionesFragment.FECHA_FIN = body.getString("fechaFin");
                        InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (body.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : body.getString("tipoPlan")));
                        InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " +
                                obtenerHorario(body.getInt("monedero")));
                        InicioCliente.planActivo = body.getString("tipoPlan");
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                    ordenCompraPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                ordenCompraPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_PLAN + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void sesionesPagadas(int idCliente, String token){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject jsonObject = data.getJSONObject("mensaje");
                    //TODO FLUJO_PLANES_EJECUTAR: Posible cambio de algortimo para obtener plan_activo, verificar la fecha de inicio si es distinto a PARTICULAR.
                    SesionesFragment.PLAN_ACTIVO = jsonObject.getBoolean("plan_activo");
                    SesionesFragment.SESIONES_PAGADAS_FINALIZADAS = jsonObject.getBoolean("sesiones_pagadas_finalizadas");
                }else
                    ordenCompraPlanPresenter.showError(data.getString("mensaje"));
            }else
                ordenCompraPlanPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.SESIONES_PAGADAS_Y_FINALIZADAS + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

}
