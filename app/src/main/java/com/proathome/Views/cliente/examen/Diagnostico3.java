package com.proathome.Views.cliente.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico3.Diagnostico3View;
import com.proathome.Presenters.cliente.examen.Diagnostico3PresenterImpl;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico3 extends AppCompatActivity implements Diagnostico3View {

    private Unbinder mUnbinder;
    private int respuesta1 = 0, respuesta2 = 0, respuesta3 = 0, respuesta4 = 0, respuesta5 = 0;
    private ProgressDialog progressDialog;
    private Diagnostico3Presenter diagnostico3Presenter;

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

        diagnostico3Presenter = new Diagnostico3PresenterImpl(this);

        cerrarExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(this,
                    R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Al salir durante el examen perderás el progreso de ésta sección.")
                    .setNegativeButton("Salir", (dialog, which) -> {
                        finish();
                    })
                    .setPositiveButton("Cancelar", ((dialog, which) -> {
                        SweetAlert.showMsg(Diagnostico3.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    }))
                    .setOnCancelListener(dialog -> {
                        SweetAlert.showMsg(Diagnostico3.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    })
                    .show();
        });

        btnContinuar.setOnClickListener(v ->{
            int puntuacionSecc2 = validarSeccion2();
            int puntuacionTotal = respuesta1 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + puntuacionSecc2;
            int idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
            diagnostico3Presenter.actualizarEstatusExamen(Constants.ENCURSO_EXAMEN, idCliente, puntuacionTotal, 30);
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

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(Diagnostico3.this, "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String mensaje) {
        Toast.makeText(Diagnostico3.this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void examenGuardado() {
        SweetAlert.showMsg(Diagnostico3.this, SweetAlert.NORMAL_TYPE, "Puntuación guardada.", "¡Continúa!", true, "OK", ()->{
            if(!Diagnostico7.ultimaPagina){
                startActivityForResult(new Intent(Diagnostico3.this, Diagnostico4.class), 1, ActivityOptions.makeSceneTransitionAnimation(Diagnostico3.this)
                        .toBundle());
                finish();
            }else
                Diagnostico7.ultimaPagina = false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        mUnbinder.unbind();
    }

}
