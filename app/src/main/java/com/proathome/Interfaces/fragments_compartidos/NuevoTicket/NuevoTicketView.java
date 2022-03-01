package com.proathome.Interfaces.fragments_compartidos.NuevoTicket;

public interface NuevoTicketView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void successTicket(String mensaje);

}
