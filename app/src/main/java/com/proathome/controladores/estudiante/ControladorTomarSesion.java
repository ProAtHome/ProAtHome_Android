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
        String[] datosSecciones = new String[]{"Básico", "Intermedio", "Avanzado"};

        adapterSecciones.addAll(datosSecciones);

        return adapterSecciones;

    }

    public ArrayAdapter obtenerNiveles(){

        ArrayAdapter<String> adapterNiveles = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(this.seccion == Constants.BASICO){
            datosSecciones = new String[]{"Básico 1", "Básico 2", "Basico 3", "Básico 4", "Básico 5"};
        }else if(this.seccion == Constants.INTERMEDIO){
            datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4", "Intermedio 5"};
        }else if(this.seccion == Constants.AVANZADO){
            datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3", "Avanzado 4", "Avanzado 5"};
        }

        adapterNiveles.addAll(datosSecciones);

        return adapterNiveles;

    }

    public ArrayAdapter obtenerNiveles(int seccion){

        ArrayAdapter<String> adapterNiveles = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(seccion == Constants.BASICO){
            datosSecciones = new String[]{"Básico 1", "Básico 2", "Básico 3", "Básico 4", "Básico 5"};
        }else if(seccion == Constants.INTERMEDIO){
            datosSecciones = new String[]{"Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4", "Intermedio 5"};
        }else if(seccion == Constants.AVANZADO){
            datosSecciones = new String[]{"Avanzado 1", "Avanzado 2", "Avanzado 3", "Avanzado 4", "Avanzado 5"};
        }

        adapterNiveles.addAll(datosSecciones);

        return adapterNiveles;

    }

    public ArrayAdapter obtenerBloques(){

        ArrayAdapter<String> adapterBloques = new ArrayAdapter<String>(this.contexto, android.R.layout.simple_spinner_item);
        String[] datosSecciones = new String[]{};

        if(this.seccion == Constants.BASICO){
            if(this.nivel == Constants.BASICO_1){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.BASICO_2){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.BASICO_3){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }else if(this.nivel == Constants.BASICO_4){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }else if(this.nivel == Constants.BASICO_5){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }
        }else if(this.seccion == Constants.INTERMEDIO){
            if(this.nivel == Constants.INTERMEDIO_1){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6", "Bloque 7"};
            }else if(this.nivel == Constants.INTERMEDIO_2){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.INTERMEDIO_3){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.INTERMEDIO_4){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }else if(this.nivel == Constants.INTERMEDIO_5){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2", "Bloque 3", "Bloque 4", "Bloque 5", "Bloque 6"};
            }
        }else if(this.seccion == Constants.AVANZADO){
            if(this.nivel == Constants.AVANZADO_1){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_2){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_3){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_4){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(this.nivel == Constants.AVANZADO_5){
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
        }else if(seccion == Constants.INTERMEDIO){
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
        }else if(seccion == Constants.AVANZADO){
            if(nivel == Constants.AVANZADO_1){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(nivel == Constants.AVANZADO_2){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(nivel == Constants.AVANZADO_3){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(nivel == Constants.AVANZADO_4){
                datosSecciones = new String[]{"Bloque 1", "Bloque 2"};
            }else if(nivel == Constants.AVANZADO_5){
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
