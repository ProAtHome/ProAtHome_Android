package com.proathome.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.ui.ayuda.AyudaFragment;
import com.proathome.ui.ayudaProfesional.AyudaProfesionalFragment;
import com.proathome.utils.ComponentTicket;
import com.proathome.utils.Constants;
import com.proathome.utils.FechaActual;
import com.proathome.utils.SweetAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NuevoTicketFragment extends DialogFragment {

    private Unbinder mUnbinder;
    private int idUsuario, tipoUsuario, idSesion;
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

        Bundle bundle = getArguments();
        this.tipoUsuario = bundle.getInt("tipoUsuario");
        this.idSesion = bundle.getInt("idSesion");

        setIdUsuario();
    }

    public void setIdUsuario(){
        if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null,
                    1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

            if(fila.moveToFirst()){
                this.idUsuario = fila.getInt(0);
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();
        }else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesionProfesional", null,
                    1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);

            if(fila.moveToFirst()){
                this.idUsuario = fila.getInt(0);
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();
        }
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

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!",
                        "Tu solicitud será revisada y en breve te contestará soporte.", true, "OK", ()->{
                            dismiss();
                            if(this.idSesion == 0){
                                if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE){
                                    AyudaFragment.configAdapter();
                                    AyudaFragment.configRecyclerView();
                                }else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL){
                                    AyudaProfesionalFragment.configAdapter();
                                    AyudaProfesionalFragment.configRecyclerView();
                                }
                                obtenerTickets();
                            }
                        });
            }, this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.NUEVO_TICKET_CLIENTE : APIEndPoints.NUEVO_TICKET_PROFESIONAL, WebServicesAPI.POST, jsonDatos);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void obtenerTickets(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE){
                            if (jsonObject.getBoolean("sinTickets")){
                                AyudaFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                            } else{
                                AyudaFragment.lottieAnimationView.setVisibility(View.INVISIBLE);
                                AyudaFragment.componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                        ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                        jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                        jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                        jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario"), jsonObject.getString("categoria")));
                            }
                        }else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL){
                            if (jsonObject.getBoolean("sinTickets")){
                                AyudaProfesionalFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                            } else{
                                AyudaProfesionalFragment.lottieAnimationView.setVisibility(View.INVISIBLE);
                                AyudaProfesionalFragment.componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                        ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                        jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                        jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                        jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario"), jsonObject.getString("categoria")));
                            }
                        }

                    }
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrió un error inseperado, intenta nuevamente.", false, null, null);
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, this.tipoUsuario ==  Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.GET_TICKETS_CLIENTE + this.idUsuario : APIEndPoints.GET_TICKETS_PROFESIONAL + this.idUsuario, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}