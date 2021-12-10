package com.proathome.servicios.cliente;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.proathome.R;
import com.proathome.ui.RutaBasico;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;

public class ControladorRutaBasico {

    private int idBloque, idNivel, idSeccion;
    private Context contexto;
    private FuncionesRutaBasico funcionesRutaBasico;

    public ControladorRutaBasico(Context contexto, int idBloque, int idNivel, int idSeccion){
        this.idBloque = idBloque;
        this.idNivel = idNivel;
        this.idSeccion = idSeccion;
        this.contexto = contexto;
        funcionesRutaBasico = new FuncionesRutaBasico(contexto);
    }

    public void evaluarNivelBasico(){
        if(this.idSeccion == Constants.INTERMEDIO || this.idSeccion == Constants.AVANZADO){
            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();
            funcionesRutaBasico.desbloquearB5();
        }else{
            if(this.idNivel == Constants.BASICO_1){
                System.out.println("Estamos en Básico 1");
                evaluarBloqueBasico1("B1");
            }else if(this.idNivel == Constants.BASICO_2){
                System.out.println("Estamos en Básico 2");
                evaluarBloqueBasico2("B2");
            }else if(this.idNivel == Constants.BASICO_3){
                System.out.println("Estamos en Básico 3");
                evaluarBloqueBasico3("B3");
            }else if(this.idNivel == Constants.BASICO_4){
                System.out.println("Estamos en Básico 4");
                evaluarBloqueBasico4("B4");
            }else if(this.idNivel == Constants.BASICO_5){
                System.out.println("Estamos en Básico 5");
                evaluarBloqueBasico5("B5");
            }
        }
    }

