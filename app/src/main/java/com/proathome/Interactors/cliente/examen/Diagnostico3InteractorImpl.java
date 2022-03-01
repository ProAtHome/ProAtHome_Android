package com.proathome.Interactors.cliente.examen;

import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3Presenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class Diagnostico3InteractorImpl implements Diagnostico3Interactor {

    private Diagnostico3Presenter diagnostico3Presenter;

    public Diagnostico3InteractorImpl(Diagnostico3Presenter diagnostico3Presenter){
        this.diagnostico3Presenter = diagnostico3Presenter;
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
                            diagnostico3Presenter.examenGuardado();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        diagnostico3Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    diagnostico3Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.EN_CURSO_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, actualizar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            diagnostico3Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

}
