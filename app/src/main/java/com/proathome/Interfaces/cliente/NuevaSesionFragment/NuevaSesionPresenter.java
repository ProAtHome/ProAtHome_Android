package com.proathome.Interfaces.cliente.NuevaSesionFragment;

import android.content.Context;
import android.os.Bundle;

public interface NuevaSesionPresenter {

    void getSesionActual(int idCliente, Context context);

    void setAdapters(int seccion, int nivel, int bloque, int minutos_horas);

    void setSeccionesListener(int seccion, int nivel, int bloque, int minutos_horas);

    void setNivelesListener(int seccion, int nivel, int bloque, int minutos_horas);

    void setBloquesListener(int seccion, int nivel, int bloque, int minutos_horas);

    void setEstatusRutaFinalizada();

    void validarPlan(int idCliente, Context context);

    void validarBanco(int idCliente, Context context);

    void setBanco(boolean valor);

    void guardarPago(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, Context context);

    void showError(String error);

    void finishFragment();

}
