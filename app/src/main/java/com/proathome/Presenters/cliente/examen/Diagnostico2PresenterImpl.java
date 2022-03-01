package com.proathome.Presenters.cliente.examen;

import com.proathome.Interactors.cliente.examen.Diagnostico2InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico2.Diagnostico2Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico2.Diagnostico2Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico2.Diagnostico2View;

public class Diagnostico2PresenterImpl implements Diagnostico2Presenter {

    private Diagnostico2View diagnostico2View;
    private Diagnostico2Interactor diagnostico2Interactor;

    public Diagnostico2PresenterImpl(Diagnostico2View diagnostico2View){
        this.diagnostico2View = diagnostico2View;
        diagnostico2Interactor = new Diagnostico2InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico2View != null);
            diagnostico2View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico2View != null);
        diagnostico2View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico2View != null);
        diagnostico2View.showError(mensaje);
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico2Interactor.actualizarEstatusExamen(estatus, idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico2View != null)
            diagnostico2View.examenGuardado();
    }

}
