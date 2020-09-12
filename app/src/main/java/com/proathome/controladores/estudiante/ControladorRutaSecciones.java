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
        }else if(this.idSeccion == Constants.INTERMEDIO){
            Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
            RutaFragment.btnBasico.setBackgroundColor(Color.parseColor("#9a0807"));
            RutaFragment.btnBasico.setEnabled(true);
            RutaFragment.btnBasico.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaFragment.btnIntermedio.startAnimation(shake);
            RutaFragment.btnIntermedio.setBackgroundColor(Color.parseColor("#cbccfd"));
            RutaFragment.btnIntermedio.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaFragment.btnAvanzado.setEnabled(false);
        }else if(this.idSeccion == Constants.AVANZADO){
            Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
            RutaFragment.btnBasico.setBackgroundColor(Color.parseColor("#9a0807"));
            RutaFragment.btnBasico.setEnabled(true);
            RutaFragment.btnBasico.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaFragment.btnIntermedio.setBackgroundColor(Color.parseColor("#cbccfd"));
            RutaFragment.btnIntermedio.setEnabled(true);
            RutaFragment.btnIntermedio.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaFragment.btnAvanzado.startAnimation(shake);
            RutaFragment.btnAvanzado.setBackgroundColor(Color.parseColor("#6d5d60"));
            RutaFragment.btnAvanzado.setIcon(this.contexto.getDrawable(R.drawable.unlock));
        }
    }

}
