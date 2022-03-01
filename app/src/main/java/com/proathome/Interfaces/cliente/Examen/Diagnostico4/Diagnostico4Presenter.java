package com.proathome.Interfaces.cliente.Examen.Diagnostico4;

public interface Diagnostico4Presenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual);

    void examenGuardado();

}
