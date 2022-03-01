package com.proathome.Presenters.cliente.examen;

import android.content.Context;
import com.proathome.Interactors.cliente.examen.Diagnostico7InteractorImpl;
import com.proathome.Interfaces.cliente.Examen.Diagnostico7.Diagnostico7Interactor;
import com.proathome.Interfaces.cliente.Examen.Diagnostico7.Diagnostico7Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico7.Diagnostico7View;

public class Diagnostico7PresenterImpl implements Diagnostico7Presenter {

    private Diagnostico7View diagnostico7View;
    private Diagnostico7Interactor diagnostico7Interactor;

    public Diagnostico7PresenterImpl(Diagnostico7View diagnostico7View){
        this.diagnostico7View = diagnostico7View;
        diagnostico7Interactor = new Diagnostico7InteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(diagnostico7View != null)
            diagnostico7View.showProgress();
    }

    @Override
    public void hideProgress() {
        if(diagnostico7View != null)
            diagnostico7View.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(diagnostico7View != null)
            diagnostico7View.showError(mensaje);
    }

    @Override
    public void getInfoExamenFinal(int idCliente, int puntacionAsumar, Context context) {
        diagnostico7Interactor.getInfoExamenFinal(idCliente, puntacionAsumar, context);
    }

    @Override
    public void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual) {
        diagnostico7Interactor.actualizarEstatusExamen(estatus, idCliente, puntacionAsumar, preguntaActual);
    }

    @Override
    public void examenGuardado() {
        if(diagnostico7View != null)
            diagnostico7View.examenGuardado();
    }

}
