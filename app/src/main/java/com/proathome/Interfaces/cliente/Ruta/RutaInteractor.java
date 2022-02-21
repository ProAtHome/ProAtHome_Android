package com.proathome.Interfaces.cliente.Ruta;

public interface RutaInteractor {

    void getEstatusExamen(int idCliente, String token);

    void getEstadoRuta(int idCliente, int secciones, String token);

    void reiniciarExamen(int idCliente);

    void cancelarExamen(int idCliente);

}
