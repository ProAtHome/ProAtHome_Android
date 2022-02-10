package com.proathome.Interfaces.cliente.Registrarse;

public interface RegistrarseView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void success(String mensaje);

}
