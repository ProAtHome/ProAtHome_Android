package com.proathome.Interfaces.cliente.Sesiones;

import org.json.JSONObject;

public interface SesionesView {

    void showProgress();

    void hideProgress();

    void setVisibilityLottie();

    void setMyAdapter(JSONObject object);

    void showError(String error);

    void setEstatusSesionesPagadas(boolean planActivo, boolean sesionesPagadas);

    void setDisponibilidadEstatus(boolean disponibilidad, int horas);

    void setInfoPlan(JSONObject jsonObject);

    void setRutaActual(JSONObject jsonObject);

    void setDatosBanco(JSONObject jsonObject);

}
