package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.planes.ServicioTaskGenerarPlan;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

public class OrdenCompraPlanFragment extends DialogFragment {

    private Unbinder mUnbinder;
    //Variables de cobro
    private String nombreEstudiante, correo, descripcion;
    public static String deviceIdString, tipoPlan, fechaIn, fechaFi;
    public static int monedero, idEstudiante;
    private double cobro;
    @BindView(R.id.etTitular)
    TextInputEditText etNombreTitular;
    @BindView(R.id.etTarjeta)
    TextInputEditText etTarjeta;
    @BindView(R.id.etMes)
    TextInputEditText etMes;
    @BindView(R.id.etAno)
    TextInputEditText etAno;
    @BindView(R.id.plan)
    TextView tvPlan;
    @BindView(R.id.fechaInicio)
    TextView tvFechaInicio;
    @BindView(R.id.fechaFin)
    TextView tvFechaFin;
    @BindView(R.id.horas)
    TextView tvHoras;
    @BindView(R.id.costo)
    TextView tvCosto;
    @BindView(R.id.etCvv)
    TextInputEditText etCVV;
    public static MaterialButton comprar;

    public OrdenCompraPlanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
        deviceIdString = Constants.openpay.getDeviceCollectorDefaultImpl().setup(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orden_compra_plan, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        comprar = view.findViewById(R.id.comprar);
        comprar.setOnClickListener(v -> {
            /*Checamos los campos de la perra tarjeta*/
            if(!etNombreTitular.getText().toString().trim().equalsIgnoreCase("") && !etTarjeta.getText().toString().trim().equalsIgnoreCase("") &&
                    !etMes.getText().toString().trim().equalsIgnoreCase("") && !etAno.getText().toString().trim().equalsIgnoreCase("") && !etCVV.getText().toString().trim().equalsIgnoreCase("")){
                if(CardValidator.validateHolderName(etNombreTitular.getText().toString())){
                    if(CardValidator.validateNumber(etTarjeta.getText().toString())){
                        if(CardValidator.validateExpiryDate(Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()))){
                            if(CardValidator.validateCVV(etCVV.getText().toString(), etTarjeta.getText().toString())){
                                comprar.setEnabled(false);
                                /*Vamos a crear un perro y poderoso token de tarjeta*/
                                Card card = new Card();
                                card.holderName(etNombreTitular.getText().toString());
                                card.cardNumber(etTarjeta.getText().toString());
                                card.expirationMonth(Integer.parseInt(etMes.getText().toString()));
                                card.expirationYear(Integer.parseInt(etAno.getText().toString()));
                                card.cvv2(etCVV.getText().toString());
                                comprar(card);
                            }else{
                                errorMsg("CVV no válido.");
                            }
                        }else{
                            errorMsg("Fecha de expiración no válida.");
                        }
                    }else{
                        errorMsg("Tarjeta no válida.");
                    }
                }else{
                    errorMsg("Nombre del titular no válido.");
                }
            }else {
                errorMsg("Llena todos los campos correctamente.");
            }
        });
        Bundle bundle = getArguments();
        String plan = bundle.getString("PLAN");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String fechaInicio = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);
        fechaIn = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        Date date = new Date();
        calendar.setTime(date);

        if(plan.equalsIgnoreCase("SEMANAL")){
            tipoPlan = "SEMANAL";
            calendar.add(Calendar.DAY_OF_YEAR, 8);
            String fechaFinal = dateFormat.format(calendar.getTime());
            tvPlan.setText("PLAN: SEMANAL");
            tvFechaInicio.setText("FECHA DE INICIO: " + fechaInicio);
            tvFechaFin.setText("FECHA DE EXPIRACIÓN: " + fechaFinal);
            tvHoras.setText("HORAS DISPONIBLES: 5 HRS");
            if(PlanesFragment.idSeccion == Constants.BASICO) {
                tvCosto.setText("COSTO TOTAL: 1000 MXN");
                cobro = 1000.00;
            }else if(PlanesFragment.idSeccion == Constants.INTERMEDIO) {
                tvCosto.setText("COSTO TOTAL: 1500 MXN");
                cobro = 1500.00;
            }else if(PlanesFragment.idSeccion == Constants.AVANZADO) {
                tvCosto.setText("COSTO TOTAL: 2000 MXN");
                cobro = 2000.00;
            }
            descripcion = "Compra de plan SEMANAL (" + fechaInicio + " - " + fechaFinal + ")";
            SimpleDateFormat dateFormatFi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            fechaFi = dateFormatFi.format(calendar.getTime());
            monedero = 300;
        }else if(plan.equalsIgnoreCase("MENSUAL")){
            tipoPlan = "MENSUAL";
            calendar.add(Calendar.DAY_OF_YEAR, 31);
            String fechaFinal = dateFormat.format(calendar.getTime());
            tvPlan.setText("PLAN: MENSUAL");
            tvFechaInicio.setText("FECHA DE INICIO: " + fechaInicio);
            tvFechaFin.setText("FECHA DE EXPIRACIÓN: " + fechaFinal);
            tvHoras.setText("HORAS DISPONIBLES: 20 HRS");
            if(PlanesFragment.idSeccion == Constants.BASICO) {
                tvCosto.setText("COSTO TOTAL: 3000 MXN");
                cobro = 3000.00;
            }else if(PlanesFragment.idSeccion == Constants.INTERMEDIO) {
                tvCosto.setText("COSTO TOTAL: 4000 MXN");
                cobro = 4000.00;
            }else if(PlanesFragment.idSeccion == Constants.AVANZADO) {
                tvCosto.setText("COSTO TOTAL: 4500 MXN");
                cobro = 4500.00;
            }
            descripcion = "Compra de plan MENSUAL (" + fechaInicio + " - " + fechaFinal + ")";
            SimpleDateFormat dateFormatFi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            fechaFi = dateFormatFi.format(calendar.getTime());
            monedero = 1200;
        }else if(plan.equalsIgnoreCase("BIMESTRAL")){
            tipoPlan = "BIMESTRAL";
            calendar.add(Calendar.DAY_OF_YEAR, 61);
            String fechaFinal = dateFormat.format(calendar.getTime());
            tvPlan.setText("PLAN: BIMESTRAL");
            tvFechaInicio.setText("FECHA DE INICIO: " + fechaInicio);
            tvFechaFin.setText("FECHA DE EXPIRACIÓN: " + fechaFinal);
            tvHoras.setText("HORAS DISPONIBLES: 40 HRS");
            if(PlanesFragment.idSeccion == Constants.BASICO) {
                tvCosto.setText("COSTO TOTAL: 5000 MXN");
                cobro = 5000.00;
            }else if(PlanesFragment.idSeccion == Constants.INTERMEDIO) {
                tvCosto.setText("COSTO TOTAL: 6000 MXN");
                cobro = 6000.00;
            }else if(PlanesFragment.idSeccion == Constants.AVANZADO) {
                tvCosto.setText("COSTO TOTAL: 6500 MXN");
                cobro = 6500.00;
            }
            descripcion = "Compra de plan BIMESTRAL (" + fechaInicio + " - " + fechaFinal + ")";
            SimpleDateFormat dateFormatFi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            fechaFi = dateFormatFi.format(calendar.getTime());
            monedero = 2400;
        }

        etNombreTitular.setText(PlanesFragment.nombreTitular);
        etTarjeta.setText(PlanesFragment.tarjeta);
        etMes.setText(PlanesFragment.mes);
        etAno.setText(PlanesFragment.ano);

        return view;
    }

    public void errorMsg(String mensaje){
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    @OnClick(R.id.cancelar)
    public void onClick(){
        dismiss();
    }

    public void comprar(Card card){
        Constants.openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {
                errorMsg(e.toString());
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                errorMsg(e.toString());
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                nombreEstudiante = PlanesFragment.nombreEstudiante;
                correo = PlanesFragment.correoEstudiante;
                JSONObject parametrosPost= new JSONObject();
                try {
                    parametrosPost.put("idCard",  operationResult.getResult().getId());
                    parametrosPost.put("nombreEstudiante", nombreEstudiante);
                    parametrosPost.put("correo", correo);
                    parametrosPost.put("cobro", cobro);
                    parametrosPost.put("descripcion", descripcion);
                    parametrosPost.put("deviceId", OrdenCompraPlanFragment.deviceIdString);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }

                WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("respuesta")){
                            ServicioTaskGenerarPlan generarPlan = new ServicioTaskGenerarPlan(getContext(), OrdenCompraPlanFragment.tipoPlan, OrdenCompraPlanFragment.fechaIn, OrdenCompraPlanFragment.fechaFi, OrdenCompraPlanFragment.monedero, OrdenCompraPlanFragment.idEstudiante);
                            generarPlan.execute();
                            new SweetAlert(getContext(), SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                                    .setTitleText("¡GENIAL!")
                                    .setContentText("Pago correcto de PLAN.")
                                    .setConfirmButton("OK", sweetAlertDialog -> {
                        /*TODO FLUJO_COBRO_PLAN: Activamos el PLAN correspondiente en el perfil y generamos las horas en monedero.
                            Guardamos el PLAN  en el historial.
                                Vamos a NuevaSesionFragment con el PLAN activo.*/
                                        dismiss();
                                        PlanesFragment.planesFragment.dismiss();
                                        sweetAlertDialog.dismissWithAnimation();
                                    })
                                    .show();
                        }else{
                            new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                                    .setTitleText("¡ERROR!")
                                    .setContentText("Fallo en la transacción - " + response)
                                    .setConfirmButton("OK", sweetAlertDialog -> {
                                        OrdenCompraPlanFragment.comprar.setEnabled(true);
                                        sweetAlertDialog.dismissWithAnimation();
                                    })
                                    .show();
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }, APIEndPoints.COBROS, getContext(), WebServicesAPI.POST, parametrosPost);
                webServicesAPI.execute();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}