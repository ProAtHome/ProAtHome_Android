package com.proathome.Interfaces.cliente.Planes;

public interface PlanesView {

    void setFechaServidor(String fecha);

    void setSesionActual(int idSeccion, int idNivel, int idBloque);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setDatosBancarios(String nombreTitular, String tarjeta, String mes, String ano);

}
