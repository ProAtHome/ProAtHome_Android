package com.proathome.Utils.profesional;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.proathome.Views.profesional.fragments.DetallesGestionarProfesionalFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;

public class CommonUtilsSesionesProfesional {

    private static String nombreClienteS = "", descripcionS = "", correoS = "", fotoS ="", tipoServicioS = "",
            horarioS = "", profesionalS = "", lugarS = "", observacionesS = "", fechaS = "";
    private static int idServicioS, tiempoS, idSeccionS, idNivelS, idBloqueS, idClienteS;
    private static double latitudS, longitudS;

    public static void setFragment(AppCompatActivity activity, String nameFragment, int contentRes,
                                   int idServicio, String nombreCliente, String descripcion, String correo,
                                        String foto, String tipoServicio, String horario, String profesional,
                                            String lugar, int tiempo, String observaciones,  double latitud,
                                                double longitud, int idSeccion, int idNivel, int idBloque, int idCliente, String fecha){

        idServicioS = idServicio;
        nombreClienteS = nombreCliente;
        descripcionS = descripcion;
        correoS = correo;
        fotoS = foto;
        tipoServicioS = tipoServicio;
        horarioS = horario;
        profesionalS = profesional;
        lugarS = lugar;
        tiempoS = tiempo;
        observacionesS = observaciones;
        latitudS = latitud;
        longitudS = longitud;
        idSeccionS = idSeccion;
        idNivelS = idNivel;
        idBloqueS = idBloque;
        idClienteS = idCliente;
        fechaS = fecha;
        Fragment fragment = getFragmentById(nameFragment);
        System.out.println(contentRes);
        Log.d("TAG1", nameFragment);
        activity.getSupportFragmentManager().beginTransaction().add(contentRes, fragment).commit();

    }

    private static Fragment getFragmentById(String nameFragment) {

        DetallesSesionProfesionalFragment fragment = null;
        DetallesGestionarProfesionalFragment fragmentGestionar = null;
        boolean fragmentBool = false;
        Bundle bun = new Bundle();

        switch (nameFragment){
            case DetallesSesionProfesionalFragment.TAG:
                fragment = new DetallesSesionProfesionalFragment();
                bun.putInt("idServicio", idServicioS);
                bun.putString("cliente", nombreClienteS);
                bun.putString("descripcion", descripcionS);
                bun.putString("correo", correoS);
                bun.putString("foto", fotoS);
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
                bun.putInt("idCliente", idClienteS);
                fragment.setArguments(bun);
                fragmentBool = true;
                break;
            case DetallesGestionarProfesionalFragment.TAG:
                fragmentGestionar = new DetallesGestionarProfesionalFragment();
                bun.putInt("idServicio", idServicioS);
                bun.putString("cliente", nombreClienteS);
                bun.putString("descripcion", descripcionS);
                bun.putString("correo", correoS);
                bun.putString("foto", fotoS);
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
                bun.putInt("idCliente", idClienteS);
                fragmentBool = false;
                fragmentGestionar.setArguments(bun);
                break;

        }

        if(fragmentBool)
            return fragment;
        else
            return fragmentGestionar;
    }

}
