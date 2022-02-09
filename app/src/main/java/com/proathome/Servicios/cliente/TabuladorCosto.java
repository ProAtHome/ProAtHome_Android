package com.proathome.Servicios.cliente;

import com.proathome.Utils.Constants;

public class TabuladorCosto {

    public static final int PARTICULAR = 1;
    public static final int HORA_TIEMPO = 60;
    public static final double COSTO_BASICO = 250.00;
    public static final double COSTO_INTERMEDIO = 300.00;
    public static final double COSTO_AVANZADO = 350.00;

    public static double getCosto(int idSeccion, int tiempo, int periodo){
        double costo = 0;
        if(periodo == TabuladorCosto.PARTICULAR){
            if(idSeccion == Constants.BASICO){
                costo = (tiempo * TabuladorCosto.COSTO_BASICO) / TabuladorCosto.HORA_TIEMPO;
            }else if(idSeccion == Constants.INTERMEDIO){
                costo = (tiempo * TabuladorCosto.COSTO_INTERMEDIO) / TabuladorCosto.HORA_TIEMPO;
            }else if(idSeccion == Constants.AVANZADO){
                costo = (tiempo * TabuladorCosto.COSTO_AVANZADO) / TabuladorCosto.HORA_TIEMPO;
            }
        }

        return (double)Math.round(costo * 100d) / 100d;
    }


}
