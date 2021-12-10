package com.proathome.servicios.cliente;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.proathome.R;
import com.proathome.ui.RutaIntermedio;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;

public class ControladorRutaIntermedio {

    private int idBloque, idNivel, idSeccion;
    private Context contexto;
    private FuncionesRutaIntermedio funcionesRutaIntermedio;

    public ControladorRutaIntermedio(Context contexto, int idBloque, int idNivel, int idSeccion){
        this.idBloque = idBloque;
        this.idNivel = idNivel;
        this.idSeccion = idSeccion;
        this.contexto = contexto;
        funcionesRutaIntermedio = new FuncionesRutaIntermedio(contexto);
    }

    public void evaluarNivelIntermedio(){
        if(this.idSeccion == Constants.AVANZADO){
            funcionesRutaIntermedio.desbloquearI1();
            funcionesRutaIntermedio.desbloquearI2();
            funcionesRutaIntermedio.desbloquearI3();
            funcionesRutaIntermedio.desbloquearI4();
            funcionesRutaIntermedio.desbloquearI5();
        }else{
            if(this.idNivel == Constants.INTERMEDIO_1){
                evaluarBloqueIntermedio1("I1");
            }else if(this.idNivel == Constants.INTERMEDIO_2){
                evaluarBloqueIntermedio2("I2");
            }else if(this.idNivel == Constants.INTERMEDIO_3){
                evaluarBloqueIntermedio3("I3");
            }else if(this.idNivel == Constants.INTERMEDIO_4){
                evaluarBloqueIntermedio4("I4");
            }else if(this.idNivel == Constants.INTERMEDIO_5){
                evaluarBloqueIntermedio5("I5");
            }
        }
    }


