package com.proathome.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferences sharedPreferences;
    private static SharedPreferencesManager instance;
    private final String SHARED_PREFERENCES = "SHARED_SESION";
    private final String SHARED_ID_CLIENTE = "SHARED_ID_CLIENTE";
    private final String SHARED_CORREO_CLIENTE = "SHARED_CORREO_CLIENTE";
    private final String SHARED_TOKEN_CLIENTE = "SHARED_TOKEN_CLIENTE";
    private final String SHARED_ID_PROFESIONAL = "SHARED_ID_PROFESIONAL";
    private final String SHARED_CORREO_PROFESIONAL = "SHARED_CORREO_PROFESIONAL";
    private final String SHARED_RANGO_PROFESIONAL = "SHARED_RANGO_PROFESIONAL";
    private final String SHARED_TOKEN_PROFESIONAL = "SHARED_TOKEN_PROFESIONAL";
    private SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesManager getInstance(Context context){
        if(instance == null)
            instance = new SharedPreferencesManager(context);

        return instance;
    }

    public void guardarSesionCliente(int idCliente, String correo, String token){
        editor.putString(SHARED_TOKEN_CLIENTE, token);
        editor.putInt(SHARED_ID_CLIENTE, idCliente);
        editor.putString(SHARED_CORREO_CLIENTE, correo);
        editor.apply();
    }

    public String getTokenCliente(){
        return sharedPreferences.getString(SHARED_TOKEN_CLIENTE, null);
    }

    public int getIDCliente(){
        return sharedPreferences.getInt(SHARED_ID_CLIENTE, 0);
    }

    public String getCorreoCliente(){
        return sharedPreferences.getString(SHARED_CORREO_CLIENTE, null);
    }

    public void guardarSesionProfesional(int idProfesional, String correo, int rango, String token){
        editor.putString(SHARED_TOKEN_PROFESIONAL, token);
        editor.putInt(SHARED_ID_PROFESIONAL, idProfesional);
        editor.putString(SHARED_CORREO_PROFESIONAL, correo);
        editor.putInt(SHARED_RANGO_PROFESIONAL, rango);
        editor.apply();
    }

    public String getTokenProfesional(){
        return sharedPreferences.getString(SHARED_TOKEN_PROFESIONAL, null);
    }

    public int getIDProfesional(){
        return sharedPreferences.getInt(SHARED_ID_PROFESIONAL, 0);
    }

    public String getCorreoProfesional(){
        return sharedPreferences.getString(SHARED_CORREO_PROFESIONAL, null);
    }

    public int getRangoServicioProfeisonal(){
        return sharedPreferences.getInt(SHARED_RANGO_PROFESIONAL, 0);
    }

    public void logout(){
        editor.clear().commit();
    }

}
