package com.proathome.Interactors.profesional;

import android.util.Log;

import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionInteractor;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchSesionInteractorImpl implements MatchSesionInteractor {

    private MatchSesionPresenter matchSesionPresenter;

    public MatchSesionInteractorImpl(MatchSesionPresenter matchSesionPresenter){
        this.matchSesionPresenter = matchSesionPresenter;
    }

    @Override
    public void getInfoSesion(int idSesion) {
        matchSesionPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response == null){
                matchSesionPresenter.showError("Error, ocurrió un problema con el servidor.");
            }else {
                if(!response.equals("null")){
                    JSONObject jsonObject = new JSONObject(response);
                    matchSesionPresenter.setInfoSesion(jsonObject);
                }else
                    matchSesionPresenter.showError("Sin datos, ocurrió un error.");
            }
            matchSesionPresenter.hideProgress();
        }, APIEndPoints.INFO_SESION_MATCH + idSesion, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void matchSesion(int idProfesional, int idSesion, JSONObject serviciosDisponibles,
                            JSONObject serviciosPendientes, String fechaActual, String fechaServicio,
                                String horario, int rango) {
        //VALIDAR SI LA FECHA DEL SERVICIO TIENE 3 PENDIENTES
        try {
            JSONArray serviciosFecha = serviciosPendientes.getJSONArray(fechaServicio);
            Log.d("TAGL", serviciosFecha.toString());
            Log.d("TAGL" , "nUM " + serviciosFecha.length());
            if(serviciosFecha.length() == 3){
                //AGENDA LLENA
                matchSesionPresenter.showError("AGENDA LLENA EN FECHA: " + fechaServicio);
            }else if(serviciosFecha.length() == 0){
                //LIBRE DE AGENDAR
                agendar(idProfesional, idSesion, rango);
            }else{
                //RECORREMOS Y VALIDAMOS HORARIOS
                int horarioServicioElegido = getHorarioNumber(horario);
                boolean disponible = true;
                //VALIDAR SI LOS HORARIOS DE MIS SERVICIOS AGENDADOS TIENEN UYNA DIFERENCIA DE 3 HRS
                JSONArray serviciosAgendadosFecha = serviciosPendientes.getJSONArray(fechaServicio);
                for (int i = 0; i < serviciosAgendadosFecha.length(); i++) {
                    JSONObject servicio = serviciosAgendadosFecha.getJSONObject(i);
                    int horarioServicioAgendado = getHorarioNumber(servicio.getString("horario"));
                    int diferencia = horarioServicioAgendado - horarioServicioElegido;
                    diferencia = Math.abs(diferencia);
                    if(!(diferencia >= 3))
                        disponible = false;
                }

                if(disponible)
                    agendar(idProfesional, idSesion, rango);
                else
                    matchSesionPresenter.showError("DEBES TENER UNA DIFERENCIA MINIMA DE 3 HRS ENTRE SERVICIOS EN LA FECHA ELEGIDA.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void agendar(int idProfesional, int idSesion, int rango){
        matchSesionPresenter.showProgress();
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", idProfesional);
            parametrosPUT.put("idSesion", idSesion);
            parametrosPUT.put("rango", rango);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                matchSesionPresenter.hideProgress();
                if(response != null){
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        //NOTIFICAMOS A CLIENTE
                        matchSesionPresenter.successMatch(data.getString("mensaje"));
                    }else
                        matchSesionPresenter.showError(data.getString("mensaje"));
                }else
                    matchSesionPresenter.showError("Ocurrió un error inesperado");
            }, APIEndPoints.MATCH_SESION, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notificarCliente(JSONObject jsonObject) {
        matchSesionPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            matchSesionPresenter.hideProgress();
            matchSesionPresenter.closeActivity();
        }, APIEndPoints.NOTIFICACION_CLIENTE, WebServicesAPI.POST, jsonObject);
        webServicesAPI.execute();
    }

    public int getHorarioNumber(String horario){
        int hora = 0;

        if(horario.equals("00:00 HRS"))
            hora = 0;
        else if(horario.equals("01:00 HRS"))
            hora = 1;
        else if(horario.equals("02:00 HRS"))
            hora = 2;
        else if(horario.equals("03:00 HRS"))
            hora = 3;
        else if(horario.equals("04:00 HRS"))
            hora = 4;
        else if(horario.equals("05:00 HRS"))
            hora = 5;
        else if(horario.equals("06:00 HRS"))
            hora = 6;
        else if(horario.equals("07:00 HRS"))
            hora = 7;
        else if(horario.equals("08:00 HRS"))
            hora = 8;
        else if(horario.equals("09:00 HRS"))
            hora = 9;
        else if(horario.equals("10:00 HRS"))
            hora = 10;
        else if(horario.equals("11:00 HRS"))
            hora = 11;
        else if(horario.equals("12:00 HRS"))
            hora = 12;
        else if(horario.equals("13:00 HRS"))
            hora = 13;
        else if(horario.equals("14:00 HRS"))
            hora = 14;
        else if(horario.equals("15:00 HRS"))
            hora = 15;
        else if(horario.equals("16:00 HRS"))
            hora = 16;
        else if(horario.equals("17:00 HRS"))
            hora = 17;
        else if(horario.equals("18:00 HRS"))
            hora = 18;
        else if(horario.equals("19:00 HRS"))
            hora = 19;
        else if(horario.equals("20:00 HRS"))
            hora = 20;
        else if(horario.equals("21:00 HRS"))
            hora = 21;
        else if(horario.equals("22:00 HRS"))
            hora = 22;
        else if(horario.equals("23:00 HRS"))
            hora = 23;

        return hora;
    }

    @Override
    public void setImage(String foto) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            if(response != null)
                matchSesionPresenter.setImageBitmap(response);
            else
                matchSesionPresenter.showError("No pudimos cargar la foto de perfil.");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

}
