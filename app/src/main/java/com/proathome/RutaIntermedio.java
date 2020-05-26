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

public class RutaIntermedio extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cerrar)
    MaterialButton btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_intermedio);
        mUnbinder = ButterKnife.bind(this);
    }

    private void verBloque(String contenido, String bloque, String horas){
        Bundle bundle = new Bundle();
        bundle.putString("Contenido", contenido);
        bundle.putString("Bloque", bloque);
        bundle.putString("Horas", horas);
        bundle.putString("Nivel", "Intermedio");
        DetallesBloque detallesBloque = new DetallesBloque();
        detallesBloque.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detallesBloque.show(fragmentTransaction, "Bloque: " + bloque);
    }

    /*Onclick intermedio*/
    @OnClick({R.id.cerrar, R.id.bloque1, R.id.bloque2, R.id.bloque3, R.id.bloque4, R.id.bloque5, R.id.bloque6, R.id.bloque7})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cerrar:
                finish();
                break;
            case R.id.bloque1:
                verBloque("Using articles.\nQuantifiers whit countable and uncountable nouns/City life.\nGeographical features/The letter i intonation in asking for and giving directions.", "1", "9HRS");
                break;
            case R.id.bloque2:
                verBloque("May, might, will definitely, etc.\nPresent tense after if, when and other time words/Modern equipment adjectives for describing places/Stress in compund nouns.", "2", "9HRS");
                break;
            case R.id.bloque3:
                verBloque("Past continuous used to/Accidents and injuries feeling ill/Used to and didn´t used to intonation in questions at the doctor´s.", "3", "9HRS");
                break;
            case R.id.bloque4:
                verBloque("Like and wolud like conditional sentences whit would/Adejctives whit dependent prepositions survival items/Intonation in invitations.", "4", "9HRS");
                break;
            case R.id.bloque5:
                verBloque("Present simple passive.\nPast simple passive/Types of products personal items/Regular past participles intonation in making and responding to suggestions.", "5", "9HRS");
                break;
            case R.id.bloque6:
                verBloque("Present perfect continuous whit how long, for and since.\nPresent perfect continuous and Present perfect simple/Personal characteristics getting a job/Contracted forms (Present perfect continuous).", "6", "10HRS");
                break;
            case R.id.bloque7:
                verBloque("Past perfect narrative tenses review/Money verbs and phrases about money/Numbers intonations in requests.", "7", "9HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_i2, R.id.bloque2_i2, R.id.bloque3_i2, R.id.bloque4_i2, R.id.bloque5_i2, R.id.bloque6_i2})
    public void OnClickB2(View view){
        switch (view.getId()){
            case R.id.bloque_1_i2:
                verBloque("Questions and short anwers.\nPresent simple and present continuous/People around you.\nEveryday activities/Sentence stress in questions.\nUsing intonation to show interest.", "1", "11HRS");
                break;
            case R.id.bloque2_i2:
                verBloque("Past simple and past continuous used to and would/Childhood and upbringing.\nRemebering and forgetting/Past simple - ed ending.", "2", "11HRS");
                break;
            case R.id.bloque3_i2:
                verBloque("Comparatives and superlatives.\nDifferent ways of comparing/Features and sights.\nAdjectives for describing places/Stress and-a sounds in a comparative phrases.\nSentence stress in polite questions.", "3", "11HRS");
                break;
            case R.id.bloque4_i2:
                verBloque("Present perfect and Past simple.\nPresent perfect simple and present perfect continuous/Life events.\nPersonal qualities/Strong and weaks forms of have.\nLinking in time phrases.", "4", "11HRS");
                break;
            case R.id.bloque5_i2:
                verBloque("Future forms.\nFuture clauses whit if. When, unless, etc/Word families work/Word stress in word families.\nPolite intonation in questions.", "5", "10HRS");
                break;
            case R.id.bloque6_i2:
                verBloque("Pass perfect.\nReport speech/Say and tell.\nAdverbs for telling stories/Hearting the diference between past simple and past perfect in connected speech.", "6", "10HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_i3, R.id.bloque2_i3, R.id.bloque3_i3, R.id.bloque4_i3, R.id.bloque5_i3, R.id.bloque6_i3})
    public void OnClickB3(View view){
        switch (view.getId()){
            case R.id.bloque_1_i3:
                verBloque("Ed - ing adjectives.\nThe passive/Entertainment and television.\nExtreme adjectives/Word stress.\nSentence stress.", "1", "HRS");
                break;
            case R.id.bloque2_i3:
                verBloque("Polite requests will and shall for instant responses/Social behaviour.\nTalking about norms and customs/Polite intonation in requests.", "2", "HRS");
                break;
            case R.id.bloque3_i3:
                verBloque("Defining relative clauses Quantifiers/How gadgets work.\nDescribing everyday objects/Stress in compound nouns.", "3", "HRS");
                break;
            case R.id.bloque4_i3:
                verBloque("Making predictions hypothetical possibilities whit if/Numbers and statistic.\nSociety and change.\nSociety and social issues/Shifting stress in word families, di or d`in connected speech.", "4", "HRS");
                break;
            case R.id.bloque5_i3:
                verBloque("Obligation and permission in the present.\nObligation and Permission in the past/Linking words.\nCrime and punishment/Modal verbs in connected speech.", "5", "HRS");
                break;
            case R.id.bloque6_i3:
                verBloque("Could have, should have, would have.\nHypothetical situations in the past whit if/Problems and solutions/Past modal forms in connected speech.", "6", "HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_i4, R.id.bloque2_i4, R.id.bloque3_i4, R.id.bloque4_i4, R.id.bloque5_i4, R.id.bloque6_i4})
    public void OnClickB4(View view){
        switch (view.getId()){
            case R.id.bloque_1_i4:
                verBloque("Past and present verb forms uses of auxiliary verbs/Relationships.\nFriendship wordspot: get/Auxiliary verbs sounding sympathetic.", "1", "11HRS");
                break;
            case R.id.bloque2_i4:
                verBloque("Forming adjectives.\nForming nouns and gerunds/Describing how you feel things that make you feel good/Noun suffixes.", "2", "10HRS");
                break;
            case R.id.bloque3_i4:
                verBloque("Narrative tenses continuous aspect in other tenses/Mishaps crime and punishment headlines/Sounding calm or angry.", "3", "11HRS");
                break;
            case R.id.bloque4_i4:
                verBloque("Use and non-use of the passive.\nPassive form whit have or get/Mental skills wordspot mind personal characteristics/Word stress.", "4", "11HRS");
                break;
            case R.id.bloque5_i4:
                verBloque("Review of future forms more complex question forms/Certing together colloquial language/Intonation of statements and questions.", "5", "10HRS");
                break;
            case R.id.bloque6_i4:
                verBloque("Perfect sentences more about the Present perfect, simple and continuous/Human achievements wordspot: first/Weak and strong forms in questions.", "6", "11HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_i5, R.id.bloque2_i5, R.id.bloque3_i5, R.id.bloque4_i5, R.id.bloque5_i5, R.id.bloque6_i5})
    public void OnClickB5(View view){
        switch (view.getId()){
            case R.id.bloque_1_i5:
                verBloque("Relative clauses quantifiers/Celebrations and protests special events wordspot: take/Sounding polite.", "1", "10HRS");
                break;
            case R.id.bloque2_i5:
                verBloque("Overview of modal verbs past modals/Mysteries and oddities extreme adjectives/The weak form of have.", "2", "10HRS");
                break;
            case R.id.bloque3_i5:
                verBloque("Use and non-use of articles different ways of giving emphasis/Phrasal verbs wordspot: right and wrong/Using stress for emphasis.", "3", "11HRS");
                break;
            case R.id.bloque4_i5:
                verBloque("Reporting people´s exact word.\nVerbs that summaries what people say/The media wordspot: speak and talk/Reporting what people said.", "4", "11HRS");
                break;
            case R.id.bloque5_i5:
                verBloque("Hypothetical situations in the present.\nHypothetical situation in the past/Science and processes wordspot: life/Word stress and vowel sounds.", "5", "11HRS");
                break;
            case R.id.bloque6_i5:
                verBloque("Use of gerunds and infinitives.\nDifferent infinitive and gerund forms/Fame/Sentence stress.", "6", "11HRS");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
