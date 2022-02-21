package com.proathome.Interfaces.cliente.Inicio;

import android.content.Context;
import org.json.JSONObject;

public interface InicioFragmentPresenter {

    void getSesiones(int idCliente, Context context);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setLottieVisible();

    void setAdapter(JSONObject object);

}
