package com.proathome.Presenters.profesional;

import android.content.Context;
import android.graphics.Bitmap;
import com.proathome.Interactors.profesional.DetallesSesionProfesionalInteractorImpl;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalInteractor;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalPresenter;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalView;

public class DetallesSesionProfesionalPresenterImpl implements DetallesSesionProfesionalPresenter {

    private DetallesSesionProfesionalView detallesSesionProfesionalView;
    private DetallesSesionProfesionalInteractor detallesSesionProfesionalInteractor;

    public DetallesSesionProfesionalPresenterImpl(DetallesSesionProfesionalView detallesSesionProfesionalView){
        this.detallesSesionProfesionalView = detallesSesionProfesionalView;
        detallesSesionProfesionalInteractor = new DetallesSesionProfesionalInteractorImpl(this);
    }

    @Override
    public void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible) {
        detallesSesionProfesionalInteractor.cambiarDisponibilidadProfesional(idSesion, idPerfil, disponible);
    }

    @Override
    public void validarServicioFinalizadoProfesional(int idSesion, int idPerfil, Context context) {
        detallesSesionProfesionalInteractor.validarServicioFinalizadoProfesional(idSesion, idPerfil, context);
    }

    @Override
    public void validarvaloracionCliente(int idSesion, int idCliente) {
        detallesSesionProfesionalInteractor.validarvaloracionCliente(idSesion, idCliente);
    }

    @Override
    public void getFotoPerfil(String foto) {
        detallesSesionProfesionalInteractor.getFotoPerfil(foto);
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(detallesSesionProfesionalView != null)
            detallesSesionProfesionalView.setFotoBitmap(bitmap);
    }

    @Override
    public void showError(String mensaje) {
        if(detallesSesionProfesionalView != null)
            detallesSesionProfesionalView.showError(mensaje);
    }

}
