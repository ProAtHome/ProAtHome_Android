package com.proathome.controladores;

import com.proathome.utils.Constants;

public class TabuladorCosto {

    public static final int PARTICULAR = 1;
    public static final int SEMANAL = 2;
    public static final int MENSUAL = 3;
    public static final int BIMESTRAL = 4;
    public static final int HORA_TIEMPO = 60;
    public static final int COSTO_BASICO = 250;
    public static final int COSTO_INTERMEDIO = 300;
    public static final int COSTO_AVANZADO = 350;

    public static double getCosto(int idSeccion, int tiempo, int periodo){
        double costo = 0;
        if(periodo == TabuladorCosto.PARTICULAR){
            if(idSeccion == Constants.BASICO){
                costo = (double) (tiempo * TabuladorCosto.COSTO_BASICO) / TabuladorCosto.HORA_TIEMPO;
            }else if(idSeccion == Constants.INTERMEDIO){
                costo = (double) (tiempo * TabuladorCosto.COSTO_INTERMEDIO) / TabuladorCosto.HORA_TIEMPO;
            }else if(idSeccion == Constants.AVANZADO){
                costo = (double) (tiempo * TabuladorCosto.COSTO_AVANZADO) / TabuladorCosto.HORA_TIEMPO;
            }
        }

        return costo;
    }


}
