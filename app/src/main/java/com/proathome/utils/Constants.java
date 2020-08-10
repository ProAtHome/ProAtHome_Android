package com.proathome.utils;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Constants {

    public static final String ARG_NAME = "name";
    public static final int SCROLL = 0;
    public static final int STATIC = 1;
    public static final int FOTO_PERFIL = 1;
    public static final int FOTO_EDITAR_PERFIL = 2;
    public static final int SESIONES_INICIO = 1;
    public static final int SESIONES_GESTIONAR = 2;
    public static final int INFO_PERFIL = 1;
    public static final int INFO_PERFIl_EDITAR = 2;
    /*Sincronizar Clases*/
    public static final int VERIFICAR_DISPONIBLIDAD = 1;
    public static final int CAMBIAR_DISPONIBILIDAD = 2;
    public static final int CAMBIAR_ESTATUS_CLASE = 3;
    public static final int GUARDAR_PROGRESO = 4;
    public static final int OBTENER_PROGRESO_INFO = 5;
    public static final int ESTATUS_INICIO = 10;
    public static final int ESTATUS_ENCURSO = 11;
    public static final int ESTATUS_ENPAUSA = 12;
    public static final int ESTATUS_TERMINADO = 13;
    public static final int ESTATUS_ENCURSO_TE = 14;
    public static final int ESTATUS_ENPAUSA_TE = 15;
    public static final int ESTATUS_TERMINADO_TE = 16;
    public static final int FINALIZAR_CLASE = 1;
    public static final int VALIDAR_CLASE_FINALIZADA = 2;
    public static final int VALIDAR_CLASE_FINALIZADA_AMBOS_PERFILES = 3;
    /*Fin Sincronizar*/
    /*Examen*/
    public static final int INICIO_EXAMEN  = 1;
    public static final int ENCURSO_EXAMEN  = 2;
    public static final int CANCELADO_EXAMEN  = 3;
    public static final int ESTATUS_EXAMEN = 4;
    public static final int INFO_EXAMEN_FINAL = 6;
    public static final int EXAMEN_GUARDADO = 7;
    public static final int CONTINUAR_EXAMEN = 8;
    public static final int EXAMEN_FINALIZADO = 10;
    public static final int REINICIAR_EXAMEN = 11;
    /*Fin Examen*/
    /*Ayuda*/
    public static final int TIPO_ESTUDIANTE = 1;
    public static final int TIPO_PROFESOR = 2;
    /*Fin Ayuda*/
    /*Ruta de A*/
    public static final int INICIO_RUTA = 1;
    public static final int RUTA_ENCURSO = 2;
    public static final int RUTA_ACTUALIZADA = 3;
    public static final int ESTADO_RUTA = 4;
    /*Fin Ruta de A*/
    /*RUTA NIVELES*/
    public static final int BASICO = 1;
    public static final int INTERMEDIO = 2;
    public static final int AVANZADO = 3;
    public static final int BASICO_1 = 1;
    public static final int BASICO_2 = 2;
    public static final int BASICO_3 = 3;
    public static final int BASICO_4 = 4;
    public static final int BASICO_5 = 5;
    public static final int AVANZADO_1 = 1;
    public static final int AVANZADO_2 = 2;
    public static final int AVANZADO_3 = 3;
    public static final int AVANZADO_4 = 4;
    public static final int AVANZADO_5 = 5;
    public static final int INTERMEDIO_1 = 1;
    public static final int INTERMEDIO_2 = 2;
    public static final int INTERMEDIO_3 = 3;
    public static final int INTERMEDIO_4 = 4;
    public static final int INTERMEDIO_5 = 5;
    public static final  int BLOQUE1_BASICO1 = 1;
    public static final  int BLOQUE2_BASICO1 = 2;
    public static final  int BLOQUE3_BASICO1 = 3;
    public static final  int BLOQUE4_BASICO1 = 4;
    public static final  int BLOQUE5_BASICO1 = 5;
    public static final  int BLOQUE6_BASICO1 = 6;
    public static final  int BLOQUE1_BASICO2 = 1;
    public static final  int BLOQUE2_BASICO2 = 2;
    public static final  int BLOQUE3_BASICO2 = 3;
    public static final  int BLOQUE4_BASICO2 = 4;
    public static final  int BLOQUE5_BASICO2 = 5;
    public static final  int BLOQUE6_BASICO2 = 6;
    public static final  int BLOQUE1_BASICO3 = 1;
    public static final  int BLOQUE2_BASICO3 = 2;
    public static final  int BLOQUE3_BASICO3 = 3;
    public static final  int BLOQUE4_BASICO3 = 4;
    public static final  int BLOQUE5_BASICO3 = 5;
    public static final  int BLOQUE6_BASICO3 = 6;
    public static final  int BLOQUE7_BASICO3 = 7;
    public static final  int BLOQUE1_BASICO4 = 1;
    public static final  int BLOQUE2_BASICO4 = 2;
    public static final  int BLOQUE3_BASICO4 = 3;
    public static final  int BLOQUE4_BASICO4 = 4;
    public static final  int BLOQUE5_BASICO4 = 5;
    public static final  int BLOQUE6_BASICO4 = 6;
    public static final  int BLOQUE7_BASICO4 = 7;
    public static final  int BLOQUE1_BASICO5 = 1;
    public static final  int BLOQUE2_BASICO5 = 2;
    public static final  int BLOQUE3_BASICO5 = 3;
    public static final  int BLOQUE4_BASICO5 = 4;
    public static final  int BLOQUE5_BASICO5 = 5;
    public static final  int BLOQUE6_BASICO5 = 6;
    public static final  int BLOQUE7_BASICO5 = 7;
    public static final  int BLOQUE1_INTERMEDIO1= 1;
    public static final  int BLOQUE2_INTERMEDIO1= 2;
    public static final  int BLOQUE3_INTERMEDIO1= 3;
    public static final  int BLOQUE4_INTERMEDIO1= 4;
    public static final  int BLOQUE5_INTERMEDIO1= 5;
    public static final  int BLOQUE6_INTERMEDIO1= 6;
    public static final  int BLOQUE7_INTERMEDIO1= 7;
    public static final  int BLOQUE1_INTERMEDIO2= 1;
    public static final  int BLOQUE2_INTERMEDIO2= 2;
    public static final  int BLOQUE3_INTERMEDIO2= 3;
    public static final  int BLOQUE4_INTERMEDIO2= 4;
    public static final  int BLOQUE5_INTERMEDIO2= 5;
    public static final  int BLOQUE6_INTERMEDIO2= 6;
    public static final  int BLOQUE1_INTERMEDIO3= 1;
    public static final  int BLOQUE2_INTERMEDIO3= 2;
    public static final  int BLOQUE3_INTERMEDIO3= 3;
    public static final  int BLOQUE4_INTERMEDIO3= 4;
    public static final  int BLOQUE5_INTERMEDIO3= 5;
    public static final  int BLOQUE6_INTERMEDIO3= 6;
    public static final  int BLOQUE1_INTERMEDIO4= 1;
    public static final  int BLOQUE2_INTERMEDIO4= 2;
    public static final  int BLOQUE3_INTERMEDIO4= 3;
    public static final  int BLOQUE4_INTERMEDIO4= 4;
    public static final  int BLOQUE5_INTERMEDIO4= 5;
    public static final  int BLOQUE6_INTERMEDIO4= 6;
    public static final  int BLOQUE1_INTERMEDIO5= 1;
    public static final  int BLOQUE2_INTERMEDIO5= 2;
    public static final  int BLOQUE3_INTERMEDIO5= 3;
    public static final  int BLOQUE4_INTERMEDIO5= 4;
    public static final  int BLOQUE5_INTERMEDIO5= 5;
    public static final  int BLOQUE6_INTERMEDIO5= 6;
    public static final  int BLOQUE1_AVANZADO1= 1;
    public static final  int BLOQUE2_AVANZADO1= 2;
    public static final  int BLOQUE1_AVANZADO2= 1;
    public static final  int BLOQUE2_AVANZADO2= 2;
    public static final  int BLOQUE1_AVANZADO3= 1;
    public static final  int BLOQUE2_AVANZADO3= 2;
    public static final  int BLOQUE1_AVANZADO4= 1;
    public static final  int BLOQUE2_AVANZADO4= 2;
    public static final  int BLOQUE1_AVANZADO5= 1;
    public static final  int BLOQUE2_AVANZADO5= 2;
    /*FIN RUTA NIVELES*/
    public static final String IP = "192.168.100.243";



    /*Prueba Estaticos Singleton*/

    /*ASYNCTASK SERVICIO OBTENER DISPONIBILIDAD Y PROGRESO*/
    public static Activity activity;
    public static FragmentActivity fragmentActivity;
    public static boolean TE_activado_DISPONIBILIDAD_PROGRESO;
    public static int progresoSegundosTE_DISPONIBILIDAD_PROGRESO;
    public static int progresoTE_DISPONIBILIDAD_PROGRESO;
    public static String linkSincronizarEstudiante_DISPONIBILIDAD_PROGRESO = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/validarEstatusClase/";
    public static String linkSincronizarProfesor_DISPONIBILIDAD_PROGRESO = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/validarEstatusClase/";
    public static int idPerfil_DISPONIBILIDAD_PROGRESO, tipoPerfil_DISPONIBILIDAD_PROGRESO, idSesion_DISPONIBILIDAD_PROGRESO;
    public static Context contexto_DISPONIBILIDAD_PROGRESO;
    public static String wsURL_DISPONIBILIDAD_PROGRESO;
    public static String result_DISPONIBILIDAD_PROGRESO;
    public static int progreso_DISPONIBILIDAD_PROGRESO, progresoSegundos_DISPONIBILIDAD_PROGRESO, estatus_DISPONIBILIDAD_PROGRESO;
    public static boolean dispProfesor_DISPONIBILIDAD_PROGRESO, dispEstudiante_DISPONIBILIDAD_PROGRESO;
    public static URL obtenerURL_DISPONIBILIDAD_PROGRESO() throws MalformedURLException {
        URL url = new URL(Constants.wsURL_DISPONIBILIDAD_PROGRESO);
        return url;
    }
    /*FIN ASYNCTASK SERVICIO OBTENER DISPONIBILIDAD Y PROGRESO*/

    /*ASYNCTASK SERVICIO OBTENER PROGRESO*/
    public static String linkSincronizarEstudiante_PROGRESO = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/validarEstatusClase/";
    public static String linkSincronizarProfesor_PROGRESO = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/validarEstatusClase/";
    public static int idPerfil_PROGRESO, tipoPerfil_PROGRESO, idSesion_PROGRESO, progresoSegundosS_PROGRESO, progresoS_PROGRESO;
    public static Context contexto_PROGRESO;
    public static String wsURL_PROGRESO;
    public static String result_PROGRESO;
    public static URL obtenerURL_PROGRESO() throws MalformedURLException {
        URL url = new URL(Constants.wsURL_PROGRESO);
        return url;
    }
    /*FIN ASYNCTASK SERVICIO OBTENER PROGRESO*/

    /*ASYNCTASK SERVICIO GUARDAR PROGRESO*/
    public static String linkActualizarProgreso_GUARDAR_PROGRESO = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/actualizarProgresoClase/";
    public static String wsURL_GUARDAR_PROGRESO;
    public static Context contexto_GUARDAR_PROGRESO;
    public static int idPerfil_GUARDAR_PROGRESO, tipoDeTiempo_GUARDAR_PROGRESO, idSesion_GUARDAR_PROGRESO, progreso_GUARDAR_PROGRESO, progresoSegundos_GUARDAR_PROGRESO;
    public static String result_GUARDAR_PROGRESO;
    public static URL obtenerURL_GUARDAR_PROGRESO() throws MalformedURLException {
        URL url = new URL(Constants.wsURL_GUARDAR_PROGRESO);
        return url;
    }
    /*FIN ASYNCTASK GUARDAR PROGRESO*/

    /*ASYNCTASK CAMBIAR ESTATUS CLASE*/
    public static String linkCambiarEstatusProfesor_CAMBIAR_ESTATUS = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/cambiarEstatusClase/";
    public static String linkCambiarEstatusEstudiante_CAMBIAR_ESTATUS = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/cambiarEstatusClase/";
    public static Context contexto_CAMBIAR_ESTATUS;
    public static String wsURL_CAMBIAR_ESTATUS;
    public static String result_CAMBIAR_ESTATUS;
    public static int idPerfil_CAMBIAR_ESTATUS, idSesion_CAMBIAR_ESTATUS, estatus_CAMBIAR_ESTATUS, tipoPerfil_CAMBIAR_ESTATUS;
    public static URL obtenerURL_CAMBIAR_ESTATUS() throws MalformedURLException {
        URL url = new URL(Constants.wsURL_CAMBIAR_ESTATUS);
        return url;
    }
    /*ASYNCTASK CAMBIAR ESTATUS CLASE*/

}
