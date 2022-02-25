package com.proathome.Interfaces.fragments_compartidos.DatosFiscales;

import org.json.JSONObject;

public interface DatosFiscalesView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setInfoDatos(JSONObject datos);

    void updateSuccess(String mensaje);

}
