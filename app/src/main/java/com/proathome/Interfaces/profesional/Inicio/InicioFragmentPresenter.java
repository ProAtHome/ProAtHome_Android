package com.proathome.Interfaces.profesional.Inicio;

import org.json.JSONObject;

public interface InicioFragmentPresenter {

    void getSesiones(int idProfesional, String token);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setLottieVisible();

    void setAdapterSesiones(JSONObject object);

}
