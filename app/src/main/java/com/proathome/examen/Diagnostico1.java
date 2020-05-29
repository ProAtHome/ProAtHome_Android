package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.proathome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Diagnostico1 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private String respuesta1 = "";
    @BindView(R.id.pregunta1_text)
    TextInputEditText pregunta1;
    @BindView(R.id.pregunta1)
    TextInputLayout pregunta1Config;
    @BindView(R.id.chip_p1_1)
    Chip chip_p1_1;
    @BindView(R.id.chip_p1_2)
    Chip chip_p1_2;
    @BindView(R.id.chip_p1_3)
    Chip chip_p1_3;
    @BindView(R.id.chip_p1_4)
    Chip chip_p1_4;
    @BindView(R.id.chip_p1_5)
    Chip chip_p1_5;
    @BindView(R.id.chip_p1_6)
    Chip chip_p1_6;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico1);
        mUnbinder = ButterKnife.bind(this);

        btnContinuar.setOnClickListener(v ->{
            Intent intent = new Intent(this, Diagnostico2.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        pregunta1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta1.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta1 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta1.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta1 = "";
            }
        });

    }

    @OnClick({R.id.chip_p1_1, R.id.chip_p1_2, R.id.chip_p1_3, R.id.chip_p1_4, R.id.chip_p1_5, R.id.chip_p1_6})
    public void onClickChipPregunta1(View view){
        switch (view.getId()){
            case R.id.chip_p1_1:
                respuesta1 += chip_p1_1.getText() + " ";
                pregunta1.setText(respuesta1);
                break;
            case R.id.chip_p1_2:
                respuesta1 += chip_p1_2.getText() + " ";
                pregunta1.setText(respuesta1);
                break;
            case R.id.chip_p1_3:
                respuesta1 += chip_p1_3.getText() + " ";
                pregunta1.setText(respuesta1);
                break;
            case R.id.chip_p1_4:
                respuesta1 += chip_p1_4.getText() + " ";
                pregunta1.setText(respuesta1);
                break;
            case R.id.chip_p1_5:
                respuesta1 += chip_p1_5.getText() + " ";
                pregunta1.setText(respuesta1);
                break;
            case R.id.chip_p1_6:
                respuesta1 += chip_p1_6.getText() + " ";
                pregunta1.setText(respuesta1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
