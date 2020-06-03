package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.proathome.R;

import butterknife.BindView;
import butterknife.Unbinder;

public class Diagnostico6 extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.btnContinuar)
    MaterialButton btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico6);

        btnContinuar.setOnClickListener(v ->{
            Intent intent = new Intent(this, Diagnostico7.class);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
