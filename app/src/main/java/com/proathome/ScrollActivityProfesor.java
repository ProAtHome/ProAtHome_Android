package com.proathome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.proathome.utils.CommonUtils;
import com.proathome.utils.CommonUtilsSesionesProfesor;
import com.proathome.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollActivityProfesor extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_profesor);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        String nameFragment = null;
        if(savedInstanceState == null){
            nameFragment = getIntent().getStringExtra(Constants.ARG_NAME);
            int tiempo = getIntent().getIntExtra("tiempo", 0);
            int idClase = getIntent().getIntExtra("idClase" , 0);
            int idSeccion = getIntent().getIntExtra("idSeccion", 0);
            int idNivel = getIntent().getIntExtra("idNivel", 0);
            int idBloque = getIntent().getIntExtra("idBloque", 0);
            String estudiante = getIntent().getStringExtra("estudiante");
            String profesor = getIntent().getStringExtra("profesor");
            String lugar = getIntent().getStringExtra("lugar");
            String fecha = getIntent().getStringExtra("fecha");
            String observaciones = getIntent().getStringExtra("observaciones");
            String tipoClase = getIntent().getStringExtra("tipoClase");
            String horario = getIntent().getStringExtra("horario");
            String foto= getIntent().getStringExtra("foto");
            String correo = getIntent().getStringExtra("correo");
            String descripcion = getIntent().getStringExtra("descripcion");
            String tipoPlan = getIntent().getStringExtra("tipoPlan");
            double latitud = getIntent().getDoubleExtra("latitud", 0.0);
            double longitud = getIntent().getDoubleExtra("longitud", 0.0);
            int idEstudiante = getIntent().getIntExtra("idEstudiante", 0);
            CommonUtilsSesionesProfesor.setFragment(this, nameFragment, R.id.content_scroll_profesor, idClase, estudiante, descripcion, correo,
                    foto, tipoClase, horario, profesor, lugar, tiempo, observaciones,
                    latitud, longitud, idSeccion, idNivel, idBloque, idEstudiante, fecha);

        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(nameFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}