package com.proathome.Presenters.cliente.inicio;

import com.proathome.Interactors.cliente.CobroFinalInteractorImpl;
import com.proathome.Interfaces.cliente.CobroFinal.CobroFinalInteractor;
import com.proathome.Interfaces.cliente.CobroFinal.CobroFinalPresenter;
import com.proathome.Interfaces.cliente.CobroFinal.CobroFinalView;
import org.json.JSONObject;

public class CobroFinalPresenterImpl implements CobroFinalPresenter {

    private CobroFinalView cobroFinalView;
    private CobroFinalInteractor cobroFinalInteractor;

    public CobroFinalPresenterImpl(CobroFinalView cobroFinalView){
        this.cobroFinalView = cobroFinalView;
        cobroFinalInteractor = new CobroFinalInteractorImpl(this);
    }

    @Override
    public void getPreOrden(int idCliente, int idSesion, String token) {
        cobroFinalInteractor.getPreOrden(idCliente, idSesion, token);
    }

    @Override
    public void showProgress() {
        if(cobroFinalView != null)
            cobroFinalView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(cobroFinalView != null)
            cobroFinalView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(cobroFinalView != null)
            cobroFinalView.showError(error);
    }

    @Override
    public void setDatosPreOrden(JSONObject jsonObject) {
        if(cobroFinalView != null)
            cobroFinalView.setDatosPreOrden(jsonObject);
    }

}
