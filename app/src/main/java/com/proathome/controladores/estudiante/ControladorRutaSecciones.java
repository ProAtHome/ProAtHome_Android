package com.proathome.controladores.estudiante;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.proathome.R;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;

public class ControladorRutaSecciones {

    private int idBloque, idNivel, idSeccion;
    private Context contexto;
    private FuncionesRutaBasico funcionesRutaBasico;

    public ControladorRutaSecciones(Context contexto, int idBloque, int idNivel, int idSeccion){
        this.idBloque = idBloque;
        this.idNivel = idNivel;
        this.idSeccion = idSeccion;
        this.contexto = contexto;
        funcionesRutaBasico = new FuncionesRutaBasico(contexto);
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
        }else if(this.idSeccion == Constants.INTERMEDIO){
            Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
            RutaFragment.btnBasico.setBackgroundColor(Color.parseColor("#9a0807"));
            RutaFragment.btnBasico.setEnabled(true);
            RutaFragment.btnBasico.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaFragment.btnIntermedio.startAnimation(shake);
            RutaFragment.btnIntermedio.setBackgroundColor(Color.parseColor("#cbccfd"));
            RutaFragment.btnIntermedio.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaFragment.btnAvanzado.setEnabled(false);
            System.out.println("Activar burbuja intermedio....");
        }else if(this.idSeccion == Constants.AVANZADO){
            System.out.println("Activar burbuja activado....");
        }
    }


    public void evaluarNivelAvanzado(){
        if(this.idNivel == Constants.AVANZADO_1){

        }else if(this.idNivel == Constants.AVANZADO_2){

        }else if(this.idNivel == Constants.AVANZADO_3){

        }
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
