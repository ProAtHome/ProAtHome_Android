package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.proathome.fragments.DetallesBloque;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaAvanzado extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cerrar)
    MaterialButton btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_avanzado);
        mUnbinder = ButterKnife.bind(this);
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
                verBloque("Continuous verb forms.\nIntroducing points in an argument/Globalisation.\nUrbanisation/Varieties of english.", "1", "13HRS");
                break;
            case R.id.bloque2:
                verBloque("Perfect verbs forms cleft sentences/Feelings adversiting and emotions wordspot: Idioms whit laugh, cry and tears/Word stress.", "2", "12HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a2, R.id.bloque2_a2})
    public void OnClickB2(View view){
        switch (view.getId()){
            case R.id.bloque_1_a2:
                verBloque("Time and tense inversion whit negative adverbials/Money and enterprise wordspot: worth.", "1", "12HRS");
                break;
            case R.id.bloque2_a2:
                verBloque("Patterns whit comparatives and superlatives adjectives/Self-improvement fitness wordspot: Body idioms/Accuracy.", "2", "13HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a3, R.id.bloque2_a3})
    public void OnClickB3(View view){
        switch (view.getId()){
            case R.id.bloque_1_a3:
                verBloque("Modals and related verbs.\nPatternswhit abstract nouns and relative clauses/Polite social behaviour image communication/Intonation of phrases for getting people to do something.", "1", "13HRS");
                break;
            case R.id.bloque2_a3:
                verBloque("Use and non-use of the passive particles wich modify meaning/Education learning/Stress on particles.", "2", "12HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a4, R.id.bloque2_a4})
    public void OnClickB4(View view){
        switch (view.getId()){
            case R.id.bloque_1_a4:
                verBloque("Adding emphasis with auxiliaries and inversion adverbs/Desciptive adjectives Fashion wordspot: look.\nSound and feel/Emphasis whit auxiliaries and inversion.", "1", "12HRS");
                break;
            case R.id.bloque2_a4:
                verBloque("Describing typical habits infinitives and -ingforms.\nCompounds phrases/Characteristics and behaviour wordspot: just/Stress in compound phrases.", "2", "13HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_a5, R.id.bloque2_a5})
    public void OnClickB5(View view){
        switch (view.getId()){
            case R.id.bloque_1_a5:
                verBloque("Future forms describing current trends/Describing future developments wordspot: way.", "1", "12HRS");
                break;
            case R.id.bloque2_a5:
                verBloque("Phraseswhit as…as+ Verb.\nEllipsis and substitution/Truth and lies wordspot: well.", "2", "13HRS");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
