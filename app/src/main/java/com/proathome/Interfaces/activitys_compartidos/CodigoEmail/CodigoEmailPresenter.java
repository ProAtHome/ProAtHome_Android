package com.proathome.Interfaces.activitys_compartidos.CodigoEmail;

public interface CodigoEmailPresenter {

    void validarCodigo(String d1, String d2, String d3, String d4, int tipoPerfil, String correo, String token);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void successCode(String codigo);

}
