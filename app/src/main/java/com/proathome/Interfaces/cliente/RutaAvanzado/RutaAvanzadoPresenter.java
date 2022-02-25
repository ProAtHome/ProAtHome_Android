package com.proathome.Interfaces.cliente.RutaAvanzado;

public interface RutaAvanzadoPresenter {

    void getEstadoRuta(int idCliente, int nivel, String token);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void evaluarNivel(int idSeccion, int idNivel, int idBloque);

}
