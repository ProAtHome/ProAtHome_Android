package com.proathome.Views.fragments_compartidos;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarPresenter;
import com.proathome.Interfaces.fragments_compartidos.Evaluar.EvaluarView;
import com.proathome.Presenters.fragments_compartidos.EvaluarPresenterImpl;
import com.proathome.R;
import com.proathome.Views.cliente.fragments.PagoPendienteFragment;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EvaluarFragment extends DialogFragment implements EvaluarView {

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tieComentario)
    TextInputEditText tieComentario;
    @BindView(R.id.tvEvaluar)
    TextView tvEvaluar;
    @BindView(R.id.btnEnviar)
    MaterialButton btnEnviar;

    public static int PROCEDENCIA_CLIENTE = 1, PROCEDENCIA_PROFESIONAL = 2;
    private int procedencia;
    private EvaluarPresenter evaluarPresenter;
    private ProgressDialog progressDialog;
    private Unbinder mUnbinder;

    public EvaluarFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluar, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        evaluarPresenter = new EvaluarPresenterImpl(this);

        Bundle bundle = getArguments();
        procedencia = bundle.getInt("procedencia");
        if(bundle.getInt("procedencia") == EvaluarFragment.PROCEDENCIA_CLIENTE)
            tvEvaluar.setText("Evalúa a tu pofesional");
        else if(bundle.getInt("procedencia") == EvaluarFragment.PROCEDENCIA_PROFESIONAL)
            tvEvaluar.setText("Evalúa a tu cliente");

        return view;
    }

    @OnClick(R.id.btnEnviar)
    public void onClick(){
        if(!tieComentario.getText().toString().trim().equalsIgnoreCase("")){
            if(ratingBar.getRating() != 0.0){
                SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!",
                        "Gracias por tu evaluación.", true, "OK", ()->{
                                evaluarPresenter.enviarEvaluacion(tieComentario.getText().toString(), ratingBar.getRating(), procedencia, SharedPreferencesManager.getInstance(getContext()).getTokenCliente());
                        });
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ESPERA!", "Da una puntuación por favor.", false, null, null);
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ESPERA!", "Deja un comentario por favor.", false, null, null);
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
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void toPagoPendiente(Bundle bundle) {
        PagoPendienteFragment pagoPendienteFragment = new PagoPendienteFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        pagoPendienteFragment.setArguments(bundle);
        pagoPendienteFragment.show(fragmentTransaction, "Pago pendiente");
    }

    @Override
    public void cerrarFragment() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        mUnbinder.unbind();
    }

}