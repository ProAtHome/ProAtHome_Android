package com.proathome.ui.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ServiciosExamenDiagnostico;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Diagnostico2 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int respuesta1 = 0;
    private int respuesta2 = 0;
    private int respuesta3 = 0;
    private int respuesta4 = 0;
    private int respuesta5 = 0;
    private int respuesta6 = 0;
    private int respuesta7 = 0;
    private int respuesta8 = 0;
    private int respuesta9 = 0;
    private int respuesta10 = 0;
    /*Pregunta 11*/
    @BindView(R.id.under1)
    TextView under1;
    @BindView(R.id.under2)
    TextView under2;
    @BindView(R.id.under3)
    TextView under3;
    /*Pregunta 12*/
    @BindView(R.id.under1_2)
    TextView under1_2;
    @BindView(R.id.under2_2)
    TextView under2_2;
    @BindView(R.id.under3_2)
    TextView under3_2;
    /*Pregunta 13*/
    @BindView(R.id.under1_3)
    TextView under1_3;
    @BindView(R.id.under2_3)
    TextView under2_3;
    @BindView(R.id.under3_3)
    TextView under3_3;
    /*Pregunta 14*/
    @BindView(R.id.under1_4)
    TextView under1_4;
    @BindView(R.id.under2_4)
    TextView under2_4;
    @BindView(R.id.under3_4)
    TextView under3_4;
    /*Pregunta 15*/
    @BindView(R.id.under1_5)
    TextView under1_5;
    @BindView(R.id.under2_5)
    TextView under2_5;
    @BindView(R.id.under3_5)
    TextView under3_5;
    /*Pregunta 16*/
    @BindView(R.id.under1_secc3)
    TextView under1_secc3;
    @BindView(R.id.under2_secc3)
    TextView under2_secc3;
    /*Pregunta 17*/
    @BindView(R.id.under1_2_secc3)
    TextView under1_2_secc3;
    @BindView(R.id.under2_2_secc3)
    TextView under2_2_secc3;
    /*Pregunta 18*/
    @BindView(R.id.under1_3_secc3)
    TextView under1_3_secc3;
    @BindView(R.id.under2_3_secc3)
    TextView under2_3_secc3;
    /*Pregunta 19*/
    @BindView(R.id.under1_4_secc3)
    TextView under1_4_secc3;
    @BindView(R.id.under2_4_secc3)
    TextView under2_4_secc3;
    @BindView(R.id.under3_4_secc3)
    TextView under3_4_secc3;
    /*Pregunta 20*/
    @BindView(R.id.under1_5_secc3)
    TextView under1_5_secc3;
    @BindView(R.id.under2_5_secc3)
    TextView under2_5_secc3;
    @BindView(R.id.under3_5_secc3)
    TextView under3_5_secc3;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico2);
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
            int puntuacion = respuesta1 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + respuesta6 +
                    respuesta7 + respuesta8 + respuesta9 + respuesta10;

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"sesion", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

            int idCliente = 0;
            if (fila.moveToFirst()) {
                idCliente = fila.getInt(0);
                ServiciosExamenDiagnostico.actualizarEstatusExamen(Constants.ENCURSO_EXAMEN, idCliente, puntuacion, 20, this, Diagnostico2.this, Diagnostico3.class);
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();

        });
    }

    private void configRespuesta(String principal, TextView tvPrincipal, String secundario, TextView tvSecundario,
                                 String terciario, TextView tvTerciario){
        SpannableString mitextoU = new SpannableString(principal);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvPrincipal.setText(mitextoU);
        tvPrincipal.setTextColor(Color.RED);
        tvSecundario.setText(secundario);
        tvSecundario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
        tvTerciario.setText(terciario);
        tvTerciario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
    }

    public void salirMsg(){
        new SweetAlert(this, SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                .setTitleText("¡NO TE RINDAS!")
                .show();
    }


    private void configRespuesta2(String principal, TextView tvPrincipal, String secundario, TextView tvSecundario){
        SpannableString mitextoU = new SpannableString(principal);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvPrincipal.setText(mitextoU);
        tvPrincipal.setTextColor(Color.RED);
        tvSecundario.setText(secundario);
        tvSecundario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
    }

    @OnClick({R.id.under1, R.id.under2, R.id.under3})
    public void onClick1(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1:
                configRespuesta("withdraw", under1, "withdrew", under2,
                        "withdrawing", under3);
                respuesta1 = 0;
                break;
            case R.id.under2:
                configRespuesta("withdrew", under2, "withdraw", under1,
                        "withdrawing", under3);
                respuesta1 = 0;
                break;
            case R.id.under3:
                configRespuesta("withdrawing", under3, "withdrew", under2,
                        "withdraw", under1);
                respuesta1 = 1;
                break;
        }
    }

    @OnClick({R.id.under1_2, R.id.under2_2, R.id.under3_2})
    public void onClick2(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_2:
                configRespuesta("bring", under1_2, "brought", under2_2, "bringing", under3_2);
                respuesta2 = 0;
                break;
            case R.id.under2_2:
                configRespuesta("brought", under2_2, "bring", under1_2, "bringing", under3_2);
                respuesta2 = 0;
                break;
            case R.id.under3_2:
                configRespuesta("bringing", under3_2, "brought", under2_2, "bring", under1_2);
                respuesta2 = 1;
                break;
        }
    }

    @OnClick({R.id.under1_3, R.id.under2_3, R.id.under3_3})
    public void onClick3(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_3:
                configRespuesta("Do", under1_3, "Are", under2_3, "Is", under3_3);
                respuesta3 = 0;
                break;
            case R.id.under2_3:
                configRespuesta("Are", under2_3, "Do", under1_3, "Is", under3_3);
                respuesta3 = 1;
                break;
            case R.id.under3_3:
                configRespuesta("Is", under3_3, "Are", under2_3, "Do", under1_3);
                respuesta3 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_4, R.id.under2_4, R.id.under3_4})
    public void onClick4(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_4:
                configRespuesta("is", under1_4, "am", under2_4, "are", under3_4);
                respuesta4 = 1;
                break;
            case R.id.under2_4:
                configRespuesta("am", under2_4, "is", under1_4, "are", under3_4);
                respuesta4 = 0;
                break;
            case R.id.under3_4:
                configRespuesta("are", under3_4, "am", under2_4, "is", under1_4);
                respuesta4 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_5, R.id.under2_5, R.id.under3_5})
    public void onClick5(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_5:
                configRespuesta("draw", under1_5, "drew", under2_5, "drawing", under3_5);
                respuesta5 = 0;
                break;
            case R.id.under2_5:
                configRespuesta("drew", under2_5, "draw", under1_5, "drawing", under3_5);
                respuesta5 = 0;
                break;
            case R.id.under3_5:
                configRespuesta("drawing", under3_5, "drew", under2_5, "draw", under1_5);
                respuesta5 = 1;
                break;
        }
    }

    @OnClick({R.id.under1_secc3, R.id.under2_secc3})
    public void onClick6(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_secc3:
                configRespuesta2("funnier", under1_secc3, "funniest", under2_secc3);
                respuesta6 = 1;
                break;
            case R.id.under2_secc3:
                configRespuesta2("funniest", under2_secc3, "funnier", under1_secc3);
                respuesta6 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_2_secc3, R.id.under2_2_secc3})
    public void onClick7(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_2_secc3:
                configRespuesta2("shortest", under1_2_secc3, "shorter", under2_2_secc3);
                respuesta7 = 0;
                break;
            case R.id.under2_2_secc3:
                configRespuesta2("shorter", under2_2_secc3, "shortest", under1_2_secc3);
                respuesta7 = 1;
                break;

        }
    }

    @OnClick({R.id.under1_3_secc3, R.id.under2_3_secc3})
    public void onClick8(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_3_secc3:
                configRespuesta2("the baddest", under1_3_secc3, "the worst", under2_3_secc3);
                respuesta8 = 0;
                break;
            case R.id.under2_3_secc3:
                configRespuesta2("the worst", under2_3_secc3, "the baddest", under1_3_secc3);
                respuesta8 = 1;
                break;
        }
    }

    @OnClick({R.id.under1_4_secc3, R.id.under2_4_secc3, R.id.under3_4_secc3})
    public void onClick9(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_4_secc3:
                configRespuesta("the famousest", under1_4_secc3, "the most famous",
                        under2_4_secc3, "the more famous", under3_4_secc3);
                respuesta9 = 0;
                break;
            case R.id.under2_4_secc3:
                configRespuesta("the most famous", under2_4_secc3, "the famousest",
                        under1_4_secc3, "the more famous", under3_4_secc3);
                respuesta9 = 1;
                break;
            case R.id.under3_4_secc3:
                configRespuesta("the more famous", under3_4_secc3, "the most famous",
                        under2_4_secc3, "the famousest", under1_4_secc3);
                respuesta9 = 0;
                break;
        }
    }

    @OnClick({R.id.under1_5_secc3, R.id.under2_5_secc3, R.id.under3_5_secc3})
    public void onClick10(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1_5_secc3:
                configRespuesta("harder", under1_5_secc3, "harderer", under2_5_secc3,
                        "hardest", under3_5_secc3);
                respuesta10 = 1;
                break;
            case R.id.under2_5_secc3:
                configRespuesta("harderer", under2_5_secc3, "harder", under1_5_secc3,
                        "hardest", under3_5_secc3);
                respuesta10 = 0;
                break;
            case R.id.under3_5_secc3:
                configRespuesta("hardest", under3_5_secc3, "harderer", under2_5_secc3,
                        "harder", under1_5_secc3);
                respuesta10 = 0;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
