package com.proathome.Interfaces.fragments_compartidos.NuevoTicket;

import org.json.JSONObject;

public interface NuevoTicketPresenter {

    void nuevoTicket(JSONObject jsonObject, int idUsuario);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void successTicket(String mensaje);

    void obtenerTickets(int tipoUsuario, int idUsuario, String token);

}
