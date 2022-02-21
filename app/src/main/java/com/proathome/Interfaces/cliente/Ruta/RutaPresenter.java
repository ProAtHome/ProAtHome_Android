package com.proathome.Interfaces.cliente.Ruta;

public interface RutaPresenter {

    void getEstatusExamen(int idCliente, String token);

    void getEstadoRuta(int idCliente, int secciones, String token);

    void reiniciarExamen(int idCliente);

    void showMsg(String titulo, String mensaje, int tipo);

    void successReinicio();

    void setRutaActual(int idSeccion, int idNivel, int idBloque);

    void setVisibilityButtonExamen(int visibility);

    void showDialogExamen();

    void continuarExamen(int pregunta);

    void cancelarExamen(int idCliente);

}
