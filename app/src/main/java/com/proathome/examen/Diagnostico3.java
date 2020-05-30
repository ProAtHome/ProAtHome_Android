package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.proathome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico3 extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.chip_p1_1)
    Chip chip_p1_1;
    @BindView(R.id.chip_p1_2)
    Chip chip_p1_2;
    @BindView(R.id.chip_p1_3)
    Chip chip_p1_3;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico3);
        mUnbinder = ButterKnife.bind(this);

        btnContinuar.setOnClickListener(v ->{
            Intent intent = new Intent(this, Diagnostico4.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        chekeableChips(chip_p1_1, chip_p1_2, chip_p1_3);
        chekeableChips(chip_p1_2, chip_p1_1, chip_p1_3);
        chekeableChips(chip_p1_3, chip_p1_1, chip_p1_2);

    }

    public void chekeableChips(Chip principal, Chip secundario, Chip terciario){
        principal.setOnClickListener(v -> {
            principal.setChecked(true);
            secundario.setChecked(false);
            terciario.setChecked(false);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
