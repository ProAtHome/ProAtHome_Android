package com.proathome.utils;

public class ComponentMsgTickets {

    private String nombreUsuario, mensaje;
    private boolean operador;

    public boolean isOperador() {
        return operador;
    }

    public void setOperador(boolean operador) {
        this.operador = operador;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
