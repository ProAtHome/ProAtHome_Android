package com.proathome.Interactors.cliente;

import android.view.View;
import com.proathome.Interfaces.cliente.Ruta.RutaInteractor;
import com.proathome.Interfaces.cliente.Ruta.RutaPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

public class RutaInteractorImpl implements RutaInteractor {

    private RutaPresenter rutaPresenter;

    public RutaInteractorImpl(RutaPresenter rutaPresenter){
        this.rutaPresenter = rutaPresenter;
    }

    @Override
    public void getEstatusExamen(int idCliente, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        //VALIDAMOS QUE LAS SESIONES ESTEN FINALZIADAS PARA EVITAR CRUCE DE NIVELES
                        if(jsonObject.getBoolean("sesionesFinalizadas")){
                            int estatus = jsonObject.getInt("estatus");
                            if(estatus == Constants.INICIO_EXAMEN)
                                rutaPresenter.showDialogExamen();
                            else if(estatus == Constants.ENCURSO_EXAMEN)
                                continuarExamen(idCliente, token);
                            else if(estatus == Constants.CANCELADO_EXAMEN)
                                rutaPresenter.setVisibilityButtonExamen(View.VISIBLE);
                            else if(estatus == Constants.EXAMEN_FINALIZADO)
                                rutaPresenter.showMsg("Ya tenemos tu diagnóstico.", "", SweetAlert.NORMAL_TYPE);
                        }else
                            rutaPresenter.showMsg("¡AVISO!","Para realizar el cuestionario diagnóstico finaliza todos tus servicios.", SweetAlert.WARNING_TYPE);
                    }else
                        rutaPresenter.showMsg("ERROR", data.getString("mensaje"), SweetAlert.ERROR_TYPE);
                }catch(JSONException ex){
                    rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
                    ex.printStackTrace();
                }
            }else
                rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
        }, APIEndPoints.GET_ESTATUS_EXAMEN_DIAGNOSTICO + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getEstadoRuta(int idCliente, int secciones, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject rutaJSON = data.getJSONObject("mensaje");
                        int estado = rutaJSON.getInt("estado");
            /*if(estado == Constants.INICIO_RUTA){
    }else */            if(estado == Constants.RUTA_ENCURSO) {
                            int idBloque = rutaJSON.getInt("idBloque");
                            int idNivel = rutaJSON.getInt("idNivel");
                            int idSeccion = rutaJSON.getInt("idSeccion");
                            rutaPresenter.setRutaActual(idSeccion, idNivel, idBloque);
                        }
                    }else
                        rutaPresenter.showMsg("ERROR", data.getString("mensaje"), SweetAlert.ERROR_TYPE);
                }catch(JSONException ex){
                    ex.printStackTrace();
                    rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
                }
            }else
                rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
        }, APIEndPoints.GET_ESTADO_RUTA + idCliente+ "/" + secciones + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void reiniciarExamen(int idCliente) {
        JSONObject reiniciar = new JSONObject();
        try {
            reiniciar.put("aciertos", 0);
            reiniciar.put("idCliente", idCliente);
            reiniciar.put("preguntaActual", 0);
            reiniciar.put("estatus", Constants.REINICIAR_EXAMEN);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        int estatus = jsonObject.getInt("estatus");
                        if(estatus == Constants.REINICIAR_EXAMEN)
                            rutaPresenter.successReinicio();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
                    }
                }else
                    rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
            }, APIEndPoints.REINICIAR_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, reiniciar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelarExamen(int idCliente) {
        JSONObject cancelar = new JSONObject();
        try {
            cancelar.put("idCliente", idCliente);
            cancelar.put("aciertos", 0);
            cancelar.put("preguntaActual", 0);
            cancelar.put("estatus", Constants.CANCELADO_EXAMEN);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        int estatusBD = jsonObject.getInt("estatus");
                        if(estatusBD == Constants.CANCELADO_EXAMEN)
                            rutaPresenter.setVisibilityButtonExamen(View.VISIBLE);
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
                    }
                }else
                    rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
            }, APIEndPoints.INICIO_O_CANCELAR_EXAMEN_DIAGNOSTICO, WebServicesAPI.POST, cancelar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void continuarExamen(int idCliente, String token){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        int estatus = jsonObject.getInt("estatus");
                        if(estatus == Constants.CONTINUAR_EXAMEN){
                            int pregunta = jsonObject.getInt("preguntaActual");
                            rutaPresenter.continuarExamen(pregunta);
                        }
                    }else
                        rutaPresenter.showMsg("ERROR", data.getString("mensaje"), SweetAlert.ERROR_TYPE);
                }catch (JSONException ex){
                    ex.printStackTrace();
                    rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
                }
            }else
                rutaPresenter.showMsg("ERROR", "Ocurrio un error, intente de nuevo mas tarde.", SweetAlert.ERROR_TYPE);
        }, APIEndPoints.GET_INFO_EXAMEN_DIAGNOSTICO + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
