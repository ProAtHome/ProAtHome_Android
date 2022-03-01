package com.proathome.Interactors.cliente.examen;

import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6Interactor;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class Diagnostico6InteractorImpl implements Diagnostico6Interactor {

    private Diagnostico6Presenter diagnostico6Presenter;

    public Diagnostico6InteractorImpl(Diagnostico6Presenter diagnostico6Presenter){
        this.diagnostico6Presenter = diagnostico6Presenter;
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        JSONObject actualizar = new JSONObject();
        try {
            actualizar.put("estatus", estatus);
            actualizar.put("idCliente", idCliente);
            actualizar.put("aciertos", puntacionAsumar);
            actualizar.put("preguntaActual", preguntaActual);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        int estatusBD = jsonObject.getInt("estatus");
                        if(estatusBD == Constants.EXAMEN_GUARDADO)
                            diagnostico6Presenter.examenGuardado();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        diagnostico6Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    diagnostico6Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.EN_CURSO_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, actualizar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            diagnostico6Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

}
