package com.proathome.Views.cliente.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioPresenter;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioView;
import com.proathome.Presenters.cliente.PreOrdenServicioPresenterImpl;
import com.proathome.R;
import com.proathome.Servicios.cliente.TabuladorCosto;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.openpay.android.validation.CardValidator;

public class PreOrdenServicio extends DialogFragment implements PreOrdenServicioView {

    public static String nombreTitular, tarjeta, mes, ano, tiempo, sesion, deviceID, correo;
    public static int idSeccion, tiempoPasar;
    public Bundle bundle;
    public static DialogFragment dialogFragment;
    private PreOrdenServicioPresenter preOrdenServicioPresenter;
    private Unbinder mUnbinder;
    public static boolean clickComprar = false;

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
    @BindView(R.id.tvSesion)
    TextView tvSesion;
    @BindView(R.id.tvTiempo)
    TextView tvTiempo;
    @BindView(R.id.tvCosto)
    TextView tvCosto;

    public PreOrdenServicio() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        deviceID = Constants.openpay.getDeviceCollectorDefaultImpl().setup(getActivity());
        dialogFragment = this;
        preOrdenServicioPresenter = new PreOrdenServicioPresenterImpl(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_orden_servicio, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        bundle = getArguments();

        Log.d("TAG12", "ENTRO");

        /*Mostramos los datos de la PreOrden*/
        etNombreTitular.setText(nombreTitular);
        etTarjeta.setText(tarjeta);
        etMes.setText(mes);
        etAno.setText(ano);
        tvSesion.setText(sesion);
        tvTiempo.setText(tiempo);
        tvCosto.setText("Total: " + String.format("%.2f", TabuladorCosto.getCosto(this.idSeccion, this.tiempoPasar, TabuladorCosto.PARTICULAR)) + " MXN");
        return view;
    }

    @OnClick(R.id.btnCancelar)
    public void onClickCancelar(){
        NuevaSesionFragment.clickSolicitar = false;
        dismiss();
    }

    @OnClick(R.id.validar)
    public void onClick(){
        /*Checamos los campos de la perra tarjeta*/
        if(!etNombreTitular.getText().toString().trim().equalsIgnoreCase("") && !etTarjeta.getText().toString().trim().equalsIgnoreCase("") &&
            !etMes.getText().toString().trim().equalsIgnoreCase("") && !etAno.getText().toString().trim().equalsIgnoreCase("") && !etCVV.getText().toString().trim().equalsIgnoreCase("")){
            if(CardValidator.validateHolderName(etNombreTitular.getText().toString())){
                if(CardValidator.validateNumber(etTarjeta.getText().toString())){
                    if(CardValidator.validateExpiryDate(Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()))){
                        if(CardValidator.validateCVV(etCVV.getText().toString(), etTarjeta.getText().toString())){
                            /*Vamos a crear un perro y poderoso token de tarjeta*/
                            if(!PreOrdenServicio.clickComprar){
                                PreOrdenServicio.clickComprar = true;
                                bundle.putDouble("costoTotal", TabuladorCosto.getCosto(this.idSeccion, this.tiempoPasar, TabuladorCosto.PARTICULAR));
                                bundle.putString("deviceID", deviceID);
                                bundle.putString("sesion", sesion);
                                preOrdenServicioPresenter.pagar(getContext(), etNombreTitular.getText().toString(),
                                        etTarjeta.getText().toString(), Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()),
                                        etCVV.getText().toString(), NuevaSesionFragment.idCliente, bundle);
                            }
                        }else
                            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "CVV no válido.", false, null, null);
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Fecha de expiración no válida.", false, null, null);
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Tarjeta no válida.", false, null, null);
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Nombre del titular no válido.", false, null, null);
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos correctamente.", false, null, null);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        NuevaSesionFragment.clickSolicitar = false;
    }

}