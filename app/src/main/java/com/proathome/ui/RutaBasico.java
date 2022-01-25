package com.proathome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ControladorRutaAvanzado;
import com.proathome.servicios.cliente.ControladorRutaBasico;
import com.proathome.servicios.cliente.ControladorRutaIntermedio;
import com.proathome.servicios.cliente.ControladorRutaSecciones;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.ui.fragments.DetallesBloque;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaBasico extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int idCliente = 0;
    public static final int NIVEL_BASICO = 2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cerrar)
    MaterialButton btnCerrar;

    public static MaterialButton btnB1_Bloque1;
    public static MaterialButton btnB1_Bloque2;
    public static MaterialButton btnB1_Bloque3;
    public static MaterialButton btnB1_Bloque4;
    public static MaterialButton btnB1_Bloque5;
    public static MaterialButton btnB1_Bloque6;

    public static MaterialButton btnB2_Bloque1;
    public static MaterialButton btnB2_Bloque2;
    public static MaterialButton btnB2_Bloque3;
    public static MaterialButton btnB2_Bloque4;
    public static MaterialButton btnB2_Bloque5;
    public static MaterialButton btnB2_Bloque6;

    public static MaterialButton btnB3_Bloque1;
    public static MaterialButton btnB3_Bloque2;
    public static MaterialButton btnB3_Bloque3;
    public static MaterialButton btnB3_Bloque4;
    public static MaterialButton btnB3_Bloque5;
    public static MaterialButton btnB3_Bloque6;
    public static MaterialButton btnB3_Bloque7;

    public static MaterialButton btnB4_Bloque1;
    public static MaterialButton btnB4_Bloque2;
    public static MaterialButton btnB4_Bloque3;
    public static MaterialButton btnB4_Bloque4;
    public static MaterialButton btnB4_Bloque5;
    public static MaterialButton btnB4_Bloque6;
    public static MaterialButton btnB4_Bloque7;

    public static MaterialButton btnB5_Bloque1;
    public static MaterialButton btnB5_Bloque2;
    public static MaterialButton btnB5_Bloque3;
    public static MaterialButton btnB5_Bloque4;
    public static MaterialButton btnB5_Bloque5;
    public static MaterialButton btnB5_Bloque6;
    public static MaterialButton btnB5_Bloque7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruta_basico);
        mUnbinder = ButterKnife.bind(this);

        btnB1_Bloque1 = findViewById(R.id.bloque1);
        btnB1_Bloque2 = findViewById(R.id.bloque2);
        btnB1_Bloque3 = findViewById(R.id.bloque3);
        btnB1_Bloque4 = findViewById(R.id.bloque4);
        btnB1_Bloque5 = findViewById(R.id.bloque5);
        btnB1_Bloque6 = findViewById(R.id.bloque6);

        btnB2_Bloque1 = findViewById(R.id.bloque_1_b2);
        btnB2_Bloque2 = findViewById(R.id.bloque2_b2);
        btnB2_Bloque3 = findViewById(R.id.bloque3_b2);
        btnB2_Bloque4 = findViewById(R.id.bloque4_b2);
        btnB2_Bloque5 = findViewById(R.id.bloque5_b2);
        btnB2_Bloque6 = findViewById(R.id.bloque6_b2);

        btnB3_Bloque1 = findViewById(R.id.bloque_1_b3);
        btnB3_Bloque2 = findViewById(R.id.bloque2_b3);
        btnB3_Bloque3 = findViewById(R.id.bloque3_b3);
        btnB3_Bloque4 = findViewById(R.id.bloque4_b3);
        btnB3_Bloque5 = findViewById(R.id.bloque5_b3);
        btnB3_Bloque6 = findViewById(R.id.bloque6_b3);
        btnB3_Bloque7 = findViewById(R.id.bloque7_b3);

        btnB4_Bloque1 = findViewById(R.id.bloque_1_b4);
        btnB4_Bloque2 = findViewById(R.id.bloque2_b4);
        btnB4_Bloque3 = findViewById(R.id.bloque3_b4);
        btnB4_Bloque4 = findViewById(R.id.bloque4_b4);
        btnB4_Bloque5 = findViewById(R.id.bloque5_b4);
        btnB4_Bloque6 = findViewById(R.id.bloque6_b4);
        btnB4_Bloque7 = findViewById(R.id.bloque7_b4);

        btnB5_Bloque1 = findViewById(R.id.bloque_1_b5);
        btnB5_Bloque2 = findViewById(R.id.bloque2_b5);
        btnB5_Bloque3 = findViewById(R.id.bloque3_b5);
        btnB5_Bloque4 = findViewById(R.id.bloque4_b5);
        btnB5_Bloque5 = findViewById(R.id.bloque5_b5);
        btnB5_Bloque6 = findViewById(R.id.bloque6_b5);
        btnB5_Bloque7 = findViewById(R.id.bloque7_b5);

        idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
        getEstadoRuta();
        ServiciosCliente.avisoContenidoRuta(this);
    }

    private void getEstadoRuta(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject rutaJSON = data.getJSONObject("mensaje");
                    int estado = rutaJSON.getInt("estado");
            /*if(estado == Constants.INICIO_RUTA){
    }else */        if(estado == Constants.RUTA_ENCURSO) {
                        int idBloque = rutaJSON.getInt("idBloque");
                        int idNivel = rutaJSON.getInt("idNivel");
                        int idSeccion = rutaJSON.getInt("idSeccion");
                        ControladorRutaBasico rutaAprendizaje = new ControladorRutaBasico(this, idBloque, idNivel, idSeccion);
                        rutaAprendizaje.evaluarNivelBasico();
                    }
                }else
                    Toast.makeText(this, data.getString("mensaje"), Toast.LENGTH_LONG).show();
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_ESTADO_RUTA + this.idCliente + "/" + NIVEL_BASICO + "/" + SharedPreferencesManager.getInstance(this).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void verBloque(String contenido, String bloque, String horas){
        Bundle bundle = new Bundle();
        bundle.putString("Contenido", contenido);
        bundle.putString("Bloque", bloque);
        bundle.putString("Horas", horas);
        bundle.putString("Nivel", "Basico");
        DetallesBloque detallesBloque = new DetallesBloque();
        detallesBloque.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detallesBloque.show(fragmentTransaction, "Bloque: " + bloque);
    }

    /*Onclick básico*/
    @OnClick({R.id.cerrar, R.id.bloque1, R.id.bloque2, R.id.bloque3, R.id.bloque4, R.id.bloque5, R.id.bloque6})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cerrar:
                finish();
                break;
            case R.id.bloque1:
                verBloque("I, you and my, your a, an whit jobs.\nJobs, Alphabet, Numbers 0-20." +
                        "\nShort forms, Word stress: jobs and numbers.", "1", "10HRS");
                break;
            case R.id.bloque2:
                verBloque("I, you, he, she, it, his, her, their and our/Countries, Nationalities." +
                        "\nNumbers 21-100.\nPeople age/Word stress: nationalities, sounds: His and He`s.",
                        "2", "11HRS");
                break;
            case R.id.bloque3:
                verBloque("This, that, these, those, be whit we and they/Plural nounds." +
                        "\nAdjetives-opposites.\nFood and drink/Pronunciation th, Word stress: adjetives.",
                        "3", "11HRS");
                break;
            case R.id.bloque4:
                verBloque("Prepositions of place: there is, there are, a, an, some, any and" +
                        " a lot of/Places.\nNatural features/Word stress: places and natural features," +
                        " Pronunciation th.", "4", "10HRS");
                break;
            case R.id.bloque5:
                verBloque("Possesive`s, Present simple (I, you, we, they).\nPresent simple " +
                        "questions (I, you, we,  they)/Family verbs whit noun/Word stress: family words." +
                        "\nPronunciation possessive`s.", "5", "11HRS");
                break;
            case R.id.bloque6:
                verBloque("Present simple (he, she, it).\nPresent simple questions/Activities:" +
                        " Likes and dislikes/Pronunciation: present simple verb endings whit`s and -es." +
                        "\nDoes he and Does she.", "6", "11HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_b2, R.id.bloque2_b2, R.id.bloque3_b2, R.id.bloque4_b2, R.id.bloque5_b2, R.id.bloque6_b2})
    public void OnClickB2(View view){
        switch (view.getId()){
            case R.id.bloque_1_b2:
                verBloque("Frequency adverbs.\nPresent simple Wh-questions/Daily routines and" +
                        " times.\nDays and times.\nPrepositions whit time expressions/Word stress: days" +
                        " of the week.", "1", "11HRS");
                break;
            case R.id.bloque2_b2:
                verBloque("Can, can`t, Questions whit can.\nReview of questions/Verbs:things" +
                        " you do.\nParts of the body/Sounds: can and can`t.\nWord stress: parts of the body.",
                        "2", "10HRS");
                break;
            case R.id.bloque3_b2:
                verBloque("Past simple of be (was/were)/ Month of the year.\nOrdinal numbers and" +
                        " dates Years/Strong and weak forms (was, wasn`t, were and weren`t).\nSounds: dates" +
                        " and months.", "3", "11HRS");
                break;
            case R.id.bloque4_b2:
                verBloque("Past simple (regular verbs).\nPast simple (irregular verbs)/Life" +
                        " events/Regular past simple forms (ed- endings).\nWords stress: jobs.", "4",
                        "10HRS");
                break;
            case R.id.bloque5_b2:
                verBloque("Past simple: yes, no (questions).\nPast simple: wh (questions)/Transport" +
                        " and travel.\nTime phrases.\nHoliday activities/Linking: Did you…? Were you…?",
                        "5", "11HRS");
                break;
            case R.id.bloque6_b2:
                verBloque("Want, want to, going to/Verb phrases about wants.\nThings you can buy." +
                        "\nDescribing objects: colours and sizes.", "6", "11HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_b3, R.id.bloque2_b3, R.id.bloque3_b3, R.id.bloque4_b3, R.id.bloque5_b3, R.id.bloque6_b3,
            R.id.bloque7_b3})
    public void OnClickB3(View view){
        switch (view.getId()){
            case R.id.bloque_1_b3:
                verBloque("Be:positive forms, be: positive and negative short forms.\nArticles " +
                        "whit jobs be: personal questions/Countries and nationalities jobs/Word stress:" +
                        " short forms (am, are, is).\nStress in question and short answers.", "1",
                        "10HRS");
                break;
            case R.id.bloque2_b3:
                verBloque("This-that, these-those.\nPossesive`s have got/Everyday objects family/Word" +
                        " stress: this, that, these, those.\nShort forms: has-have got.\nVocabulary - family.",
                        "2", "9HRS");
                break;
            case R.id.bloque3_b3:
                verBloque("Present simple: positive and negative (I, you, we, they).\nPresent" +
                        " simple: questions and short anwers (I, you, we, they)/Common verbs.\nTelling the time." +
                        "\nPlaces in a town/Stress and weak form questions.\nStress and weak form telling the time.",
                        "3", "9HRS");
                break;
            case R.id.bloque4_b3:
                verBloque("Present simple: positive and negative (he-she-it).\nPresent simple:" +
                        " questions and short answer (he-she-it)/Activities phrases for time and frequency/Verb" +
                        " forms (he-she-it).\nStrong and weak forms does.", "4", "9HRS");
                break;
            case R.id.bloque5_b3:
                verBloque("Can-can`t: possibility and ability.\nArticles: a-an, the and no " +
                        "article/Transport.\nTravelling/Weak forms - prepositions and articles.\nStrong and" +
                        " weak forms: can-can`t.", "5", "9HRS");
                break;
            case R.id.bloque6_b3:
                verBloque("There is and there are: some and any.\nHow much and how many/Food:" +
                        " countable and uncountable nouns.\nFood pairs/Linking in sentence.\nStress on word pairs.",
                        "6", "9HRS");
                break;
            case R.id.bloque7_b3:
                verBloque("Past simple: was-were.\nPast simple: regular an irregular verbs/Life" +
                        " events.\nPast time phrases/Strong and weaks forms: was-were.\nRegular past simple" +
                        " forms - ed endings.", "7", "9HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_b4, R.id.bloque2_b4, R.id.bloque3_b4, R.id.bloque4_b4, R.id.bloque5_b4,
            R.id.bloque6_b4, R.id.bloque7_b4})
    public void OnClickB4(View view){
        switch (view.getId()){
            case R.id.bloque_1_b4:
                verBloque("Past simple negative form.\nPast simple question form/Adjectives" +
                        " to describe stories.\nEntertainment/Liking did-you.", "1", "9HRS");
                break;
            case R.id.bloque2_b4:
                verBloque("Comparative adjectives.\nSuperlative adjectives/Describing objects." +
                        "\nShops and services/Stress comparatives adjectives.", "2", "9HRS");
                break;
            case R.id.bloque3_b4:
                verBloque("Present continuous.\nPresent simple or continuous?/Clothes.\nDescribing" +
                        " Personality/Vocabulary-clothes.", "3", "9HRS");
                break;
            case R.id.bloque4_b4:
                verBloque("Question words.\nQuatifiers: a lot of a little; a few, not any, not much," +
                        " not many/Animals and natural features.\nBig numbers/Vocabulary-numbers.", "4",
                        "9HRS");
                break;
            case R.id.bloque5_b4:
                verBloque("Going to de future intentions.\nWould like to and want to for future" +
                        " wishes/Celebrations and parties.\nWeather and seasons/Weak forms-going to.", "5",
                        "9HRS");
                break;
            case R.id.bloque6_b4:
                verBloque("Have to and don`t have to.\nMight and will/School and university " +
                        "subjects.\nEducation and traning.\nWeak forms and liking - have to and don`t have to.",
                        "6", "10HRS");
                break;
            case R.id.bloque7_b4:
                verBloque("Present perfect (unfinished time).\nPresent perfect (whit ever)/Ways of" +
                        " communicating technology/Strong and weak forms - have (present perfect).", "7",
                        "10HRS");
                break;
        }
    }

    @OnClick({R.id.bloque_1_b5, R.id.bloque2_b5, R.id.bloque3_b5, R.id.bloque4_b5, R.id.bloque5_b5,
            R.id.bloque6_b5, R.id.bloque7_b5})
    public void OnClickB5(View view){
        switch (view.getId()){
            case R.id.bloque_1_b5:
                verBloque("Revision of questions.\nPresent simple and frequency phrases/Leisure " +
                        "activities.\nSport and games/Stress in questions.", "1", "9HRS");
                break;
            case R.id.bloque2_b5:
                verBloque("Past simple - positive and negative.\nPast simple - questions/Time" +
                        " phrases: at, on, in, ago words to describe feelings/-ed endeings (past forms) was" +
                        " and were.\nStress on adjectives intonation in questions.", "2", "9HRS");
                break;
            case R.id.bloque3_b5:
                verBloque("Should, shouldn´t, can can´t, have to, don´t have to/Daily routines" +
                        " jobs/Should and shouldn´t, can, can´t and have to.", "3", "9HRS");
                break;
            case R.id.bloque4_b5:
                verBloque("Present simple and Present continuous.\nPresent continuous for future" +
                        " arrangements/Verb phrases for special days.\nDescriptive adjectives/Stress on" +
                        " months and dates intonation on phrases for special days.", "4", "9HRS");
                break;
            case R.id.bloque5_b5:
                verBloque("Comparative and superlative adjectives.\nQuestion whit How.\nWhat " +
                        "and What…like?/Physical apperance parts of the body/Weak forms of prepositions." +
                        "\nVowel sounds and silent letters.\nStress on content words in question.", "5",
                        "10HRS");
                break;
            case R.id.bloque6_b5:
                verBloque("Plans and intentions.\nPredictions whit will and won´t/Going on holiday." +
                        "\nDescribing holidays/Will/´ll and would/´d.\nIntonation in making request.",
                        "6", "9HRS");
                break;
            case R.id.bloque7_b5:
                verBloque("Present perfect and Past simple whit for.\nPresent perfect and Past" +
                        " simple whit other time words/Verb phrases about ambitions.\nThe internet/for and have.",
                        "7", "9HRS");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
