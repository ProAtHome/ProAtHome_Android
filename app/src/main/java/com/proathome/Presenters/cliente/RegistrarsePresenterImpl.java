package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.RegistrarseInteractorImpl;
import com.proathome.Interfaces.cliente.Registrarse.RegistrarseInteractor;
import com.proathome.Interfaces.cliente.Registrarse.RegistrarsePresenter;
import com.proathome.Interfaces.cliente.Registrarse.RegistrarseView;

public class RegistrarsePresenterImpl implements RegistrarsePresenter {

    private RegistrarseView registrarseView;
    private RegistrarseInteractor registrarseInteractor;

    public RegistrarsePresenterImpl(RegistrarseView registrarseView){
        this.registrarseView = registrarseView;
        registrarseInteractor = new RegistrarseInteractorImpl(this);
    }

    @Override
    public void registrar(String nombre, String paterno, String materno, String correo, String celular, String telefono, String direccion, String fecha, String genero, String pass) {
        registrarseInteractor.registrar(nombre, paterno, materno, correo, celular, telefono, direccion, fecha, genero, pass);
    }

    @Override
    public void showProgress() {
        if(registrarseView != null)
            registrarseView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(registrarseView != null)
            registrarseView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(registrarseView != null)
            registrarseView.showError(error);
    }

    @Override
    public void success(String mensaje) {
        if(registrarseView != null)
            registrarseView.success(mensaje);
    }

}
