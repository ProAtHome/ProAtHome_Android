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
import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico6.Diagnostico6View;
import com.proathome.Presenters.cliente.examen.Diagnostico6PresenterImpl;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico6 extends AppCompatActivity implements Diagnostico6View {

    private Unbinder mUnbinder;
    private int respuesta2 = 0, respuesta3 = 0, respuesta4 = 0, respuesta5 = 0, respuesta6 = 0,
            respuesta7 = 0, respuesta8 = 0, respuesta9 = 0, respuesta10 = 0, respuesta11 = 0;
    private ProgressDialog progressDialog;
    private Diagnostico6Presenter diagnostico6Presenter;

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

        diagnostico6Presenter = new Diagnostico6PresenterImpl(this);

        cerrarExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(this,
                    R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Al salir durante el examen perderás el progreso de ésta sección.")
                    .setNegativeButton("Salir", (dialog, which) -> {
                        finish();
                    })
                    .setPositiveButton("Cancelar", ((dialog, which) -> {
                        SweetAlert.showMsg(Diagnostico6.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    }))
                    .setOnCancelListener(dialog -> {
                        SweetAlert.showMsg(Diagnostico6.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    })
                    .show();
        });

        btnContinuar.setOnClickListener(v ->{
            int puntuacion = respuesta11 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + respuesta6 +
                    respuesta7 + respuesta8 + respuesta9 + respuesta10;
            int idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
            diagnostico6Presenter.actualizarEstatusExamen(Constants.ENCURSO_EXAMEN, idCliente, puntuacion, 60);
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
    public void showProgress() {
        progressDialog = ProgressDialog.show(Diagnostico6.this, "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String mensaje) {
        Toast.makeText(Diagnostico6.this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void examenGuardado() {
        SweetAlert.showMsg(Diagnostico6.this, SweetAlert.NORMAL_TYPE, "Puntuación guardada.", "¡Continúa!", true, "OK", ()->{
            if(!Diagnostico7.ultimaPagina){
                startActivityForResult(new Intent(Diagnostico6.this, Diagnostico7.class), 1, ActivityOptions.makeSceneTransitionAnimation(Diagnostico6.this)
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
