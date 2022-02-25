package com.proathome.Views.cliente.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaPresenter;
import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaView;
import com.proathome.Presenters.cliente.RutaGeneradaPresenterImpl;
import com.proathome.R;
import com.proathome.Views.cliente.examen.EvaluarRuta;
import com.proathome.Utils.SharedPreferencesManager;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentRutaGenerada extends DialogFragment implements RutaGeneradaView{

    private Unbinder mUnbinder;
    public static MaterialButton ruta;
    public static TextView nivel;
    public static int aciertos = 0;
    private ProgressDialog progressDialog;
    private RutaGeneradaPresenter rutaGeneradaPresenter;

    public FragmentRutaGenerada() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ruta_generada, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        rutaGeneradaPresenter = new RutaGeneradaPresenterImpl(this);

        ruta = view.findViewById(R.id.ruta);
        nivel = view.findViewById(R.id.nivel);

        return view;
    }

    @OnClick({R.id.rutainicio, R.id.ruta})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ruta:
                clickRuta();
                break;
            case R.id.rutainicio:
                clickRutaInicio();
                break;
        }
    }

    private void clickRuta(){
        EvaluarRuta evaluarRuta = new EvaluarRuta(aciertos);
        rutaGeneradaPresenter.rutaExamen(evaluarRuta, SharedPreferencesManager.getInstance(getContext()).getIDCliente());
    }

    private void clickRutaInicio(){
        cerrarFragment();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void cerrarFragment() {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}