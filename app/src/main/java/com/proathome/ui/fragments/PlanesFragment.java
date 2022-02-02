package com.proathome.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlanesFragment extends DialogFragment {

    private Unbinder mUnbinder;
    public static final int PLAN_SEMANAL = 1, PLAN_MENSUAL = 2, PLAN_BIMESTRAL = 3;
    public static String tarjeta, nombreTitular, mes, ano, nombreCliente, correoCliente, fechaServidor;
    public static int idCliente, idSeccion, idNivel, idBloque;
    public static DialogFragment planesFragment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnCancelar)
    MaterialButton btnCancelar;
    @BindView(R.id.card1)
    MaterialCardView card1;
    @BindView(R.id.card2)
    MaterialCardView card2;
    @BindView(R.id.card3)
    MaterialCardView card3;

    public PlanesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        setCancelable(false);
        getFechaServidor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        planesFragment = PlanesFragment.this;
        toolbar.setTitle("Promociones Disponibles");
        toolbar.setTitleTextColor(Color.WHITE);

        idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        OrdenCompraPlanFragment.idCliente = idCliente;

        //TODO FLUJO_COMPRAR_PLANES: Obtener fecha de inicio y término del servidor.

        ServiciosCliente.getSesionActual(ServiciosCliente.PLANES_FRAGMENT, idCliente, getContext());
        datosBancariosPagoPlanes();

        //TODO FLUJO_COMPRAR_PLANES:  Mostramos la información del Plan (Título, Descripción, Fechas Inicio-Fin, términos y condiciones)
        card1.setOnClickListener(v -> {
            verPromo("PLAN SEMANAL", "Descripción de plan Semanal.", PlanesFragment.PLAN_SEMANAL);
        });

        card2.setOnClickListener(v -> {
            verPromo("PLAN MENSUAL", "Descripción de plan Mensual.", PlanesFragment.PLAN_MENSUAL);
        });

        card3.setOnClickListener(v -> {
            verPromo("PLAN BIMESTRAL", "Descripción de plan Bimestral.", PlanesFragment.PLAN_BIMESTRAL);
        });

        return view;
    }

    private void datosBancariosPagoPlanes(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                    if(mensaje.getBoolean("existe")){
                        nombreTitular = mensaje.getString("nombreTitular");
                        tarjeta = mensaje.getString("tarjeta");
                        mes = mensaje.getString("mes");
                        ano = mensaje.getString("ano");
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡AVISO!", "No tienes datos bancarios registrados", false, null, null);
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR", jsonObject.get("mensaje").toString(), false, null, null);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_DATOS_BANCO_CLIENTE + this.idCliente + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void getFechaServidor(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                fechaServidor = jsonObject.getString("fechaServidor");
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.FECHA_SERVIDOR, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void verPromo(String titulo, String mensaje, int plan){
        OrdenCompraPlanFragment ordenCompraPlanFragment = new OrdenCompraPlanFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Comprar", (dialog, which) -> {
                    //TODO FLUJO_COMPRAR_PLANES Mostramos Orden de Compra (MODAL con datos Bancarios, PLAN y costo)
                    Calendar calendar = Calendar.getInstance();
                    String fechaDispositivo = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try{
                        Date fechaCelular = sdf.parse(fechaDispositivo);
                        Date fechaServer = sdf.parse(PlanesFragment.fechaServidor);

                        if(fechaCelular.equals(fechaServer)){
                            if(plan == PlanesFragment.PLAN_SEMANAL){
                                bundle.putString("PLAN", "SEMANAL");
                                ordenCompraPlanFragment.setArguments(bundle);
                            }else if(plan == PlanesFragment.PLAN_MENSUAL){
                                bundle.putString("PLAN", "MENSUAL");
                                ordenCompraPlanFragment.setArguments(bundle);
                            }else if(plan == PlanesFragment.PLAN_BIMESTRAL){
                                bundle.putString("PLAN", "BIMESTRAL");
                                ordenCompraPlanFragment.setArguments(bundle);
                            }

                            ordenCompraPlanFragment.show(transaction, "Orden de Compra");
                        }else
                            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Fecha de dispositivo alterada.", false, null, null);
                    }catch(ParseException ex){
                        ex.printStackTrace();
                    }

                })
                .setNegativeButton("Regresar", (dialog, which) -> {
                })
                .setOnCancelListener(dialog -> {
                })
                .show();
    }

    @OnClick(R.id.btnCancelar)
    public void OnClick(){
        SweetAlert.showMsg(SesionesFragment.contexto, SweetAlert.NORMAL_TYPE, "SERVICIO CON PLAN: PARTICULAR", "",
                true, "OK", ()->{
                    //TODO FLUJO_PLANES: Aquí iremos por nuevo servicio PARTICULAR.
                    dismiss();
                    //TODO FLUJO_EJECUTAR_PLAN: Pasar por Bundle el tipo de PLAN en nuevaSesionFragment.
                    NuevaSesionFragment nueva = new NuevaSesionFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    nueva.show(transaction, NuevaSesionFragment.TAG);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}