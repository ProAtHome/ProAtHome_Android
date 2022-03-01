package com.proathome.Interfaces.cliente.DatosBancoPlan;

public interface DatosBancoPlanView {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void setEstatusButtonValidarDatos(boolean estatus);

    void cerrarFragment();

    void successOperation(String mensaje, String titulo, int tipo);

}
