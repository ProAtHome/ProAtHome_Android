package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.RutaIntermedio.RutaIntermedioInteractor;
import com.proathome.Interfaces.cliente.RutaIntermedio.RutaIntermedioPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class RutaIntermedioInteractorImpl implements RutaIntermedioInteractor {

    private RutaIntermedioPresenter rutaIntermedioPresenter;

    public RutaIntermedioInteractorImpl(RutaIntermedioPresenter rutaIntermedioPresenter){
        this.rutaIntermedioPresenter = rutaIntermedioPresenter;
    }

    @Override
    public void getEstadoRuta(int idCliente, int nivel, String token) {
        rutaIntermedioPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            rutaIntermedioPresenter.hideProgress();
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
                            rutaIntermedioPresenter.evaluarNivel(idSeccion, idNivel, idBloque);
                        }
                    }else
                        rutaIntermedioPresenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    rutaIntermedioPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                rutaIntermedioPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_ESTADO_RUTA + idCliente + "/" + nivel + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
