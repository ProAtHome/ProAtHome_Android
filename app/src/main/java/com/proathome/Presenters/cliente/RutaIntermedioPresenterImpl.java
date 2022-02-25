package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.RutaIntermedioInteractorImpl;
import com.proathome.Interfaces.cliente.RutaIntermedio.RutaIntermedioInteractor;
import com.proathome.Interfaces.cliente.RutaIntermedio.RutaIntermedioPresenter;
import com.proathome.Interfaces.cliente.RutaIntermedio.RutaIntermedioView;

public class RutaIntermedioPresenterImpl implements RutaIntermedioPresenter {

    private RutaIntermedioView rutaIntermedioView;
    private RutaIntermedioInteractor rutaIntermedioInteractor;

    public RutaIntermedioPresenterImpl(RutaIntermedioView rutaIntermedioView){
        this.rutaIntermedioView = rutaIntermedioView;
        rutaIntermedioInteractor = new RutaIntermedioInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(rutaIntermedioView != null)
            rutaIntermedioView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(rutaIntermedioView != null)
            rutaIntermedioView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(rutaIntermedioView != null)
            rutaIntermedioView.showError(error);
    }

    @Override
    public void getEstadoRuta(int idCliente, int nivel, String token) {
        rutaIntermedioInteractor.getEstadoRuta(idCliente, nivel, token);
    }

    @Override
    public void evaluarNivel(int idSeccion, int idNivel, int idBloque) {
        if(rutaIntermedioView != null)
            rutaIntermedioView.evaluarNivel(idSeccion, idNivel, idBloque);
    }

}
