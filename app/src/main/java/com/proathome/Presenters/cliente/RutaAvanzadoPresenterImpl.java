package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.RutaAvanzadoInteractorImpl;
import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoInteractor;
import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoPresenter;
import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoView;

public class RutaAvanzadoPresenterImpl implements RutaAvanzadoPresenter {

    private RutaAvanzadoView rutaAvanzadoView;
    private RutaAvanzadoInteractor rutaAvanzadoInteractor;

    public RutaAvanzadoPresenterImpl(RutaAvanzadoView rutaAvanzadoView){
        this.rutaAvanzadoView = rutaAvanzadoView;
        rutaAvanzadoInteractor = new RutaAvanzadoInteractorImpl(this);
    }

    @Override
    public void getEstadoRuta(int idCliente, int nivel, String token) {
        rutaAvanzadoInteractor.getEstadoRuta(idCliente, nivel, token);
    }

    @Override
    public void showProgress() {
        if(rutaAvanzadoView != null)
            rutaAvanzadoView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(rutaAvanzadoView != null)
            rutaAvanzadoView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(rutaAvanzadoView != null)
            rutaAvanzadoView.showError(error);
    }

    @Override
    public void evaluarNivel(int idSeccion, int idNivel, int idBloque) {
        if(rutaAvanzadoView != null)
            rutaAvanzadoView.evaluarNivel(idSeccion, idNivel, idBloque);
    }

}
