package com.proathome.utils;

import java.util.Objects;

public class Component {

    private String nivel, tipoClase, horario;
    private int photoRes;
    private int type;

    public Component(){

    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getPhotoRes() {
        return photoRes;
    }

    public void setPhotoRes(int photoRes) {
        this.photoRes = photoRes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return photoRes == component.photoRes &&
                type == component.type &&
                Objects.equals(nivel, component.nivel) &&
                Objects.equals(tipoClase, component.tipoClase) &&
                Objects.equals(horario, component.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nivel, tipoClase, horario, photoRes, type);
    }

}