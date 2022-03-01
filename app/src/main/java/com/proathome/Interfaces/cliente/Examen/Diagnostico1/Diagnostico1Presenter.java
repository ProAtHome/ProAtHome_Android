package com.proathome.Interfaces.cliente.Examen.Diagnostico1;

public interface Diagnostico1Presenter {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void inicioExamen(int idCliente, int puntacionAsumar, int preguntaActual);

    void examenGuardado();

}
