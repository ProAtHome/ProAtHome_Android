package com.proathome.utils;

import java.util.Objects;

public class ComponentSesionesProfesor {

    private String nombreEstudiante, descripcion, correo, foto, nivel, tipoClase, horario, profesor, lugar, tiempo, observaciones, actualizado;
    private int photoRes, idClase;
    private double latitud, longitud;
    private int type;

    public ComponentSesionesProfesor(){

    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getTipoClase() {
        return tipoClase;
    }

    public void setTipoClase(String tipoClase) {
        this.tipoClase = tipoClase;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getActualizado() {
        return actualizado;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
    }

    public int getPhotoRes() {
        return photoRes;
    }

    public void setPhotoRes(int photoRes) {
        this.photoRes = photoRes;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentSesionesProfesor that = (ComponentSesionesProfesor) o;
        return photoRes == that.photoRes &&
                idClase == that.idClase &&
                Double.compare(that.latitud, latitud) == 0 &&
                Double.compare(that.longitud, longitud) == 0 &&
                type == that.type &&
                Objects.equals(nombreEstudiante, that.nombreEstudiante) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(correo, that.correo) &&
                Objects.equals(foto, that.foto) &&
                Objects.equals(nivel, that.nivel) &&
                Objects.equals(tipoClase, that.tipoClase) &&
                Objects.equals(horario, that.horario) &&
                Objects.equals(profesor, that.profesor) &&
                Objects.equals(lugar, that.lugar) &&
                Objects.equals(tiempo, that.tiempo) &&
                Objects.equals(observaciones, that.observaciones) &&
                Objects.equals(actualizado, that.actualizado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreEstudiante, descripcion, correo, foto, nivel, tipoClase, horario, profesor, lugar, tiempo, observaciones, actualizado, photoRes, idClase, latitud, longitud, type);
    }

}
