package com.proathome.Interfaces.cliente.Examen.Diagnostico2;


public interface Diagnostico2Presenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual);

    void examenGuardado();

}
