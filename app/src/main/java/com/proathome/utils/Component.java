package com.proathome.utils;

import java.util.Objects;

public class Component {

    private String fecha, tipoClase, horario, profesor, lugar, observaciones, actualizado;
    private int photoRes, idClase, idSeccion, idNivel, idBloque, tiempo;
    private double latitud, longitud;
    private int type;

    public Component(){

    }

    public String getActualizado() {
        return actualizado;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
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

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public int getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(int idBloque) {
        this.idBloque = idBloque;
    }

    public String obtenerNivel(int seccion, int nivel, int bloque){
        String nivelString = "";

        if(seccion == Constants.BASICO){
            nivelString += "Básico/";
            if(nivel == Constants.BASICO_1){
                nivelString += "Básico 1/";
                if(bloque == Constants.BLOQUE1_BASICO1){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_BASICO1){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_BASICO1){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_BASICO1){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_BASICO1){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_BASICO1){
                    nivelString += "Bloque 6";
                }
            }else if(nivel == Constants.BASICO_2){
                nivelString += "Básico 2/";
                if(bloque == Constants.BLOQUE1_BASICO2){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_BASICO2){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_BASICO2){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_BASICO2){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_BASICO2){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_BASICO2){
                    nivelString += "Bloque 6";
                }
            }else if(nivel == Constants.BASICO_3){
                nivelString += "Básico 3/";
                if(bloque == Constants.BLOQUE1_BASICO3){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_BASICO3){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_BASICO3){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_BASICO3){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_BASICO3){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_BASICO3){
                    nivelString += "Bloque 6";
                }else if(bloque == Constants.BLOQUE7_BASICO3){
                    nivelString += "Bloque 7";
                }
            }else if(nivel == Constants.BASICO_4){
                nivelString += "Básico 4/";
                if(bloque == Constants.BLOQUE1_BASICO4){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_BASICO4){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_BASICO4){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_BASICO4){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_BASICO4){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_BASICO4){
                    nivelString += "Bloque 6";
                }else if(bloque == Constants.BLOQUE7_BASICO4){
                    nivelString += "Bloque 7";
                }
            }else if(nivel == Constants.BASICO_5){
                nivelString += "Básico 5/";
                if(bloque == Constants.BLOQUE1_BASICO5){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_BASICO5){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_BASICO5){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_BASICO5){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_BASICO5){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_BASICO5){
                    nivelString += "Bloque 6";
                }else if(bloque == Constants.BLOQUE7_BASICO5){
                    nivelString += "Bloque 7";
                }
            }
        }else if(seccion == Constants.INTERMEDIO){
            nivelString += "Intermedio/";
            if(nivel == Constants.INTERMEDIO_1){
                nivelString += "Intermedio 1/";
                if(bloque == Constants.BLOQUE1_INTERMEDIO1){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO1){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO1){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO1){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO1){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO1){
                    nivelString += "Bloque 6";
                }else if(bloque == Constants.BLOQUE7_INTERMEDIO1){
                    nivelString += "Bloque 7";
                }
            }else if(nivel == Constants.INTERMEDIO_2){
                nivelString += "Intermedio 2/";
                if(bloque == Constants.BLOQUE1_INTERMEDIO2){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO2){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO2){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO2){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO2){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO2){
                    nivelString += "Bloque 6";
                }
            }else if(nivel == Constants.INTERMEDIO_3){
                nivelString += "Intermedio 3/";
                if(bloque == Constants.BLOQUE1_INTERMEDIO1){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO3){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO3){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO3){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO3){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO3){
                    nivelString += "Bloque 6";
                }
            }else if(nivel == Constants.INTERMEDIO_4){
                nivelString += "Intermedio 4/";
                if(bloque == Constants.BLOQUE1_INTERMEDIO4){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO4){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO4){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO4){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO4){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO4){
                    nivelString += "Bloque 6";
                }
            }else if(nivel == Constants.INTERMEDIO_5){
                nivelString += "Intermedio 5/";
                if(bloque == Constants.BLOQUE1_INTERMEDIO5){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO5){
                    nivelString += "Bloque 2";
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO5){
                    nivelString += "Bloque 3";
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO5){
                    nivelString += "Bloque 4";
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO5){
                    nivelString += "Bloque 5";
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO5){
                    nivelString += "Bloque 6";
                }
            }
        }else if(seccion == Constants.AVANZADO){
            nivelString += "Avanzado/";
            if(nivel == Constants.AVANZADO_1){
                nivelString += "Avanzado 1/";
                if(bloque == Constants.BLOQUE1_AVANZADO1){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_AVANZADO1){
                    nivelString += "Bloque 2";
                }
            }else if(nivel == Constants.AVANZADO_2){
                nivelString += "Avanzado 2/";
                if(bloque == Constants.BLOQUE1_AVANZADO2){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_AVANZADO2){
                    nivelString += "Bloque 2";
                }
            }else if(nivel == Constants.AVANZADO_3){
                nivelString += "Avanzado 3/";
                if(bloque == Constants.BLOQUE1_AVANZADO3){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_AVANZADO3){
                    nivelString += "Bloque 2";
                }
            }else if(nivel == Constants.AVANZADO_4){
                nivelString += "Avanzado 4/";
                if(bloque == Constants.BLOQUE1_AVANZADO4){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_AVANZADO4){
                    nivelString += "Bloque 2";
                }
            }else if(nivel == Constants.AVANZADO_5){
                nivelString += "Avanzado 5/";
                if(bloque == Constants.BLOQUE1_AVANZADO5){
                    nivelString += "Bloque 1";
                }else if(bloque == Constants.BLOQUE2_AVANZADO5){
                    nivelString += "Bloque 2";
                }
            }
        }

        return  nivelString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return photoRes == component.photoRes &&
                type == component.type &&
                Objects.equals(tipoClase, component.tipoClase) &&
                Objects.equals(horario, component.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, tipoClase, horario, photoRes, type);
    }

}