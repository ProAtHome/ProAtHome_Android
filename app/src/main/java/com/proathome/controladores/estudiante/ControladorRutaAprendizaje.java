package com.proathome.controladores.estudiante;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.proathome.R;
import com.proathome.RutaAvanzado;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;

public class ControladorRutaAprendizaje {

    private int idBloque, idNivel, idSeccion;
    private Context contexto;

    public ControladorRutaAprendizaje(Context contexto, int idBloque, int idNivel, int idSeccion){
        this.idBloque = idBloque;
        this.idNivel = idNivel;
        this.idSeccion = idSeccion;
        this.contexto = contexto;
    }

    public void evaluarRuta(){
        if(this.idSeccion == Constants.BASICO){
            Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
            RutaFragment.btnBasico.startAnimation(shake);
            RutaFragment.btnBasico.setBackgroundColor(Color.parseColor("#9a0807"));
            RutaFragment.btnBasico.setIconSize(100);
            RutaFragment.btnIntermedio.setEnabled(false);
            RutaFragment.btnAvanzado.setEnabled(false);
            System.out.println("Activar burbuja basico....");
            evaluarNivelBasico();
        }else if(this.idSeccion == Constants.INTERMEDIO){
            System.out.println("Activar burbuja intermedio....");
            evaluarNivelIntermedio();
        }else if(this.idSeccion == Constants.AVANZADO){
            System.out.println("Activar burbuja activado....");
            evaluarNivelAvanzado();
        }
    }

    public void evaluarNivelBasico(){
        if(this.idNivel == Constants.BASICO_1){
            System.out.println("Estamos en Básico 1");
            evaluarBloqueBasico1("B1");
        }else if(this.idNivel == Constants.BASICO_2){
            System.out.println("Estamos en Básico 2");
            evaluarBloqueBasico2();
        }else if(this.idNivel == Constants.BASICO_3){
            System.out.println("Estamos en Básico 3");
            evaluarBloqueBasico3();
        }
    }

    public void evaluarNivelIntermedio(){
        if(this.idNivel == Constants.INTERMEDIO_1){

        }else if(this.idNivel == Constants.INTERMEDIO_2){

        }else if(this.idNivel == Constants.INTERMEDIO_3){

        }
    }

    public void evaluarNivelAvanzado(){
        if(this.idNivel == Constants.AVANZADO_1){

        }else if(this.idNivel == Constants.AVANZADO_2){

        }else if(this.idNivel == Constants.AVANZADO_3){

        }
    }

    public void evaluarBloqueBasico1(String estatus){
        if(this.idBloque == Constants.BLOQUE1_BASICO1){
            estatus += "- Bloque 1";
            System.out.println(estatus);
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 1 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_BASICO1){
            estatus += "- Bloque 2";
            System.out.println(estatus);
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 2 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_BASICO1){
            estatus += "- Bloque 3";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 3 de Básico 1 activado.");
        }
    }

    public void evaluarBloqueBasico2(){

    }

    public void evaluarBloqueBasico3(){

    }

    public void evaluarBloqueBasico4(){

    }

    public void evaluarBloqueBasico5(){

    }

    public void evaluarBloqueIntermedio1(){

    }

    public void evaluarBloqueIntermedio2(){

    }

    public void evaluarBloqueIntermedio3(){

    }

    public void evaluarBloqueIntermedio4(){

    }

    public void evaluarBloqueIntermedio5(){

    }

    public void evaluarBloqueAvanzado1(){

    }

    public void evaluarBloqueAvanzado2(){

    }

    public void evaluarBloqueAvanzado3(){

    }

    public void evaluarBloqueAvanzado4(){

    }

    public void evaluarBloqueAvanzado5(){

    }

}
