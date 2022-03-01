package com.proathome.Interactors.cliente.examen;

import com.proathome.Interfaces.cliente.Examen.Diagnostico1.Diagnostico1Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico1.Diagnostico1Presenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class Diagnostico1InteractorImpl implements Diagnostico1Interactor {

    private Diagnostico1Presenter diagnostico1Presenter;

    public Diagnostico1InteractorImpl(Diagnostico1Presenter diagnostico1Presenter){
        this.diagnostico1Presenter = diagnostico1Presenter;
    }

    @Override
    public void inicioExamen(int idCliente, int puntacionAsumar, int preguntaActual) {
        JSONObject iniciar = new JSONObject();

        try {
            iniciar.put("estatus", Constants.INICIO_EXAMEN);
            iniciar.put("idCliente", idCliente);
            iniciar.put("aciertos", puntacionAsumar);
            iniciar.put("preguntaActual", preguntaActual);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        int estatusBD = jsonObject.getInt("estatus");
                        if(estatusBD == Constants.EXAMEN_GUARDADO)
                            diagnostico1Presenter.examenGuardado();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        diagnostico1Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    diagnostico1Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.INICIO_O_CANCELAR_EXAMEN_DIAGNOSTICO, WebServicesAPI.POST, iniciar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            diagnostico1Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

}