    public void evaluarBloqueBasico1(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_BASICO1){
            RutaBasico.btnB1_Bloque1.startAnimation(shake);
            RutaBasico.btnB1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB1_Bloque1.setIconSize(50);

            RutaBasico.btnB1_Bloque2.setEnabled(false);
            RutaBasico.btnB1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque2.setIconSize(50);

            RutaBasico.btnB1_Bloque3.setEnabled(false);
            RutaBasico.btnB1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque3.setIconSize(50);

            RutaBasico.btnB1_Bloque4.setEnabled(false);
            RutaBasico.btnB1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque4.setIconSize(50);

            RutaBasico.btnB1_Bloque5.setEnabled(false);
            RutaBasico.btnB1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque5.setIconSize(50);

            RutaBasico.btnB1_Bloque6.setEnabled(false);
            RutaBasico.btnB1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB2_B5();

            estatus += "- Bloque 1";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 1 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_BASICO1){
            RutaBasico.btnB1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque1.setIconSize(50);

            RutaBasico.btnB1_Bloque2.startAnimation(shake);
            RutaBasico.btnB1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB1_Bloque2.setIconSize(50);

            RutaBasico.btnB1_Bloque3.setEnabled(false);
            RutaBasico.btnB1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque3.setIconSize(50);

            RutaBasico.btnB1_Bloque4.setEnabled(false);
            RutaBasico.btnB1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque4.setIconSize(50);

            RutaBasico.btnB1_Bloque5.setEnabled(false);
            RutaBasico.btnB1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque5.setIconSize(50);

            RutaBasico.btnB1_Bloque6.setEnabled(false);
            RutaBasico.btnB1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB2_B5();

            estatus += "- Bloque 2";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 2 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_BASICO1){
            RutaBasico.btnB1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque1.setIconSize(50);

            RutaBasico.btnB1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque2.setIconSize(50);

            RutaBasico.btnB1_Bloque3.startAnimation(shake);
            RutaBasico.btnB1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB1_Bloque3.setIconSize(50);

            RutaBasico.btnB1_Bloque4.setEnabled(false);
            RutaBasico.btnB1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque4.setIconSize(50);

            RutaBasico.btnB1_Bloque5.setEnabled(false);
            RutaBasico.btnB1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque5.setIconSize(50);

            RutaBasico.btnB1_Bloque6.setEnabled(false);
            RutaBasico.btnB1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB2_B5();

            estatus += "- Bloque 3";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 3 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_BASICO1){
            RutaBasico.btnB1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque1.setIconSize(50);

            RutaBasico.btnB1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque2.setIconSize(50);

            RutaBasico.btnB1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque3.setIconSize(50);

            RutaBasico.btnB1_Bloque4.startAnimation(shake);
            RutaBasico.btnB1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB1_Bloque4.setIconSize(50);

            RutaBasico.btnB1_Bloque5.setEnabled(false);
            RutaBasico.btnB1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque5.setIconSize(50);

            RutaBasico.btnB1_Bloque6.setEnabled(false);
            RutaBasico.btnB1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB2_B5();

            estatus += "- Bloque 4";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 4 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_BASICO1){
            RutaBasico.btnB1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque1.setIconSize(50);

            RutaBasico.btnB1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque2.setIconSize(50);

            RutaBasico.btnB1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque3.setIconSize(50);

            RutaBasico.btnB1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque4.setIconSize(50);

            RutaBasico.btnB1_Bloque5.startAnimation(shake);
            RutaBasico.btnB1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB1_Bloque5.setIconSize(50);

            RutaBasico.btnB1_Bloque6.setEnabled(false);
            RutaBasico.btnB1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB1_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB2_B5();

            estatus += "- Bloque 5";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 5 de Básico 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_BASICO1){
            RutaBasico.btnB1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque1.setIconSize(50);

            RutaBasico.btnB1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque2.setIconSize(50);

            RutaBasico.btnB1_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque3.setIconSize(50);

            RutaBasico.btnB1_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque4.setIconSize(50);

            RutaBasico.btnB1_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB1_Bloque5.setIconSize(50);

            RutaBasico.btnB1_Bloque6.startAnimation(shake);
            RutaBasico.btnB1_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB1_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB2_B5();

            estatus += "- Bloque 6";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 6 de Básico 1 activado.");
        }
    }

    public void evaluarBloqueBasico2(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_BASICO2){

            funcionesRutaBasico.desbloquearB1();

            RutaBasico.btnB2_Bloque1.startAnimation(shake);
            RutaBasico.btnB2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB2_Bloque1.setIconSize(50);

            RutaBasico.btnB2_Bloque2.setEnabled(false);
            RutaBasico.btnB2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque2.setIconSize(50);

            RutaBasico.btnB2_Bloque3.setEnabled(false);
            RutaBasico.btnB2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque3.setIconSize(50);

            RutaBasico.btnB2_Bloque4.setEnabled(false);
            RutaBasico.btnB2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque4.setIconSize(50);

            RutaBasico.btnB2_Bloque5.setEnabled(false);
            RutaBasico.btnB2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque5.setIconSize(50);

            RutaBasico.btnB2_Bloque6.setEnabled(false);
            RutaBasico.btnB2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB3_B5();

            estatus += "- Bloque 1";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 1 de Básico 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_BASICO2){

            funcionesRutaBasico.desbloquearB1();

            RutaBasico.btnB2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque1.setIconSize(50);

            RutaBasico.btnB2_Bloque2.startAnimation(shake);
            RutaBasico.btnB2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB2_Bloque2.setIconSize(50);

            RutaBasico.btnB2_Bloque3.setEnabled(false);
            RutaBasico.btnB2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque3.setIconSize(50);

            RutaBasico.btnB2_Bloque4.setEnabled(false);
            RutaBasico.btnB2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque4.setIconSize(50);

            RutaBasico.btnB2_Bloque5.setEnabled(false);
            RutaBasico.btnB2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque5.setIconSize(50);

            RutaBasico.btnB2_Bloque6.setEnabled(false);
            RutaBasico.btnB2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB3_B5();

            estatus += "- Bloque 2";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 2 de Básico 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_BASICO2){

            funcionesRutaBasico.desbloquearB1();

            RutaBasico.btnB2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque1.setIconSize(50);

            RutaBasico.btnB2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque2.setIconSize(50);

            RutaBasico.btnB2_Bloque3.startAnimation(shake);
            RutaBasico.btnB2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB2_Bloque3.setIconSize(50);

            RutaBasico.btnB2_Bloque4.setEnabled(false);
            RutaBasico.btnB2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque4.setIconSize(50);

            RutaBasico.btnB2_Bloque5.setEnabled(false);
            RutaBasico.btnB2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque5.setIconSize(50);

            RutaBasico.btnB2_Bloque6.setEnabled(false);
            RutaBasico.btnB2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB3_B5();

            estatus += "- Bloque 3";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 3 de Básico 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_BASICO2){

            funcionesRutaBasico.desbloquearB1();

            RutaBasico.btnB2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque1.setIconSize(50);

            RutaBasico.btnB2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque2.setIconSize(50);

            RutaBasico.btnB2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque3.setIconSize(50);

            RutaBasico.btnB2_Bloque4.startAnimation(shake);
            RutaBasico.btnB2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB2_Bloque4.setIconSize(50);

            RutaBasico.btnB2_Bloque5.setEnabled(false);
            RutaBasico.btnB2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque5.setIconSize(50);

            RutaBasico.btnB2_Bloque6.setEnabled(false);
            RutaBasico.btnB2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB3_B5();

            estatus += "- Bloque 4";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 4 de Básico 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_BASICO2){

            funcionesRutaBasico.desbloquearB1();

            RutaBasico.btnB2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque1.setIconSize(50);

            RutaBasico.btnB2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque2.setIconSize(50);

            RutaBasico.btnB2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque3.setIconSize(50);

            RutaBasico.btnB2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque4.setIconSize(50);

            RutaBasico.btnB2_Bloque5.startAnimation(shake);
            RutaBasico.btnB2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB2_Bloque5.setIconSize(50);

            RutaBasico.btnB2_Bloque6.setEnabled(false);
            RutaBasico.btnB2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB2_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB3_B5();

            estatus += "- Bloque 5";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 5 de Básico 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_BASICO2){

            funcionesRutaBasico.desbloquearB1();

            RutaBasico.btnB2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque1.setIconSize(50);

            RutaBasico.btnB2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque2.setIconSize(50);

            RutaBasico.btnB2_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque3.setIconSize(50);

            RutaBasico.btnB2_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque4.setIconSize(50);

            RutaBasico.btnB2_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB2_Bloque5.setIconSize(50);

            RutaBasico.btnB2_Bloque6.startAnimation(shake);
            RutaBasico.btnB2_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB2_Bloque6.setIconSize(50);

            funcionesRutaBasico.bloquearB3_B5();

            estatus += "- Bloque 6";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 6 de Básico 2 activado.");
        }
    }

    public void evaluarBloqueBasico3(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.startAnimation(shake);
            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.setEnabled(false);
            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.setEnabled(false);
            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.setEnabled(false);
            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.setEnabled(false);
            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.setEnabled(false);
            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.setEnabled(false);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 1";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 1 de Básico 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.startAnimation(shake);
            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.setEnabled(false);
            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.setEnabled(false);
            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.setEnabled(false);
            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.setEnabled(false);
            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.setEnabled(false);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 2";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 2 de Básico 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.startAnimation(shake);
            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.setEnabled(false);
            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.setEnabled(false);
            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.setEnabled(false);
            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.setEnabled(false);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 3";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 3 de Básico 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.startAnimation(shake);
            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.setEnabled(false);
            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.setEnabled(false);
            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.setEnabled(false);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 4";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 4 de Básico 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.startAnimation(shake);
            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.setEnabled(false);
            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.setEnabled(false);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 5";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 5 de Básico 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.startAnimation(shake);
            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.setEnabled(false);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 6";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 6 de Básico 2 activado.");
        }
        else if(this.idBloque == Constants.BLOQUE7_BASICO3){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();

            RutaBasico.btnB3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque1.setIconSize(50);

            RutaBasico.btnB3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque2.setIconSize(50);

            RutaBasico.btnB3_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque3.setIconSize(50);

            RutaBasico.btnB3_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque4.setIconSize(50);

            RutaBasico.btnB3_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque5.setIconSize(50);

            RutaBasico.btnB3_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB3_Bloque6.setIconSize(50);

            RutaBasico.btnB3_Bloque7.startAnimation(shake);
            RutaBasico.btnB3_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB3_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB4_B5();

            estatus += "- Bloque 7";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 7 de Básico 3 activado.");
        }
    }

    public void evaluarBloqueBasico4(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.startAnimation(shake);
            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.setEnabled(false);
            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.setEnabled(false);
            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.setEnabled(false);
            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.setEnabled(false);
            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.setEnabled(false);
            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.setEnabled(false);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 1";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 1 de Básico 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.startAnimation(shake);
            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.setEnabled(false);
            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.setEnabled(false);
            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.setEnabled(false);
            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.setEnabled(false);
            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.setEnabled(false);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 2";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 2 de Básico 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.startAnimation(shake);
            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.setEnabled(false);
            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.setEnabled(false);
            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.setEnabled(false);
            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.setEnabled(false);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 3";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 3 de Básico 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.startAnimation(shake);
            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.setEnabled(false);
            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.setEnabled(false);
            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.setEnabled(false);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 4";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 4 de Básico 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.startAnimation(shake);
            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.setEnabled(false);
            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.setEnabled(false);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 5";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 5 de Básico 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.startAnimation(shake);
            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.setEnabled(false);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 6";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 6 de Básico 4 activado.");
        }
        else if(this.idBloque == Constants.BLOQUE7_BASICO4){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();

            RutaBasico.btnB4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque1.setIconSize(50);

            RutaBasico.btnB4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque2.setIconSize(50);

            RutaBasico.btnB4_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque3.setIconSize(50);

            RutaBasico.btnB4_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque4.setIconSize(50);

            RutaBasico.btnB4_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque5.setIconSize(50);

            RutaBasico.btnB4_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB4_Bloque6.setIconSize(50);

            RutaBasico.btnB4_Bloque7.startAnimation(shake);
            RutaBasico.btnB4_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB4_Bloque7.setIconSize(50);

            funcionesRutaBasico.bloquearB5();

            estatus += "- Bloque 7";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 7 de Básico 4 activado.");
        }
    }

    public void evaluarBloqueBasico5(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.startAnimation(shake);
            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.setEnabled(false);
            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.setEnabled(false);
            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.setEnabled(false);
            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.setEnabled(false);
            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.setEnabled(false);
            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.setEnabled(false);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 1";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 1 de Básico 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.startAnimation(shake);
            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.setEnabled(false);
            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.setEnabled(false);
            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.setEnabled(false);
            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.setEnabled(false);
            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.setEnabled(false);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 2";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 2 de Básico 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE3_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.startAnimation(shake);
            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.setEnabled(false);
            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.setEnabled(false);
            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.setEnabled(false);
            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.setEnabled(false);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 3";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 3 de Básico 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE4_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.startAnimation(shake);
            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.setEnabled(false);
            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.setEnabled(false);
            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.setEnabled(false);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 4";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 4 de Básico 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE5_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.startAnimation(shake);
            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.setEnabled(false);
            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.setEnabled(false);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 5";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 5 de Básico 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE6_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.startAnimation(shake);
            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.setEnabled(false);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 6";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 6 de Básico 5 activado.");
        }
        else if(this.idBloque == Constants.BLOQUE7_BASICO5){

            funcionesRutaBasico.desbloquearB1();
            funcionesRutaBasico.desbloquearB2();
            funcionesRutaBasico.desbloquearB3();
            funcionesRutaBasico.desbloquearB4();

            RutaBasico.btnB5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque1.setIconSize(50);

            RutaBasico.btnB5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque2.setIconSize(50);

            RutaBasico.btnB5_Bloque3.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque3.setIconSize(50);

            RutaBasico.btnB5_Bloque4.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque4.setIconSize(50);

            RutaBasico.btnB5_Bloque5.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque5.setIconSize(50);

            RutaBasico.btnB5_Bloque6.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaBasico.btnB5_Bloque6.setIconSize(50);

            RutaBasico.btnB5_Bloque7.startAnimation(shake);
            RutaBasico.btnB5_Bloque7.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaBasico.btnB5_Bloque7.setIconSize(50);

            estatus += "- Bloque 7";
            RutaFragment.textBasico.setText(estatus);
            System.out.println("Bloque 7 de Básico 5 activado.");
        }
    }

}
