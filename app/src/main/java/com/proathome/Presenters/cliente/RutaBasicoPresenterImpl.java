package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.RutaBasicoInteractorImpl;
import com.proathome.Interfaces.cliente.RutaBasico.RutaBasicoInteractor;
import com.proathome.Interfaces.cliente.RutaBasico.RutaBasicoPresenter;
import com.proathome.Interfaces.cliente.RutaBasico.RutaBasicoView;

public class RutaBasicoPresenterImpl implements RutaBasicoPresenter {

    private RutaBasicoView rutaBasicoView;
    private RutaBasicoInteractor rutaBasicoInteractor;

    public RutaBasicoPresenterImpl(RutaBasicoView rutaBasicoView){
        this.rutaBasicoView = rutaBasicoView;
        rutaBasicoInteractor = new RutaBasicoInteractorImpl(this);
    }

    @Override
    public void getEstadoRuta(int idCliente, int nivel, String token) {
        rutaBasicoInteractor.getEstadoRuta(idCliente, nivel, token);
    }

    @Override
    public void showError(String error) {
        if(rutaBasicoView != null)
            rutaBasicoView.showError(error);
    }

    @Override
    public void showProgress() {
        if(rutaBasicoView != null)
            rutaBasicoView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(rutaBasicoView != null)
            rutaBasicoView.hideProgress();
    }

    @Override
    public void evaluarNivelBasico(int idSeccion, int idNivel, int idBloque) {
        if(rutaBasicoView != null)
            rutaBasicoView.evaluarNivelBasico(idSeccion, idNivel, idBloque);
    }

}
