package com.proathome.ui;

import android.os.Bundle;

import com.proathome.R;
import com.proathome.utils.CommonUtilsSesionesProfesional;
import com.proathome.utils.Constants;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StaticActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        String nameFragment = null;
        if(savedInstanceState == null){
            nameFragment = getIntent().getStringExtra(Constants.ARG_NAME);
            Log.d("tag1", nameFragment);
            int idServicio = getIntent().getIntExtra("idServicio" , 0);
            String profesional = getIntent().getStringExtra("profesional");
            String cliente = getIntent().getStringExtra("nombreCliente");
            String correo = getIntent().getStringExtra("correo");
            String descripcion = getIntent().getStringExtra("descripcion");
            String foto = getIntent().getStringExtra("foto");
            String lugar = getIntent().getStringExtra("lugar");
            int tiempo = getIntent().getIntExtra("tiempo", 0);
            String observaciones = getIntent().getStringExtra("observaciones");
            String tipoServicio = getIntent().getStringExtra("tipoServicio");
            String horario = getIntent().getStringExtra("horario");
            double latitud = getIntent().getDoubleExtra("latitud", 0.0);
            double longitud = getIntent().getDoubleExtra("longitud", 0.0);
            int idSeccion = getIntent().getIntExtra("idSeccion",0);
            int idNivel = getIntent().getIntExtra("idNivel", 0);
            int idBloque = getIntent().getIntExtra("idBloque", 0);
            int idCliente = getIntent().getIntExtra("idCliente", 0);
            String fecha = getIntent().getStringExtra("fecha");
            CommonUtilsSesionesProfesional.setFragment(this, nameFragment, R.id.content_static, idServicio, cliente, descripcion, correo,
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

