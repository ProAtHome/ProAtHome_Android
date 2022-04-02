package com.proathome.Interactors.cliente;

import android.content.Context;
import android.util.Log;

import com.proathome.Interfaces.cliente.DetallesGestionar.DetallesGestionarInteractor;
import com.proathome.Interfaces.cliente.DetallesGestionar.DetallesGestionarPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.cliente.ServiciosCliente;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class DetallesGestionarInteractorImpl implements DetallesGestionarInteractor {

    private DetallesGestionarPresenter detallesGestionarPresenter;

    public DetallesGestionarInteractorImpl(DetallesGestionarPresenter detallesGestionarPresenter){
        this.detallesGestionarPresenter = detallesGestionarPresenter;
    }

    @Override
    public void getFechaServidor(String fechaSesion, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    detallesGestionarPresenter.setFechaServidor(jsonObject.getString("fechaServidor"));
                    ServiciosCliente.validarExpiracionServicio(jsonObject.getString("fechaServidor"), fechaSesion, context);
                }catch(JSONException ex){
                    ex.printStackTrace();
                    detallesGestionarPresenter.showMsg(SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                detallesGestionarPresenter.showMsg(SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.FECHA_SERVIDOR, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void actualizarSesion(JSONObject jsonObject) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                JSONObject dataResponse = new JSONObject(response);
                Log.d("TAG1", dataResponse.toString());
                if(dataResponse.getBoolean("respuesta"))
                    detallesGestionarPresenter.successUpdate(dataResponse.getString("mensaje"));
                else
                    detallesGestionarPresenter.showMsg(SweetAlert.ERROR_TYPE, "¡ERROR!", dataResponse.getString("mensaje"));
            }else
                SweetAlert.showMsg(SesionesFragment.contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al actualizar el servicio.", false, null, null);
        }, APIEndPoints.ACTUALIZAR_SESION, WebServicesAPI.PUT, jsonObject);
        webServicesAPI.execute();
    }

    @Override
    public void eliminarSesion(JSONObject jsonData) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta"))
                    detallesGestionarPresenter.successDelete(jsonObject.getString("mensaje"));
                else
                    detallesGestionarPresenter.showMsg(SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"));
            }else
                detallesGestionarPresenter.showMsg(SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.ELIMINAR_SESION, WebServicesAPI.POST, jsonData);
        webServicesAPI.execute();
    }

    @Override
    public void notificarProfesional(JSONObject jsonObject) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            detallesGestionarPresenter.closeFragment();
        }, APIEndPoints.NOTIFICACION_PROFESIONAL, WebServicesAPI.POST, jsonObject);
        webServicesAPI.execute();
    }

}
