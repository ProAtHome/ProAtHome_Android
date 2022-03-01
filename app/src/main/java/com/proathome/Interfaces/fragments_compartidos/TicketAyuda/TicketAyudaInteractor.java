package com.proathome.Interfaces.fragments_compartidos.TicketAyuda;

public interface TicketAyudaInteractor {

    void obtenerMsgTicket(int tipoUsuario, int idUsuario, int idTicket);

    void enviarMensaje(String mensaje, int idUsuario, int tipoUsuario, boolean operador, int idTicket);

    void ticketSolucionado(int tipoUsuario, int idUsuario, int idTicket);

    void nuevosMensajes(int tipoUsuario, int idUsuario, int idTicket);

    void cancelTime();

}
