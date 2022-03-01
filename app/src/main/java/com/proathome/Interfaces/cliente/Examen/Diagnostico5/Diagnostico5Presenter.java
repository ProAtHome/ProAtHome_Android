package com.proathome.Interfaces.cliente.Examen.Diagnostico5;

public interface Diagnostico5Presenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual);

    void examenGuardado();

}
