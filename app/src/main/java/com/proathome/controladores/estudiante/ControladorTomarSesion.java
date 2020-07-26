package com.proathome.controladores.estudiante;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.proathome.utils.Constants;

public class ControladorTomarSesion {

    private int seccion, nivel, bloque;
    private Context contexto;

    public ControladorTomarSesion(Context contexto, int seccion, int nivel, int bloque){
        this.contexto = contexto;
        this.seccion = seccion;
        this.nivel = nivel;
        this.bloque = bloque;
    }

    public ArrayAdapter obtenerSecciones(){

        ArrayAdapter<String> adapterSecciones = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};
        if(this.seccion == Constants.BASICO)
             datosSecciones = new String[]{"Básico"};
        else if(this.seccion == Constants.INTERMEDIO)
            datosSecciones = new String[]{"Básico", "Intermedio"};
        else if(this.seccion == Constants.AVANZADO)
            datosSecciones = new String[]{"Básico", "Intermedio", "Avanzado"};

        adapterSecciones.addAll(datosSecciones);

        return adapterSecciones;

    }

    public ArrayAdapter obtenerNiveles(){

        ArrayAdapter<String> adapterNiveles = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(this.seccion == Constants.BASICO){//TODO SECCION BASICO
            if(this.nivel == Constants.BASICO_1)
                datosSecciones = new String[]{"Básico 1"};
            else if(this.nivel == Constants.BASICO_2)
                datosSecciones = new String[]{"Básico 1", "Básico 2"};
            else if(this.nivel == Constants.BASICO_3)
                datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3"};
            else if(this.nivel == Constants.BASICO_4)
                datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3", "Básico 4"};
            else if(this.nivel == Constants.BASICO_5)
                datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3", "Básico 4", "Básico 5"};
        }else if(this.seccion == Constants.INTERMEDIO){// TODO SECCION INTERMEDIO
            if(this.nivel == Constants.INTERMEDIO_1)
                datosSecciones = new String[]{"Intermedio 1"};
            else if(this.nivel == Constants.INTERMEDIO_2)
                datosSecciones = new String[]{"Intermedio 1", "Intermedio 2"};
            else if(this.nivel == Constants.INTERMEDIO_3)
                datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3"};
            else if(this.nivel == Constants.INTERMEDIO_4)
                datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4"};
            else if(this.nivel == Constants.INTERMEDIO_5)
                datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4", "Intermedio 5"};
        }else if(this.seccion == Constants.AVANZADO){ // TODO SECCION AVANZADO
            if(this.nivel == Constants.AVANZADO_1)
                datosSecciones = new String[]{"Avanzado 1"};
            else if(this.nivel == Constants.AVANZADO_2)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2"};
            else if(this.nivel == Constants.AVANZADO_3)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3"};
            else if(this.nivel == Constants.AVANZADO_4)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3", "Avanzado 4"};
            else if(this.nivel == Constants.AVANZADO_5)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3", "Avanzado 4", "Avanzado 5"};
        }

        adapterNiveles.addAll(datosSecciones);

        return adapterNiveles;

    }

    public ArrayAdapter obtenerNiveles(int seccion){

        ArrayAdapter<String> adapterNiveles = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(seccion == Constants.BASICO){// TODO SECCION BASICO
            if(this.seccion == Constants.INTERMEDIO || this.seccion == Constants.AVANZADO){
                datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3", "Básico 4", "Básico 5"};
            }else{
                if(this.nivel == Constants.BASICO_1)
                    datosSecciones = new String[]{"Básico 1"};
                else if(this.nivel == Constants.BASICO_2)
                    datosSecciones = new String[]{"Básico 1", "Básico 2"};
                else if(this.nivel == Constants.BASICO_3)
                    datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3"};
                else if(this.nivel == Constants.BASICO_4)
                    datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3", "Básico 4"};
                else if(this.nivel == Constants.BASICO_5)
                    datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3", "Básico 4", "Básico 5"};
            }
        }else if(seccion == Constants.INTERMEDIO){ // TODO SECCION INTERMEDIO
            if(this.seccion == Constants.AVANZADO){
                datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4", "Intermedio 5"};
            }else{
                if(this.nivel == Constants.INTERMEDIO_1)
                    datosSecciones = new String[]{"Intermedio 1"};
                else if(this.nivel == Constants.INTERMEDIO_2)
                    datosSecciones = new String[]{"Intermedio 1", "Intermedio 2"};
                else if(this.nivel == Constants.INTERMEDIO_3)
                    datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3"};
                else if(this.nivel == Constants.INTERMEDIO_4)
                    datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4"};
                else if(this.nivel == Constants.INTERMEDIO_5)
                    datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4", "Intermedio 5"};
            }
        }else if(seccion == Constants.AVANZADO){ // TODO SECCION AVANZADO
            if(this.nivel == Constants.AVANZADO_1)
                datosSecciones = new String[]{"Avanzado 1"};
            else if(this.nivel == Constants.AVANZADO_2)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2"};
            else if(this.nivel == Constants.AVANZADO_3)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3"};
            else if(this.nivel == Constants.AVANZADO_4)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3", "Avanzado 4"};
            else if(this.nivel == Constants.AVANZADO_5)
                datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3", "Avanzado 4", "Avanzado 5"};
        }

        adapterNiveles.addAll(datosSecciones);

        return adapterNiveles;

    }

    public ArrayAdapter obtenerBloques(){

        ArrayAdapter<String> adapterBloques = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(this.seccion == Constants.BASICO){ // TODO SECCION BASICO
            if(this.nivel == Constants.BASICO_1){ // TODO NIVEL BASICO1
                if(this.bloque == Constants.BLOQUE1_BASICO1)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_BASICO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_BASICO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_BASICO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_BASICO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_BASICO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.BASICO_2){ // TODO NIVEL BASICO2
                if(this.bloque == Constants.BLOQUE1_BASICO2)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_BASICO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_BASICO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_BASICO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_BASICO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_BASICO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.BASICO_3){ // TODO NIVEL BASICO3
                if(this.bloque == Constants.BLOQUE1_BASICO3)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_BASICO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_BASICO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_BASICO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_BASICO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_BASICO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                else if(this.bloque == Constants.BLOQUE7_BASICO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }else if(this.nivel == Constants.BASICO_4){ // TODO NIVEL BASICO4
                if(this.bloque == Constants.BLOQUE1_BASICO4)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_BASICO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_BASICO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_BASICO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_BASICO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_BASICO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                else if(this.bloque == Constants.BLOQUE7_BASICO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }else if(this.nivel == Constants.BASICO_5){ // TODO NIVEL BASICO5
                if(this.bloque == Constants.BLOQUE1_BASICO5)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_BASICO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_BASICO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_BASICO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_BASICO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_BASICO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                else if(this.bloque == Constants.BLOQUE7_BASICO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }
        }else if(this.seccion == Constants.INTERMEDIO){ // TODO SECCION INTERMEDIO
            if(this.nivel == Constants.INTERMEDIO_1){ // TODO NIVEL INTERMEDIO1
                if(this.bloque == Constants.BLOQUE1_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                else if(this.bloque == Constants.BLOQUE7_INTERMEDIO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }else if(this.nivel == Constants.INTERMEDIO_2){// TODO NIVEL INTERMEDIO2
                if(this.bloque == Constants.BLOQUE1_INTERMEDIO2)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_INTERMEDIO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_INTERMEDIO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_INTERMEDIO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_INTERMEDIO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_INTERMEDIO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.INTERMEDIO_3){// TODO NIVEL INTERMEDIO3
                if(this.bloque == Constants.BLOQUE1_INTERMEDIO3)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_INTERMEDIO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_INTERMEDIO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_INTERMEDIO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_INTERMEDIO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_INTERMEDIO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.INTERMEDIO_4){// TODO NIVEL INTERMEDIO4
                if(this.bloque == Constants.BLOQUE1_INTERMEDIO4)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_INTERMEDIO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_INTERMEDIO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_INTERMEDIO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_INTERMEDIO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_INTERMEDIO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.INTERMEDIO_5){// TODO NIVEL INTERMEDIO5
                if(this.bloque == Constants.BLOQUE1_INTERMEDIO5)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_INTERMEDIO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                else if(this.bloque == Constants.BLOQUE3_INTERMEDIO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                else if(this.bloque == Constants.BLOQUE4_INTERMEDIO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                else if(this.bloque == Constants.BLOQUE5_INTERMEDIO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                else if(this.bloque == Constants.BLOQUE6_INTERMEDIO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }
        }else if(this.seccion == Constants.AVANZADO){ // TODO SECCION AVANZADO
            if(this.nivel == Constants.AVANZADO_1){//TODO NIVEL AVANZADO1
                if(this.bloque == Constants.BLOQUE1_AVANZADO1)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_AVANZADO1)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_2){//TODO NIVEL AVANZADO2
                if(this.bloque == Constants.BLOQUE1_AVANZADO2)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_AVANZADO2)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_3){//TODO NIVEL AVANZADO3
                if(this.bloque == Constants.BLOQUE1_AVANZADO3)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_AVANZADO3)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_4){//TODO NIVEL AVANZADO4
                if(this.bloque == Constants.BLOQUE1_AVANZADO4)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_AVANZADO4)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_5){//TODO NIVEL AVANZADO5
                if(this.bloque == Constants.BLOQUE1_AVANZADO5)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_AVANZADO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }
        }

        adapterBloques.addAll(datosSecciones);

        return adapterBloques;

    }

    public ArrayAdapter obtenerBloques(int seccion, int nivel){

        ArrayAdapter<String> adapterBloques = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(seccion == Constants.BASICO){
            if(this.seccion == Constants.INTERMEDIO || this.seccion == Constants.AVANZADO){
                if(nivel == Constants.BASICO_1){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }else if(nivel == Constants.BASICO_2){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }else if(nivel == Constants.BASICO_3){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                }else if(nivel == Constants.BASICO_4){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                }else if(nivel == Constants.BASICO_5){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                }
            }else{
                if(nivel == Constants.BASICO_1){ // TODO NIVEL BASICO1
                    if(this.nivel == Constants.BASICO_2 || this.nivel == Constants.BASICO_3 || this.nivel == Constants.BASICO_4 || this.nivel == Constants.BASICO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_BASICO1)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_BASICO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_BASICO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_BASICO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_BASICO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_BASICO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }
                }else if(nivel == Constants.BASICO_2){ // TODO NIVEL BASICO2
                    if(this.nivel == Constants.BASICO_3 || this.nivel == Constants.BASICO_4 || this.nivel == Constants.BASICO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_BASICO2)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_BASICO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_BASICO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_BASICO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_BASICO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_BASICO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }
                }else if(nivel == Constants.BASICO_3){ // TODO NIVEL BASICO3
                    if(this.nivel == Constants.BASICO_4 || this.nivel == Constants.BASICO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_BASICO3)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_BASICO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_BASICO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_BASICO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_BASICO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_BASICO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                        else if(this.bloque == Constants.BLOQUE7_BASICO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                    }
                }else if(nivel == Constants.BASICO_4){ // TODO NIVEL BASICO4
                    if(this.nivel == Constants.BASICO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_BASICO4)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_BASICO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_BASICO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_BASICO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_BASICO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_BASICO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                        else if(this.bloque == Constants.BLOQUE7_BASICO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                    }
                }else if(nivel == Constants.BASICO_5){ // TODO NIVEL BASICO5
                    if(this.bloque == Constants.BLOQUE1_BASICO5)
                        datosSecciones = new String[]{"Bloque 1"};
                    else if(this.bloque == Constants.BLOQUE2_BASICO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                    else if(this.bloque == Constants.BLOQUE3_BASICO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                    else if(this.bloque == Constants.BLOQUE4_BASICO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                    else if(this.bloque == Constants.BLOQUE5_BASICO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                    else if(this.bloque == Constants.BLOQUE6_BASICO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    else if(this.bloque == Constants.BLOQUE7_BASICO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                }
            }

        }else if(seccion == Constants.INTERMEDIO){
            if(this.seccion == Constants.AVANZADO){
                if(nivel == Constants.INTERMEDIO_1){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                }else if(nivel == Constants.INTERMEDIO_2){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }else if(nivel == Constants.INTERMEDIO_3){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }else if(nivel == Constants.INTERMEDIO_4){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }else if(nivel == Constants.INTERMEDIO_5){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }
            }else{
                if(nivel == Constants.INTERMEDIO_1){ // TODO NIVEL INTERMEDIO1
                    if(this.nivel == Constants.INTERMEDIO_2 || this.nivel == Constants.INTERMEDIO_3 || this.nivel == Constants.INTERMEDIO_4 || this.nivel == Constants.INTERMEDIO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                        else if(this.bloque == Constants.BLOQUE7_INTERMEDIO1)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
                    }
                }else if(nivel == Constants.INTERMEDIO_2){// TODO NIVEL INTERMEDIO2
                    if(this.nivel == Constants.INTERMEDIO_3 || this.nivel == Constants.INTERMEDIO_4 || this.nivel == Constants.INTERMEDIO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_INTERMEDIO2)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_INTERMEDIO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_INTERMEDIO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_INTERMEDIO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_INTERMEDIO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_INTERMEDIO2)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }
                }else if(nivel == Constants.INTERMEDIO_3){// TODO NIVEL INTERMEDIO3
                    if(this.nivel == Constants.INTERMEDIO_4 || this.nivel == Constants.INTERMEDIO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_INTERMEDIO3)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_INTERMEDIO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_INTERMEDIO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_INTERMEDIO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_INTERMEDIO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_INTERMEDIO3)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }
                }else if(nivel == Constants.INTERMEDIO_4){// TODO NIVEL INTERMEDIO4
                    if(this.nivel == Constants.INTERMEDIO_5){
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }else{
                        if(this.bloque == Constants.BLOQUE1_INTERMEDIO4)
                            datosSecciones = new String[]{"Bloque 1"};
                        else if(this.bloque == Constants.BLOQUE2_INTERMEDIO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                        else if(this.bloque == Constants.BLOQUE3_INTERMEDIO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                        else if(this.bloque == Constants.BLOQUE4_INTERMEDIO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                        else if(this.bloque == Constants.BLOQUE5_INTERMEDIO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                        else if(this.bloque == Constants.BLOQUE6_INTERMEDIO4)
                            datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                    }
                }else if(nivel == Constants.INTERMEDIO_5){// TODO NIVEL INTERMEDIO5
                    if(this.bloque == Constants.BLOQUE1_INTERMEDIO5)
                        datosSecciones = new String[]{"Bloque 1"};
                    else if(this.bloque == Constants.BLOQUE2_INTERMEDIO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                    else if(this.bloque == Constants.BLOQUE3_INTERMEDIO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3"};
                    else if(this.bloque == Constants.BLOQUE4_INTERMEDIO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4"};
                    else if(this.bloque == Constants.BLOQUE5_INTERMEDIO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5"};
                    else if(this.bloque == Constants.BLOQUE6_INTERMEDIO5)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
                }
            }
        }else if(seccion == Constants.AVANZADO){
            if(nivel == Constants.AVANZADO_1){
                if(this.nivel == Constants.AVANZADO_2 || this.nivel == Constants.AVANZADO_3 || this.nivel == Constants.AVANZADO_4 || this.nivel == Constants.AVANZADO_5){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }else{
                    if(this.bloque == Constants.BLOQUE1_AVANZADO1)
                        datosSecciones = new String[]{"Bloque 1"};
                    else if(this.bloque == Constants.BLOQUE2_AVANZADO1)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }
            }else if(nivel == Constants.AVANZADO_2){
                if(this.nivel == Constants.AVANZADO_3 || this.nivel == Constants.AVANZADO_4 || this.nivel == Constants.AVANZADO_5){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }else{
                    if(this.bloque == Constants.BLOQUE1_AVANZADO2)
                        datosSecciones = new String[]{"Bloque 1"};
                    else if(this.bloque == Constants.BLOQUE2_AVANZADO2)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }
            }else if(nivel == Constants.AVANZADO_3){
                if(this.nivel == Constants.AVANZADO_4 || this.nivel == Constants.AVANZADO_5){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }else{
                    if(this.bloque == Constants.BLOQUE1_AVANZADO3)
                        datosSecciones = new String[]{"Bloque 1"};
                    else if(this.bloque == Constants.BLOQUE2_AVANZADO3)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }
            }else if(nivel == Constants.AVANZADO_4){
                if(this.nivel == Constants.AVANZADO_5){
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }else{
                    if(this.bloque == Constants.BLOQUE1_AVANZADO4)
                        datosSecciones = new String[]{"Bloque 1"};
                    else if(this.bloque == Constants.BLOQUE2_AVANZADO4)
                        datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
                }
            }else if(nivel == Constants.AVANZADO_5){
                if(this.bloque == Constants.BLOQUE1_AVANZADO5)
                    datosSecciones = new String[]{"Bloque 1"};
                else if(this.bloque == Constants.BLOQUE2_AVANZADO5)
                    datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }
        }

        adapterBloques.addAll(datosSecciones);

        return adapterBloques;

    }

    public boolean validarSesionCorrecta(int seccionREST, int nivelREST, int bloqueREST, int seccionSelect, int nivelSelect, int bloqueSelect){
        boolean resultado = true;
        if(seccionREST == seccionSelect){
            if(nivelREST == nivelSelect){
                if(bloqueREST == bloqueSelect)
                    resultado = true;
                else
                    resultado = false;
            }else{
                resultado = false;
            }
        }else{
            resultado = false;
        }

        return  resultado;
    }

    public String minutosAHRS(int minutos){
        int horas = minutos / 60;
        int partHoras = minutos % 60;
        String partHorasTexto = "";

        if(partHoras < 10)
            partHorasTexto = "0" + String.valueOf(partHoras);
        else
            partHorasTexto = String.valueOf(partHoras);

        String horasTexto =  String.valueOf(horas) + ":" + partHorasTexto + " HRS";

        return horasTexto;
    }

    public int validarHorasRestantes(int seccion, int nivel, int bloque){

        int minutos = 0;

        if(seccion == Constants.BASICO){
            if(nivel == Constants.BASICO_1){
                if(bloque == Constants.BLOQUE1_BASICO1){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE2_BASICO1){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE3_BASICO1){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE4_BASICO1){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE5_BASICO1){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE6_BASICO1){
                    minutos = 660;
                }
            }else if(nivel == Constants.BASICO_2){
                if(bloque == Constants.BLOQUE1_BASICO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE2_BASICO2){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE3_BASICO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE4_BASICO2){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE5_BASICO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE6_BASICO2){
                    minutos = 660;
                }
            }else if(nivel == Constants.BASICO_3){
                if(bloque == Constants.BLOQUE1_BASICO3){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE2_BASICO3){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE3_BASICO3){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE4_BASICO3){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE5_BASICO3){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE6_BASICO3){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE7_BASICO3){
                    minutos = 540;
                }
            }else if(nivel == Constants.BASICO_4){
                if(bloque == Constants.BLOQUE1_BASICO4){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE2_BASICO4){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE3_BASICO4){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE4_BASICO4){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE5_BASICO4){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE6_BASICO4){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE7_BASICO4){
                    minutos = 600;
                }
            }else if(nivel == Constants.BASICO_5){
                if(bloque == Constants.BLOQUE1_BASICO5){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE2_BASICO5){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE3_BASICO5){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE4_BASICO5){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE5_BASICO5){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE6_BASICO5){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE7_BASICO5){
                    minutos = 540;
                }
            }
        }else if(seccion == Constants.INTERMEDIO){
            if(nivel == Constants.INTERMEDIO_1){
                if(bloque == Constants.BLOQUE1_INTERMEDIO1){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO1){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO1){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO1){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO1){
                    minutos = 540;
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO1){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE7_INTERMEDIO1){
                    minutos = 540;
                }
            }else if(nivel == Constants.INTERMEDIO_2){
                if(bloque == Constants.BLOQUE1_INTERMEDIO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO2){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO2){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO2){
                    minutos = 600;
                }
            }else if(nivel == Constants.INTERMEDIO_3){
                if(bloque == Constants.BLOQUE1_INTERMEDIO1){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO3){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO3){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO3){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO3){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO3){
                    minutos = 600;
                }
            }else if(nivel == Constants.INTERMEDIO_4){
                if(bloque == Constants.BLOQUE1_INTERMEDIO4){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO4){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO4){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO4){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO4){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO4){
                    minutos = 660;
                }
            }else if(nivel == Constants.INTERMEDIO_5){
                if(bloque == Constants.BLOQUE1_INTERMEDIO5){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE2_INTERMEDIO5){
                    minutos = 600;
                }else if(bloque == Constants.BLOQUE3_INTERMEDIO5){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE4_INTERMEDIO5){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE5_INTERMEDIO5){
                    minutos = 660;
                }else if(bloque == Constants.BLOQUE6_INTERMEDIO5){
                    minutos = 660;
                }
            }
        }else if(seccion == Constants.AVANZADO){
            if(nivel == Constants.AVANZADO_1){
                if(bloque == Constants.BLOQUE1_AVANZADO1){
                    minutos = 780;
                }else if(bloque == Constants.BLOQUE2_AVANZADO1){
                    minutos = 720;
                }
            }else if(nivel == Constants.AVANZADO_2){
                if(bloque == Constants.BLOQUE1_AVANZADO2){
                    minutos = 720;
                }else if(bloque == Constants.BLOQUE2_AVANZADO2){
                    minutos = 780;
                }
            }else if(nivel == Constants.AVANZADO_3){
                if(bloque == Constants.BLOQUE1_AVANZADO3){
                    minutos = 780;
                }else if(bloque == Constants.BLOQUE2_AVANZADO3){
                    minutos = 720;
                }
            }else if(nivel == Constants.AVANZADO_4){
                if(bloque == Constants.BLOQUE1_AVANZADO4){
                    minutos = 720;
                }else if(bloque == Constants.BLOQUE2_AVANZADO4){
                    minutos = 780;
                }
            }else if(nivel == Constants.AVANZADO_5){
                if(bloque == Constants.BLOQUE1_AVANZADO5){
                    minutos = 720;
                }else if(bloque == Constants.BLOQUE2_AVANZADO5){
                    minutos = 780;
                }
            }
        }

        return minutos;
    }

    public int getSeccion() {
        return seccion;
    }

    public int getNivel() {
        return nivel;
    }

    public int getBloque() {
        return bloque;
    }

}
