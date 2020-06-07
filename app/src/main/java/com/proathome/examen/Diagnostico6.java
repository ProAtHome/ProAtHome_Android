package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico6 extends AppCompatActivity {

    private Unbinder mUnbinder;
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
            finish();
        });

        btnContinuar.setOnClickListener(v ->{
            Intent intent = new Intent(this, Diagnostico7.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
