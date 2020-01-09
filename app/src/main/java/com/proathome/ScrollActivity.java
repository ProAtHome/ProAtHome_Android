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
            String nivel= getIntent().getStringExtra("nivel");
            String idClase= getIntent().getStringExtra("idClase");
            String profesor = getIntent().getStringExtra("profesor");
            String lugar = getIntent().getStringExtra("lugar");
            String tiempo = getIntent().getStringExtra("tiempo");
            String observaciones = getIntent().getStringExtra("observaciones");
            String tipoClase = getIntent().getStringExtra("tipoClase");
            String horario = getIntent().getStringExtra("horario");
            String latitud = getIntent().getStringExtra("latitud");
            String longitud = getIntent().getStringExtra("longitud");
            CommonUtils.setFragment(this, nameFragment, R.id.content_scroll, idClase, nivel, tipoClase, horario, profesor, lugar, tiempo, observaciones, latitud, longitud);

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