package com.proathome.Interactors.activitys_compartidos;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioInteractor;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.activitys_compartidos.SincronizarServicio;
import com.proathome.Views.cliente.ServicioCliente;
import com.proathome.Views.profesional.ServicioProfesional;
import org.json.JSONException;
import org.json.JSONObject;

public class SincronizarServicioInteractorImpl implements SincronizarServicioInteractor {

    private SincronizarServicioPresenter sincronizarServicioPresenter;

    public SincronizarServicioInteractorImpl(SincronizarServicioPresenter sincronizarServicioPresenter){
        this.sincronizarServicioPresenter = sincronizarServicioPresenter;
    }

    @Override
    public void verificarDisponibilidadProfesional(int idSesion, int idPerfil, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject resultado = data.getJSONObject("mensaje");
                        boolean disponibilidad = resultado.getBoolean("dispProfesional");
                        if(disponibilidad) {
                            sincronizarServicioPresenter.cancelTime();
                            getProgresoCliente(idSesion, idPerfil, context);
                        }
                    }else
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, APIEndPoints.VERIFICAR_DISPONIBILIDAD_DE_PROFESIONAL + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void verificarDisponibilidadCliente(int idSesion, int idPerfil, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject resultado = data.getJSONObject("mensaje");
                        boolean disponibilidad = resultado.getBoolean("dispCliente");
                        if(disponibilidad) {
                            sincronizarServicioPresenter.cancelTime();
                            getProgresoProfesional(idSesion, idPerfil, context);
                        }
                    }else
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, APIEndPoints.VERIFICAR_DISPONIBILIDAD_DE_CLIENTE + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_CLIENTE + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    @Override
    public void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_PROFESIONAL + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    public static void getProgresoCliente(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                    /*Aquí vamos a obtener el progreso inicial de el servicio dependiendo el perfil PROFESIONAL o CLIENTE para iniciar
                        las variables correspondientes antes de entrar en la Actividad de el Servicio.*/
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        Intent intent = new Intent(context, ServicioCliente.class);
                        if(jsonObject.getBoolean("TE"))
                            ServicioCliente.mTimeLeftMillis = (jsonObject.getInt("progresoTE") * 60 * 1000) + (jsonObject.getInt("progresoSegundosTE") * 1000);
                        else
                            ServicioCliente.mTimeLeftMillis = (jsonObject.getInt("progreso") * 60 * 1000) + (jsonObject.getInt("progresoSegundos") * 1000);

                        intent.putExtra("idSesion", idSesion);
                        intent.putExtra("idCliente", idPerfil);
                        intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                        intent.putExtra("idNivel", SincronizarServicio.idNivel);
                        intent.putExtra("idBloque", SincronizarServicio.idBloque);
                        intent.putExtra("sumar", SincronizarServicio.sumar);
                        intent.putExtra("tiempo", SincronizarServicio.tiempo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        jsonObject = null;
                    }else
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, APIEndPoints.GET_PROGRESO_CLIENTE + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void getProgresoProfesional(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                    /*Aquí vamos a obtener el progreso inicial de el servicio dependiendo el perfil PROFESIONAL o CLIENTE para iniciar
                            las variables correspondientes antes de entrar en la Actividad de el Servicio.*/
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        Intent intent = new Intent(context, ServicioProfesional.class);
                        if(jsonObject.getBoolean("TE"))
                            ServicioProfesional.mTimeLeftMillis = (jsonObject.getInt("progresoTE") * 60 * 1000) + (jsonObject.getInt("progresoSegundosTE") * 1000);
                        else
                            ServicioProfesional.mTimeLeftMillis = (jsonObject.getInt("progreso") * 60 * 1000) + (jsonObject.getInt("progresoSegundos") * 1000);

                        intent.putExtra("idSesion", idSesion);
                        intent.putExtra("idProfesional", idPerfil);
                        intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                        intent.putExtra("idNivel", SincronizarServicio.idNivel);
                        intent.putExtra("idBloque", SincronizarServicio.idBloque);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        jsonObject = null;
                    }else
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, APIEndPoints.GET_PROGRESO_PROFESIONAL + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}
