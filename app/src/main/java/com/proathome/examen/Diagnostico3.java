package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.servicios.estudiante.ServicioExamenDiagnostico;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico3 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int respuesta1 = 0;
    private int respuesta2 = 0;
    private int respuesta3 = 0;
    private int respuesta4 = 0;
    private int respuesta5 = 0;
    private int respuestaSeccion2 = 0;
    /*Pregunta 21*/
    @BindView(R.id.chip_p1_1)
    Chip chip_p1_1;
    @BindView(R.id.chip_p1_2)
    Chip chip_p1_2;
    @BindView(R.id.chip_p1_3)
    Chip chip_p1_3;
    /*Pregunta 22*/
    @BindView(R.id.chip_p2_1)
    Chip chip_p2_1;
    @BindView(R.id.chip_p2_2)
    Chip chip_p2_2;
    @BindView(R.id.chip_p2_3)
    Chip chip_p2_3;
    /*Pregunta 23*/
    @BindView(R.id.chip_p3_1)
    Chip chip_p3_1;
    @BindView(R.id.chip_p3_2)
    Chip chip_p3_2;
    @BindView(R.id.chip_p3_3)
    Chip chip_p3_3;
    /*Pregunta 24*/
    @BindView(R.id.chip_p4_1)
    Chip chip_p4_1;
    @BindView(R.id.chip_p4_2)
    Chip chip_p4_2;
    @BindView(R.id.chip_p4_3)
    Chip chip_p4_3;
    /*Pregunta 25*/
    @BindView(R.id.chip_p5_1)
    Chip chip_p5_1;
    @BindView(R.id.chip_p5_2)
    Chip chip_p5_2;
    @BindView(R.id.chip_p5_3)
    Chip chip_p5_3;
    /*Seccion 2*/
    @BindView(R.id.r1)
    TextInputEditText res1;
    @BindView(R.id.r2)
    TextInputEditText res2;
    @BindView(R.id.r3)
    TextInputEditText res3;
    @BindView(R.id.r4)
    TextInputEditText res4;
    @BindView(R.id.r5)
    TextInputEditText res5;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico3);
        mUnbinder = ButterKnife.bind(this);

        cerrarExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(this,
                    R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Al salir durante el examen perderás el progreso de ésta sección.")
                    .setNegativeButton("Salir", (dialog, which) -> {
                        finish();
                    })
                    .setPositiveButton("Cancelar", ((dialog, which) -> {
                        salirMsg();
                    }))
                    .setOnCancelListener(dialog -> {
                        salirMsg();
                    })
                    .show();
        });

        btnContinuar.setOnClickListener(v ->{
            int puntuacionSecc2 = validarSeccion2();
            int puntuacionTotal = respuesta1 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + puntuacionSecc2;

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"sesion", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

            int idCliente = 0;
            if (fila.moveToFirst()) {
                idCliente = fila.getInt(0);
                ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this, idCliente, Diagnostico3.this,
                        Diagnostico4.class, Constants.ENCURSO_EXAMEN, puntuacionTotal, 30);
                examen.execute();
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();

        });

        chekeableChips(chip_p1_1, chip_p1_2, chip_p1_3);
        chekeableChips(chip_p1_2, chip_p1_1, chip_p1_3);
        chekeableChips(chip_p1_3, chip_p1_1, chip_p1_2);

        chekeableChips(chip_p2_1, chip_p2_2, chip_p2_3);
        chekeableChips(chip_p2_2, chip_p2_1, chip_p2_3);
        chekeableChips(chip_p2_3, chip_p2_1, chip_p2_2);

        chekeableChips(chip_p3_1, chip_p3_2, chip_p3_3);
        chekeableChips(chip_p3_2, chip_p3_1, chip_p3_3);
        chekeableChips(chip_p3_3, chip_p3_1, chip_p3_2);

        chekeableChips(chip_p4_1, chip_p4_2, chip_p4_3);
        chekeableChips(chip_p4_2, chip_p4_1, chip_p4_3);
        chekeableChips(chip_p4_3, chip_p4_1, chip_p4_2);

        chekeableChips(chip_p5_1, chip_p5_2, chip_p5_3);
        chekeableChips(chip_p5_2, chip_p5_1, chip_p5_3);
        chekeableChips(chip_p5_3, chip_p5_1, chip_p5_2);

    }

    public int validarSeccion2(){
        int puntuacion = 0;

        if(res1.getText().toString().equals("b"))
            puntuacion++;
        if(res2.getText().toString().equals("a"))
            puntuacion++;
        if(res3.getText().toString().equals("d"))
            puntuacion++;
        if(res4.getText().toString().equals("c"))
            puntuacion++;
        if(res5.getText().toString().equals("e"))
            puntuacion++;

        return puntuacion;
    }

    public void chekeableChips(Chip principal, Chip secundario, Chip terciario){
        principal.setOnClickListener(v -> {

            if(principal.getId() == R.id.chip_p1_1)
                respuesta1 = 1;
            else if(principal.getId() == R.id.chip_p1_2)
                respuesta1 = 0;
            else if(principal.getId() == R.id.chip_p1_3)
                respuesta1 = 0;

            if(principal.getId() == R.id.chip_p2_1)
                respuesta2 = 0;
            else if(principal.getId() == R.id.chip_p2_2)
                respuesta2 = 0;
            else if(principal.getId() == R.id.chip_p2_3)
                respuesta2 = 1;

            if(principal.getId() == R.id.chip_p3_1)
                respuesta3 = 0;
            else if(principal.getId() == R.id.chip_p3_2)
                respuesta3 = 1;
            else if(principal.getId() == R.id.chip_p3_3)
                respuesta3 = 0;

            if(principal.getId() == R.id.chip_p4_1)
                respuesta4 = 0;
            else if(principal.getId() == R.id.chip_p4_2)
                respuesta4 = 1;
            else if(principal.getId() == R.id.chip_p4_3)
                respuesta4 = 0;

            if(principal.getId() == R.id.chip_p5_1)
                respuesta5 = 1;
            else if(principal.getId() == R.id.chip_p5_2)
                respuesta5 = 0;
            else if(principal.getId() == R.id.chip_p5_3)
                respuesta5 = 0;

            principal.setChecked(true);
            secundario.setChecked(false);
            terciario.setChecked(false);
        });
    }

    public void salirMsg(){
        new SweetAlert(this, SweetAlert.NORMAL_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡NO TE RINDAS!")
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
