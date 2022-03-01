package com.proathome.Interactors.cliente.examen;

import android.content.Context;
import com.proathome.Interfaces.cliente.Examen.Diagnostico7.Diagnostico7Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico7.Diagnostico7Presenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.cliente.examen.EvaluarRuta;
import com.proathome.Views.cliente.fragments.FragmentRutaGenerada;
import org.json.JSONException;
import org.json.JSONObject;

public class Diagnostico7InteractorImpl implements Diagnostico7Interactor {

    private Diagnostico7Presenter diagnostico7Presenter;

    public Diagnostico7InteractorImpl(Diagnostico7Presenter diagnostico7Presenter){
        this.diagnostico7Presenter = diagnostico7Presenter;
    }

    @Override
    public void getInfoExamenFinal(int idCliente, int puntacionAsumar, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        int estatus = jsonObject.getInt("estatus");
                        if(estatus == Constants.INFO_EXAMEN_FINAL){
                            int aciertos = jsonObject.getInt("aciertos");
                            int puntuacionTotal = puntacionAsumar + aciertos;
                            EvaluarRuta evaluarRuta = new EvaluarRuta(puntuacionTotal);
                            FragmentRutaGenerada.ruta.setText(evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                            FragmentRutaGenerada.nivel.setText("Nivel: " + evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                            FragmentRutaGenerada.aciertos = puntuacionTotal;
                        }
                    }else
                        diagnostico7Presenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    diagnostico7Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                diagnostico7Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_INFO_EXAMEN_FINAL + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
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
                            diagnostico7Presenter.examenGuardado();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        diagnostico7Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                    }
                }else
                    diagnostico7Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.EN_CURSO_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, actualizar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            diagnostico7Presenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

}
