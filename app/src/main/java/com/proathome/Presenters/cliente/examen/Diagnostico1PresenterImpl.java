package com.proathome.Presenters.cliente.examen;

import com.proathome.Interactors.cliente.examen.Diagnostico1InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico1.Diagnostico1Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico1.Diagnostico1Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico1.Diagnostico1View;

public class Diagnostico1PresenterImpl implements Diagnostico1Presenter {

    private Diagnostico1View diagnostico1View;
    private Diagnostico1Interactor diagnostico1Interactor;

    public Diagnostico1PresenterImpl(Diagnostico1View diagnostico1View){
        this.diagnostico1View = diagnostico1View;
        diagnostico1Interactor = new Diagnostico1InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico1View != null)
            diagnostico1View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico1View != null)
            diagnostico1View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico1View != null)
            diagnostico1View.showError(mensaje);
    }

    @Override
    public void inicioExamen(int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico1Interactor.inicioExamen(idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico1View != null)
            diagnostico1View.examenGuardado();
    }

}
