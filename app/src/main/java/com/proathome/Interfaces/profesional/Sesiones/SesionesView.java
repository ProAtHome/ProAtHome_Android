package com.proathome.Interfaces.profesional.Sesiones;

import org.json.JSONObject;

public interface SesionesView {

    void showError(String error);

    void showProgress();

    void hideProgress();

    void setVisibilityLottie(int visibilityLottie);

    void setAdapterSesiones(JSONObject object);

}
