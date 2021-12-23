package com.proathome.servicios.cliente;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.examen.Diagnostico1;
import com.proathome.ui.examen.Diagnostico2;
import com.proathome.ui.examen.Diagnostico3;
import com.proathome.ui.examen.Diagnostico4;
import com.proathome.ui.examen.Diagnostico5;
import com.proathome.ui.examen.Diagnostico6;
import com.proathome.ui.examen.Diagnostico7;
import com.proathome.ui.examen.EvaluarRuta;
import com.proathome.ui.fragments.FragmentRutaGenerada;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiciosExamenDiagnostico {

    public static void cancelarExamen(int idCliente){
        JSONObject cancelar = new JSONObject();
        try {
            cancelar.put("idCliente", idCliente);
            cancelar.put("aciertos", 0);
            cancelar.put("preguntaActual", 0);
            cancelar.put("estatus", Constants.CANCELADO_EXAMEN);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int estatusBD = jsonObject.getInt("estatus");
                    if(estatusBD == Constants.CANCELADO_EXAMEN)
                        RutaFragment.imgExamen.setVisibility(View.VISIBLE);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.INICIO_O_CANCELAR_EXAMEN_DIAGNOSTICO, WebServicesAPI.POST, cancelar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                        new SweetAlert(context, SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                                .setTitleText("Puntuación guardada.")
                                .setConfirmButton("Continuar", sweetAlertDialog -> {
                                    sweetAlertDialog.dismissWithAnimation();
                                    if(!Diagnostico7.ultimaPagina){
                                        activity.startActivityForResult(new Intent(context, activitySiguiente), 1, ActivityOptions.makeSceneTransitionAnimation(activity)
                                                .toBundle());
                                        activity.finish();
                                    }else{
                                        Diagnostico7.ultimaPagina = false;
                                    }
                                })
                                .show();
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
                        new SweetAlert(context, SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                                .setTitleText("Puntuación guardada.")
                                .setConfirmButton("Continuar", sweetAlertDialog -> {
                                    sweetAlertDialog.dismissWithAnimation();
                                    if(!Diagnostico7.ultimaPagina){
                                        activity.startActivityForResult(new Intent(context, activitySiguiente), 1, ActivityOptions.makeSceneTransitionAnimation(activity)
                                                .toBundle());
                                        activity.finish();
                                    }else{
                                        Diagnostico7.ultimaPagina = false;
                                    }
                                })
                                .show();
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

    public static void getInfoExamenFinal(int idCliente, int puntacionAsumar){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                int estatus = jsonObject.getInt("estatus");
                if(estatus == Constants.INFO_EXAMEN_FINAL){
                    int aciertos = jsonObject.getInt("aciertos");
                    int puntuacionTotal = puntacionAsumar + aciertos;
                    EvaluarRuta evaluarRuta = new EvaluarRuta(puntuacionTotal);
                    FragmentRutaGenerada.ruta.setText(evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                    FragmentRutaGenerada.nivel.setText("Nivel: " + evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                    FragmentRutaGenerada.aciertos = puntuacionTotal;
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_INFO_EXAMEN_FINAL + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void getEstatusExamen(int idCliente, Context contexto){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                int estatus = jsonObject.getInt("estatus");
                if(estatus == Constants.INICIO_EXAMEN){
                    new MaterialAlertDialogBuilder(contexto, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                            .setTitle("EXÁMEN DIAGNÓSTICO")
                            .setMessage("Tenemos un exámen para evaluar tus habilidades y colocarte en la ruta de aprendizaje de" +
                                    " acuerdo a tus conocimientos, si no deseas realizar el exámen sigue tu camino desde un inicio.")
                            .setNegativeButton("Cerrar", (dialog, which) -> {
                                ServiciosExamenDiagnostico.cancelarExamen(idCliente);
                            })
                            .setPositiveButton("EVALUAR", ((dialog, which) -> {
                                contexto.startActivity(new Intent(contexto, Diagnostico1.class));
                            }))
                            .setOnCancelListener(dialog -> {

                            })
                            .show();
                }else if(estatus == Constants.ENCURSO_EXAMEN){
                    ServiciosExamenDiagnostico.continuarExamen(idCliente, contexto);
                }else if(estatus == Constants.CANCELADO_EXAMEN){
                    RutaFragment.imgExamen.setVisibility(View.VISIBLE);
                }else if(estatus == Constants.EXAMEN_FINALIZADO) {
                    new SweetAlert(contexto, SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                            .setTitleText("Ya tenemos tu diagnóstico.")
                            .show();
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_ESTATUS_EXAMEN_DIAGNOSTICO + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void continuarExamen(int idCliente, Context contexto){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int estatus = jsonObject.getInt("estatus");
                if(estatus == Constants.CONTINUAR_EXAMEN){
                    int pregunta = jsonObject.getInt("preguntaActual");
                    if(pregunta == 10)
                        contexto.startActivity(new Intent(contexto, Diagnostico2.class));
                    else if(pregunta == 20)
                        contexto.startActivity(new Intent(contexto, Diagnostico3.class));
                    else if(pregunta == 30)
                        contexto.startActivity(new Intent(contexto, Diagnostico4.class));
                    else if(pregunta == 40)
                        contexto.startActivity(new Intent(contexto, Diagnostico5.class));
                    else if(pregunta == 50)
                        contexto.startActivity(new Intent(contexto, Diagnostico6.class));
                    else if(pregunta == 60)
                        contexto.startActivity(new Intent(contexto, Diagnostico7.class));
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_INFO_EXAMEN_DIAGNOSTICO + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void reiniciarExamen(int idCliente, Context context){
        JSONObject reiniciar = new JSONObject();
        try {
            reiniciar.put("aciertos", 0);
            reiniciar.put("idCliente", idCliente);
            reiniciar.put("preguntaActual", 0);
            reiniciar.put("estatus", Constants.REINICIAR_EXAMEN);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int estatus = jsonObject.getInt("estatus");
                    if(estatus == Constants.REINICIAR_EXAMEN) {
                        new SweetAlert(context, SweetAlert.SUCCESS_TYPE, SweetAlert.CLIENTE)
                                .setTitleText("¡GENIAL!")
                                .setContentText("Examen reiniciado, suerte.")
                                .setConfirmButton("VAMOS", listener ->{
                                    RutaFragment.imgExamen.setVisibility(View.INVISIBLE);
                                    listener.dismissWithAnimation();
                                    context.startActivity(new Intent(context, Diagnostico1.class));
                                })
                                .show();
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.REINICIAR_EXAMEN_DIAGNOSTICO, WebServicesAPI.PUT, reiniciar);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
