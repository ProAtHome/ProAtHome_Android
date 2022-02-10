package com.proathome.Views.fragments_compartidos;

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
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.cliente.fragments.PagoPendienteFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import com.proathome.Utils.pojos.Component;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
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
                            if(this.procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE){
                                valorar();
                                //Esta peticion es por que bloquearemos el perfil después de evaluar.
                                WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
                                    try{
                                        if(response != null){
                                            JSONObject data = new JSONObject(response);
                                            if(data.getBoolean("respuesta")){
                                                JSONObject jsonObject = data.getJSONObject("mensaje");
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
                                                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.", false, null, null);
                                        }else
                                            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.", false, null, null);
                                    }catch(JSONException ex){
                                        ex.printStackTrace();
                                    }
                                }, APIEndPoints.BLOQUEAR_PERFIL + "/" + DetallesFragment.idCliente + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
                                bloquearPerfil.execute();
                            }else if(this.procedencia == EvaluarFragment.PROCEDENCIA_PROFESIONAL)
                                valorar();
                        });
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ESPERA!", "Da una puntuación por favor.", false, null, null);
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ESPERA!", "Deja un comentario por favor.", false, null, null);
    }

    private void valorar(){
        JSONObject jsonDatos = new JSONObject();
        try{
            if(this.procedencia == EvaluarFragment.PROCEDENCIA_PROFESIONAL){
                jsonDatos.put("idCliente", DetallesSesionProfesionalFragment.idCliente);
                jsonDatos.put("idProfesional", DetallesSesionProfesionalFragment.idProfesional);
                jsonDatos.put("idSesion", DetallesSesionProfesionalFragment.idSesion);
            }else if(this.procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE){
                jsonDatos.put("idCliente",DetallesFragment.idCliente);
                jsonDatos.put("idProfesional", DetallesFragment.idProfesional);
                jsonDatos.put("idSesion", DetallesFragment.idSesion);
            }
            jsonDatos.put("valoracion", ratingBar.getRating());
            jsonDatos.put("comentario", tieComentario.getText().toString());

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                dismiss();
            },this.procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE ? APIEndPoints.VALORAR_PROFESIONAL : APIEndPoints.VALORAR_CLIENTE,WebServicesAPI.POST, jsonDatos);
            webServicesAPI.execute();
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}