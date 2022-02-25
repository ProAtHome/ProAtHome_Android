package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.RutaBasico.RutaBasicoInteractor;
import com.proathome.Interfaces.cliente.RutaBasico.RutaBasicoPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class RutaBasicoInteractorImpl implements RutaBasicoInteractor {

    private RutaBasicoPresenter rutaBasicoPresenter;

    public RutaBasicoInteractorImpl(RutaBasicoPresenter rutaBasicoPresenter){
        this.rutaBasicoPresenter = rutaBasicoPresenter;
    }

    @Override
    public void getEstadoRuta(int idCliente, int nivel, String token) {
        rutaBasicoPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            rutaBasicoPresenter.hideProgress();
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
                            rutaBasicoPresenter.evaluarNivelBasico(idSeccion, idNivel, idBloque);
                        }
                    }else
                        rutaBasicoPresenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    rutaBasicoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                rutaBasicoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_ESTADO_RUTA + idCliente + "/" + nivel + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