    public void evaluarBloqueIntermedio1(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_INTERMEDIO1){


            RutaIntermedio.btnI1_Bloque1.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.setEnabled(false);
            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.setEnabled(false);
            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.setEnabled(false);
            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.setEnabled(false);
            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.setEnabled(false);
            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.setEnabled(false);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 1";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 1 de Intermedio 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_INTERMEDIO1){

            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.setEnabled(false);
            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.setEnabled(false);
            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.setEnabled(false);
            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.setEnabled(false);
            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.setEnabled(false);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 2";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 2 de Intermedio 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_INTERMEDIO1){

            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.setEnabled(false);
            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.setEnabled(false);
            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.setEnabled(false);
            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.setEnabled(false);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 3";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 3 de Intermedio 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_INTERMEDIO1){

            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.setEnabled(false);
            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.setEnabled(false);
            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.setEnabled(false);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 4";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 4 de Intermedio 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_INTERMEDIO1){

            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.setEnabled(false);
            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.setEnabled(false);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 5";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 5 de Intermedio 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_INTERMEDIO1){

            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.setEnabled(false);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 6";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 6 de Intermedio 1 activado.");
        }
        else if(this.idBloque == Constants.BLOQUE7_INTERMEDIO1){

            RutaIntermedio.btnI1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque1.setIconSize(50);

            RutaIntermedio.btnI1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque2.setIconSize(50);

            RutaIntermedio.btnI1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque3.setIconSize(50);

            RutaIntermedio.btnI1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque4.setIconSize(50);

            RutaIntermedio.btnI1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque5.setIconSize(50);

            RutaIntermedio.btnI1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI1_Bloque6.setIconSize(50);

            RutaIntermedio.btnI1_Bloque7.startAnimation(shake);
            RutaIntermedio.btnI1_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI1_Bloque7.setIconSize(50);

            funcionesRutaIntermedio.bloquearI2_I5();

            estatus += "- Bloque 7";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 7 de Intermedio 1 activado.");
        }
    }

    public void evaluarBloqueIntermedio2(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaIntermedio.desbloquearI1();

        if(this.idBloque == Constants.BLOQUE1_INTERMEDIO2){

            funcionesRutaIntermedio.desbloquearI1();

            RutaIntermedio.btnI2_Bloque1.startAnimation(shake);
            RutaIntermedio.btnI2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI2_Bloque1.setIconSize(50);

            RutaIntermedio.btnI2_Bloque2.setEnabled(false);
            RutaIntermedio.btnI2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque2.setIconSize(50);

            RutaIntermedio.btnI2_Bloque3.setEnabled(false);
            RutaIntermedio.btnI2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque3.setIconSize(50);

            RutaIntermedio.btnI2_Bloque4.setEnabled(false);
            RutaIntermedio.btnI2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque4.setIconSize(50);

            RutaIntermedio.btnI2_Bloque5.setEnabled(false);
            RutaIntermedio.btnI2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque5.setIconSize(50);

            RutaIntermedio.btnI2_Bloque6.setEnabled(false);
            RutaIntermedio.btnI2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI3_I5();

            estatus += "- Bloque 1";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 1 de Intermedio 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_INTERMEDIO2){

            funcionesRutaIntermedio.desbloquearI1();

            RutaIntermedio.btnI2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque1.setIconSize(50);

            RutaIntermedio.btnI2_Bloque2.startAnimation(shake);
            RutaIntermedio.btnI2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI2_Bloque2.setIconSize(50);

            RutaIntermedio.btnI2_Bloque3.setEnabled(false);
            RutaIntermedio.btnI2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque3.setIconSize(50);

            RutaIntermedio.btnI2_Bloque4.setEnabled(false);
            RutaIntermedio.btnI2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque4.setIconSize(50);

            RutaIntermedio.btnI2_Bloque5.setEnabled(false);
            RutaIntermedio.btnI2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque5.setIconSize(50);

            RutaIntermedio.btnI2_Bloque6.setEnabled(false);
            RutaIntermedio.btnI2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI3_I5();

            estatus += "- Bloque 2";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 2 de Intermedio 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_INTERMEDIO2){


            RutaIntermedio.btnI2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque1.setIconSize(50);

            RutaIntermedio.btnI2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque2.setIconSize(50);

            RutaIntermedio.btnI2_Bloque3.startAnimation(shake);
            RutaIntermedio.btnI2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI2_Bloque3.setIconSize(50);

            RutaIntermedio.btnI2_Bloque4.setEnabled(false);
            RutaIntermedio.btnI2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque4.setIconSize(50);

            RutaIntermedio.btnI2_Bloque5.setEnabled(false);
            RutaIntermedio.btnI2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque5.setIconSize(50);

            RutaIntermedio.btnI2_Bloque6.setEnabled(false);
            RutaIntermedio.btnI2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI3_I5();

            estatus += "- Bloque 3";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 3 de Intermedio 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_INTERMEDIO2){

            RutaIntermedio.btnI2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque1.setIconSize(50);

            RutaIntermedio.btnI2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque2.setIconSize(50);

            RutaIntermedio.btnI2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque3.setIconSize(50);

            RutaIntermedio.btnI2_Bloque4.startAnimation(shake);
            RutaIntermedio.btnI2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI2_Bloque4.setIconSize(50);

            RutaIntermedio.btnI2_Bloque5.setEnabled(false);
            RutaIntermedio.btnI2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque5.setIconSize(50);

            RutaIntermedio.btnI2_Bloque6.setEnabled(false);
            RutaIntermedio.btnI2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI3_I5();

            estatus += "- Bloque 4";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 4 de Intermedio 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_INTERMEDIO2){

            RutaIntermedio.btnI2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque1.setIconSize(50);

            RutaIntermedio.btnI2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque2.setIconSize(50);

            RutaIntermedio.btnI2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque3.setIconSize(50);

            RutaIntermedio.btnI2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque4.setIconSize(50);

            RutaIntermedio.btnI2_Bloque5.startAnimation(shake);
            RutaIntermedio.btnI2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI2_Bloque5.setIconSize(50);

            RutaIntermedio.btnI2_Bloque6.setEnabled(false);
            RutaIntermedio.btnI2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI2_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI3_I5();

            estatus += "- Bloque 5";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 5 de Intermedio 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_INTERMEDIO2){

            RutaIntermedio.btnI2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque1.setIconSize(50);

            RutaIntermedio.btnI2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque2.setIconSize(50);

            RutaIntermedio.btnI2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque3.setIconSize(50);

            RutaIntermedio.btnI2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque4.setIconSize(50);

            RutaIntermedio.btnI2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI2_Bloque5.setIconSize(50);

            RutaIntermedio.btnI2_Bloque6.startAnimation(shake);
            RutaIntermedio.btnI2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI2_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI3_I5();

            estatus += "- Bloque 6";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 6 de Intermedio 2 activado.");
        }
    }

    public void evaluarBloqueIntermedio3(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaIntermedio.desbloquearI1();
        funcionesRutaIntermedio.desbloquearI2();

        if(this.idBloque == Constants.BLOQUE1_INTERMEDIO3){

            RutaIntermedio.btnI3_Bloque1.startAnimation(shake);
            RutaIntermedio.btnI3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI3_Bloque1.setIconSize(50);

            RutaIntermedio.btnI3_Bloque2.setEnabled(false);
            RutaIntermedio.btnI3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque2.setIconSize(50);

            RutaIntermedio.btnI3_Bloque3.setEnabled(false);
            RutaIntermedio.btnI3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque3.setIconSize(50);

            RutaIntermedio.btnI3_Bloque4.setEnabled(false);
            RutaIntermedio.btnI3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque4.setIconSize(50);

            RutaIntermedio.btnI3_Bloque5.setEnabled(false);
            RutaIntermedio.btnI3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque5.setIconSize(50);

            RutaIntermedio.btnI3_Bloque6.setEnabled(false);
            RutaIntermedio.btnI3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI4_I5();

            estatus += "- Bloque 1";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 1 de Intermedio 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_INTERMEDIO3){

            RutaIntermedio.btnI3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque1.setIconSize(50);

            RutaIntermedio.btnI3_Bloque2.startAnimation(shake);
            RutaIntermedio.btnI3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI3_Bloque2.setIconSize(50);

            RutaIntermedio.btnI3_Bloque3.setEnabled(false);
            RutaIntermedio.btnI3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque3.setIconSize(50);

            RutaIntermedio.btnI3_Bloque4.setEnabled(false);
            RutaIntermedio.btnI3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque4.setIconSize(50);

            RutaIntermedio.btnI3_Bloque5.setEnabled(false);
            RutaIntermedio.btnI3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque5.setIconSize(50);

            RutaIntermedio.btnI3_Bloque6.setEnabled(false);
            RutaIntermedio.btnI3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI4_I5();

            estatus += "- Bloque 2";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 2 de Intermedio 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_INTERMEDIO3){

            RutaIntermedio.btnI3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque1.setIconSize(50);

            RutaIntermedio.btnI3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque2.setIconSize(50);

            RutaIntermedio.btnI3_Bloque3.startAnimation(shake);
            RutaIntermedio.btnI3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI3_Bloque3.setIconSize(50);

            RutaIntermedio.btnI3_Bloque4.setEnabled(false);
            RutaIntermedio.btnI3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque4.setIconSize(50);

            RutaIntermedio.btnI3_Bloque5.setEnabled(false);
            RutaIntermedio.btnI3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque5.setIconSize(50);

            RutaIntermedio.btnI3_Bloque6.setEnabled(false);
            RutaIntermedio.btnI3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI4_I5();

            estatus += "- Bloque 3";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 3 de Intermedio 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_INTERMEDIO3){

            RutaIntermedio.btnI3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque1.setIconSize(50);

            RutaIntermedio.btnI3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque2.setIconSize(50);

            RutaIntermedio.btnI3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque3.setIconSize(50);

            RutaIntermedio.btnI3_Bloque4.startAnimation(shake);
            RutaIntermedio.btnI3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI3_Bloque4.setIconSize(50);

            RutaIntermedio.btnI3_Bloque5.setEnabled(false);
            RutaIntermedio.btnI3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque5.setIconSize(50);

            RutaIntermedio.btnI3_Bloque6.setEnabled(false);
            RutaIntermedio.btnI3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI4_I5();

            estatus += "- Bloque 4";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 4 de Intermedio 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_INTERMEDIO3){

            RutaIntermedio.btnI3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque1.setIconSize(50);

            RutaIntermedio.btnI3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque2.setIconSize(50);

            RutaIntermedio.btnI3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque3.setIconSize(50);

            RutaIntermedio.btnI3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque4.setIconSize(50);

            RutaIntermedio.btnI3_Bloque5.startAnimation(shake);
            RutaIntermedio.btnI3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI3_Bloque5.setIconSize(50);

            RutaIntermedio.btnI3_Bloque6.setEnabled(false);
            RutaIntermedio.btnI3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI3_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI4_I5();

            estatus += "- Bloque 5";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 5 de Intermedio 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_INTERMEDIO3){

            RutaIntermedio.btnI3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque1.setIconSize(50);

            RutaIntermedio.btnI3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque2.setIconSize(50);

            RutaIntermedio.btnI3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque3.setIconSize(50);

            RutaIntermedio.btnI3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque4.setIconSize(50);

            RutaIntermedio.btnI3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI3_Bloque5.setIconSize(50);

            RutaIntermedio.btnI3_Bloque6.startAnimation(shake);
            RutaIntermedio.btnI3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI3_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI4_I5();

            estatus += "- Bloque 6";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 6 de Intermedio 3 activado.");
        }
    }

    public void evaluarBloqueIntermedio4(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaIntermedio.desbloquearI1();
        funcionesRutaIntermedio.desbloquearI2();
        funcionesRutaIntermedio.desbloquearI3();

        if(this.idBloque == Constants.BLOQUE1_INTERMEDIO4){

            RutaIntermedio.btnI4_Bloque1.startAnimation(shake);
            RutaIntermedio.btnI4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI4_Bloque1.setIconSize(50);

            RutaIntermedio.btnI4_Bloque2.setEnabled(false);
            RutaIntermedio.btnI4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque2.setIconSize(50);

            RutaIntermedio.btnI4_Bloque3.setEnabled(false);
            RutaIntermedio.btnI4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque3.setIconSize(50);

            RutaIntermedio.btnI4_Bloque4.setEnabled(false);
            RutaIntermedio.btnI4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque4.setIconSize(50);

            RutaIntermedio.btnI4_Bloque5.setEnabled(false);
            RutaIntermedio.btnI4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque5.setIconSize(50);

            RutaIntermedio.btnI4_Bloque6.setEnabled(false);
            RutaIntermedio.btnI4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI5();

            estatus += "- Bloque 1";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 1 de Intermedio 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_INTERMEDIO4){

            RutaIntermedio.btnI4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque1.setIconSize(50);

            RutaIntermedio.btnI4_Bloque2.startAnimation(shake);
            RutaIntermedio.btnI4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI4_Bloque2.setIconSize(50);

            RutaIntermedio.btnI4_Bloque3.setEnabled(false);
            RutaIntermedio.btnI4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque3.setIconSize(50);

            RutaIntermedio.btnI4_Bloque4.setEnabled(false);
            RutaIntermedio.btnI4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque4.setIconSize(50);

            RutaIntermedio.btnI4_Bloque5.setEnabled(false);
            RutaIntermedio.btnI4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque5.setIconSize(50);

            RutaIntermedio.btnI4_Bloque6.setEnabled(false);
            RutaIntermedio.btnI4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI5();

            estatus += "- Bloque 2";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 2 de Intermedio 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_INTERMEDIO4){

            RutaIntermedio.btnI4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque1.setIconSize(50);

            RutaIntermedio.btnI4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque2.setIconSize(50);

            RutaIntermedio.btnI4_Bloque3.startAnimation(shake);
            RutaIntermedio.btnI4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI4_Bloque3.setIconSize(50);

            RutaIntermedio.btnI4_Bloque4.setEnabled(false);
            RutaIntermedio.btnI4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque4.setIconSize(50);

            RutaIntermedio.btnI4_Bloque5.setEnabled(false);
            RutaIntermedio.btnI4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque5.setIconSize(50);

            RutaIntermedio.btnI4_Bloque6.setEnabled(false);
            RutaIntermedio.btnI4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI5();

            estatus += "- Bloque 3";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 3 de Intermedio 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_INTERMEDIO4){

            RutaIntermedio.btnI4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque1.setIconSize(50);

            RutaIntermedio.btnI4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque2.setIconSize(50);

            RutaIntermedio.btnI4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque3.setIconSize(50);

            RutaIntermedio.btnI4_Bloque4.startAnimation(shake);
            RutaIntermedio.btnI4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI4_Bloque4.setIconSize(50);

            RutaIntermedio.btnI4_Bloque5.setEnabled(false);
            RutaIntermedio.btnI4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque5.setIconSize(50);

            RutaIntermedio.btnI4_Bloque6.setEnabled(false);
            RutaIntermedio.btnI4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI5();

            estatus += "- Bloque 4";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 4 de Intermedio 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_INTERMEDIO4){

            RutaIntermedio.btnI4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque1.setIconSize(50);

            RutaIntermedio.btnI4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque2.setIconSize(50);

            RutaIntermedio.btnI4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque3.setIconSize(50);

            RutaIntermedio.btnI4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque4.setIconSize(50);

            RutaIntermedio.btnI4_Bloque5.startAnimation(shake);
            RutaIntermedio.btnI4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI4_Bloque5.setIconSize(50);

            RutaIntermedio.btnI4_Bloque6.setEnabled(false);
            RutaIntermedio.btnI4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI4_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI5();

            estatus += "- Bloque 5";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 5 de Intermedio 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_INTERMEDIO4){

            RutaIntermedio.btnI4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque1.setIconSize(50);

            RutaIntermedio.btnI4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque2.setIconSize(50);

            RutaIntermedio.btnI4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque3.setIconSize(50);

            RutaIntermedio.btnI4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque4.setIconSize(50);

            RutaIntermedio.btnI4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI4_Bloque5.setIconSize(50);

            RutaIntermedio.btnI4_Bloque6.startAnimation(shake);
            RutaIntermedio.btnI4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI4_Bloque6.setIconSize(50);

            funcionesRutaIntermedio.bloquearI5();

            estatus += "- Bloque 6";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 6 de Intermedio 4 activado.");
        }
    }

    public void evaluarBloqueIntermedio5(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaIntermedio.desbloquearI1();
        funcionesRutaIntermedio.desbloquearI2();
        funcionesRutaIntermedio.desbloquearI3();
        funcionesRutaIntermedio.desbloquearI4();

        if(this.idBloque == Constants.BLOQUE1_INTERMEDIO5){

            RutaIntermedio.btnI5_Bloque1.startAnimation(shake);
            RutaIntermedio.btnI5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI5_Bloque1.setIconSize(50);

            RutaIntermedio.btnI5_Bloque2.setEnabled(false);
            RutaIntermedio.btnI5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque2.setIconSize(50);

            RutaIntermedio.btnI5_Bloque3.setEnabled(false);
            RutaIntermedio.btnI5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque3.setIconSize(50);

            RutaIntermedio.btnI5_Bloque4.setEnabled(false);
            RutaIntermedio.btnI5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque4.setIconSize(50);

            RutaIntermedio.btnI5_Bloque5.setEnabled(false);
            RutaIntermedio.btnI5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque5.setIconSize(50);

            RutaIntermedio.btnI5_Bloque6.setEnabled(false);
            RutaIntermedio.btnI5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque6.setIconSize(50);

            estatus += "- Bloque 1";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 1 de Intermedio 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_INTERMEDIO5){

            RutaIntermedio.btnI5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque1.setIconSize(50);

            RutaIntermedio.btnI5_Bloque2.startAnimation(shake);
            RutaIntermedio.btnI5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI5_Bloque2.setIconSize(50);

            RutaIntermedio.btnI5_Bloque3.setEnabled(false);
            RutaIntermedio.btnI5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque3.setIconSize(50);

            RutaIntermedio.btnI5_Bloque4.setEnabled(false);
            RutaIntermedio.btnI5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque4.setIconSize(50);

            RutaIntermedio.btnI5_Bloque5.setEnabled(false);
            RutaIntermedio.btnI5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque5.setIconSize(50);

            RutaIntermedio.btnI5_Bloque6.setEnabled(false);
            RutaIntermedio.btnI5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque6.setIconSize(50);

            estatus += "- Bloque 2";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 2 de Intermedio 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_INTERMEDIO5){

            RutaIntermedio.btnI5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque1.setIconSize(50);

            RutaIntermedio.btnI5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque2.setIconSize(50);

            RutaIntermedio.btnI5_Bloque3.startAnimation(shake);
            RutaIntermedio.btnI5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI5_Bloque3.setIconSize(50);

            RutaIntermedio.btnI5_Bloque4.setEnabled(false);
            RutaIntermedio.btnI5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque4.setIconSize(50);

            RutaIntermedio.btnI5_Bloque5.setEnabled(false);
            RutaIntermedio.btnI5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque5.setIconSize(50);

            RutaIntermedio.btnI5_Bloque6.setEnabled(false);
            RutaIntermedio.btnI5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque6.setIconSize(50);

            estatus += "- Bloque 3";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 3 de Intermedio 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_INTERMEDIO5){

            RutaIntermedio.btnI5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque1.setIconSize(50);

            RutaIntermedio.btnI5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque2.setIconSize(50);

            RutaIntermedio.btnI5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque3.setIconSize(50);

            RutaIntermedio.btnI5_Bloque4.startAnimation(shake);
            RutaIntermedio.btnI5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI5_Bloque4.setIconSize(50);

            RutaIntermedio.btnI5_Bloque5.setEnabled(false);
            RutaIntermedio.btnI5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque5.setIconSize(50);

            RutaIntermedio.btnI5_Bloque6.setEnabled(false);
            RutaIntermedio.btnI5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque6.setIconSize(50);

            estatus += "- Bloque 4";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 4 de Intermedio 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_INTERMEDIO5){

            RutaIntermedio.btnI5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque1.setIconSize(50);

            RutaIntermedio.btnI5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque2.setIconSize(50);

            RutaIntermedio.btnI5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque3.setIconSize(50);

            RutaIntermedio.btnI5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque4.setIconSize(50);

            RutaIntermedio.btnI5_Bloque5.startAnimation(shake);
            RutaIntermedio.btnI5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI5_Bloque5.setIconSize(50);

            RutaIntermedio.btnI5_Bloque6.setEnabled(false);
            RutaIntermedio.btnI5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaIntermedio.btnI5_Bloque6.setIconSize(50);

            estatus += "- Bloque 5";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 5 de Intermedio 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_INTERMEDIO5){

            RutaIntermedio.btnI5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque1.setIconSize(50);

            RutaIntermedio.btnI5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque2.setIconSize(50);

            RutaIntermedio.btnI5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque3.setIconSize(50);

            RutaIntermedio.btnI5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque4.setIconSize(50);

            RutaIntermedio.btnI5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaIntermedio.btnI5_Bloque5.setIconSize(50);

            RutaIntermedio.btnI5_Bloque6.startAnimation(shake);
            RutaIntermedio.btnI5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaIntermedio.btnI5_Bloque6.setIconSize(50);

            estatus += "- Bloque 6";
            RutaFragment.textIntermedio.setText(estatus);
            System.out.println("Bloque 6 de Intermedio 5 activado.");
        }
    }

}
