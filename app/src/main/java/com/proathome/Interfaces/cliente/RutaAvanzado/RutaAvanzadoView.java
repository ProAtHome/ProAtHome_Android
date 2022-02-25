package com.proathome.Interfaces.cliente.RutaAvanzado;

public interface RutaAvanzadoView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void evaluarNivel(int idSeccion, int idNivel, int idBloque);

}
