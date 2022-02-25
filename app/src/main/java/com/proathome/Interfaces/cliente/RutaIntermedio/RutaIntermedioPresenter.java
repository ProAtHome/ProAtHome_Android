package com.proathome.Interfaces.cliente.RutaIntermedio;

public interface RutaIntermedioPresenter {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void getEstadoRuta(int idCliente, int nivel, String token);

    void evaluarNivel(int idSeccion, int idNivel, int idBloque);

}
