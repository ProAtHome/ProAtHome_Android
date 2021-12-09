package com.proathome;

import android.os.Bundle;
import com.proathome.utils.CommonUtils;
import com.proathome.utils.Constants;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
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
            int idProfesional = getIntent().getIntExtra("idProfesional", 0);
            String profesional = getIntent().getStringExtra("profesional");
            String lugar = getIntent().getStringExtra("lugar");
            String fecha = getIntent().getStringExtra("fecha");
            String observaciones = getIntent().getStringExtra("observaciones");
            String tipoServicio = getIntent().getStringExtra("tipoServicio");
            String horario = getIntent().getStringExtra("horario");
            String fotoProfesional = getIntent().getStringExtra("fotoProfesional");
            String correoProfesional = getIntent().getStringExtra("correoProfesional");
            String descripcionProfesional = getIntent().getStringExtra("descripcionProfesional");
            String tipoPlan = getIntent().getStringExtra("tipoPlan");
            double latitud = getIntent().getDoubleExtra("latitud", 0.0);
            double longitud = getIntent().getDoubleExtra("longitud", 0.0);
            boolean sumar = getIntent().getBooleanExtra("sumar", true);
            CommonUtils.setFragment(this, nameFragment, R.id.content_scroll, idServicio, tipoServicio,
                    horario, profesional, lugar, tiempo, observaciones, latitud, longitud, idSeccion, idNivel,
                    idBloque, fecha, fotoProfesional, descripcionProfesional, correoProfesional, sumar, tipoPlan, idProfesional);

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