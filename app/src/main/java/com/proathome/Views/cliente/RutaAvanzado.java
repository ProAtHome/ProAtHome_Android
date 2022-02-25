package com.proathome.Views.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoPresenter;
import com.proathome.Interfaces.cliente.RutaAvanzado.RutaAvanzadoView;
import com.proathome.Presenters.cliente.RutaAvanzadoPresenterImpl;
import com.proathome.R;
import com.proathome.Servicios.cliente.ControladorRutaAvanzado;
import com.proathome.Servicios.cliente.ServiciosCliente;
import com.proathome.Views.cliente.fragments.DetallesBloque;
import com.proathome.Utils.SharedPreferencesManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaAvanzado extends AppCompatActivity implements RutaAvanzadoView {

    private Unbinder mUnbinder;
    private ProgressDialog progressDialog;
    public static final int NIVEL_AVANZADO = 4;
    private RutaAvanzadoPresenter rutaAvanzadoPresenter;

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

        rutaAvanzadoPresenter = new RutaAvanzadoPresenterImpl(this);

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

        rutaAvanzadoPresenter.getEstadoRuta(SharedPreferencesManager.getInstance(this).getIDCliente(), NIVEL_AVANZADO, SharedPreferencesManager.getInstance(this).getTokenCliente());
        ServiciosCliente.avisoContenidoRuta(this);

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
    public void showProgress() {
        progressDialog = ProgressDialog.show(RutaAvanzado.this, "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void evaluarNivel(int idSeccion, int idNivel, int idBloque) {
        ControladorRutaAvanzado rutaAvanzado = new ControladorRutaAvanzado(this, idBloque, idNivel, idSeccion);
        rutaAvanzado.evaluarNivelAvanzado();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
