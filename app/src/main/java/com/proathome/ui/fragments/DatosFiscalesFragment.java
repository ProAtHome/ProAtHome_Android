package com.proathome.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ProgressDialog progressDialog;
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

        getDatosFiscales();

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
            upDatosFiscales();
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos correctamente.", false, null, null);
    }

    private void upDatosFiscales(){
        JSONObject jsonDatos = new JSONObject();
        try{
            if(this.tipoPerfil == Constants.TIPO_USUARIO_CLIENTE)
                jsonDatos.put("idCliente", this.idUsuario);
            else if(this.tipoPerfil == Constants.TIPO_USUARIO_PROFESIONAL)
                jsonDatos.put("idProfesional", this.idUsuario);
            jsonDatos.put("tipoPersona", spTipoPersona.getSelectedItem().toString());
            jsonDatos.put("razonSocial", etRazonSocial.getText().toString());
            jsonDatos.put("rfc", etRFC.getText().toString());
            jsonDatos.put("cfdi", spCFDI.getSelectedItem().toString());

            progressDialog = ProgressDialog.show(getContext(), "Validando", "Espere, por favor...");
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                progressDialog.dismiss();
                try{
                    Log.d("FISCO", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", jsonObject.getString("mensaje"),
                                true, "OK", ()->{
                                    dismiss();
                                });
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);

                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }, this.tipoPerfil == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.UPDATE_DATOS_FISCALES_CLIENTE : APIEndPoints.UPDATE_DATOS_FISCALES_PROFESIONAL, WebServicesAPI.POST, jsonDatos);
            webServicesAPI.execute();
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private void getDatosFiscales(){
        String apiDatos = this.tipoPerfil == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.GET_DATOS_FISCALES_CLIENTE + this.idUsuario
                + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente() : APIEndPoints.GET_DATOS_FISCALES_PROFESIONAL +
                this.idUsuario;
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere, por favor...");
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            progressDialog.dismiss();
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject datos = jsonObject.getJSONObject("mensaje");
                    if(datos.getBoolean("existe")){
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
                    }
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);

            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, apiDatos, WebServicesAPI.GET, null);
        webServicesAPI.execute();
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