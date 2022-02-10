package com.proathome.Presenters.profesional;

import com.proathome.Interactors.profesional.RegistrarseProfesionalInteractorImpl;
import com.proathome.Interfaces.profesional.Registrarse.RegistrarseProfesionalInteractor;
import com.proathome.Interfaces.profesional.Registrarse.RegistrarseProfesionalPresenter;
import com.proathome.Interfaces.profesional.Registrarse.RegistrarseProfesionalView;

public class RegistrarseProfesionalPresenterImpl implements RegistrarseProfesionalPresenter {

    private RegistrarseProfesionalView registrarseProfesionalView;
    private RegistrarseProfesionalInteractor registrarseProfesionalInteractor;

    public RegistrarseProfesionalPresenterImpl(RegistrarseProfesionalView registrarseProfesionalView){
        this.registrarseProfesionalView = registrarseProfesionalView;
        registrarseProfesionalInteractor = new RegistrarseProfesionalInteractorImpl(this);
    }

    @Override
    public void registrar(String nombre, String paterno, String materno, String fecha, String celular, String telefono, String direccion, String correo, String pass, String genero) {
        registrarseProfesionalInteractor.registrar(nombre, paterno, materno, fecha, celular, telefono, direccion, correo, pass, genero);
    }

    @Override
    public void showProgress() {
        if(registrarseProfesionalView != null)
            registrarseProfesionalView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(registrarseProfesionalView != null)
            registrarseProfesionalView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(registrarseProfesionalView != null)
            registrarseProfesionalView.showError(error);
    }

    @Override
    public void toLogin(String mensaje) {
        if(registrarseProfesionalView != null)
            registrarseProfesionalView.toLogin(mensaje);
    }

}
