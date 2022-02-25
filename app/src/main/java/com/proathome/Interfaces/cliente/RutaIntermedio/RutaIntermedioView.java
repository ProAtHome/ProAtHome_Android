package com.proathome.Interfaces.cliente.RutaIntermedio;

public interface RutaIntermedioView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void evaluarNivel(int idSeccion, int idNivel, int idBloque);

}
