package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.proathome.R;

import butterknife.BindView;
import butterknife.Unbinder;

public class Diagnostico4 extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
