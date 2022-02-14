package com.proathome.Interfaces.cliente.Sesiones;

import android.content.Context;
import org.json.JSONObject;

public interface SesionesPresenter {

    void getSesiones(int idCliente, Context context);

    void showProgress();

    void hideProgress();

    void setVisibilityLottie();

    void setMyAdapter(JSONObject object);

    void showError(String error);

    void setEstatusSesionesPagadas(boolean planActivo, boolean sesionesPagadas);

    void getDisponibilidadServicio(int idCliente, Context context);

    void setDisponibilidadEstatus(boolean disponibilidad, int horas);

    void getInfoInicioSesiones(int idCliente);

    void setInfoPlan(JSONObject jsonObject);

    void setRutaActual(JSONObject jsonObject);

    void setDatosBanco(JSONObject jsonObject);

}
