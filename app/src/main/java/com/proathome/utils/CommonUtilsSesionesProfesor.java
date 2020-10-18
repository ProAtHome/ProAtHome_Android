package com.proathome.utils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;

public class CommonUtilsSesionesProfesor {

    private static String nombreEstudianteS = "", descripcionS = "", correoS = "", fotoS ="", tipoClaseS = "", horarioS = "", profesorS = "", lugarS = "", observacionesS = "";
    private static int idClaseS, tiempoS, idSeccionS, idNivelS, idBloqueS, idEstudianteS;
    private static double latitudS, longitudS;

    public static void setFragment(AppCompatActivity activity, String nameFragment, int contentRes, int idClase, String nombreEstudiante, String descripcion, String correo, String foto, String tipoClase, String horario, String profesor,
                                   String lugar, int tiempo, String observaciones,  double latitud,  double longitud, int idSeccion, int idNivel, int idBloque, int idEstudiante){

        idClaseS = idClase;
        nombreEstudianteS = nombreEstudiante;
        descripcionS = descripcion;
        correoS = correo;
        fotoS = foto;
        tipoClaseS = tipoClase;
        horarioS = horario;
        profesorS = profesor;
        lugarS = lugar;
        tiempoS = tiempo;
        observacionesS = observaciones;
        latitudS = latitud;
        longitudS = longitud;
        idSeccionS = idSeccion;
        idNivelS = idNivel;
        idBloqueS = idBloque;
        idEstudianteS = idEstudiante;
        Fragment fragment = getFragmentById(nameFragment);
        activity.getSupportFragmentManager().beginTransaction().add(contentRes, fragment).commit();

    }

    private static Fragment getFragmentById(String nameFragment) {

        DetallesSesionProfesorFragment fragment = null;
        switch (nameFragment){

            case DetallesSesionProfesorFragment.TAG:
                fragment = new DetallesSesionProfesorFragment();
                Bundle bun = new Bundle();
                bun.putInt("idClase", idClaseS);
                bun.putString("nombreEstudiante", nombreEstudianteS);
                bun.putString("descripcion", descripcionS);
                bun.putString("correo", correoS);
                bun.putString("foto", fotoS);
                bun.putString("tipoClase", tipoClaseS);
                bun.putString("horario", horarioS);
                bun.putString("profesor", profesorS);
                bun.putString("lugar", lugarS);
                bun.putInt("tiempo", tiempoS);
                bun.putString("observaciones", observacionesS);
                bun.putDouble("latitud", latitudS);
                bun.putDouble("longitud", longitudS);
                bun.putInt("idSeccion", idSeccionS);
                bun.putInt("idNivel", idNivelS);
                bun.putInt("idBloque", idBloqueS);
                bun.putInt("idEstudiante", idEstudianteS);
                fragment.setArguments(bun);
                break;

        }

        return fragment;

    }

}
