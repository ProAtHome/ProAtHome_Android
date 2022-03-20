package com.proathome.Interactors.fragments_compartidos;

import android.os.Handler;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaInteractor;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class TicketAyudaInteractorImpl implements TicketAyudaInteractor {

    private TicketAyudaPresenter ticketAyudaPresenter;
    public Timer timer;

    public TicketAyudaInteractorImpl(TicketAyudaPresenter ticketAyudaPresenter){
        this.ticketAyudaPresenter = ticketAyudaPresenter;
    }

    @Override
    public void obtenerMsgTicket(int tipoUsuario, int idUsuario, int idTicket) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject mensajes = jsonArray.getJSONObject(1);
                    JSONArray jsonArrayMensajes = mensajes.getJSONArray("mensajes");

                    ticketAyudaPresenter.setAdapterMsg(jsonArrayMensajes);

                    jsonArray = null;
                    mensajes = null;
                    jsonArrayMensajes =  null;
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
        }, validacionURL_API(tipoUsuario, idUsuario, idTicket), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void enviarMensaje(String mensaje, int idUsuario, int tipoUsuario, boolean operador, int idTicket) {
        JSONObject jsonDatos = new JSONObject();
        try {
            jsonDatos.put("mensaje", mensaje);
            jsonDatos.put("idUsuario", idUsuario);
            jsonDatos.put("operador", operador);
            jsonDatos.put("idTicket", idTicket);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                obtenerMsgTicket(tipoUsuario, idUsuario, idTicket);
                ticketAyudaPresenter.resetAdapter();
            }, tipoUsuario == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.ENVIAR_MSG_TICKET_CLIENTE : APIEndPoints.ENVIAR_MSG_TICKET_PROFESIONAL, WebServicesAPI.POST, jsonDatos);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ticketSolucionado(int tipoUsuario, int idUsuario, int idTicket) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            ticketAyudaPresenter.ticketFinalizado();
        }, tipoUsuario == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.FINALIZAR_TICKET_CLIENTE + idTicket : APIEndPoints.FINALIZAR_TICKET_PROFESIONAL + idTicket, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void nuevosMensajes(int tipoUsuario, int idUsuario, int idTicket) {
        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    obtenerMsgTicket(tipoUsuario, idUsuario, idTicket);
                });
            }
        };

        timer.schedule(task, 0, 3000);
    }

    @Override
    public void cancelTime() {
        timer.cancel();
    }

    private String validacionURL_API(int tipoUsuario, int idUsuario, int idTicket){
        String url = null;
        if(tipoUsuario == Constants.TIPO_USUARIO_CLIENTE)
            url = APIEndPoints.GET_MSG_TICKET + idUsuario + "/" + Constants.TIPO_USUARIO_CLIENTE + "/" + idTicket;
        else if(tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
            url = APIEndPoints.GET_MSG_TICKET + idUsuario + "/" + Constants.TIPO_USUARIO_PROFESIONAL + "/" + idTicket;

        return url;
    }

}
