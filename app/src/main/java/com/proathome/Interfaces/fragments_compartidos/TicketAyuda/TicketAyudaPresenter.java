package com.proathome.Interfaces.fragments_compartidos.TicketAyuda;

import org.json.JSONArray;

public interface TicketAyudaPresenter {

    void obtenerMsgTicket(int tipoUsuario, int idUsuario, int idTicket);

    void enviarMensaje(String mensaje, int idUsuario, int tipoUsuario, boolean operador, int idTicket);

    void ticketSolucionado(int tipoUsuario, int idUsuario, int idTicket);

    void nuevosMensajes(int tipoUsuario, int idUsuario, int idTicket);

    void cancelTime();

    void showProgress();

    void hideProgress();

    void ticketFinalizado();

    void resetAdapter();

    void setAdapterMsg(JSONArray jsonArray);

}
