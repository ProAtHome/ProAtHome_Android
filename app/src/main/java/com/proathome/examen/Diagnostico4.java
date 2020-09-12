package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioExamenDiagnostico;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Diagnostico4 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int respuesta1 = 0;
    private int respuesta2 = 0;
    private int respuesta3 = 0;
    private int respuesta4 = 0;
    private int respuesta5 = 0;
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
    /*Pregunta 36*/
    @BindView(R.id.under1)
    TextView under1;
    @BindView(R.id.under2)
    TextView under2;
    @BindView(R.id.under3)
    TextView under3;
    /*Pregunta 37*/
    @BindView(R.id.under1_2)
    TextView under1_2;
    @BindView(R.id.under2_2)
    TextView under2_2;
    @BindView(R.id.under3_2)
    TextView under3_2;
    /*Pregunta 38*/
    @BindView(R.id.under1_3)
    TextView under1_3;
    @BindView(R.id.under2_3)
    TextView under2_3;
    @BindView(R.id.under3_3)
    TextView under3_3;
    /*Pregunta 39*/
    @BindView(R.id.under1_4)
    TextView under1_4;
    @BindView(R.id.under2_4)
    TextView under2_4;
    @BindView(R.id.under3_4)
    TextView under3_4;
    /*Pregunta 40*/
    @BindView(R.id.under1_5)
    TextView under1_5;
    @BindView(R.id.under2_5)
    TextView under2_5;
    @BindView(R.id.under3_5)
    TextView under3_5;
    @BindView(R.id.under4_5)
    TextView under4_5;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico4);
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
            int puntuacion = respuesta1 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + validarSeccion1();
            Toast.makeText(this, "Puntuación: " + puntuacion, Toast.LENGTH_LONG).show();

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"sesion", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

            int idCliente = 0;
            if (fila.moveToFirst()) {
                idCliente = fila.getInt(0);
                ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this, idCliente, Constants.ENCURSO_EXAMEN, puntuacion, 40);
                examen.execute();
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();

            Intent intent = new Intent(this, Diagnostico5.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

    }

    public int validarSeccion1(){
        int puntuacion = 0;

        if(res1.getText().toString().equals("a"))
            puntuacion++;
        if(res2.getText().toString().equals("f"))
            puntuacion++;
        if(res3.getText().toString().equals("e"))
            puntuacion++;
        if(res4.getText().toString().equals("c"))
            puntuacion++;
        if(res5.getText().toString().equals("d"))
            puntuacion++;

        return puntuacion;
    }

    private void configRespuesta(String principal, TextView tvPrincipal, String secundario, TextView tvSecundario, String terciario, TextView tvTerciario){
        SpannableString mitextoU = new SpannableString(principal);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvPrincipal.setText(mitextoU);
        tvPrincipal.setTextColor(Color.RED);
        tvSecundario.setText(secundario);
        tvSecundario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
        tvTerciario.setText(terciario);
        tvTerciario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
    }

    private void configRespuesta2(String principal, TextView tvPrincipal, String secundario, TextView tvSecundario, String terciario, TextView tvTerciario, String cuarto, TextView tvCuarto){
        SpannableString mitextoU = new SpannableString(principal);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvPrincipal.setText(mitextoU);
        tvPrincipal.setTextColor(Color.RED);
        tvSecundario.setText(secundario);
        tvSecundario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
        tvTerciario.setText(terciario);
        tvTerciario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
        tvCuarto.setText(cuarto);
        tvCuarto.setTextColor(getResources().getColor(R.color.colorPersonalDark));
    }

    @OnClick({R.id.under1, R.id.under2, R.id.under3})
    public void onClick1(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1:
                configRespuesta("premiere", under1, "premieres", under2, "premiered", under3);
                respuesta1 = 0;
                break;
            case R.id.under2:
                configRespuesta("premieres", under2, "premiere", under1, "premiered", under3);
                respuesta1 = 0;
                break;
            case R.id.under3:
                configRespuesta("premiered", under3, "premieres", under2, "premiere", under1);
                respuesta1 = 1;
                break;
        }
    }

    @OnClick({R.id.under1_2, R.id.under2_2, R.id.under3_2})
    public void onClick2(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_2:
                configRespuesta("write", under1_2, "written", under2_2, "wrote", under3_2);
                respuesta2 = 0;
                break;
            case R.id.under2_2:
                configRespuesta("written", under2_2, "write", under1_2, "wrote", under3_2);
                respuesta2 = 1;
                break;
            case R.id.under3_2:
                configRespuesta("wrote", under3_2, "written", under2_2, "write", under1_2);
                respuesta2 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_3, R.id.under2_3, R.id.under3_3})
    public void onClick3(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_3:
                configRespuesta("cultivate", under1_3, "cultivated", under2_3, "cultivates", under3_3);
                respuesta3 = 0;
                break;
            case R.id.under2_3:
                configRespuesta("cultivated", under2_3, "cultivate", under1_3, "cultivates", under3_3);
                respuesta3 = 1;
                break;
            case R.id.under3_3:
                configRespuesta("cultivates", under3_3, "cultivated", under2_3, "cultivate", under1_3);
                respuesta3 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_4, R.id.under2_4, R.id.under3_4})
    public void onClick4(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_4:
                configRespuesta("produced", under1_4, "producer", under2_4, "produces", under3_4);
                respuesta4 = 1;
                break;
            case R.id.under2_4:
                configRespuesta("producer", under2_4, "produced", under1_4, "produces", under3_4);
                respuesta4 = 0;
                break;
            case R.id.under3_4:
                configRespuesta("produces", under3_4, "producer", under2_4, "produced", under1_4);
                respuesta4 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_5, R.id.under2_5, R.id.under3_5, R.id.under4_5})
    public void onClick5(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_5:
                configRespuesta2("was", under1_5, "were", under2_5, "were not", under3_5, "are not", under4_5);
                respuesta5 = 0;
                break;
            case R.id.under2_5:
                configRespuesta2("were", under2_5, "was", under1_5, "were not", under3_5, "are not", under4_5);
                respuesta5 = 0;
                break;
            case R.id.under3_5:
                configRespuesta2("were not", under3_5, "were", under2_5, "was", under1_5, "are not", under4_5);
                respuesta5 = 1;
                break;
            case R.id.under4_5:
                configRespuesta2("are not", under4_5, "was", under1_5, "were", under2_5, "were not", under3_5);
                respuesta5 = 0;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
