package com.proathome.Views.cliente.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.proathome.Interfaces.cliente.Examen.Diagnostico2.Diagnostico2Presenter;
import com.proathome.Interfaces.cliente.Examen.Diagnostico2.Diagnostico2View;
import com.proathome.Presenters.cliente.examen.Diagnostico2PresenterImpl;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Diagnostico2 extends AppCompatActivity implements Diagnostico2View {

    private Unbinder mUnbinder;
    private int respuesta1 = 0, respuesta2 = 0, respuesta3 = 0, respuesta4 = 0, respuesta5 = 0,
            respuesta6 = 0, respuesta7 = 0, respuesta8 = 0, respuesta9 = 0, respuesta10 = 0;
    private ProgressDialog progressDialog;
    private Diagnostico2Presenter diagnostico2Presenter;

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

        diagnostico2Presenter = new Diagnostico2PresenterImpl(this);

        cerrarExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(this,
                    R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Al salir durante el examen perderás el progreso de ésta sección.")
                    .setNegativeButton("Salir", (dialog, which) -> {
                        finish();
                    })
                    .setPositiveButton("Cancelar", ((dialog, which) -> {
                        SweetAlert.showMsg(Diagnostico2.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    }))
                    .setOnCancelListener(dialog -> {
                        SweetAlert.showMsg(Diagnostico2.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, ()->{ });
                    })
                    .show();
        });

        btnContinuar.setOnClickListener(v ->{
            int puntuacion = respuesta1 + respuesta2 + respuesta3 + respuesta4 + respuesta5 + respuesta6 +
                    respuesta7 + respuesta8 + respuesta9 + respuesta10;
            int idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
            diagnostico2Presenter.actualizarEstatusExamen(Constants.ENCURSO_EXAMEN, idCliente, puntuacion, 20);
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
    public void showProgress() {
        progressDialog = ProgressDialog.show(Diagnostico2.this, "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String mensaje) {
        Toast.makeText(Diagnostico2.this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void examenGuardado() {
        SweetAlert.showMsg(Diagnostico2.this, SweetAlert.NORMAL_TYPE, "Puntuación guardada.", "¡Continúa!", true, "OK", ()->{
            if(!Diagnostico7.ultimaPagina){
                startActivityForResult(new Intent(Diagnostico2.this, Diagnostico3.class), 1, ActivityOptions.makeSceneTransitionAnimation(Diagnostico2.this)
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
