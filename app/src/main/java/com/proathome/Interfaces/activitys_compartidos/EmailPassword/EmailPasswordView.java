package com.proathome.Interfaces.activitys_compartidos.EmailPassword;

public interface EmailPasswordView {

    void showProgress();

    void hideProgress();

    void successEmail(String token, String correo, String mensaje);

    void showError(String error);

}
