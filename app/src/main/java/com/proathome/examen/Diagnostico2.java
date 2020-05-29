package com.proathome.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.proathome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Diagnostico2 extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.under1)
    TextView under1;
    @BindView(R.id.under2)
    TextView under2;
    @BindView(R.id.under3)
    TextView under3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico2);
        mUnbinder = ButterKnife.bind(this);
    }

    private void configRespuesta(String principal, TextView tvPrincipal, String secundario, TextView tvSecundario, String terciario, TextView tvTerciario){
        SpannableString mitextoU = new SpannableString(principal);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvPrincipal.setText(mitextoU);
        tvPrincipal.setTextColor(Color.RED);
        tvSecundario.setText(secundario);
        tvSecundario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
        tvTerciario.setText(terciario);
        tvTerciario.setTextColor(getResources().getColor(R.color.colorPersonalDark));
    }

    @OnClick({R.id.under1, R.id.under2, R.id.under3})
    public void onClick1(View view){
        SpannableString mitextoU;
        switch (view.getId()){
            case R.id.under1:
                configRespuesta("withdraw", under1, "withdrew", under2, "withdrawing", under3);
                break;
            case R.id.under2:
                configRespuesta("withdrew", under2, "withdraw", under1, "withdrawing", under3);
                break;
            case R.id.under3:
                configRespuesta("withdrawing", under3, "withdrew", under2, "withdraw", under1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
