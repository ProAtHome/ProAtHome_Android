package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioExamenDiagnostico;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico5 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int respuesta1 = 0;
    private int respuesta2 = 0;
    private int respuesta3 = 0;
    private int respuesta4 = 0;
    private int respuesta5 = 0;
    /*Primera sección¨*/
    @BindView(R.id.resp1)
    TextInputEditText r1;
    @BindView(R.id.resp2)
    TextInputEditText r2;
    @BindView(R.id.resp3)
    TextInputEditText r3;
    @BindView(R.id.resp4)
    TextInputEditText r4;
    @BindView(R.id.resp5)
    TextInputEditText r5;
    /*Segunda sección*/
    @BindView(R.id.chip_p1_1)
    Chip chip_p1_1;
    @BindView(R.id.chip_p1_2)
    Chip chip_p1_2;
    @BindView(R.id.chip_p2_1)
    Chip chip_p2_1;
    @BindView(R.id.chip_p2_2)
    Chip chip_p2_2;
    @BindView(R.id.chip_p3_1)
    Chip chip_p3_1;
    @BindView(R.id.chip_p3_2)
    Chip chip_p3_2;
    @BindView(R.id.chip_p4_1)
    Chip chip_p4_1;
    @BindView(R.id.chip_p4_2)
    Chip chip_p4_2;
    @BindView(R.id.chip_p5_1)
    Chip chip_p5_1;
    @BindView(R.id.chip_p5_2)
    Chip chip_p5_2;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico5);
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
            int puntuacion = respuesta1 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + evaluarParte1();
            Toast.makeText(this, "Puntuación: " + puntuacion, Toast.LENGTH_LONG).show();

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"sesion", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

            int idCliente = 0;
            if (fila.moveToFirst()) {
                idCliente = fila.getInt(0);
                ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this, idCliente, Constants.ENCURSO_EXAMEN, puntuacion, 50);
                examen.execute();
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();

            Intent intent = new Intent(this, Diagnostico6.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        checkeableChips(chip_p1_1, chip_p1_2);
        checkeableChips(chip_p1_2, chip_p1_1);

        checkeableChips(chip_p2_1, chip_p2_2);
        checkeableChips(chip_p2_2, chip_p2_1);

        checkeableChips(chip_p3_1, chip_p3_2);
        checkeableChips(chip_p3_2, chip_p3_1);

        checkeableChips(chip_p4_1, chip_p4_2);
        checkeableChips(chip_p4_2, chip_p4_1);

        checkeableChips(chip_p5_1, chip_p5_2);
        checkeableChips(chip_p5_2, chip_p5_1);

    }

    public int evaluarParte1(){
        int puntuacion = 0;
        if(r1.getText().toString().trim().equalsIgnoreCase("To invite"))
            puntuacion++;
        if(r2.getText().toString().trim().equalsIgnoreCase("To get"))
            puntuacion++;
        if(r3.getText().toString().trim().equalsIgnoreCase("To dance"))
            puntuacion++;
        if(r4.getText().toString().trim().equalsIgnoreCase("Doing"))
            puntuacion++;
        if(r5.getText().toString().trim().equalsIgnoreCase("Being"))
            puntuacion++;

        return puntuacion;
    }

    public void checkeableChips(Chip principal, Chip secundario){
        principal.setOnClickListener(v ->{

            if (principal.getId() == R.id.chip_p1_1)
                respuesta1 = 0;
            else if(principal.getId() == R.id.chip_p1_2)
                respuesta1 = 1;

            if (principal.getId() == R.id.chip_p2_1)
                respuesta2 = 0;
            else if(principal.getId() == R.id.chip_p2_2)
                respuesta2 = 1;

            if (principal.getId() == R.id.chip_p3_1)
                respuesta3 = 0;
            else if(principal.getId() == R.id.chip_p3_2)
                respuesta3 = 1;

            if (principal.getId() == R.id.chip_p4_1)
                respuesta4 = 1;
            else if(principal.getId() == R.id.chip_p4_2)
                respuesta4 = 0;

            if (principal.getId() == R.id.chip_p5_1)
                respuesta5 = 1;
            else if(principal.getId() == R.id.chip_p5_2)
                respuesta5 = 0;

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
