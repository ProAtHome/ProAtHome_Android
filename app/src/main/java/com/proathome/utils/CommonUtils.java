package com.proathome.utils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesGestionarFragment;

public class CommonUtils {

    private static String nivelS = "", tipoClaseS = "", horarioS = "", profesorS = "", lugarS = "",  tiempoS = "", observacionesS = "";
    private static int idClaseS;
    private static double latitudS, longitudS;

    public static void setFragment(AppCompatActivity activity, String nameFragment, int contentRes, int idClase, String nivel, String tipoClase, String horario, String profesor,
                                   String lugar, String tiempo, String observaciones,  double latitud,  double longitud){

        idClaseS = idClase;
        tipoClaseS = tipoClase;
        horarioS = horario;
        profesorS = profesor;
        lugarS = lugar;
        tiempoS = tiempo;
        nivelS = nivel;
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
                bun.putString("tiempo", tiempoS);
                bun.putString("nivel", nivelS);
                bun.putString("observaciones", observacionesS);
                bun.putDouble("latitud", latitudS);
                bun.putDouble("longitud", longitudS);
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
                bunG.putString("tiempo", tiempoS);
                bunG.putString("nivel", nivelS);
                bunG.putString("observaciones", observacionesS);
                bunG.putDouble("latitud", latitudS);
                bunG.putDouble("longitud", longitudS);
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