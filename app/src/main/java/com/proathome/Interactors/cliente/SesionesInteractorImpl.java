package com.proathome.Interactors.cliente;

import android.content.Context;
import android.util.Log;
import com.proathome.Interfaces.cliente.Sesiones.SesionesInteractor;
import com.proathome.Interfaces.cliente.Sesiones.SesionesPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SesionesInteractorImpl implements SesionesInteractor {

    private SesionesPresenter sesionesPresenter;

    public SesionesInteractorImpl(SesionesPresenter sesionesPresenter){
        this.sesionesPresenter = sesionesPresenter;
    }

    @Override
    public void getSesiones(int idCliente, Context context) {
        sesionesPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                //iniciarProcesoRuta();

                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject jsonObject = data.getJSONObject("mensaje");
                    JSONArray jsonArray = jsonObject.getJSONArray("sesiones");

                    if(jsonArray.length() == 0)
                        sesionesPresenter.setVisibilityLottie();

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        //TODO FLUJO_PLANES: Nota - Si el servicio está finalizada no se puede eliminar ni editar (No mostrar en Gestión)
                        if(!object.getBoolean("finalizado"))
                            sesionesPresenter.setMyAdapter(object);
                    }
                }else
                    sesionesPresenter.showError(data.getString("mensaje"));

                sesionesPresenter.hideProgress();
            }catch(JSONException ex){
                ex.printStackTrace();
                sesionesPresenter.hideProgress();
            }
            //sesionesPagadas(idCliente, context);
        }, APIEndPoints.GET_SESIONES_CLIENTE  + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    /*
    @Override
    public void getDisponibilidadServicio(int idCliente, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject jsonObject = data.getJSONObject("mensaje");
                    if(jsonObject.getBoolean("disponibilidad"))
                        sesionesPresenter.setDisponibilidadEstatus(true, jsonObject.getInt("horasDisponibles"));
                    else
                        sesionesPresenter.setDisponibilidadEstatus(false, 0);
                }else{
                    sesionesPresenter.showError(data.getString("mensaje"));*/
                    /*
                    SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ERROR!", data.getString("mensaje"), true, "OK", ()->{
                        validarPlan_Monedero();
                    });*/
          /*      }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.DISPONIBILIDAD_SERVICIO + idCliente + "/" +  SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

    @Override
    public void getInfoInicioSesiones(int idCliente) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            JSONObject data = new JSONObject(response);
            if(data.getBoolean("respuesta")){
                JSONObject body  = data.getJSONObject("mensaje");

                //DISPONIBILIDAD SERVICIO
                if(body.getBoolean("disponibilidad"))
                    sesionesPresenter.setDisponibilidadEstatus(true, body.getInt("horasDisponibles"));
                else
                    sesionesPresenter.setDisponibilidadEstatus(false, 0);

                //SESIONES PAGADAS Y FINALIZADAS
                sesionesPresenter.setEstatusSesionesPagadas(body.getBoolean("plan_activo"), body.getBoolean("sesiones_pagadas_finalizadas"));

                //VALIDAR PLAN
                JSONObject bodyPlan = body.getJSONObject("plan");
                sesionesPresenter.setInfoPlan(bodyPlan);

                //SESION ACTUAL
                sesionesPresenter.setRutaActual(body);

                //DATOS BANCO
                JSONObject bodyDatos = body.getJSONObject("datosBancarios");
                sesionesPresenter.setDatosBanco(bodyDatos);

            }else
                sesionesPresenter.showError(data.getString("mensaje"));
        }, APIEndPoints.GET_INFO_INICIO_SESIONES + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    /*
    private void sesionesPagadas(int idCliente, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            JSONObject data = new JSONObject(response);
            if(data.getBoolean("respuesta")){
                JSONObject jsonObject = data.getJSONObject("mensaje");
                //TODO FLUJO_PLANES_EJECUTAR: Posible cambio de algortimo para obtener plan_activo, verificar la fecha de inicio si es distinto a PARTICULAR.
                sesionesPresenter.setEstatusSesionesPagadas(jsonObject.getBoolean("plan_activo"), jsonObject.getBoolean("sesiones_pagadas_finalizadas"));
            }else
                sesionesPresenter.showError(data.getString("mensaje"));
        }, APIEndPoints.SESIONES_PAGADAS_Y_FINALIZADAS + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

}
