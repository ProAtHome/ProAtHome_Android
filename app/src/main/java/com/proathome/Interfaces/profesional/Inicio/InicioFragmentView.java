package com.proathome.Interfaces.profesional.Inicio;

import org.json.JSONObject;

public interface InicioFragmentView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setLottieVisible();

    void setAdapterSesiones(JSONObject object);

}
