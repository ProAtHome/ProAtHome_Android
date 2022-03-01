package com.proathome.Interfaces.cliente.MasTiempo;

public interface MasTiempoPresenter {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void getPreOrden(int idCliente, int idSesion, String token);

    void finalizar(int idCliente, int idSesion);

    void cerrarFragment();

}
