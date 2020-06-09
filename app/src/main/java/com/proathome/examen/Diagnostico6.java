package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico6 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int respuesta2 = 0;
    private int respuesta3 = 0;
    private int respuesta4 = 0;
    private int respuesta5 = 0;
    private int respuesta6 = 0;
    private int respuesta7 = 0;
    private int respuesta8 = 0;
    private int respuesta9 = 0;
    private int respuesta10 = 0;
    private  int respuesta11 = 0;
    @BindView(R.id.chip_r2_1)
    Chip chip_r2_1;
    @BindView(R.id.chip_r2_2)
    Chip chip_r2_2;
    @BindView(R.id.chip_r3_1)
    Chip chip_r3_1;
    @BindView(R.id.chip_r3_2)
    Chip chip_r3_2;
    @BindView(R.id.chip_r4_1)
    Chip chip_r4_1;
    @BindView(R.id.chip_r4_2)
    Chip chip_r4_2;
    @BindView(R.id.chip_r5_1)
    Chip chip_r5_1;
    @BindView(R.id.chip_r5_2)
    Chip chip_r5_2;
    @BindView(R.id.chip_r6_1)
    Chip chip_r6_1;
    @BindView(R.id.chip_r6_2)
    Chip chip_r6_2;
    @BindView(R.id.chip_r7_1)
    Chip chip_r7_1;
    @BindView(R.id.chip_r7_2)
    Chip chip_r7_2;
    @BindView(R.id.chip_r8_1)
    Chip chip_r8_1;
    @BindView(R.id.chip_r8_2)
    Chip chip_r8_2;
    @BindView(R.id.chip_r9_1)
    Chip chip_r9_1;
    @BindView(R.id.chip_r9_2)
    Chip chip_r9_2;
    @BindView(R.id.chip_r10_1)
    Chip chip_r10_1;
    @BindView(R.id.chip_r10_2)
    Chip chip_r10_2;
    @BindView(R.id.chip_r11_1)
    Chip chip_r11_1;
    @BindView(R.id.chip_r11_2)
    Chip chip_r11_2;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico6);
        mUnbinder = ButterKnife.bind(this);

        cerrarExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Al salir durante el examen perderás el progreso de ésta sección.")
                    .setNegativeButton("Salir", (dialog, which) -> {
                        finish();
                    })
                    .setPositiveButton("Cancelar", ((dialog, which) -> {
                        Toast.makeText(this, "¡No te rindas!", Toast.LENGTH_LONG);
                    }))
                    .setOnCancelListener(dialog -> {
                        Toast.makeText(this, "¡No te rindas!", Toast.LENGTH_LONG);
                    })
                    .show();
        });

        btnContinuar.setOnClickListener(v ->{
            int puntuacion = respuesta11 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + respuesta6 + respuesta7 + respuesta8 + respuesta9+
                    respuesta10;
            Toast.makeText(this, "Puntuación: " + puntuacion, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Diagnostico7.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        checkeableChips(chip_r2_1, chip_r2_2);
        checkeableChips(chip_r2_2, chip_r2_1);

        checkeableChips(chip_r3_1, chip_r3_2);
        checkeableChips(chip_r3_2, chip_r3_1);

        checkeableChips(chip_r4_1, chip_r4_2);
        checkeableChips(chip_r4_2, chip_r4_1);

        checkeableChips(chip_r5_1, chip_r5_2);
        checkeableChips(chip_r5_2, chip_r5_1);

        checkeableChips(chip_r6_1, chip_r6_2);
        checkeableChips(chip_r6_2, chip_r6_1);

        checkeableChips(chip_r7_1, chip_r7_2);
        checkeableChips(chip_r7_2, chip_r7_1);

        checkeableChips(chip_r8_1, chip_r8_2);
        checkeableChips(chip_r8_2, chip_r8_1);

        checkeableChips(chip_r9_1, chip_r9_2);
        checkeableChips(chip_r9_2, chip_r9_1);

        checkeableChips(chip_r10_1, chip_r10_2);
        checkeableChips(chip_r10_2, chip_r10_1);

        checkeableChips(chip_r11_1, chip_r11_2);
        checkeableChips(chip_r11_2, chip_r11_1);

    }

    public void checkeableChips(Chip principal, Chip secundario){
        principal.setOnClickListener(v ->{

            if(principal.getId() == R.id.chip_r2_1)
                respuesta2 = 0;
            else if(principal.getId() == R.id.chip_r2_2)
                respuesta2 = 1;

            if(principal.getId() == R.id.chip_r3_1)
                respuesta3 = 1;
            else if(principal.getId() == R.id.chip_r3_2)
                respuesta3 = 0;

            if(principal.getId() == R.id.chip_r4_1)
                respuesta4 = 1;
            else if(principal.getId() == R.id.chip_r4_2)
                respuesta4 = 0;

            if(principal.getId() == R.id.chip_r5_1)
                respuesta5 = 0;
            else if(principal.getId() == R.id.chip_r5_2)
                respuesta5 = 1;

            if(principal.getId() == R.id.chip_r6_1)
                respuesta6 = 0;
            else if(principal.getId() == R.id.chip_r6_2)
                respuesta6 = 1;

            if(principal.getId() == R.id.chip_r7_1)
                respuesta7 = 1;
            else if(principal.getId() == R.id.chip_r7_2)
                respuesta7 = 0;

            if(principal.getId() == R.id.chip_r8_1)
                respuesta8 = 0;
            else if(principal.getId() == R.id.chip_r8_2)
                respuesta8 = 1;

            if(principal.getId() == R.id.chip_r9_1)
                respuesta9 = 0;
            else if(principal.getId() == R.id.chip_r9_2)
                respuesta9 = 1;

            if(principal.getId() == R.id.chip_r10_1)
                respuesta10 = 0;
            else if(principal.getId() == R.id.chip_r10_2)
                respuesta10 = 1;

            if(principal.getId() == R.id.chip_r11_1)
                respuesta11 = 1;
            else if(principal.getId() == R.id.chip_r11_2)
                respuesta11 = 0;

            principal.setChecked(true);
            secundario.setChecked(false);

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
