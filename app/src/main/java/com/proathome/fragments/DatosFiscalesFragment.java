package com.proathome.fragments;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.fiscales.ServicioTaskDatosFiscales;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DatosFiscalesFragment extends DialogFragment {

    public Unbinder mUnbinder;
    public static Spinner spTipoPersona;
    public static TextInputEditText etRazonSocial;
    public static TextInputEditText etRFC;
    public static Spinner spCFDI;
    private int idUsuario, tipoPerfil;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnActualizar)
    MaterialButton btnActualizar;

    public DatosFiscalesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_fiscales, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        spTipoPersona = view.findViewById(R.id.tipoPersona);
        etRazonSocial = view.findViewById(R.id.etRazonSocial);
        etRFC = view.findViewById(R.id.etRFC);
        spCFDI = view.findViewById(R.id.spCFDI);

        Bundle bundle = getArguments();
        this.idUsuario = bundle.getInt("idUsuario");
        this.tipoPerfil = bundle.getInt("tipoPerfil");

        String[] datosTipo = new String[]{"FISICA", "MORAL"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosTipo);
        String[] datosCFDI = new String[]{"POR DEFINIR"};
        ArrayAdapter<String> adapterCFDI= new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datosCFDI);

        spTipoPersona.setAdapter(adapterTipo);
        spCFDI.setAdapter(adapterCFDI);
        toolbar.setTitle("Información de Facturación");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        ServicioTaskDatosFiscales datosFiscales = new ServicioTaskDatosFiscales(getContext(), this.tipoPerfil, this.idUsuario, Constants.GET_DATOS_FISCALES);
        datosFiscales.execute();

        if(this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            btnActualizar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }else if(this.tipoPerfil == Constants.TIPO_USUARIO_PROFESOR){
            toolbar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
            btnActualizar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
        }

        return view;
    }

    @OnClick(R.id.btnActualizar)
    public void onClick(){
        if(!etRazonSocial.getText().toString().trim().equalsIgnoreCase("") && !etRFC.getText().toString().trim().equalsIgnoreCase("")){
            ServicioTaskDatosFiscales datosFiscales = new ServicioTaskDatosFiscales(getContext(), this.tipoPerfil, this.idUsuario, Constants.UP_DATOS_FISCALES);
            datosFiscales.setUpDatos(spTipoPersona.getSelectedItem().toString(), etRazonSocial.getText().toString(), etRFC.getText().toString(), spCFDI.getSelectedItem().toString(), this);
            datosFiscales.execute();
        }else{
            new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE ? SweetAlert.ESTUDIANTE : SweetAlert.PROFESOR)
                    .setTitleText("¡ERROR!")
                    .setContentText("Llena todos los campos correctamente.")
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}