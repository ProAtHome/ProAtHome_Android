package com.proathome.Interactors.cliente.examen;

import com.proathome.Interfaces.cliente.Examen.Diagnostico5.Diagnostico5Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico5.Diagnostico5Presenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class Diagnostico5InteractorImpl implements Diagnostico5Interactor {

    private Diagnostico5Presenter diagnostico5Presenter;

    public Diagnostico5InteractorImpl(Diagnostico5Presenter diagnostico5Presenter){
        this.diagnostico5Presenter = diagnostico5Presenter;
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
                            diagnostico5Presenter.examenGuardado();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        diagnostico5Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    diagnostico5Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.EN_CURSO_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, actualizar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            diagnostico5Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

}
