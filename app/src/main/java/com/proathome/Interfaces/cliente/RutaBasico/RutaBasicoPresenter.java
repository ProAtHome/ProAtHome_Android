package com.proathome.Interfaces.cliente.RutaBasico;

public interface RutaBasicoPresenter {

    void getEstadoRuta(int idCliente, int nivel, String token);

    void showError(String error);

    void showProgress();

    void hideProgress();

    void evaluarNivelBasico(int idSeccion, int idNivel, int idBloque);

}
