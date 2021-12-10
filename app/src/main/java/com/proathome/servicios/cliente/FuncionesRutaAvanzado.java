package com.proathome.servicios.cliente;

import android.content.Context;
import com.proathome.R;
import com.proathome.ui.RutaAvanzado;


public class FuncionesRutaAvanzado {

    private Context contexto;

    public FuncionesRutaAvanzado(Context contexto){
        this.contexto = contexto;
    }

    public void desbloquearA1(){
        RutaAvanzado.btnA1_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA1_Bloque1.setIconSize(50);

        RutaAvanzado.btnA1_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA1_Bloque2.setIconSize(50);
    }

    public void desbloquearA2(){
        RutaAvanzado.btnA2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA2_Bloque1.setIconSize(50);

        RutaAvanzado.btnA2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA2_Bloque2.setIconSize(50);
    }

    public void desbloquearA3(){
        RutaAvanzado.btnA3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA3_Bloque1.setIconSize(50);

        RutaAvanzado.btnA3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA3_Bloque2.setIconSize(50);
    }

    public void desbloquearA4(){
        RutaAvanzado.btnA4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA4_Bloque1.setIconSize(50);

        RutaAvanzado.btnA4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA4_Bloque2.setIconSize(50);
    }

    public void desbloquearA5(){
        RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA5_Bloque1.setIconSize(50);

        RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.ok));
        RutaAvanzado.btnA5_Bloque2.setIconSize(50);
    }

    public void bloquearA2_A5(){
        RutaAvanzado.btnA2_Bloque1.setEnabled(false);
        RutaAvanzado.btnA2_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA2_Bloque1.setIconSize(50);

        RutaAvanzado.btnA2_Bloque2.setEnabled(false);
        RutaAvanzado.btnA2_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA2_Bloque2.setIconSize(50);

        RutaAvanzado.btnA3_Bloque1.setEnabled(false);
        RutaAvanzado.btnA3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA3_Bloque1.setIconSize(50);

        RutaAvanzado.btnA3_Bloque2.setEnabled(false);
        RutaAvanzado.btnA3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA3_Bloque2.setIconSize(50);

        RutaAvanzado.btnA4_Bloque1.setEnabled(false);
        RutaAvanzado.btnA4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA4_Bloque1.setIconSize(50);

        RutaAvanzado.btnA4_Bloque2.setEnabled(false);
        RutaAvanzado.btnA4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA4_Bloque2.setIconSize(50);

        RutaAvanzado.btnA5_Bloque1.setEnabled(false);
        RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque1.setIconSize(50);

        RutaAvanzado.btnA5_Bloque2.setEnabled(false);
        RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque2.setIconSize(50);
    }

    public void bloquearA3_A5(){
        RutaAvanzado.btnA3_Bloque1.setEnabled(false);
        RutaAvanzado.btnA3_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA3_Bloque1.setIconSize(50);

        RutaAvanzado.btnA3_Bloque2.setEnabled(false);
        RutaAvanzado.btnA3_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA3_Bloque2.setIconSize(50);

        RutaAvanzado.btnA4_Bloque1.setEnabled(false);
        RutaAvanzado.btnA4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA4_Bloque1.setIconSize(50);

        RutaAvanzado.btnA4_Bloque2.setEnabled(false);
        RutaAvanzado.btnA4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA4_Bloque2.setIconSize(50);

        RutaAvanzado.btnA5_Bloque1.setEnabled(false);
        RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque1.setIconSize(50);

        RutaAvanzado.btnA5_Bloque2.setEnabled(false);
        RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque2.setIconSize(50);

    }

    public void bloquearA4_A5(){
        RutaAvanzado.btnA4_Bloque1.setEnabled(false);
        RutaAvanzado.btnA4_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA4_Bloque1.setIconSize(50);

        RutaAvanzado.btnA4_Bloque2.setEnabled(false);
        RutaAvanzado.btnA4_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA4_Bloque2.setIconSize(50);

        RutaAvanzado.btnA5_Bloque1.setEnabled(false);
        RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque1.setIconSize(50);

        RutaAvanzado.btnA5_Bloque2.setEnabled(false);
        RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque2.setIconSize(50);

    }

    public void bloquearA5(){
        RutaAvanzado.btnA5_Bloque1.setEnabled(false);
        RutaAvanzado.btnA5_Bloque1.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque1.setIconSize(50);

        RutaAvanzado.btnA5_Bloque2.setEnabled(false);
        RutaAvanzado.btnA5_Bloque2.setIcon(this.contexto.getDrawable(R.drawable.nivel_lock));
        RutaAvanzado.btnA5_Bloque2.setIconSize(50);

    }

}
