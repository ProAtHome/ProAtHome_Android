package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ControladorRutaAvanzado;
import com.proathome.servicios.cliente.ControladorRutaBasico;
import com.proathome.ui.fragments.DetallesBloque;
import com.proathome.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaAvanzado extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int idCliente = 0;
    public static final int NIVEL_AVANZADO = 4;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cerrar)
    MaterialButton btnCerrar;

    public static MaterialButton btnA1_Bloque1;
    public static MaterialButton btnA1_Bloque2;

    public static MaterialButton btnA2_Bloque1;
    public static MaterialButton btnA2_Bloque2;

    public static MaterialButton btnA3_Bloque1;
    public static MaterialButton btnA3_Bloque2;

    public static MaterialButton btnA4_Bloque1;
    public static MaterialButton btnA4_Bloque2;

    public static MaterialButton btnA5_Bloque1;
    public static MaterialButton btnA5_Bloque2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_avanzado);
        mUnbinder = ButterKnife.bind(this);

        btnA1_Bloque1 = findViewById(R.id.bloque1);
        btnA1_Bloque2 = findViewById(R.id.bloque2);

        btnA2_Bloque1 = findViewById(R.id.bloque_1_a2);
        btnA2_Bloque2 = findViewById(R.id.bloque2_a2);

        btnA3_Bloque1 = findViewById(R.id.bloque_1_a3);
        btnA3_Bloque2 = findViewById(R.id.bloque2_a3);

        btnA4_Bloque1 = findViewById(R.id.bloque_1_a4);
        btnA4_Bloque2 = findViewById(R.id.bloque2_a4);

        btnA5_Bloque1 = findViewById(R.id.bloque_1_a5);
        btnA5_Bloque2 = findViewById(R.id.bloque2_a5);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
            getEstadoRuta();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

    }

    private void getEstadoRuta(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject rutaJSON = new JSONObject(response);
                int estado = rutaJSON.getInt("estado");
            /*if(estado == Constants.INICIO_RUTA){
    }else */    if(estado == Constants.RUTA_ENCURSO) {
                    int idBloque = rutaJSON.getInt("idBloque");
                    int idNivel = rutaJSON.getInt("idNivel");
                    int idSeccion = rutaJSON.getInt("idSeccion");
                    ControladorRutaAvanzado rutaAvanzado = new ControladorRutaAvanzado(this, idBloque, idNivel, idSeccion);
                    rutaAvanzado.evaluarNivelAvanzado();
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_ESTADO_RUTA + this.idCliente + "/" + NIVEL_AVANZADO, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void verBloque(String contenido, String bloque, String horas){
        Bundle bundle = new Bundle();
        bundle.putString("Contenido", contenido);
        bundle.putString("Bloque", bloque);
        bundle.putString("Horas", horas);
        bundle.putString("Nivel", "Avanzado");
        DetallesBloque detallesBloque = new DetallesBloque();
        detallesBloque.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detallesBloque.show(fragmentTransaction, "Bloque: " + bloque);
    }

    /*Onclick avanzado*/
    @OnClick({R.id.cerrar, R.id.bloque1, R.id.bloque2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cerrar:
                finish();
                break;
            case R.id.bloque1:
                verBloque("Continuous verb forms.\nIntroducing points in an argument/Globalisation." +
                        "\nUrbanisation/Varieties of english.", "1", "13HRS");
                break;
            case R.id.bloque2:
                verBloque("Perfect verbs forms cleft sentences/Feelings adversiting and emotions" +
                        " wordspot: Idioms whit laugh, cry and tears/Word stress.", "2", "12HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a2, R.id.bloque2_a2})
    public void OnClickB2(View view){
        switch (view.getId()){
            case R.id.bloque_1_a2:
                verBloque("Time and tense inversion whit negative adverbials/Money and enterprise" +
                        " wordspot: worth.", "1", "12HRS");
                break;
            case R.id.bloque2_a2:
                verBloque("Patterns whit comparatives and superlatives adjectives/Self-improvement" +
                        " fitness wordspot: Body idioms/Accuracy.", "2", "13HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a3, R.id.bloque2_a3})
    public void OnClickB3(View view){
        switch (view.getId()){
            case R.id.bloque_1_a3:
                verBloque("Modals and related verbs.\nPatternswhit abstract nouns and relative" +
                        " clauses/Polite social behaviour image communication/Intonation of phrases for" +
                        " getting people to do something.", "1", "13HRS");
                break;
            case R.id.bloque2_a3:
                verBloque("Use and non-use of the passive particles wich modify meaning/Education " +
                        "learning/Stress on particles.", "2", "12HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a4, R.id.bloque2_a4})
    public void OnClickB4(View view){
        switch (view.getId()){
            case R.id.bloque_1_a4:
                verBloque("Adding emphasis with auxiliaries and inversion adverbs/Desciptive" +
                        " adjectives Fashion wordspot: look.\nSound and feel/Emphasis whit auxiliaries" +
                        " and inversion.", "1", "12HRS");
                break;
            case R.id.bloque2_a4:
                verBloque("Describing typical habits infinitives and -ingforms.\nCompounds" +
                        " phrases/Characteristics and behaviour wordspot: just/Stress in compound phrases.",
                        "2", "13HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a5, R.id.bloque2_a5})
    public void OnClickB5(View view){
        switch (view.getId()){
            case R.id.bloque_1_a5:
                verBloque("Future forms describing current trends/Describing future developments" +
                        " wordspot: way.", "1", "12HRS");
                break;
            case R.id.bloque2_a5:
                verBloque("Phraseswhit as…as+ Verb.\nEllipsis and substitution/Truth and lies" +
                        " wordspot: well.", "2", "13HRS");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
