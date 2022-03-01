package com.proathome.Interfaces.fragments_compartidos.NuevoTicket;

import org.json.JSONObject;

public interface NuevoTicketInteractor {

    void nuevoTicket(JSONObject jsonObject, int tipoUsuario);

    void obtenerTickets(int tipoUsuario, int idUsuario, String token);

}
