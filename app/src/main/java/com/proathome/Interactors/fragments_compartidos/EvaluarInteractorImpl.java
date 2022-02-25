package com.proathome.Interactors.fragments_compartidos;

import android.os.Bundle;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarInteractor;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.pojos.Component;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.fragments_compartidos.EvaluarFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class EvaluarInteractorImpl implements EvaluarInteractor {

    private EvaluarPresenter evaluarPresenter;

    public EvaluarInteractorImpl(EvaluarPresenter evaluarPresenter){
        this.evaluarPresenter = evaluarPresenter;
    }

    @Override
    public void enviarEvaluacion(String comentario, float evaluacion, int procedencia, String token) {
        if(procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE){
            valorar(procedencia, comentario, evaluacion);
            //Esta peticion es por que bloquearemos el perfil después de evaluar.
            WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
                try{
                    if(response != null){
                        JSONObject data = new JSONObject(response);
                        if(data.getBoolean("respuesta")){
                            JSONObject jsonObject = data.getJSONObject("mensaje");
                            Bundle bundle = new Bundle();
                            if(jsonObject.getBoolean("bloquear")){
                                bundle.putDouble("deuda", jsonObject.getDouble("deuda"));
                                bundle.putString("sesion", Component.getSeccion(jsonObject.getInt("idSeccion")) +
                                        " / " + Component.getNivel(jsonObject.getInt("idSeccion"),
                                        jsonObject.getInt("idNivel")) + " / " + jsonObject.getInt("idBloque"));
                                bundle.putString("lugar", jsonObject.getString("lugar"));
                                bundle.putString("nombre", jsonObject.getString("nombre"));
                                bundle.putString("correo", jsonObject.getString("correo"));
                                bundle.putInt("idSesion", jsonObject.getInt("idSesion"));
                                evaluarPresenter.toPagoPendiente(bundle);
                            }
                        }else
                            evaluarPresenter.showError("Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.");
                    }else
                        evaluarPresenter.showError("Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.");
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.BLOQUEAR_PERFIL + "/" + DetallesFragment.idCliente + "/" + token, WebServicesAPI.GET, null);
            bloquearPerfil.execute();
        }else if(procedencia == EvaluarFragment.PROCEDENCIA_PROFESIONAL)
            valorar(procedencia, comentario, evaluacion);
    }

    private void valorar(int procedencia, String comentario, float evaluacion){
        JSONObject jsonDatos = new JSONObject();
        try{
            if(procedencia == EvaluarFragment.PROCEDENCIA_PROFESIONAL){
                jsonDatos.put("idCliente", DetallesSesionProfesionalFragment.idCliente);
                jsonDatos.put("idProfesional", DetallesSesionProfesionalFragment.idProfesional);
                jsonDatos.put("idSesion", DetallesSesionProfesionalFragment.idSesion);
            }else if(procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE){
                jsonDatos.put("idCliente",DetallesFragment.idCliente);
                jsonDatos.put("idProfesional", DetallesFragment.idProfesional);
                jsonDatos.put("idSesion", DetallesFragment.idSesion);
            }
            jsonDatos.put("valoracion", evaluacion);
            jsonDatos.put("comentario", comentario);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                evaluarPresenter.cerrarFragment();
            }, procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE ? APIEndPoints.VALORAR_PROFESIONAL : APIEndPoints.VALORAR_CLIENTE, WebServicesAPI.POST, jsonDatos);
            webServicesAPI.execute();
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

}
