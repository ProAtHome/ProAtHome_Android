package com.proathome.utils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesGestionarFragment;

public class CommonUtils {

    private static String tipoClaseS = "", horarioS = "", profesorS = "", lugarS = "", observacionesS = "", fechaS = "";
    private static int idClaseS, tiempoS, idSeccionS, idNivelS, idBloqueS;
    private static double latitudS, longitudS;

    public static void setFragment(AppCompatActivity activity, String nameFragment, int contentRes, int idClase, String tipoClase, String horario, String profesor,
                                   String lugar, int tiempo, String observaciones,  double latitud,  double longitud, int idSeccion, int idNivel, int idBloque, String fecha){

        idClaseS = idClase;
        tipoClaseS = tipoClase;
        horarioS = horario;
        profesorS = profesor;
        lugarS = lugar;
        tiempoS = tiempo;
        tiempoS = tiempo;
        fechaS = fecha;
        idSeccionS = idSeccion;
        idNivelS = idNivel;
        idBloqueS = idBloque;
        observacionesS = observaciones;
        latitudS = latitud;
        longitudS = longitud;
        Fragment fragment = getFragmentById(nameFragment);
        activity.getSupportFragmentManager().beginTransaction().add(contentRes, fragment).commit();

    }

    private static Fragment getFragmentById(String nameFragment) {

        DetallesFragment fragment = null;
        DetallesGestionarFragment fragmentGestionar = null;
        boolean fragmentBool = false;

        switch (nameFragment) {

            case DetallesFragment.TAG:
                fragment = new DetallesFragment();
                Bundle bun = new Bundle();
                bun.putInt("idClase", idClaseS);
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
                bun.putString("fecha", fechaS);
                fragment.setArguments(bun);
                fragmentBool = true;
                break;
            case DetallesGestionarFragment.TAG:
                fragmentGestionar = new DetallesGestionarFragment();
                Bundle bunG = new Bundle();
                bunG.putInt("idClase", idClaseS);
                bunG.putString("tipoClase", tipoClaseS);
                bunG.putString("horario", horarioS);
                bunG.putString("profesor", profesorS);
                bunG.putString("lugar", lugarS);
                bunG.putInt("tiempo", tiempoS);
                bunG.putString("observaciones", observacionesS);
                bunG.putDouble("latitud", latitudS);
                bunG.putDouble("longitud", longitudS);
                bunG.putInt("idSeccion", idSeccionS);
                bunG.putInt("idNivel", idNivelS);
                bunG.putInt("idBloque", idBloqueS);
                bunG.putString("fecha", fechaS);
                fragmentGestionar.setArguments(bunG);
                fragmentBool = false;
                break;

        }

        if(fragmentBool)
            return fragment;
        else
            return fragmentGestionar;

    }

}