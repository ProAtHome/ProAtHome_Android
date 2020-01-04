package com.proathome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class registrarseProfesor extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_profesor);
    }

    public void iniciarSesion(View view){

        intent = new Intent(this, loginProfesor.class);
        startActivity(intent);
        finish();

    }//Fin método iniciarSesion.

    public void registrar(View view){



    }//Fin método registrar.

}
