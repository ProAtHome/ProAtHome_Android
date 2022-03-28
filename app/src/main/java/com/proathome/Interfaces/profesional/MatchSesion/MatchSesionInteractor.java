package com.proathome.Interfaces.profesional.MatchSesion;

import org.json.JSONObject;

public interface MatchSesionInteractor {

    void getInfoSesion(int idSesion);

    void matchSesion(int idProfesional, int idSesion, JSONObject serviciosDisponibles,
                     JSONObject serviciosPendientes, String fechaActual, String fechaServicio,
                        String horario, int rango);

    void setImage(String foto);

}
