package com.proathome.controladores.estudiante;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.proathome.R;
import com.proathome.RutaAvanzado;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;

public class ControladorRutaAvanzado {

    private int idBloque, idNivel, idSeccion;
    private Context contexto;
    private FuncionesRutaAvanzado funcionesRutaAvanzado;

    public ControladorRutaAvanzado(Context contexto, int idBloque, int idNivel, int idSeccion){
        this.idBloque = idBloque;
        this.idNivel = idNivel;
        this.idSeccion = idSeccion;
        this.contexto = contexto;
        funcionesRutaAvanzado = new FuncionesRutaAvanzado(contexto);
    }

    public void evaluarNivelAvanzado(){

            if(this.idNivel == Constants.AVANZADO_1){
                evaluarBloqueAvanzado1("A1");
            }else if(this.idNivel == Constants.AVANZADO_2){
                evaluarBloqueAvanzado2("A2");
            }else if(this.idNivel == Constants.AVANZADO_3){
                evaluarBloqueAvanzado3("A3");
            }else if(this.idNivel == Constants.AVANZADO_4){
                evaluarBloqueAvanzado4("A4");
            }else if(this.idNivel == Constants.AVANZADO_5){
                evaluarBloqueAvanzado5("A5");
            }

    }


    public void evaluarBloqueAvanzado1(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        if(this.idBloque == Constants.BLOQUE1_AVANZADO1){

            RutaAvanzado.btnA1_Bloque1.startAnimation(shake);
            RutaAvanzado.btnA1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA1_Bloque1.setIconSize(50);

            RutaAvanzado.btnA1_Bloque2.setEnabled(false);
            RutaAvanzado.btnA1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaAvanzado.btnA1_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA2_A5();

            estatus += "- Bloque 1";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 1 de Avanzado 1 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_AVANZADO1){

            RutaAvanzado.btnA1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaAvanzado.btnA1_Bloque1.setIconSize(50);

            RutaAvanzado.btnA1_Bloque2.startAnimation(shake);
            RutaAvanzado.btnA1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA1_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA2_A5();

            estatus += "- Bloque 2";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 2 de Avanzado 1 activado.");
        }

    }

    public void evaluarBloqueAvanzado2(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaAvanzado.desbloquearA1();

        if(this.idBloque == Constants.BLOQUE1_AVANZADO2){

            funcionesRutaAvanzado.desbloquearA1();

            RutaAvanzado.btnA2_Bloque1.startAnimation(shake);
            RutaAvanzado.btnA2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA2_Bloque1.setIconSize(50);

            RutaAvanzado.btnA2_Bloque2.setEnabled(false);
            RutaAvanzado.btnA2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaAvanzado.btnA2_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA3_A5();

            estatus += "- Bloque 1";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 1 de Avanzado 2 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_AVANZADO2){

            funcionesRutaAvanzado.desbloquearA1();

            RutaAvanzado.btnA2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaAvanzado.btnA2_Bloque1.setIconSize(50);

            RutaAvanzado.btnA2_Bloque2.startAnimation(shake);
            RutaAvanzado.btnA2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA2_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA3_A5();

            estatus += "- Bloque 2";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 2 de Avanzado 2 activado.");
        }
    }

    public void evaluarBloqueAvanzado3(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaAvanzado.desbloquearA1();
        funcionesRutaAvanzado.desbloquearA2();

        if(this.idBloque == Constants.BLOQUE1_AVANZADO3){

            RutaAvanzado.btnA3_Bloque1.startAnimation(shake);
            RutaAvanzado.btnA3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA3_Bloque1.setIconSize(50);

            RutaAvanzado.btnA3_Bloque2.setEnabled(false);
            RutaAvanzado.btnA3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaAvanzado.btnA3_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA4_A5();

            estatus += "- Bloque 1";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 1 de Avanzado 3 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_AVANZADO3){

            RutaAvanzado.btnA3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaAvanzado.btnA3_Bloque1.setIconSize(50);

            RutaAvanzado.btnA3_Bloque2.startAnimation(shake);
            RutaAvanzado.btnA3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA3_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA4_A5();

            estatus += "- Bloque 2";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 2 de Avanzado 3 activado.");
        }

    }

    public void evaluarBloqueAvanzado4(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaAvanzado.desbloquearA1();
        funcionesRutaAvanzado.desbloquearA2();
        funcionesRutaAvanzado.desbloquearA3();

        if(this.idBloque == Constants.BLOQUE1_AVANZADO4){

            RutaAvanzado.btnA4_Bloque1.startAnimation(shake);
            RutaAvanzado.btnA4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA4_Bloque1.setIconSize(50);

            RutaAvanzado.btnA4_Bloque2.setEnabled(false);
            RutaAvanzado.btnA4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaAvanzado.btnA4_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA5();

            estatus += "- Bloque 1";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 1 de Avanzado 4 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_AVANZADO4){

            RutaAvanzado.btnA4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaAvanzado.btnA4_Bloque1.setIconSize(50);

            RutaAvanzado.btnA4_Bloque2.startAnimation(shake);
            RutaAvanzado.btnA4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA4_Bloque2.setIconSize(50);

            funcionesRutaAvanzado.bloquearA5();

            estatus += "- Bloque 2";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 2 de Avanzado 4 activado.");
        }
    }

    public void evaluarBloqueAvanzado5(String estatus){
        Animation shake = AnimationUtils.loadAnimation(this.contexto, R.anim.shake);
        funcionesRutaAvanzado.desbloquearA1();
        funcionesRutaAvanzado.desbloquearA2();
        funcionesRutaAvanzado.desbloquearA3();
        funcionesRutaAvanzado.desbloquearA4();

        if(this.idBloque == Constants.BLOQUE1_AVANZADO5){

            RutaAvanzado.btnA5_Bloque1.startAnimation(shake);
            RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA5_Bloque1.setIconSize(50);

            RutaAvanzado.btnA5_Bloque2.setEnabled(false);
            RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
            RutaAvanzado.btnA5_Bloque2.setIconSize(50);

            estatus += "- Bloque 1";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 1 de Avanzado 5 activado.");
        }else if(this.idBloque == Constants.BLOQUE2_AVANZADO5){

            RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
            RutaAvanzado.btnA5_Bloque1.setIconSize(50);

            RutaAvanzado.btnA5_Bloque2.startAnimation(shake);
            RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.unlock));
            RutaAvanzado.btnA5_Bloque2.setIconSize(50);

            estatus += "- Bloque 2";
            RutaFragment.textAvanzado.setText(estatus);
            System.out.println("Bloque 2 de Avanzado 5 activado.");
        }
    }

}
