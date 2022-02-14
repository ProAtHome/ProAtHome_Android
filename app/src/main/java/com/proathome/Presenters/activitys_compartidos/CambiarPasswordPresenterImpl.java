package com.proathome.Presenters.activitys_compartidos;

import com.proathome.Interactors.activitys_compartidos.CambiarPasswordInteractorImpl;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordInteractor;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordPresenter;
import com.proathome.Interfaces.activitys_compartidos.CambiarPassword.CambiarPasswordView;

public class CambiarPasswordPresenterImpl implements CambiarPasswordPresenter {

    private CambiarPasswordView cambiarPasswordView;
    private CambiarPasswordInteractor cambiarPasswordInteractor;

    public CambiarPasswordPresenterImpl(CambiarPasswordView cambiarPasswordView){
        this.cambiarPasswordView = cambiarPasswordView;
        cambiarPasswordInteractor = new CambiarPasswordInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(cambiarPasswordView != null)
            cambiarPasswordView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(cambiarPasswordView != null)
            cambiarPasswordView.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(cambiarPasswordView != null)
            cambiarPasswordView.showError(mensaje);
    }

    @Override
    public void guardarPass(String nuevaPass, String nuevaPassRep, String token, String correo, String codigo, int tipoPerfil) {
        cambiarPasswordInteractor.guardarPass(nuevaPass, nuevaPassRep, token, correo, codigo, tipoPerfil);
    }

    @Override
    public void successPassword(String mensaje) {
        if(cambiarPasswordView != null)
            cambiarPasswordView.successPassword(mensaje);
    }
}
