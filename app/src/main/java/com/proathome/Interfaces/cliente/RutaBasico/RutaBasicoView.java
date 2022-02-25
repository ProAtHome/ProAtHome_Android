package com.proathome.Interfaces.cliente.RutaBasico;

public interface RutaBasicoView {

    void showError(String error);

    void showProgress();

    void hideProgress();

    void evaluarNivelBasico(int idSeccion, int idNivel, int idBloque);

}
