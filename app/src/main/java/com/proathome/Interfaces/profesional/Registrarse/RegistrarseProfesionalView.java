package com.proathome.Interfaces.profesional.Registrarse;

public interface RegistrarseProfesionalView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void toLogin(String mensaje);

}
