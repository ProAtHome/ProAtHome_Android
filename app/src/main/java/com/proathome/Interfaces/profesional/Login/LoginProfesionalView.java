package com.proathome.Interfaces.profesional.Login;

import org.json.JSONObject;

public interface LoginProfesionalView {

    void showProgress();

    void hideProgress();

    void showErrorLogin(String error);

    void toInicio(JSONObject jsonObject, String correo);

    void toActivarCuenta();

    void toBloquearPerfil();

}
