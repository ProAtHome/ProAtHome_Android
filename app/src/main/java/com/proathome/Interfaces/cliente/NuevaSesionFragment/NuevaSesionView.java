package com.proathome.Interfaces.cliente.NuevaSesionFragment;

public interface NuevaSesionView {

    void setAdapters(int seccion, int nivel, int bloque, int minutos_horas);

    void setSeccionesListener(int seccion, int nivel, int bloque, int minutos_horas);

    void setNivelesListener(int seccion, int nivel, int bloque, int minutos_horas);

    void setBloquesListener(int seccion, int nivel, int bloque, int minutos_horas);

    //void setEstatusRutaFinalizada();

    void setBanco(boolean valor);

    void showError(String error);

    void finishFragment();

    void validacionPlanes_Ruta();

    void showProgress();

    void hideProgress();

}
