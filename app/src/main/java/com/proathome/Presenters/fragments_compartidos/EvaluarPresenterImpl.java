package com.proathome.Presenters.fragments_compartidos;

import android.os.Bundle;
import com.proathome.Interactors.fragments_compartidos.EvaluarInteractorImpl;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarInteractor;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarPresenter;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarView;

public class EvaluarPresenterImpl implements EvaluarPresenter {

    private EvaluarView evaluarView;
    private EvaluarInteractor evaluarInteractor;

    public EvaluarPresenterImpl(EvaluarView evaluarView){
        this.evaluarView = evaluarView;
        evaluarInteractor = new EvaluarInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(evaluarView != null)
            evaluarView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(evaluarView != null)
            evaluarView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(evaluarView != null)
            evaluarView.showError(error);
    }

    @Override
    public void enviarEvaluacion(String comentario, float evaluacion, int procedencia, String token) {
        evaluarInteractor.enviarEvaluacion(comentario, evaluacion, procedencia, token);
    }

    @Override
    public void toPagoPendiente(Bundle bundle) {
        if(evaluarView != null)
            evaluarView.toPagoPendiente(bundle);
    }

    @Override
    public void cerrarFragment() {
        if(evaluarView != null)
            evaluarView.cerrarFragment();
    }

}
