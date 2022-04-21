package com.proathome.Views.cliente.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanPresenter;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanView;
import com.proathome.Presenters.cliente.DatosBancoPlanPresenterImpl;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Views.cliente.ServicioCliente;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DatosBancoPlanFragment extends DialogFragment implements DatosBancoPlanView {

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
    @BindView(R.id.btnCancelar)
    Button cancelar;

    private DatosBancoPlanPresenter datosBancoPlanPresenter;
    private ProgressDialog progressDialog;
    public static String deviceIdString, descripcion, nombre, correo;
    public static int idSesion, idCliente, procedencia;
    public static double costoTotal, deuda;
    public static final int PROCEDENCIA_PAGO_PLAN = 1, PROCEDENCIA_PAGO_PENDIENTE = 2;
    public String deviceId;

    public DatosBancoPlanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
        deviceId = Constants.openpay.getDeviceCollectorDefaultImpl().setup(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_banco_plan, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        datosBancoPlanPresenter = new DatosBancoPlanPresenterImpl(this);

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
            idCliente = bundle.getInt("idCliente");
            costoTotal = bundle.getDouble("costoTotal");
            etNombreTitular.setText(CobroFinalFragment.nombreTitular);
            etTarjeta.setText(CobroFinalFragment.metodoRegistrado);
            etMes.setText(CobroFinalFragment.mes);
            etAno.setText(CobroFinalFragment.ano);
        }

        return view;
    }

    @OnClick(R.id.btnCancelar)
    public void cancelar(){
        dismiss();
        MasTiempo masTiempo = new MasTiempo();
        Bundle bundle = new Bundle();
        bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
        bundle.putInt("idCliente", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
        masTiempo.setArguments(bundle);
        masTiempo.show(ServicioCliente.obtenerFargment(Constants.fragmentActivity), "Tiempo Extra");
    }

    @OnClick(R.id.validarDatos)
    public void onClick(){
        validarDatos.setEnabled(false);
        /*Checamos los campos de la perra tarjeta*/
        try {
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("nombreCliente", nombre);
            parametrosPost.put("correo", correo);
            parametrosPost.put("cobro", formato1.format(deuda));
            parametrosPost.put("descripcion", descripcion);
            parametrosPost.put("deviceId", deviceId);
            if(NetworkValidate.validate(getContext())){
                datosBancoPlanPresenter.validarDatos(etNombreTitular.getText().toString(), etTarjeta.getText().toString(),
                        etMes.getText().toString(), etAno.getText().toString(), etCVV.getText().toString(), procedencia, getContext(),
                        parametrosPost, idSesion);
            }else
                Toast.makeText(getContext(), "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Validando tarjeta", "Por favor, espere...");
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
    public void setEstatusButtonValidarDatos(boolean estatus) {
        validarDatos.setEnabled(false);
    }

    @Override
    public void cerrarFragment() {
        dismiss();
    }

    @Override
    public void successOperation(String mensaje, String titulo, int tipo) {
        SweetAlert.showMsg(getContext(), tipo, titulo, mensaje, true,
                "OK", ()->{
                    dismiss();
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