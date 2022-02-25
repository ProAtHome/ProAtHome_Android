package com.proathome.Interfaces.profesional.Ayuda;

import org.json.JSONObject;

public interface AyudaView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setVisibilityLottie(int visibilityLottie);

    void setAdapterTickets(JSONObject jsonObject);

}
