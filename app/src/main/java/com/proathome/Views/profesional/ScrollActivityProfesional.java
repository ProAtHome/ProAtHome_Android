package com.proathome.Views.profesional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.proathome.R;
import com.proathome.Utils.profesional.CommonUtilsSesionesProfesional;
import com.proathome.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollActivityProfesional extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_profesional);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        String nameFragment = null;
        if(savedInstanceState == null){
            nameFragment = getIntent().getStringExtra(Constants.ARG_NAME);
            int tiempo = getIntent().getIntExtra("tiempo", 0);
            int idServicio = getIntent().getIntExtra("idServicio" , 0);
            int idSeccion = getIntent().getIntExtra("idSeccion", 0);
            int idNivel = getIntent().getIntExtra("idNivel", 0);
            int idBloque = getIntent().getIntExtra("idBloque", 0);
            String cliente = getIntent().getStringExtra("cliente");
            String profesional = getIntent().getStringExtra("profesional");
            String lugar = getIntent().getStringExtra("lugar");
            String fecha = getIntent().getStringExtra("fecha");
            String observaciones = getIntent().getStringExtra("observaciones");
            String tipoServicio = getIntent().getStringExtra("tipoServicio");
            String horario = getIntent().getStringExtra("horario");
            String foto= getIntent().getStringExtra("foto");
            String correo = getIntent().getStringExtra("correo");
            String descripcion = getIntent().getStringExtra("descripcion");
            String tipoPlan = getIntent().getStringExtra("tipoPlan");
            double latitud = getIntent().getDoubleExtra("latitud", 0.0);
            double longitud = getIntent().getDoubleExtra("longitud", 0.0);
            int idCliente = getIntent().getIntExtra("idCliente", 0);
            CommonUtilsSesionesProfesional.setFragment(this, nameFragment, R.id.content_scroll_profesional, idServicio, cliente, descripcion, correo,
                    foto, tipoServicio, horario, profesional, lugar, tiempo, observaciones,
                    latitud, longitud, idSeccion, idNivel, idBloque, idCliente, fecha);

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