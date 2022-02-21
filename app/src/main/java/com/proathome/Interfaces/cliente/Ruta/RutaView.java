package com.proathome.Interfaces.cliente.Ruta;

public interface RutaView {

    void showMsg(String titulo, String mensaje, int tipo);

    void successReinicio();

    void setRutaActual(int idSeccion, int idNivel, int idBloque);

    void setVisibilityButtonExamen(int visibility);

    void showDialogExamen();

    void continuarExamen(int pregunta);

}
