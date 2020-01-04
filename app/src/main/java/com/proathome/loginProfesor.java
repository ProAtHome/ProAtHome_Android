package com.proathome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class loginProfesor extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profesor);
    }

    public void soyCliente(View view){

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }//Fin método soyProfesor.

    public void registrarse(View view){

        intent = new Intent(this, registrarseProfesor.class);
        startActivity(intent);
        finish();

    }//Fin método registrarse.

    public void entrar(View view){



    }//Fin método entrar.

}
