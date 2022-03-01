package com.proathome.Interfaces.cliente.DatosBancoPlan;

import android.content.Context;
import org.json.JSONObject;

public interface DatosBancoPlanPresenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void validarDatos(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                      int procedencia, Context context, JSONObject datosPago, int idSesion);

    void setEstatusButtonValidarDatos(boolean estatus);

    void cerrarFragment();

    void successOperation(String mensaje, String titulo, int tipo);

}
