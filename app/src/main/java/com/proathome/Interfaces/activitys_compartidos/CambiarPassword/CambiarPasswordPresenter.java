package com.proathome.Interfaces.activitys_compartidos.CambiarPassword;

public interface CambiarPasswordPresenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void guardarPass(String nuevaPass, String nuevaPassRep, String token, String correo, String codigo, int tipoPerfil);

    void successPassword(String mensaje);

}
