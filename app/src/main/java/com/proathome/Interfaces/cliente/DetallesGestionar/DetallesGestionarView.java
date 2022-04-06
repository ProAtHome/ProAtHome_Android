package com.proathome.Interfaces.cliente.DetallesGestionar;

public interface DetallesGestionarView {

    void setFechaServidor(String fecha);

    void showMsg(int tipo, String titulo, String mensaje);

    void successDelete(String mensaje);

    void successUpdate(String mensaje);

    void closeFragment();

    void update(boolean cambioFecha);

}
