package com.proathome.Interfaces.cliente.Examen.Diagnostico3;

public interface Diagnostico3Presenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual);

    void examenGuardado();

}
