package com.proathome.Utils.pojos;

public class ComponentMsgTickets {

    private String nombreUsuario, mensaje;
    private boolean operador;
    private int tipoUsuario;

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

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
