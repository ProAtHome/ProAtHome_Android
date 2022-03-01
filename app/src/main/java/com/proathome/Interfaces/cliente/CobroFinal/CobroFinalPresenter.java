package com.proathome.Interfaces.cliente.CobroFinal;

import org.json.JSONObject;

public interface CobroFinalPresenter {

    void getPreOrden(int idCliente, int idSesion, String token);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setDatosPreOrden(JSONObject jsonObject);

}
