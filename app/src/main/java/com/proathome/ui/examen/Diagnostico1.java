package com.proathome.ui.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ServiciosExamenDiagnostico;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Diagnostico1 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private String respuesta1 = "";
    private String respuesta2 = "";
    private String respuesta3 = "";
    private String respuesta4 = "";
    private String respuesta5 = "";
    private String respuesta6 = "";
    private String respuesta7 = "";
    private String respuesta8 = "";
    private String respuesta9 = "";
    private String respuesta10 = "";
    @BindView(R.id.pregunta1_text)
    TextInputEditText pregunta1;
    @BindView(R.id.pregunta2_text)
    TextInputEditText pregunta2;
    @BindView(R.id.pregunta3_text)
    TextInputEditText pregunta3;
    @BindView(R.id.pregunta4_text)
    TextInputEditText pregunta4;
    @BindView(R.id.pregunta5_text)
    TextInputEditText pregunta5;
    @BindView(R.id.pregunta6_text)
    TextInputEditText pregunta6;
    @BindView(R.id.pregunta7_text)
    TextInputEditText pregunta7;
    @BindView(R.id.pregunta8_text)
    TextInputEditText pregunta8;
    @BindView(R.id.pregunta9_text)
    TextInputEditText pregunta9;
    @BindView(R.id.pregunta10_text)
    TextInputEditText pregunta10;
    /*Pregunta 1*/
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
    /*Pregunta 2*/
    @BindView(R.id.chip_p2_1)
    Chip chip_p2_1;
    @BindView(R.id.chip_p2_2)
    Chip chip_p2_2;
    @BindView(R.id.chip_p2_3)
    Chip chip_p2_3;
    @BindView(R.id.chip_p2_4)
    Chip chip_p2_4;
    @BindView(R.id.chip_p2_5)
    Chip chip_p2_5;
    @BindView(R.id.chip_p2_6)
    Chip chip_p2_6;
    @BindView(R.id.chip_p2_7)
    Chip chip_p2_7;
    /*Prergunta 3*/
    @BindView(R.id.chip_p3_1)
    Chip chip_p3_1;
    @BindView(R.id.chip_p3_2)
    Chip chip_p3_2;
    @BindView(R.id.chip_p3_3)
    Chip chip_p3_3;
    @BindView(R.id.chip_p3_4)
    Chip chip_p3_4;
    @BindView(R.id.chip_p3_5)
    Chip chip_p3_5;
    /* Pregunta 4*/
    @BindView(R.id.chip_p4_1)
    Chip chip_p4_1;
    @BindView(R.id.chip_p4_2)
    Chip chip_p4_2;
    @BindView(R.id.chip_p4_3)
    Chip chip_p4_3;
    @BindView(R.id.chip_p4_4)
    Chip chip_p4_4;
    @BindView(R.id.chip_p4_5)
    Chip chip_p4_5;
    @BindView(R.id.chip_p4_6)
    Chip chip_p4_6;
    @BindView(R.id.chip_p4_7)
    Chip chip_p4_7;
    @BindView(R.id.chip_p4_8)
    Chip chip_p4_8;
    /*Pregunta 5*/
    @BindView(R.id.chip_p5_1)
    Chip chip_p5_1;
    @BindView(R.id.chip_p5_2)
    Chip chip_p5_2;
    @BindView(R.id.chip_p5_3)
    Chip chip_p5_3;
    @BindView(R.id.chip_p5_4)
    Chip chip_p5_4;
    @BindView(R.id.chip_p5_5)
    Chip chip_p5_5;
    @BindView(R.id.chip_p5_6)
    Chip chip_p5_6;
    /*Pregunta 6*/
    @BindView(R.id.chip_p6_1)
    Chip chip_p6_1;
    @BindView(R.id.chip_p6_2)
    Chip chip_p6_2;
    @BindView(R.id.chip_p6_3)
    Chip chip_p6_3;
    @BindView(R.id.chip_p6_4)
    Chip chip_p6_4;
    @BindView(R.id.chip_p6_5)
    Chip chip_p6_5;
    @BindView(R.id.chip_p6_6)
    Chip chip_p6_6;
    /*Pregunta 7*/
    @BindView(R.id.chip_p7_1)
    Chip chip_p7_1;
    @BindView(R.id.chip_p7_2)
    Chip chip_p7_2;
    @BindView(R.id.chip_p7_3)
    Chip chip_p7_3;
    @BindView(R.id.chip_p7_4)
    Chip chip_p7_4;
    @BindView(R.id.chip_p7_5)
    Chip chip_p7_5;
    @BindView(R.id.chip_p7_6)
    Chip chip_p7_6;
    @BindView(R.id.chip_p7_7)
    Chip chip_p7_7;
    /*Pregunta 8*/
    @BindView(R.id.chip_p8_1)
    Chip chip_p8_1;
    @BindView(R.id.chip_p8_2)
    Chip chip_p8_2;
    @BindView(R.id.chip_p8_3)
    Chip chip_p8_3;
    @BindView(R.id.chip_p8_4)
    Chip chip_p8_4;
    /*Pregunta 9*/
    @BindView(R.id.chip_p9_1)
    Chip chip_p9_1;
    @BindView(R.id.chip_p9_2)
    Chip chip_p9_2;
    @BindView(R.id.chip_p9_3)
    Chip chip_p9_3;
    @BindView(R.id.chip_p9_4)
    Chip chip_p9_4;
    @BindView(R.id.chip_p9_5)
    Chip chip_p9_5;
    @BindView(R.id.chip_p9_6)
    Chip chip_p9_6;
    @BindView(R.id.chip_p9_7)
    Chip chip_p9_7;
    @BindView(R.id.chip_p9_8)
    Chip chip_p9_8;
    /*Pregunta 10*/
    @BindView(R.id.chip_p10_1)
    Chip chip_p10_1;
    @BindView(R.id.chip_p10_2)
    Chip chip_p10_2;
    @BindView(R.id.chip_p10_3)
    Chip chip_p10_3;
    @BindView(R.id.chip_p10_4)
    Chip chip_p10_4;
    @BindView(R.id.chip_p10_5)
    Chip chip_p10_5;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico1);
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
                        SweetAlert.showMsg(Diagnostico1.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    }))
                    .setOnCancelListener(dialog -> {
                        SweetAlert.showMsg(Diagnostico1.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    })
                    .show();
        });

        btnContinuar.setOnClickListener(v ->{
            int puntuacion = 0;
            if(respuesta1.equals("What do you like reading ? ")){
                puntuacion++;
            }
            if(respuesta2.equalsIgnoreCase("Where do you go on holidays ? ")){
                puntuacion++;
            }
            if(respuesta3.equalsIgnoreCase("She is my favourite aunt ")){
                puntuacion++;
            }
            if(respuesta4.equalsIgnoreCase("How often do you practice your English ? ")){
                puntuacion++;
            }
            if(respuesta5.equalsIgnoreCase("I never eat on expensive restaurants ")){
                puntuacion++;
            }
            if(respuesta6.equalsIgnoreCase("Which one do you prefer ? ")){
                puntuacion++;
            }
            if(respuesta7.equalsIgnoreCase("He does not study for his tests ")){
                puntuacion++;
            }
            if(respuesta8.equalsIgnoreCase("Engineers rarely study Anatomy ")){
                puntuacion++;
            }
            if(respuesta9.equalsIgnoreCase("They always visit the same place in NY ")){
                puntuacion++;
            }
            if(respuesta10.equalsIgnoreCase("They are always on time ")){
                puntuacion++;
            }

            int idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
            ServiciosExamenDiagnostico.inicioExamen(idCliente, puntuacion, 10, Diagnostico1.this, Diagnostico1.this, Diagnostico2.class);
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

        pregunta2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta2.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta2 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta2.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta2 = "";
            }
        });

        pregunta3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta3.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta3 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta3.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta3 = "";
            }
        });

        pregunta4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta4.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta4 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta4.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta4 = "";
            }
        });

        pregunta5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta5.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta5 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta5.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta5 = "";
            }
        });

        pregunta6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta6.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta6 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta6.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta6 = "";
            }
        });

        pregunta7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta7.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta7 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta7.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta7 = "";
            }
        });

        pregunta8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta8.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta8 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta8.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta8 = "";
            }
        });

        pregunta9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta9.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta9 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta9.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta9 = "";
            }
        });

        pregunta10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pregunta10.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta10 = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pregunta10.getText().toString().trim().equalsIgnoreCase(""))
                    respuesta10 = "";
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

    @OnClick({R.id.chip_p2_1, R.id.chip_p2_2, R.id.chip_p2_3, R.id.chip_p2_4, R.id.chip_p2_5, R.id.chip_p2_6,
            R.id.chip_p2_7})
    public void onClickChipPregunta2(View view){
        switch (view.getId()){
            case R.id.chip_p2_1:
                respuesta2 += chip_p2_1.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
            case R.id.chip_p2_2:
                respuesta2 += chip_p2_2.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
            case R.id.chip_p2_3:
                respuesta2 += chip_p2_3.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
            case R.id.chip_p2_4:
                respuesta2 += chip_p2_4.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
            case R.id.chip_p2_5:
                respuesta2 += chip_p2_5.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
            case R.id.chip_p2_6:
                respuesta2 += chip_p2_6.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
            case R.id.chip_p2_7:
                respuesta2 += chip_p2_7.getText() + " ";
                pregunta2.setText(respuesta2);
                break;
        }
    }

    @OnClick({R.id.chip_p3_1, R.id.chip_p3_2, R.id.chip_p3_3, R.id.chip_p3_4, R.id.chip_p3_5})
    public void onClickChipPregunta3(View view){
        switch (view.getId()){
            case R.id.chip_p3_1:
                respuesta3 += chip_p3_1.getText() + " ";
                pregunta3.setText(respuesta3);
                break;
            case R.id.chip_p3_2:
                respuesta3 += chip_p3_2.getText() + " ";
                pregunta3.setText(respuesta3);
                break;
            case R.id.chip_p3_3:
                respuesta3 += chip_p3_3.getText() + " ";
                pregunta3.setText(respuesta3);
                break;
            case R.id.chip_p3_4:
                respuesta3 += chip_p3_4.getText() + " ";
                pregunta3.setText(respuesta3);
                break;
            case R.id.chip_p3_5:
                respuesta3 += chip_p3_5.getText() + " ";
                pregunta3.setText(respuesta3);
                break;
        }
    }

    @OnClick({R.id.chip_p4_1, R.id.chip_p4_2, R.id.chip_p4_3, R.id.chip_p4_4, R.id.chip_p4_5, R.id.chip_p4_6,
            R.id.chip_p4_7, R.id.chip_p4_8})
    public void onClickChipPregunta4(View view){
        switch (view.getId()){
            case R.id.chip_p4_1:
                respuesta4 += chip_p4_1.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_2:
                respuesta4 += chip_p4_2.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_3:
                respuesta4 += chip_p4_3.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_4:
                respuesta4 += chip_p4_4.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_5:
                respuesta4 += chip_p4_5.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_6:
                respuesta4 += chip_p4_6.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_7:
                respuesta4 += chip_p4_7.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
            case R.id.chip_p4_8:
                respuesta4 += chip_p4_8.getText() + " ";
                pregunta4.setText(respuesta4);
                break;
        }
    }

    @OnClick({R.id.chip_p5_1, R.id.chip_p5_2, R.id.chip_p5_3, R.id.chip_p5_4, R.id.chip_p5_5, R.id.chip_p5_6})
    public void onClickChipPregunta5(View view){
        switch (view.getId()){
            case R.id.chip_p5_1:
                respuesta5 += chip_p5_1.getText() + " ";
                pregunta5.setText(respuesta5);
                break;
            case R.id.chip_p5_2:
                respuesta5 += chip_p5_2.getText() + " ";
                pregunta5.setText(respuesta5);
                break;
            case R.id.chip_p5_3:
                respuesta5 += chip_p5_3.getText() + " ";
                pregunta5.setText(respuesta5);
                break;
            case R.id.chip_p5_4:
                respuesta5 += chip_p5_4.getText() + " ";
                pregunta5.setText(respuesta5);
                break;
            case R.id.chip_p5_5:
                respuesta5 += chip_p5_5.getText() + " ";
                pregunta5.setText(respuesta5);
                break;
            case R.id.chip_p5_6:
                respuesta5 += chip_p5_6.getText() + " ";
                pregunta5.setText(respuesta5);
                break;
        }
    }

    @OnClick({R.id.chip_p6_1, R.id.chip_p6_2, R.id.chip_p6_3, R.id.chip_p6_4, R.id.chip_p6_5, R.id.chip_p6_6})
    public void onClickChipPregunta6(View view){
        switch (view.getId()){
            case R.id.chip_p6_1:
                respuesta6 += chip_p6_1.getText() + " ";
                pregunta6.setText(respuesta6);
                break;
            case R.id.chip_p6_2:
                respuesta6 += chip_p6_2.getText() + " ";
                pregunta6.setText(respuesta6);
                break;
            case R.id.chip_p6_3:
                respuesta6 += chip_p6_3.getText() + " ";
                pregunta6.setText(respuesta6);
                break;
            case R.id.chip_p6_4:
                respuesta6 += chip_p6_4.getText() + " ";
                pregunta6.setText(respuesta6);
                break;
            case R.id.chip_p6_5:
                respuesta6 += chip_p6_5.getText() + " ";
                pregunta6.setText(respuesta6);
                break;
            case R.id.chip_p6_6:
                respuesta6 += chip_p6_6.getText() + " ";
                pregunta6.setText(respuesta6);
                break;
        }
    }

    @OnClick({R.id.chip_p7_1, R.id.chip_p7_2, R.id.chip_p7_3, R.id.chip_p7_4, R.id.chip_p7_5, R.id.chip_p7_6,
            R.id.chip_p7_7})
    public void onClickChipPregunta7(View view){
        switch (view.getId()){
            case R.id.chip_p7_1:
                respuesta7 += chip_p7_1.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
            case R.id.chip_p7_2:
                respuesta7 += chip_p7_2.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
            case R.id.chip_p7_3:
                respuesta7 += chip_p7_3.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
            case R.id.chip_p7_4:
                respuesta7 += chip_p7_4.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
            case R.id.chip_p7_5:
                respuesta7 += chip_p7_5.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
            case R.id.chip_p7_6:
                respuesta7 += chip_p7_6.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
            case R.id.chip_p7_7:
                respuesta7 += chip_p7_7.getText() + " ";
                pregunta7.setText(respuesta7);
                break;
        }
    }

    @OnClick({R.id.chip_p8_1, R.id.chip_p8_2, R.id.chip_p8_3, R.id.chip_p8_4})
    public void onClickChipPregunta8(View view){
        switch (view.getId()){
            case R.id.chip_p8_1:
                respuesta8 += chip_p8_1.getText() + " ";
                pregunta8.setText(respuesta8);
                break;
            case R.id.chip_p8_2:
                respuesta8 += chip_p8_2.getText() + " ";
                pregunta8.setText(respuesta8);
                break;
            case R.id.chip_p8_3:
                respuesta8 += chip_p8_3.getText() + " ";
                pregunta8.setText(respuesta8);
                break;
            case R.id.chip_p8_4:
                respuesta8 += chip_p8_4.getText() + " ";
                pregunta8.setText(respuesta8);
                break;
        }
    }

    @OnClick({R.id.chip_p9_1, R.id.chip_p9_2, R.id.chip_p9_3, R.id.chip_p9_4, R.id.chip_p9_5, R.id.chip_p9_6,
            R.id.chip_p9_7, R.id.chip_p9_8})
    public void onClickChipPregunta9(View view){
        switch (view.getId()){
            case R.id.chip_p9_1:
                respuesta9 += chip_p9_1.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_2:
                respuesta9 += chip_p9_2.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_3:
                respuesta9 += chip_p9_3.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_4:
                respuesta9 += chip_p9_4.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_5:
                respuesta9 += chip_p9_5.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_6:
                respuesta9 += chip_p9_6.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_7:
                respuesta9 += chip_p9_7.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
            case R.id.chip_p9_8:
                respuesta9 += chip_p9_8.getText() + " ";
                pregunta9.setText(respuesta9);
                break;
        }
    }

    @OnClick({R.id.chip_p10_1, R.id.chip_p10_2, R.id.chip_p10_3, R.id.chip_p10_4, R.id.chip_p10_5})
    public void onClickChipPregunta10(View view){
        switch (view.getId()){
            case R.id.chip_p10_1:
                respuesta10 += chip_p10_1.getText() + " ";
                pregunta10.setText(respuesta10);
                break;
            case R.id.chip_p10_2:
                respuesta10 += chip_p10_2.getText() + " ";
                pregunta10.setText(respuesta10);
                break;
            case R.id.chip_p10_3:
                respuesta10 += chip_p10_3.getText() + " ";
                pregunta10.setText(respuesta10);
                break;
            case R.id.chip_p10_4:
                respuesta10 += chip_p10_4.getText() + " ";
                pregunta10.setText(respuesta10);
                break;
            case R.id.chip_p10_5:
                respuesta10 += chip_p10_5.getText() + " ";
                pregunta10.setText(respuesta10);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
