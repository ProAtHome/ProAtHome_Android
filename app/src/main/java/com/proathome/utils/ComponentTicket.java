package com.proathome.utils;

public class ComponentTicket {

    private String tituloTopico, estatus, fechaCreacion, descripcion, noTicket;
    private int idTicket;

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNoTicket() {
        return noTicket;
    }

    public void setNoTicket(String noTicket) {
        this.noTicket = noTicket;
    }

    public String getTituloTopico() {
        return tituloTopico;
    }

    public void setTituloTopico(String tituloTopico) {
        this.tituloTopico = tituloTopico;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public static String validarEstatus(int estatus){
        String estatusString = "";

        if(estatus == Constants.ESTATUS_SIN_OPERADOR)
            estatusString = "Esperando a soporte.";
        else if(estatus == Constants.ESTATUS_EN_CURSO)
            estatusString = "Solución en curso.";
        else if(estatus == Constants.ESTATUS_SOLUCIONADO)
            estatusString = "Ticket de ayuda solucionado";

        return estatusString;
    }
}
