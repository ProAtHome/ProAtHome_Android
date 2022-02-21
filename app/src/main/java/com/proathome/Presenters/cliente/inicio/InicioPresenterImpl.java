package com.proathome.Presenters.cliente.inicio;

import android.content.Context;
import android.graphics.Bitmap;
import com.proathome.Interactors.cliente.inicio.InicioInteractorImpl;
import com.proathome.Interfaces.cliente.Inicio.InicioInteractor;
import com.proathome.Interfaces.cliente.Inicio.InicioPresenter;
import com.proathome.Interfaces.cliente.Inicio.InicioView;
import org.json.JSONObject;

public class InicioPresenterImpl implements InicioPresenter {

    private InicioView inicioView;
    private InicioInteractor inicioInteractor;

    public InicioPresenterImpl(InicioView inicioView){
        this.inicioView = inicioView;
        inicioInteractor = new InicioInteractorImpl(this);
    }

    @Override
    public void validarTokenSesion(int idCliente, Context context) {
        inicioInteractor.validarTokenSesion(idCliente, context);
    }

    @Override
    public void setFoto(Bitmap bitmap) {
        if(inicioView != null)
            inicioView.setFoto(bitmap);
    }

    @Override
    public void cargarPerfil(int idCliente, Context context) {
        inicioInteractor.cargarPerfil(idCliente, context);
    }

    @Override
    public void toMainActivity() {
        if(inicioView != null)
            inicioView.toMainActivity();
    }

    @Override
    public void setInfoPerfil(JSONObject jsonObject) {
        if(inicioView != null)
            inicioView.setInfoPerfil(jsonObject);
    }

    @Override
    public void showError(String error) {
        if(inicioView != null)
            inicioView.showError(error);
    }

}
