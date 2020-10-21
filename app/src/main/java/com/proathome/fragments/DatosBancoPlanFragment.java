package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.clase.ServicioTaskPagoDeuda;
import com.proathome.controladores.planes.ServicioTaskTokenCard;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
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
    public static String deviceIdString, descripcion, nombre, correo;
    public static int idSesion, idEstudiante, procedencia;
    public static double costoTotal, deuda;
    public static final int PROCEDENCIA_PAGO_PLAN = 1, PROCEDENCIA_PAGO_PENDIENTE = 2;
    public String deviceId;

    public DatosBancoPlanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        deviceId = Constants.openpay.getDeviceCollectorDefaultImpl().setup(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_banco_plan, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        procedencia = bundle.getInt("procedencia");
        if(bundle.getInt("procedencia") == DatosBancoPlanFragment.PROCEDENCIA_PAGO_PENDIENTE){
            deviceIdString = bundle.getString("deviceIdString");
            deuda = bundle.getDouble("deuda");
            descripcion = bundle.getString("descripcion");
            nombre = bundle.getString("nombre");
            correo = bundle.getString("correo");
            idSesion = bundle.getInt("idSesion");
        }else if(bundle.getInt("procedencia") == DatosBancoPlanFragment.PROCEDENCIA_PAGO_PLAN){
            deviceIdString = bundle.getString("deviceIdString");
            idSesion = bundle.getInt("idSesion");
            idEstudiante = bundle.getInt("idEstudiante");
            costoTotal = bundle.getDouble("costoTotal");
            etNombreTitular.setText(CobroFinalFragment.nombreTitular);
            etTarjeta.setText(CobroFinalFragment.metodoRegistrado);
            etMes.setText(CobroFinalFragment.mes);
            etAno.setText(CobroFinalFragment.ano);
        }

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
                            if(this.procedencia == DatosBancoPlanFragment.PROCEDENCIA_PAGO_PENDIENTE){
                                validarDatos.setEnabled(false);
                                /*Vamos a crear un perro y poderoso token de tarjeta*/
                                Card card = new Card();
                                card.holderName(etNombreTitular.getText().toString());
                                card.cardNumber(etTarjeta.getText().toString());
                                card.expirationMonth(Integer.parseInt(etMes.getText().toString()));
                                card.expirationYear(Integer.parseInt(etAno.getText().toString()));
                                card.cvv2(etCVV.getText().toString());
                                pagar(card);
                            }else if(this.procedencia == DatosBancoPlanFragment.PROCEDENCIA_PAGO_PLAN){
                                ServicioTaskTokenCard tokenCard = new ServicioTaskTokenCard(getContext(), etNombreTitular.getText().toString(), etTarjeta.getText().toString(), Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()), etCVV.getText().toString());
                                tokenCard.execute();
                                dismiss();
                            }
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

    public void pagar(Card card){
        Constants.openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {

            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {

            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                Toast.makeText(getContext(), operationResult.getResult().getId(), Toast.LENGTH_LONG).show();
                ServicioTaskPagoDeuda pagoDeuda = new ServicioTaskPagoDeuda(getContext(), nombre, correo, operationResult.getResult().getId(), deuda, descripcion, DatosBancoPlanFragment.this, idSesion, deviceId);
                pagoDeuda.execute();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}