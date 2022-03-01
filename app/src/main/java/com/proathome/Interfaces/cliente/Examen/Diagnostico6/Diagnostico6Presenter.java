package com.proathome.Interfaces.cliente.Examen.Diagnostico6;

public interface Diagnostico6Presenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual);

    void examenGuardado();

}
