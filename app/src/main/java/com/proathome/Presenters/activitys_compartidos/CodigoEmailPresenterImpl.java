package com.proathome.Presenters.activitys_compartidos;

import com.proathome.Interactors.activitys_compartidos.CodigoEmailInteractorImpl;
import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailInteractor;
import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailPresenter;
import com.proathome.Interfaces.activitys_compartidos.CodigoEmail.CodigoEmailView;

public class CodigoEmailPresenterImpl implements CodigoEmailPresenter {

    private CodigoEmailView codigoEmailView;
    private CodigoEmailInteractor codigoEmailInteractor;

    public CodigoEmailPresenterImpl(CodigoEmailView codigoEmailView){
        this.codigoEmailView = codigoEmailView;
        codigoEmailInteractor = new CodigoEmailInteractorImpl(this);
    }

    @Override
    public void validarCodigo(String d1, String d2, String d3, String d4, int tipoPerfil, String correo, String token) {
        codigoEmailInteractor.validarCodigo(d1, d2, d3, d4, tipoPerfil, correo, token);
    }

    @Override
    public void showProgress() {
        if(codigoEmailView != null)
            codigoEmailView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(codigoEmailView != null)
            codigoEmailView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(codigoEmailView != null)
            codigoEmailView.showError(error);
    }

    @Override
    public void successCode(String codigo) {
        if(codigoEmailView != null)
            codigoEmailView.successCode(codigo);
    }

}
