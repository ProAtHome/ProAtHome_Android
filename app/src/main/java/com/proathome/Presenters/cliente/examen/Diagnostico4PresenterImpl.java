package com.proathome.Presenters.cliente.examen;

import com.proathome.Interactors.cliente.examen.Diagnostico4InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico4.Diagnostico4Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico4.Diagnostico4Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico4.Diagnostico4View;

public class Diagnostico4PresenterImpl implements Diagnostico4Presenter {

    private Diagnostico4View diagnostico4View;
    private Diagnostico4Interactor diagnostico4Interactor;

    public Diagnostico4PresenterImpl(Diagnostico4View diagnostico4View){
        this.diagnostico4View = diagnostico4View;
        diagnostico4Interactor = new Diagnostico4InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico4View != null)
            diagnostico4View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico4View != null)
            diagnostico4View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico4View != null)
            diagnostico4View.showError(mensaje);
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico4Interactor.actualizarEstatusExamen(estatus, idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico4View != null)
            diagnostico4View.examenGuardado();
    }

}
