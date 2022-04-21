package com.proathome.Interactors.profesional;

import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarInteractor;
import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import org.json.JSONException;
import org.json.JSONObject;

public class DetallesGestionarInteractorImpl implements DetallesGestionarInteractor {

    private DetallesGestionarPresenter detallesGestionarPresenter;

    public DetallesGestionarInteractorImpl(DetallesGestionarPresenter detallesGestionarPresenter){
        this.detallesGestionarPresenter = detallesGestionarPresenter;
    }

    @Override
    public void cancelarSesion(int idProfesional, int idSesion) {
        detallesGestionarPresenter.showProgress();
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", idProfesional);
            parametrosPUT.put("idServicio", idSesion);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                detallesGestionarPresenter.hideProgress();
                if(response != null){
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta"))
                        detallesGestionarPresenter.successCancel(jsonObject.getString("mensaje"));
                    else
                        detallesGestionarPresenter.showError(jsonObject.getString("mensaje"));
                }else
                    detallesGestionarPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.CANCELAR_SERVICIO, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            detallesGestionarPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

    @Override
    public void solicitudEliminarSesion(int idProfesional, int idSesion, String token) {
        detallesGestionarPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            detallesGestionarPresenter.hideProgress();
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                        if(mensaje.getBoolean("eliminar") && mensaje.getBoolean("multa")){
                            //Mostrar que se puede eliminar pero con multa.
                            detallesGestionarPresenter.showAlertCancel("Si cancelas esta servicio serás acreedor de una multa, ya que para cancelar libremente deberá ser 3 HRS antes.");
                        }else if(mensaje.getBoolean("eliminar") && !mensaje.getBoolean("multa")){
                            //Eliminar sin multa.
                            detallesGestionarPresenter.showAlertCancel("¿Deseas cancelar el servicio?");
                        }else if(!mensaje.getBoolean("eliminar")){
                            //No se puede eliminar
                            detallesGestionarPresenter.showError("No se puede cancelar el servicio a menos de 24 HRS de esta.");
                        }
                    }else
                        detallesGestionarPresenter.showError(jsonObject.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    detallesGestionarPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                detallesGestionarPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.SOLICITUD_ELIMINAR_SESION_PROFESIONAL  + idSesion + "/" + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getFotoPerfil(String foto) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            if(response != null)
                detallesGestionarPresenter.setFotoBitmap(response);
            else
                detallesGestionarPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

    @Override
    public void notificarCliente(JSONObject jsonObject) {
        detallesGestionarPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            detallesGestionarPresenter.hideProgress();
            detallesGestionarPresenter.closeFragment();
        }, APIEndPoints.NOTIFICACION_CLIENTE, WebServicesAPI.POST, jsonObject);
        webServicesAPI.execute();
    }

}
