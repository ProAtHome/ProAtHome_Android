package com.proathome.ui.fragments;

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
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.valoracion.ServicioTaskValorar;
import com.proathome.utils.Component;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
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
    public static int PROCEDENCIA_CLIENTE = 1, PROCEDENCIA_PROFESIONAL = 2;
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
        if(bundle.getInt("procedencia") == EvaluarFragment.PROCEDENCIA_CLIENTE){
            tvEvaluar.setText("Evalúa a tu pofesor");
            tvEvaluar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btnEnviar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }else if(bundle.getInt("procedencia") == EvaluarFragment.PROCEDENCIA_PROFESIONAL){
            tvEvaluar.setText("Evalúa a tu cliente");
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
                            if(this.procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE){
                                ServicioTaskValorar valorar = new ServicioTaskValorar(DetallesFragment.idCliente,
                                        DetallesFragment.idProfesional, ratingBar.getRating(), tieComentario.getText().toString(),
                                        EvaluarFragment.PROCEDENCIA_CLIENTE, DetallesFragment.idSesion);
                                valorar.execute();
                                //Esta peticion es por que bloquearemos el perfil después de evaluar.
                                WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
                                    try{
                                        if(response != null){
                                            JSONObject jsonObject = new JSONObject(response);
                                            PagoPendienteFragment pagoPendienteFragment = new PagoPendienteFragment();
                                            Bundle bundle = new Bundle();
                                            if(jsonObject.getBoolean("bloquear")){
                                                FragmentTransaction fragmentTransaction = null;
                                                bundle.putDouble("deuda", jsonObject.getDouble("deuda"));
                                                bundle.putString("sesion", Component.getSeccion(jsonObject.getInt("idSeccion")) +
                                                        " / " + Component.getNivel(jsonObject.getInt("idSeccion"),
                                                        jsonObject.getInt("idNivel")) + " / " + jsonObject.getInt("idBloque"));
                                                bundle.putString("lugar", jsonObject.getString("lugar"));
                                                bundle.putString("nombre", jsonObject.getString("nombre"));
                                                bundle.putString("correo", jsonObject.getString("correo"));
                                                bundle.putInt("idSesion", jsonObject.getInt("idSesion"));
                                                fragmentTransaction = getFragmentManager().beginTransaction();
                                                pagoPendienteFragment.setArguments(bundle);
                                                pagoPendienteFragment.show(fragmentTransaction, "Pago pendiente");
                                            }
                                        }else
                                            errorMsg("¡ERROR!", "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.", SweetAlert.ERROR_TYPE);
                                    }catch(JSONException ex){
                                        ex.printStackTrace();
                                    }
                                }, APIEndPoints.BLOQUEAR_PERFIL + "/" + DetallesFragment.idCliente, WebServicesAPI.GET, null);
                                bloquearPerfil.execute();
                            }else if(this.procedencia == EvaluarFragment.PROCEDENCIA_PROFESIONAL){
                                ServicioTaskValorar valorar = new ServicioTaskValorar(DetallesSesionProfesionalFragment.idProfesional,
                                        DetallesSesionProfesionalFragment.idCliente, ratingBar.getRating(),
                                        tieComentario.getText().toString(), EvaluarFragment.PROCEDENCIA_PROFESIONAL,
                                        DetallesSesionProfesionalFragment.idSesion);
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