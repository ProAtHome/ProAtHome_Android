package com.proathome.Presenters.cliente.examen;

import com.proathome.Interactors.cliente.examen.Diagnostico5InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico5.Diagnostico5Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico5.Diagnostico5Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico5.Diagnostico5View;

public class Diagnostico5PresenterImpl implements Diagnostico5Presenter {

    private Diagnostico5View diagnostico5View;
    private Diagnostico5Interactor diagnostico5Interactor;

    public Diagnostico5PresenterImpl(Diagnostico5View diagnostico5View){
        this.diagnostico5View = diagnostico5View;
        diagnostico5Interactor = new Diagnostico5InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico5View != null)
            diagnostico5View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico5View != null)
            diagnostico5View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico5View != null)
            diagnostico5View.showError(mensaje);
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico5Interactor.actualizarEstatusExamen(estatus, idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico5View != null)
            diagnostico5View.examenGuardado();
    }

}
