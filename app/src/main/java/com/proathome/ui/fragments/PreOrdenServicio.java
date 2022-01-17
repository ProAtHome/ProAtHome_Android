package com.proathome.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.cliente.TabuladorCosto;
import com.proathome.servicios.api.openpay.TokenCardPagoServicio;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.openpay.android.validation.CardValidator;

public class PreOrdenServicio extends DialogFragment {

    public static String nombreTitular, tarjeta, mes, ano, tiempo, sesion, deviceID, correo;
    public static int idSeccion, tiempoPasar;
    public Bundle bundle;
    public static DialogFragment dialogFragment;
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
    @BindView(R.id.validar)
    MaterialButton validar;
    @BindView(R.id.tvCosto)
    TextView tvCosto;
    private Unbinder mUnbinder;

    public PreOrdenServicio() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceID = Constants.openpay.getDeviceCollectorDefaultImpl().setup(getActivity());
        dialogFragment = this;
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
                            bundle.putString("costoTotal", String.format("%.2f", TabuladorCosto.getCosto(this.idSeccion, this.tiempoPasar, TabuladorCosto.PARTICULAR)));
                            bundle.putString("deviceID", deviceID);
                            bundle.putString("sesion", sesion);
                            TokenCardPagoServicio servicioTaskCard = new TokenCardPagoServicio(getContext(), etNombreTitular.getText().toString(),
                                    etTarjeta.getText().toString(), Integer.parseInt(etMes.getText().toString()), Integer.parseInt(etAno.getText().toString()),
                                    etCVV.getText().toString(), NuevaSesionFragment.idCliente);
                            servicioTaskCard.setBundleSesion(bundle);
                            servicioTaskCard.execute();
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
    }

}