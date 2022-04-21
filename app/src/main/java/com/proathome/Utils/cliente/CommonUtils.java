package com.proathome.Utils.cliente;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.cliente.fragments.DetallesGestionarFragment;

public class CommonUtils {

    private static String tipoPlanS = "", tipoServicioS = "", horarioS = "", profesionalS = "", lugarS = "",
            observacionesS = "", fechaS = "", fotoProfesionalS = "", descripcionProfesionalS = "", correoProfesionalS = "";
    private static int idServicioS, tiempoS, idSeccionS, idNivelS, idBloqueS, idProfesionalS;
    private static double latitudS, longitudS;
    private static boolean sumarS;

    public static void setFragment(AppCompatActivity activity, String nameFragment, int contentRes,
                                   int idServicio, String tipoServicio, String horario, String profesional,
                                        String lugar, int tiempo, String observaciones,  double latitud,
                                            double longitud, int idSeccion, int idNivel, int idBloque,
                                                String fecha, String fotoProfesional, String descripcionProfesional,
                                                    String correoProfesional, boolean sumar, String tipoPlan, int idProfesional){

        idServicioS = idServicio;
        tipoServicioS = tipoServicio;
        horarioS = horario;
        profesionalS = profesional;
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
        fotoProfesionalS = fotoProfesional;
        descripcionProfesionalS = descripcionProfesional;
        correoProfesionalS = correoProfesional;
        sumarS = sumar;
        tipoPlanS = tipoPlan;
        idProfesionalS = idProfesional;
        Fragment fragment = getFragmentById(nameFragment);
        Log.d("tag1", nameFragment);
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
                bun.putInt("idServicio", idServicioS);
                bun.putString("tipoServicio", tipoServicioS);
                bun.putString("horario", horarioS);
                bun.putString("profesional", profesionalS);
                bun.putString("lugar", lugarS);
                bun.putInt("tiempo", tiempoS);
                bun.putString("observaciones", observacionesS);
                bun.putDouble("latitud", latitudS);
                bun.putDouble("longitud", longitudS);
                bun.putInt("idSeccion", idSeccionS);
                bun.putInt("idNivel", idNivelS);
                bun.putInt("idBloque", idBloqueS);
                bun.putString("fecha", fechaS);
                bun.putString("fotoProfesional", fotoProfesionalS);
                bun.putString("descripcionProfesional", descripcionProfesionalS);
                bun.putString("correoProfesional", correoProfesionalS);
                bun.putBoolean("sumar", sumarS);
                bun.putString("tipoPlan", tipoPlanS);
                bun.putInt("idProfesional", idProfesionalS);
                fragment.setArguments(bun);
                fragmentBool = true;
                break;
            case DetallesGestionarFragment.TAG:
                fragmentGestionar = new DetallesGestionarFragment();
                Bundle bunG = new Bundle();
                bunG.putInt("idServicio", idServicioS);
                bunG.putString("tipoServicio", tipoServicioS);
                bunG.putString("horario", horarioS);
                bunG.putString("profesional", profesionalS);
                bunG.putString("lugar", lugarS);
                bunG.putInt("tiempo", tiempoS);
                bunG.putString("observaciones", observacionesS);
                bunG.putDouble("latitud", latitudS);
                bunG.putDouble("longitud", longitudS);
                bunG.putInt("idSeccion", idSeccionS);
                bunG.putString("correoProfesional", correoProfesionalS);
                bunG.putInt("idNivel", idNivelS);
                bunG.putInt("idBloque", idBloqueS);
                bunG.putString("fecha", fechaS);
                bunG.putString("tipoPlan", tipoPlanS);
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