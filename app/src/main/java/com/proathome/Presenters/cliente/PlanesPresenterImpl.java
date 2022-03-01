package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.PlanesInteractorImpl;
import com.proathome.Interfaces.cliente.Planes.PlanesInteractor;
import com.proathome.Interfaces.cliente.Planes.PlanesPresenter;
import com.proathome.Interfaces.cliente.Planes.PlanesView;

public class PlanesPresenterImpl implements PlanesPresenter {

    private PlanesView planesView;
    private PlanesInteractor planesInteractor;

    public PlanesPresenterImpl(PlanesView planesView){
        this.planesView = planesView;
        planesInteractor = new PlanesInteractorImpl(this);
    }

    @Override
    public void getFechaServidor(int idCliente, String token) {
        planesInteractor.getFechaServidor(idCliente, token);
    }

    @Override
    public void setFechaServidor(String fecha) {
        if(planesView != null)
            planesView.setFechaServidor(fecha);
    }

    @Override
    public void setSesionActual(int idSeccion, int idNivel, int idBloque) {
        if(planesView != null)
            planesView.setSesionActual(idSeccion, idNivel, idBloque);
    }

    @Override
    public void showProgress() {
        if(planesView != null)
            planesView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(planesView != null)
            planesView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(planesView != null)
            planesView.showError(error);
    }

    @Override
    public void setDatosBancarios(String nombreTitular, String tarjeta, String mes, String ano) {
        if(planesView != null)
            planesView.setDatosBancarios(nombreTitular, tarjeta, mes, ano);
    }

}
