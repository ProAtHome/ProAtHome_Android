package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MatchSesionEstudiante extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.prueba)
    TextView pruebaTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_sesion_estudiante);
        mUnbinder = ButterKnife.bind(this);
        int idSesion = Integer.parseInt(getIntent().getStringExtra("idSesion"));
        pruebaTV.setText(String.valueOf(idSesion));

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mUnbinder.unbind();

    }

}
