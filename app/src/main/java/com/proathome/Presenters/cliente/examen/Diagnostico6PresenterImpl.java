package com.proathome.Presenters.cliente.examen;

import com.proathome.Interactors.cliente.examen.Diagnostico6InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6View;

public class Diagnostico6PresenterImpl implements Diagnostico6Presenter {

    private Diagnostico6View diagnostico6View;
    private Diagnostico6Interactor diagnostico6Interactor;

    public Diagnostico6PresenterImpl(Diagnostico6View diagnostico6View){
        this.diagnostico6View = diagnostico6View;
        diagnostico6Interactor = new Diagnostico6InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico6View != null)
            diagnostico6View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico6View != null)
            diagnostico6View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico6View != null)
            diagnostico6View.showError(mensaje);
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico6Interactor.actualizarEstatusExamen(estatus, idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico6View != null)
            diagnostico6View.examenGuardado();
    }

}
