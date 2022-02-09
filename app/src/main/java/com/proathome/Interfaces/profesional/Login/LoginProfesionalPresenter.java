package com.proathome.Interfaces.profesional.Login;

import org.json.JSONObject;

public interface LoginProfesionalPresenter {

    void login(String correo, String pass);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void toInicio(JSONObject jsonObject, String correo);

    void toActivarCuenta();

    void toBloquearPerfil();

}
