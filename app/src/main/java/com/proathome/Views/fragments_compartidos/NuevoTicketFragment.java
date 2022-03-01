package com.proathome.Views.fragments_compartidos;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketPresenter;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketView;
import com.proathome.Presenters.fragments_compartidos.NuevoTicketPresenterImpl;
import com.proathome.R;
import com.proathome.Views.cliente.navigator.ayuda.AyudaFragment;
import com.proathome.Views.profesional.navigator.ayudaProfesional.AyudaProfesionalFragment;
import com.proathome.Utils.Constants;
import com.proathome.Utils.FechaActual;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NuevoTicketFragment extends DialogFragment implements NuevoTicketView {

    private Unbinder mUnbinder;
    private int idUsuario, tipoUsuario, idSesion;
    private NuevoTicketPresenter nuevoTicketPresenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.etTopico)
    TextInputEditText etTopico;
    @BindView(R.id.etDescripcion)
    TextInputEditText etDescripcion;
    @BindView(R.id.tvTopico)
    TextView tvTopico;
    @BindView(R.id.tvDescripcion)
    TextView tvDescripcion;
    @BindView(R.id.tvCategorias)
    TextView tvCategorias;
    @BindView(R.id.btnEnviar)
    MaterialButton btnEnviar;
    @BindView(R.id.categorias)
    Spinner categorias;

    public NuevoTicketFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        nuevoTicketPresenter = new NuevoTicketPresenterImpl(this);

        Bundle bundle = getArguments();
        tipoUsuario = bundle.getInt("tipoUsuario");
        idSesion = bundle.getInt("idSesion");

        setIdUsuario();
    }

    public void setIdUsuario(){
        if(tipoUsuario == Constants.TIPO_USUARIO_CLIENTE)
            idUsuario = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        else if(tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
            idUsuario = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();
    }

    public String getSelectedCategoria(){
        String cat = null;
        if(categorias.getSelectedItem().toString().equalsIgnoreCase("Soporte Técnico"))
            cat = "soporte";
        else if(categorias.getSelectedItem().toString().equalsIgnoreCase("Crédito"))
            cat = "credito";
        else if(categorias.getSelectedItem().toString().equalsIgnoreCase("Queja a Profesional"))
            cat = "queja_profesional";
        else if(categorias.getSelectedItem().toString().equalsIgnoreCase("Queja a Cliente"))
            cat = "queja_cliente";

        return cat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_ticket, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        String[] datos = null;
        ArrayAdapter<String> adapter;
        if(this.idSesion == 0){
            datos = new String[]{"Soporte Técnico", "Crédito"};
            adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, datos);
        }else{
            if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
                datos = new String[]{"Soporte Técnico", "Crédito", "Queja a Cliente"};
            else if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE)
                datos = new String[]{"Soporte Técnico", "Crédito", "Queja a Profesional"};

            adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, datos);
        }

        categorias.setAdapter(adapter);

        return view;
    }

    @OnClick({R.id.btnEnviar, R.id.btnCancelar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnEnviar:
                enviarTicket();
                break;
            case R.id.btnCancelar:
                dismiss();
                break;
        }
    }

    public void enviarTicket(){
        if(!etTopico.getText().toString().trim().equalsIgnoreCase("") &&
                !etDescripcion.getText().toString().trim().equalsIgnoreCase("")){
            nuevoTicket();
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena los campos correctamente.", false, null, null);
    }

    private void nuevoTicket(){
        JSONObject jsonDatos = new JSONObject();
        try {
            jsonDatos.put("tipoUsuario", this.tipoUsuario);
            jsonDatos.put("topico", etTopico.getText().toString());
            jsonDatos.put("descripcion",  etDescripcion.getText().toString());
            jsonDatos.put("fechaCreacion", FechaActual.getFechaActual());
            jsonDatos.put("estatus", Constants.ESTATUS_SIN_OPERADOR);
            jsonDatos.put("idUsuario", this.idUsuario);
            jsonDatos.put("categoria", getSelectedCategoria());
            jsonDatos.put("idSesion", this.idSesion);

            nuevoTicketPresenter.nuevoTicket(jsonDatos, tipoUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere..");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void successTicket(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!",
                mensaje, true, "OK", ()->{
                    dismiss();
                    if(idSesion == 0){
                        if(tipoUsuario == Constants.TIPO_USUARIO_CLIENTE){
                            AyudaFragment.configAdapter();
                            AyudaFragment.configRecyclerView();
                        }else if(tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL){
                            AyudaProfesionalFragment.configAdapter();
                            AyudaProfesionalFragment.configRecyclerView();
                        }
                        nuevoTicketPresenter.obtenerTickets(tipoUsuario, idUsuario, tipoUsuario == Constants.TIPO_USUARIO_CLIENTE ?
                                SharedPreferencesManager.getInstance(getContext()).getTokenCliente() :
                                SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}