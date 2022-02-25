package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoInteractor;
import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class RutaAvanzadoInteractorImpl implements RutaAvanzadoInteractor {

    private RutaAvanzadoPresenter rutaAvanzadoPresenter;

    public RutaAvanzadoInteractorImpl(RutaAvanzadoPresenter rutaAvanzadoPresenter){
        this.rutaAvanzadoPresenter = rutaAvanzadoPresenter;
    }

    @Override
    public void getEstadoRuta(int idCliente, int nivel, String token) {
        rutaAvanzadoPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            rutaAvanzadoPresenter.hideProgress();
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
                            rutaAvanzadoPresenter.evaluarNivel(idSeccion, idNivel, idBloque);
                        }
                    }else
                        rutaAvanzadoPresenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    rutaAvanzadoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                rutaAvanzadoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_ESTADO_RUTA + idCliente + "/" + nivel + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
