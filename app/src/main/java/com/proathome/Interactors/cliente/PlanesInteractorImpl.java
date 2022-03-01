package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.Planes.PlanesInteractor;
import com.proathome.Interfaces.cliente.Planes.PlanesPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONException;
import org.json.JSONObject;

public class PlanesInteractorImpl implements PlanesInteractor {

    private PlanesPresenter planesPresenter;

    public PlanesInteractorImpl(PlanesPresenter planesPresenter){
        this.planesPresenter = planesPresenter;
    }

    @Override
    public void getFechaServidor(int idCliente, String token) {
        planesPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    planesPresenter.setFechaServidor(jsonObject.getString("fechaServidor"));
                    getSesionActual(idCliente, token);
                }catch(JSONException ex){
                    ex.printStackTrace();
                    planesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                planesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.FECHA_SERVIDOR, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void getSesionActual(int idCliente, String token){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject rutaJSON = data.getJSONObject("mensaje");
                        //TODO FLUJO_COMPRAR_PLANES:  Mostramos la información del Plan (Título, Descripción, Fechas Inicio-Fin, términos y condiciones)
                        int seccion = rutaJSON.getInt("idSeccion");
                        int nivel = rutaJSON.getInt("idNivel");
                        int bloque = rutaJSON.getInt("idBloque");
                        int minutos_horas = rutaJSON.getInt("horas");
                        //ANUNCIO DE REPASO DE LECCIONES POR RUTA FINALIZADA
                        planesPresenter.setSesionActual(seccion, nivel, bloque);
                        datosBancariosPagoPlanes(idCliente, token);
                    }else
                        planesPresenter.showError(data.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    planesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                planesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_SESION_ACTUAL + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void datosBancariosPagoPlanes(int idCliente, String token){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            planesPresenter.hideProgress();
            if(response != null){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                        if(mensaje.getBoolean("existe"))
                            planesPresenter.setDatosBancarios(mensaje.getString("nombreTitular"), mensaje.getString("tarjeta"), mensaje.getString("mes"), mensaje.getString("ano"));
                        else
                            planesPresenter.showError("No tienes datos bancarios registrados");
                    }else
                        planesPresenter.showError(jsonObject.get("mensaje").toString());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    planesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                planesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_DATOS_BANCO_CLIENTE + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
