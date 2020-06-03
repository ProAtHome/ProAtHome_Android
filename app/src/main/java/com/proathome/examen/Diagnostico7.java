package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.proathome.R;

import butterknife.Unbinder;

public class Diagnostico7 extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico7);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
