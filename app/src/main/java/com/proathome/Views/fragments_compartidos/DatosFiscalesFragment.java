package com.proathome.Views.fragments_compartidos;

import android.app.ProgressDialog;
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
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesPresenter;
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesView;
import com.proathome.Presenters.fragments_compartidos.DatosFiscalesPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DatosFiscalesFragment extends DialogFragment implements DatosFiscalesView {

    public Unbinder mUnbinder;
    private int idUsuario, tipoPerfil;
    private DatosFiscalesPresenter datosFiscalesPresenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tipoPersona)
    Spinner spTipoPersona;
    @BindView(R.id.etRazonSocial)
    TextInputEditText etRazonSocial;
    @BindView(R.id.etRFC)
    TextInputEditText etRFC;
    @BindView(R.id.spCFDI)
    Spinner spCFDI;
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

        datosFiscalesPresenter = new DatosFiscalesPresenterImpl(this);

        Bundle bundle = getArguments();
        idUsuario = bundle.getInt("idUsuario");
        tipoPerfil = bundle.getInt("tipoPerfil");

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

        datosFiscalesPresenter.getDatosFiscales(tipoPerfil, idUsuario, getContext());

        if(this.tipoPerfil == Constants.TIPO_USUARIO_CLIENTE){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
            btnActualizar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
        }else if(this.tipoPerfil == Constants.TIPO_USUARIO_PROFESIONAL){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
            btnActualizar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
        }

        return view;
    }

    @OnClick(R.id.btnActualizar)
    public void onClick(){
        if(!etRazonSocial.getText().toString().trim().equalsIgnoreCase("") && !etRFC.getText().toString().trim().equalsIgnoreCase("")){
            datosFiscalesPresenter.upDatosFiscales(tipoPerfil, idUsuario, spTipoPersona.getSelectedItem().toString(), etRazonSocial.getText().toString(),
                    etRFC.getText().toString(), spCFDI.getSelectedItem().toString());
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos correctamente.", false, null, null);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Validando", "Espere, por favor...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", error, false, null, null);
    }

    @Override
    public void setInfoDatos(JSONObject datos) {
        try {
            etRFC.setText(datos.getString("rfc"));
            etRazonSocial.setText(datos.getString("razonSocial"));
            if(datos.getString("tipoPersona").equals("FISICA"))
                spTipoPersona.setSelection(0);
            else if(datos.getString("tipoPersona").equals("MORAL"))
                spTipoPersona.setSelection(1);

            if(datos.getString("cfdi").equals("POR DEFINIR"))
                spCFDI.setSelection(0);
            else if(datos.getString("cfdi").equals("POR DEFINIR"))
                spCFDI.setSelection(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSuccess(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", mensaje,
                true, "OK", ()->{
                    dismiss();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}