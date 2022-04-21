package com.proathome.Presenters.profesional;

import android.graphics.Bitmap;
import com.proathome.Interactors.profesional.DetallesGestionarInteractorImpl;
import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarInteractor;
import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarPresenter;
import com.proathome.Interfaces.profesional.DetallesGestionar.DetallesGestionarView;

import org.json.JSONObject;

public class DetallesGestionarPresenterImpl implements DetallesGestionarPresenter {

    private DetallesGestionarView detallesGestionarView;
    private DetallesGestionarInteractor detallesGestionarInteractor;

    public DetallesGestionarPresenterImpl(DetallesGestionarView detallesGestionarView){
        this.detallesGestionarView = detallesGestionarView;
        detallesGestionarInteractor = new DetallesGestionarInteractorImpl(this);
    }

    @Override
    public void cancelarSesion(int idProfesional, int idSesion) {
        detallesGestionarInteractor.cancelarSesion(idProfesional, idSesion);
    }

    @Override
    public void showError(String mensaje) {
        if(detallesGestionarView != null)
            detallesGestionarView.showError(mensaje);
    }

    @Override
    public void successCancel(String mensaje) {
        if(detallesGestionarView != null)
            detallesGestionarView.successCancel(mensaje);
    }

    @Override
    public void solicitudEliminarSesion(int idProfesional, int idSesion, String token) {
        detallesGestionarInteractor.solicitudEliminarSesion(idProfesional, idSesion, token);
    }

    @Override
    public void showAlertCancel(String mensaje) {
        if(detallesGestionarView != null)
            detallesGestionarView.showAlertCancel(mensaje);
    }

    @Override
    public void getFotoPerfil(String mensaje) {
        detallesGestionarInteractor.getFotoPerfil(mensaje);
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(detallesGestionarView != null)
            detallesGestionarView.setFotoBitmap(bitmap);
    }

    @Override
    public void showProgress() {
        if(detallesGestionarView != null)
            detallesGestionarView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(detallesGestionarView != null)
            detallesGestionarView.hideProgress();
    }

    @Override
    public void notificarCliente(JSONObject jsonObject) {
        detallesGestionarInteractor.notificarCliente(jsonObject);
    }

    @Override
    public void closeFragment() {
        if(detallesGestionarView != null)
            detallesGestionarView.closeFragment();
    }

}
