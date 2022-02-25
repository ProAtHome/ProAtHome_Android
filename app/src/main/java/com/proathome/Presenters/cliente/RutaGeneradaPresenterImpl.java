package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.RutaGeneradaInteractorImpl;
import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaInteractor;
import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaPresenter;
import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaView;
import com.proathome.Views.cliente.examen.EvaluarRuta;

public class RutaGeneradaPresenterImpl implements RutaGeneradaPresenter {

    private RutaGeneradaView rutaGeneradaView;
    private RutaGeneradaInteractor rutaGeneradaInteractor;

    public RutaGeneradaPresenterImpl(RutaGeneradaView rutaGeneradaView){
        this.rutaGeneradaView = rutaGeneradaView;
        rutaGeneradaInteractor = new RutaGeneradaInteractorImpl(this);
    }

    @Override
    public void rutaExamen(EvaluarRuta evaluarRuta, int idCliente) {
        rutaGeneradaInteractor.rutaExamen(evaluarRuta, idCliente);
    }

    @Override
    public void showProgress() {
        if(rutaGeneradaView != null)
            rutaGeneradaView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(rutaGeneradaView != null)
            rutaGeneradaView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(rutaGeneradaView != null)
            rutaGeneradaView.showError(error);
    }

    @Override
    public void cerrarFragment() {
        if(rutaGeneradaView != null)
            rutaGeneradaView.cerrarFragment();
    }

}
