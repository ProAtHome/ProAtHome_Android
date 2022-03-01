package com.proathome.Interfaces.cliente.CobroFinal;

import org.json.JSONObject;

public interface CobroFinalView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setDatosPreOrden(JSONObject jsonObject);

}
