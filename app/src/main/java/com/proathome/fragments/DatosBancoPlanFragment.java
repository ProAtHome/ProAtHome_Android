package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.ServicioTaskCard;
import com.proathome.controladores.planes.ServicioTaskTokenCard;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.openpay.android.validation.CardValidator;

public class DatosBancoPlanFragment extends DialogFragment {

    private Unbinder mUnbinder;
    public static String nombreTitular, tarjeta, mes, ano, tiempo, sesion;
    @BindView(R.id.etTitular)
    TextInputEditText etNombreTitular;
    @BindView(R.id.etTarjeta)
    TextInputEditText etTarjeta;
    @BindView(R.id.etMes)
    TextInputEditText etMes;
    @BindView(R.id.etAno)
    TextInputEditText etAno;
    @BindView(R.id.etCvv)
    TextInputEditText etCVV;
    @BindView(R.id.validarDatos)
    Button validarDatos;
    public static String deviceIdString;
    public static int idSesion, idEstudiante;
    public static double costoTotal;


    public DatosBancoPlanFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_banco_plan, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        deviceIdString = bundle.getString("deviceIdString");
        idSesion = bundle.getInt("idSesion");
        idEstudiante = bundle.getInt("idEstudiante");
        costoTotal = bundle.getDouble("costoTotal");
        etNombreTitular.setText(CobroFinalFragment.nombreTitular);
        etTarjeta.setText(CobroFinalFragment.metodoRegistrado);
        etMes.setText(CobroFinalFragment.mes);
        etAno.setText(CobroFinalFragment.ano);

        return view;
    }

    @OnClick(R.id.validarDatos)
    public void onClick(){
        validarDatos.setEnabled(false);
        /*Checamos los campos de la perra tarjeta*/
        if(!etNombreTitular.getText().toString().trim().equalsIgnoreCase("") && !etTarjeta.getText().toString().trim().equalsIgnoreCase("") &&
                !etMes.getText().toString().trim().equalsIgnoreCase("") && !etAno.getText().toString().trim().equalsIgnoreCase("") && !etCVV.getText().toString().trim().equalsIgnoreCase("")){
            if(CardValidator.validateHolderName(etNombreTitular.getText().toString())){
                if(CardValidator.validateNumber(etTarjeta.getText().toString())){
                    if(CardValidator.validateExpiryDate(Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()))){
                        if(CardValidator.validateCVV(etCVV.getText().toString(), etTarjeta.getText().toString())){
                            /*Vamos a crear un perro y poderoso token de tarjeta*/
                            ServicioTaskTokenCard tokenCard = new ServicioTaskTokenCard(getContext(), etNombreTitular.getText().toString(), etTarjeta.getText().toString(), Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()), etCVV.getText().toString());
                            tokenCard.execute();
                            dismiss();
                        }else{
                            Toast.makeText(getContext(), "CVV no válido.", Toast.LENGTH_LONG).show();
                            validarDatos.setEnabled(true);
                        }
                    }else{
                        Toast.makeText(getContext(), "Fecha de expiración no válida.", Toast.LENGTH_LONG).show();
                        validarDatos.setEnabled(true);
                    }
                }else{
                    Toast.makeText(getContext(), "Tarjeta no válida.", Toast.LENGTH_LONG).show();
                    validarDatos.setEnabled(true);
                }
            }else{
                Toast.makeText(getContext(), "Nombre del titular no válido.", Toast.LENGTH_LONG).show();
                validarDatos.setEnabled(true);
            }
        }else {
            Toast.makeText(getContext(), "Llena todos los campos correctamente.", Toast.LENGTH_LONG).show();
            validarDatos.setEnabled(true);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}