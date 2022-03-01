package com.proathome.Presenters.fragments_compartidos;

import com.proathome.Interactors.fragments_compartidos.NuevoTicketInteractorImpl;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketInteractor;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketPresenter;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketView;
import org.json.JSONObject;

public class NuevoTicketPresenterImpl implements NuevoTicketPresenter {

    private NuevoTicketView nuevoTicketView;
    private NuevoTicketInteractor nuevoTicketInteractor;

    public NuevoTicketPresenterImpl(NuevoTicketView nuevoTicketView){
        this.nuevoTicketView = nuevoTicketView;
        nuevoTicketInteractor = new NuevoTicketInteractorImpl(this);
    }

    @Override
    public void nuevoTicket(JSONObject jsonObject, int idUsuario) {
        nuevoTicketInteractor.nuevoTicket(jsonObject, idUsuario);
    }

    @Override
    public void showProgress() {
        if(nuevoTicketView != null)
            nuevoTicketView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(nuevoTicketView != null)
            nuevoTicketView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(nuevoTicketView != null)
            nuevoTicketView.showError(error);
    }

    @Override
    public void successTicket(String mensaje) {
        if(nuevoTicketView != null)
            nuevoTicketView.successTicket(mensaje);
    }

    @Override
    public void obtenerTickets(int tipoUsuario, int idUsuario, String token) {
        nuevoTicketInteractor.obtenerTickets(tipoUsuario, idUsuario, token);
    }

}
