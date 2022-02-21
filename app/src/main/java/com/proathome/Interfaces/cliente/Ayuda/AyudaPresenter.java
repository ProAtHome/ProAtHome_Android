package com.proathome.Interfaces.cliente.Ayuda;

import org.json.JSONObject;

public interface AyudaPresenter {

    void showProgress();

    void hideProgress();

    void obtenerTickets(int idCliente, String token);

    void showError(String error);

    void setVisibilityLottie(int visibilityLottie);

    void setAdapterTickets(JSONObject jsonObject);

}
