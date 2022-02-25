package com.proathome.Interfaces.profesional.Ayuda;

import org.json.JSONObject;

public interface AyudaPresenter {

    void obtenerTickets(int idProfesional, String token);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setVisibilityLottie(int visibilityLottie);

    void setAdapterTickets(JSONObject jsonObject);

}
