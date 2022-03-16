package com.proathome.Presenters.cliente;

import android.content.Context;
import android.graphics.Bitmap;
import com.proathome.Interactors.cliente.DetallesInteractorImpl;
import com.proathome.Interfaces.cliente.Detalles.DetallesInteractor;
import com.proathome.Interfaces.cliente.Detalles.DetallesPresenter;
import com.proathome.Interfaces.cliente.Detalles.DetallesView;

public class DetallesPresenterImpl implements DetallesPresenter {

    private DetallesView detallesView;
    private DetallesInteractor detallesInteractor;

    public DetallesPresenterImpl(DetallesView detallesView){
        this.detallesView = detallesView;
        detallesInteractor = new DetallesInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(detallesView != null)
            detallesView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(detallesView != null)
            detallesView.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(detallesView != null)
            detallesView.showError(mensaje);
    }

    @Override
    public void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible) {
        detallesInteractor.cambiarDisponibilidadCliente(idSesion, idPerfil, disponible);
    }

    @Override
    public void validarServicioFinalizadoCliente(int idSesion, int idPerfil, Context context) {
        detallesInteractor.validarServicioFinalizadoCliente(idSesion, idPerfil, context);
    }

    @Override
    public void validarValoracionProfesional(int idSesion, int idPerfil, Context context) {
        detallesInteractor.validarValoracionProfesional(idSesion, idPerfil, context);
    }

    @Override
    public void validarBloqueoPerfil(int idCliente, Context context) {
        detallesInteractor.validarBloqueoPerfil(idCliente, context);
    }

    @Override
    public void getFotoPerfil(String foto) {
        detallesInteractor.getFotoPerfil(foto);
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(detallesView != null)
            detallesView.setFotoBitmap(bitmap);
    }

}
