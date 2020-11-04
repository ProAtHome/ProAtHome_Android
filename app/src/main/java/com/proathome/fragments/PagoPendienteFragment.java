package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.proathome.R;
import com.proathome.utils.Constants;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PagoPendienteFragment extends DialogFragment {

    private Unbinder mUnbinder;
    private String sesion, lugar, nombre, correo;
    private double deuda;
    public static DialogFragment pagoPendiente;
    public static String deviceIdString;
    private int idSesion;
    @BindView(R.id.tvMonto)
    TextView tvMonto;

    public PagoPendienteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        pagoPendiente = PagoPendienteFragment.this;
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pago_pendiente, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        deuda = bundle.getDouble("deuda");
        sesion = bundle.getString("sesion");
        lugar = bundle.getString("lugar");
        nombre = bundle.getString("nombre");
        correo = bundle.getString("correo");
        idSesion = bundle.getInt("idSesion");
        DecimalFormat df = new DecimalFormat("#.00");
        tvMonto.setText(df.format(deuda) + " MXN");

        return view;
    }

    @OnClick(R.id.btnPagar)
    public void onClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("procedencia", DatosBancoPlanFragment.PROCEDENCIA_PAGO_PENDIENTE);
        bundle.putString("deviceIdString", deviceIdString);
        bundle.putDouble("deuda", deuda);
        bundle.putString("nombre", nombre);
        bundle.putString("correo", correo);
        bundle.putInt("idSesion", idSesion);
        bundle.putString("descripcion", "Saldo de deuda: " + sesion + ", lugar:" + lugar);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DatosBancoPlanFragment bancoPlanFragment = new DatosBancoPlanFragment();
        bancoPlanFragment.setArguments(bundle);
        bancoPlanFragment.show(fragmentTransaction, "Método de pago");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}