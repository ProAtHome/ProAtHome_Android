package com.proathome.Presenters.fragments_compartidos;

import com.proathome.Interactors.fragments_compartidos.TicketAyudaInteractorImpl;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaInteractor;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaPresenter;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaView;
import org.json.JSONArray;

public class TicketAyudaPresenterImpl implements TicketAyudaPresenter {

    private TicketAyudaView ticketAyudaView;
    private TicketAyudaInteractor ticketAyudaInteractor;

    public TicketAyudaPresenterImpl(TicketAyudaView ticketAyudaView){
        this.ticketAyudaView = ticketAyudaView;
        ticketAyudaInteractor = new TicketAyudaInteractorImpl(this);
    }

    @Override
    public void obtenerMsgTicket(int tipoUsuario, int idUsuario, int idTicket) {
        ticketAyudaInteractor.obtenerMsgTicket(tipoUsuario, idUsuario, idTicket);
    }

    @Override
    public void enviarMensaje(String mensaje, int idUsuario, int tipoUsuario, boolean operador, int idTicket) {
        ticketAyudaInteractor.enviarMensaje(mensaje, idUsuario, tipoUsuario, operador, idTicket);
    }

    @Override
    public void ticketSolucionado(int tipoUsuario, int idUsuario, int idTicket) {
        ticketAyudaInteractor.ticketSolucionado(tipoUsuario, idUsuario, idTicket);
    }

    @Override
    public void nuevosMensajes(int tipoUsuario, int idUsuario, int idTicket) {
        ticketAyudaInteractor.nuevosMensajes(tipoUsuario, idUsuario, idTicket);
    }

    @Override
    public void cancelTime() {
        ticketAyudaInteractor.cancelTime();
    }

    @Override
    public void showProgress() {
        if(ticketAyudaView != null)
            ticketAyudaView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(ticketAyudaView != null)
            ticketAyudaView.hideProgress();
    }

    @Override
    public void ticketFinalizado() {
        if(ticketAyudaView != null)
            ticketAyudaView.ticketFinalizado();
    }

    @Override
    public void resetAdapter() {
        if(ticketAyudaView != null)
            ticketAyudaView.resetAdapter();
    }

    @Override
    public void setAdapterMsg(JSONArray jsonArray) {
        if(ticketAyudaView != null)
            ticketAyudaView.setAdapterMsg(jsonArray);
    }

}
