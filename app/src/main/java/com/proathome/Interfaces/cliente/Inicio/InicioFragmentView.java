package com.proathome.Interfaces.cliente.Inicio;

import org.json.JSONObject;

public interface InicioFragmentView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setLottieVisible();

    void setAdapter(JSONObject object);

}
