package com.proathome.Views.cliente.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanPresenter;
import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanView;
import com.proathome.Presenters.cliente.OrdenCompraPlanPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.MainActivity;
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

public class OrdenCompraPlanFragment extends DialogFragment implements OrdenCompraPlanView {

    private Unbinder mUnbinder;
    //Variables de cobro
    private String nombreCliente, correo, descripcion;
    public static String deviceIdString, tipoPlan, fechaIn, fechaFi;
    public static int monedero, idCliente;
    private double cobro;
    private ProgressDialog progressDialog;
    private OrdenCompraPlanPresenter ordenCompraPlanPresenter;
    public static boolean clickComprar = false;

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

        ordenCompraPlanPresenter = new OrdenCompraPlanPresenterImpl(this);

        comprar = view.findViewById(R.id.comprar);
        comprar.setOnClickListener(v -> {
            nombreCliente = PlanesFragment.nombreCliente;
            correo = PlanesFragment.correoCliente;
            JSONObject parametrosPost= new JSONObject();
            try {
                parametrosPost.put("nombreCliente", nombreCliente);
                parametrosPost.put("correo", correo);
                parametrosPost.put("cobro", cobro);
                parametrosPost.put("descripcion", descripcion);
                parametrosPost.put("deviceId", OrdenCompraPlanFragment.deviceIdString);
                ordenCompraPlanPresenter.comprar(etNombreTitular.getText().toString().trim(), etTarjeta.getText().toString().trim(), etMes.getText().toString().trim(),
                        etAno.getText().toString().trim(), etCVV.getText().toString().trim(), idCliente, SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), parametrosPost);
            }catch (JSONException ex){
                ex.printStackTrace();
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

    @OnClick(R.id.cancelar)
    public void onClick(){
        dismiss();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Validando Tarjeta", "Por favor, espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
    }

    @Override
    public void btnComprarEnabled(boolean enabled) {
        comprar.setEnabled(enabled);
    }

    @Override
    public void successPlan() {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", "Disfruta de tu PLAN!.",
                true, "OK", ()->{
                                /*TODO FLUJO_COBRO_PLAN: Activamos el PLAN correspondiente en el perfil y generamos las horas en monedero.
                                            Guardamos el PLAN  en el historial.
                                                Vamos a NuevaSesionFragment con el PLAN activo.*/
                    dismiss();
                    PlanesFragment.planesFragment.dismiss();
                });
    }

    @Override
    public void errorPlan(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Fallo en la transacción - " + error,
                true, "OK", ()->{
                    OrdenCompraPlanFragment.comprar.setEnabled(true);
                });
    }

    @Override
    public void validarPlanError(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, true, "OK", ()->{
            SharedPreferencesManager.getInstance(getContext()).logout();
            startActivity(new Intent(getContext(), MainActivity.class));
        });
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