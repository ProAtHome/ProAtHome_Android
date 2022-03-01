package com.proathome.Presenters.cliente.examen;

import com.proathome.Interactors.cliente.examen.Diagnostico3InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3View;

public class Diagnostico3PresenterImpl implements Diagnostico3Presenter {

    private Diagnostico3View diagnostico3View;
    private Diagnostico3Interactor diagnostico3Interactor;

    public Diagnostico3PresenterImpl(Diagnostico3View diagnostico3View){
        this.diagnostico3View = diagnostico3View;
        diagnostico3Interactor = new Diagnostico3InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico3View != null)
            diagnostico3View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico3View != null)
            diagnostico3View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico3View != null)
            diagnostico3View.showError(mensaje);
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico3Interactor.actualizarEstatusExamen(estatus, idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico3View != null)
            diagnostico3View.examenGuardado();
    }

}
