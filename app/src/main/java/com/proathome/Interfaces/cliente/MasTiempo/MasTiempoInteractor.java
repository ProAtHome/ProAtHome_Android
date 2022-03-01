package com.proathome.Interfaces.cliente.MasTiempo;

public interface MasTiempoInteractor {

    void getPreOrden(int idCliente, int idSesion, String token);

    void generarOrdenPago();

    void finalizar(int idCliente, int idSesion);

}
