package com.proathome.Servicios.cliente;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Views.cliente.examen.Diagnostico1;
import com.proathome.Views.cliente.examen.Diagnostico2;
import com.proathome.Views.cliente.examen.Diagnostico3;
import com.proathome.Views.cliente.examen.Diagnostico4;
import com.proathome.Views.cliente.examen.Diagnostico5;
import com.proathome.Views.cliente.examen.Diagnostico6;
import com.proathome.Views.cliente.examen.Diagnostico7;
import com.proathome.Views.cliente.examen.EvaluarRuta;
import com.proathome.Views.cliente.fragments.FragmentRutaGenerada;
import com.proathome.Views.cliente.navigator.ruta.RutaFragment;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiciosExamenDiagnostico {

    public static void inicioExamen(int idCliente, int puntacionAsumar, int preguntaActual, Context context, Activity activity, Class activitySiguiente){
        JSONObject iniciar = new JSONObject();

        try {
            iniciar.put("estatus", Constants.INICIO_EXAMEN);
            iniciar.put("idCliente", idCliente);
            iniciar.put("aciertos", puntacionAsumar);
            iniciar.put("preguntaActual", preguntaActual);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int estatusBD = jsonObject.getInt("estatus");
                    if(estatusBD == Constants.EXAMEN_GUARDADO){
                        SweetAlert.showMsg(context, SweetAlert.NORMAL_TYPE, "Puntuación guardada.", "¡Continúa!", true, "OK", ()->{
                            if(!Diagnostico7.ultimaPagina){
                                activity.startActivityForResult(new Intent(context, activitySiguiente), 1, ActivityOptions.makeSceneTransitionAnimation(activity)
                                        .toBundle());
                                activity.finish();
                            }else{
                                Diagnostico7.ultimaPagina = false;
                            }
                        });
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.INICIO_O_CANCELAR_EXAMEN_DIAGNOSTICO, WebServicesAPI.POST, iniciar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual, Context context, Activity activity, Class activitySiguiente){
        JSONObject actualizar = new JSONObject();

        try {
            actualizar.put("estatus", estatus);
            actualizar.put("idCliente", idCliente);
            actualizar.put("aciertos", puntacionAsumar);
            actualizar.put("preguntaActual", preguntaActual);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int estatusBD = jsonObject.getInt("estatus");
                    if(estatusBD == Constants.EXAMEN_GUARDADO){
                        SweetAlert.showMsg(context, SweetAlert.NORMAL_TYPE, "Puntuación guardada.", "¡Continúa!", true, "OK", ()->{
                            if(!Diagnostico7.ultimaPagina){
                                activity.startActivityForResult(new Intent(context, activitySiguiente), 1, ActivityOptions.makeSceneTransitionAnimation(activity)
                                        .toBundle());
                                activity.finish();
                            }else{
                                Diagnostico7.ultimaPagina = false;
                            }
                        });
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.EN_CURSO_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, actualizar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getInfoExamenFinal(int idCliente, int puntacionAsumar, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
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
                    Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_INFO_EXAMEN_FINAL + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
