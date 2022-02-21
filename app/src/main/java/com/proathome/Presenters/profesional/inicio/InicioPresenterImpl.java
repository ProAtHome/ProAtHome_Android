package com.proathome.Presenters.profesional.inicio;

import android.graphics.Bitmap;
import com.proathome.Interactors.profesional.inicio.InicioInteractorImpl;
import com.proathome.Interfaces.profesional.Inicio.InicioInteractor;
import com.proathome.Interfaces.profesional.Inicio.InicioPresenter;
import com.proathome.Interfaces.profesional.Inicio.InicioView;
import org.json.JSONObject;

public class InicioPresenterImpl implements InicioPresenter {

    private InicioView inicioView;
    private InicioInteractor inicioInteractor;

    public InicioPresenterImpl(InicioView inicioView){
        this.inicioView = inicioView;
        inicioInteractor = new InicioInteractorImpl(this);
    }

    @Override
    public void validarTokenSesion(int idProfesional, String token) {
        inicioInteractor.validarTokenSesion(idProfesional, token);
    }

    @Override
    public void cargarPerfil(int idProfesional, String token) {
        inicioInteractor.cargarPerfil(idProfesional, token);
    }

    @Override
    public void showError(String error) {
        if(inicioView != null)
            inicioView.showError(error);
    }

    @Override
    public void setFoto(Bitmap bitmap) {
        if(inicioView != null)
            inicioView.setFoto(bitmap);
    }

    @Override
    public void sesionExpirada() {
        if(inicioView != null)
            inicioView.sesionExpirada();
    }

    @Override
    public void setInfoPerfil(JSONObject jsonObject) {
        if(inicioView != null)
            inicioView.setInfoPerfil(jsonObject);
    }

}
