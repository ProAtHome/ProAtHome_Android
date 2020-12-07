package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.estudiante.ServicioTaskBloquearPerfil;
import com.proathome.servicios.valoracion.ServicioTaskValorar;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EvaluarFragment extends DialogFragment {

    private Unbinder mUnbinder;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tieComentario)
    TextInputEditText tieComentario;
    @BindView(R.id.tvEvaluar)
    TextView tvEvaluar;
    @BindView(R.id.btnEnviar)
    MaterialButton btnEnviar;
    public static int PROCEDENCIA_ESTUDIANTE = 1, PROCEDENCIA_PROFESOR = 2;
    private int procedencia;

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

        Bundle bundle = getArguments();
        procedencia = bundle.getInt("procedencia");
        if(bundle.getInt("procedencia") == EvaluarFragment.PROCEDENCIA_ESTUDIANTE){
            tvEvaluar.setText("Evalúa a tu pofesor");
            tvEvaluar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btnEnviar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }else if(bundle.getInt("procedencia") == EvaluarFragment.PROCEDENCIA_PROFESOR){
            tvEvaluar.setText("Evalúa a tu estudiante");
            tvEvaluar.setTextColor(getResources().getColor(R.color.color_secondary));
            btnEnviar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
        }

        return view;
    }

    @OnClick(R.id.btnEnviar)
    public void onClick(){
        if(!tieComentario.getText().toString().trim().equalsIgnoreCase("")){
            if(ratingBar.getRating() != 0.0){
                new SweetAlert(getContext(), SweetAlert.SUCCESS_TYPE, procedencia)
                        .setTitleText("¡GENIAL!")
                        .setContentText("Gracias por tu evaluación.")
                        .setConfirmButton("OK", sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            if(this.procedencia == EvaluarFragment.PROCEDENCIA_ESTUDIANTE){
                                ServicioTaskValorar valorar = new ServicioTaskValorar(DetallesFragment.idEstudiante,
                                        DetallesFragment.idProfesor, ratingBar.getRating(), tieComentario.getText().toString(),
                                        EvaluarFragment.PROCEDENCIA_ESTUDIANTE, DetallesFragment.idSesion);
                                valorar.execute();
                                //Esta peticion es por que bloquearemos el perfil después de evaluar.
                                ServicioTaskBloquearPerfil bloquearPerfil = new ServicioTaskBloquearPerfil(getContext(),
                                        DetallesFragment.idEstudiante, DetallesFragment.PROCEDENCIA_DETALLES_FRAGMENT);
                                bloquearPerfil.execute();
                            }else if(this.procedencia == EvaluarFragment.PROCEDENCIA_PROFESOR){
                                ServicioTaskValorar valorar = new ServicioTaskValorar(DetallesSesionProfesorFragment.idProfesor,
                                        DetallesSesionProfesorFragment.idEstudiante, ratingBar.getRating(),
                                        tieComentario.getText().toString(), EvaluarFragment.PROCEDENCIA_PROFESOR,
                                        DetallesSesionProfesorFragment.idSesion);
                                valorar.execute();
                            }
                            dismiss();
                        })
                        .show();
            }else{
                errorMsg("¡ESPERA!","Da una puntuación por favor.", procedencia);
            }
        }else{
            errorMsg("¡ESPERA!","Deja un comentario por favor.", procedencia);
        }
    }

    public void errorMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, tipo)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}