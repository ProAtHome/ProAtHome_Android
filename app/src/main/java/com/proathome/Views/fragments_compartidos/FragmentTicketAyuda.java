package com.proathome.Views.fragments_compartidos;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaPresenter;
import com.proathome.Interfaces.fragments_compartidos.TicketAyuda.TicketAyudaView;
import com.proathome.Presenters.fragments_compartidos.TicketAyudaPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterMsgTickets;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.pojos.ComponentMsgTickets;
import com.proathome.Utils.pojos.ComponentTicket;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentTicketAyuda extends DialogFragment implements TicketAyudaView {

    private Unbinder mUnbinder;
    private static ComponentTicket mComponentTicket;
    private static ComponentMsgTickets mComponentMsgTicket;
    public static boolean primeraVez = true;
    public static RecyclerView recyclerView;
    public static ComponentAdapterMsgTickets componentAdapterMsgTickets;
    private int idUsuario, idTicket, estatus, tipoUsuario;
    private ProgressDialog progressDialog;
    private TicketAyudaPresenter ticketAyudaPresenter;

    @BindView(R.id.btnEnviarMsg)
    MaterialButton btnEnviarMsg;
    @BindView(R.id.etEscribeMsg)
    TextInputEditText etEscribeMsg;
    @BindView(R.id.tvTopico)
    TextView tvTopico;
    @BindView(R.id.tvDescripcion)
    TextView tvDescripcion;
    @BindView(R.id.tvTicket)
    TextView tvTicket;
    @BindView(R.id.tvCategoria)
    TextView tvCategoria;
    @BindView(R.id.tilEscribeMsg)
    TextInputLayout textInputLayout;
    @BindView(R.id.btnFinalizarTicket)
    MaterialButton btnFinalizarTicket;
    @BindView(R.id.tvEstatus)
    TextView tvEstatus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnRegresar)
    MaterialButton btnRegresar;

    public FragmentTicketAyuda() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
    }

    public static ComponentTicket getmInstance(String tituloTopico, String estatus, String fechaCreacion,
                                               int idTicket, String descripcion, String noTicket, int estatusINT, int tipoUsuario, String categoria){
        mComponentTicket = new ComponentTicket();
        mComponentTicket.setTituloTopico(tituloTopico);
        mComponentTicket.setEstatus(estatus);
        mComponentTicket.setFechaCreacion(fechaCreacion);
        mComponentTicket.setIdTicket(idTicket);
        mComponentTicket.setDescripcion(descripcion);
        mComponentTicket.setNoTicket(noTicket);
        mComponentTicket.setEstatusINT(estatusINT);
        mComponentTicket.setTipoUsuario(tipoUsuario);
        mComponentTicket.setCategoria(categoria);

        return mComponentTicket;
    }

    public static ComponentMsgTickets getmInstance(String nombreUsuario, String mensaje, boolean operador, int tipoUsuario){
        mComponentMsgTicket = new ComponentMsgTickets();
        mComponentMsgTicket.setNombreUsuario(nombreUsuario);
        mComponentMsgTicket.setMensaje(mensaje);
        mComponentMsgTicket.setOperador(operador);
        mComponentMsgTicket.setTipoUsuario(tipoUsuario);

        return mComponentMsgTicket;
    }

    public String getCategoria(String categoria){
        String cat = null;
        if(categoria.equalsIgnoreCase("queja_profesional"))
            cat = "Queja a Profesional";
        else if(categoria.equalsIgnoreCase("queja_cliente"))
            cat = "Queja a Cliente";
        else if(categoria.equalsIgnoreCase("soporte"))
            cat = "Soporte Técnico";
        else if(categoria.equalsIgnoreCase("credito"))
            cat = "Crédito";

        return cat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_ayuda, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.recyclerMsg);

        Bundle bundle = getArguments();
        this.idTicket = bundle.getInt("idTicket");
        this.estatus = bundle.getInt("estatus");
        tvTopico.setText(bundle.getString("topico"));
        tvDescripcion.setText(bundle.getString("descripcion"));
        tvTicket.setText(bundle.getString("noTicket"));
        tvCategoria.setText("Categoría: " + getCategoria(bundle.getString("categoria")));
        this.tipoUsuario = bundle.getInt("tipoUsuario");

        setIdUsuario();

        ticketAyudaPresenter = new TicketAyudaPresenterImpl(this);
        ticketAyudaPresenter.obtenerMsgTicket(this.tipoUsuario, this.idUsuario, this.idTicket);

        configAdapter();
        configRecyclerView();

        if(this.estatus == Constants.ESTATUS_SOLUCIONADO){
            textInputLayout.setVisibility(View.INVISIBLE);
            btnEnviarMsg.setVisibility(View.INVISIBLE);
            btnFinalizarTicket.setVisibility(View.INVISIBLE);
            tvEstatus.setText("Estatus: Solucionado");
            tvEstatus.setTextColor(Color.RED);
        }else{
            if(this.estatus == Constants.ESTATUS_SIN_OPERADOR)
                tvEstatus.setText("Estatus: Sin operador asignado.");
            else if(this.estatus == Constants.ESTATUS_EN_CURSO)
                tvEstatus.setText("Estatus: En curso de solución.");
            ticketAyudaPresenter.nuevosMensajes(this.tipoUsuario, this.idUsuario, this.idTicket);
        }

        return view;
    }

    public void setIdUsuario(){
        if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE)
            this.idUsuario = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
            this.idUsuario = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();
    }

    @OnClick({R.id.btnEnviarMsg, R.id.btnRegresar, R.id.btnFinalizarTicket})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnEnviarMsg:
                if(etEscribeMsg.getText().toString().trim().equalsIgnoreCase("")){
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Escribe un mensaje para el operador.", false, null, null);
                }else{
                    recyclerView.getLayoutManager().scrollToPosition(componentAdapterMsgTickets.getItemCount()-1);
                    ticketAyudaPresenter.enviarMensaje(etEscribeMsg.getText().toString(), this.idUsuario, this.tipoUsuario, false, this.idTicket);
                    etEscribeMsg.setText("");
                }
                break;
            case R.id.btnRegresar:
                dismiss();
                break;
            case R.id.btnFinalizarTicket:
                SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ESPERA!", "¿Seguro quieres finalizar el ticket?",
                        true, "SI", ()->{
                            ticketAyudaPresenter.ticketSolucionado(this.tipoUsuario, this.idUsuario, this.idTicket);
                        });
                break;
        }
    }

    public void configAdapter(){
        componentAdapterMsgTickets = new ComponentAdapterMsgTickets(new ArrayList<>());
    }

    public static void configRecyclerView(){
        recyclerView.setAdapter(componentAdapterMsgTickets);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void ticketFinalizado() {
        Toast.makeText(getContext(), "Ticket Finalizado.", Toast.LENGTH_LONG).show();
        dismiss();
    }

    @Override
    public void resetAdapter() {
        configAdapter();
        configRecyclerView();
    }

    @Override
    public void setAdapterMsg(JSONArray jsonArrayMensajes) {
        boolean entro = true;
        try{
            for(int i = componentAdapterMsgTickets.getItemCount(); i < jsonArrayMensajes.length(); i++){
                JSONObject mensaje = jsonArrayMensajes.getJSONObject(i);
                componentAdapterMsgTickets.add(FragmentTicketAyuda.getmInstance(
                        mensaje.getBoolean("operador") ? "Operador - Soporte" : mensaje.getString("nombreUsuario"), mensaje.getString("msg"),
                        mensaje.getBoolean("operador"), this.tipoUsuario));
                if(entro)
                    recyclerView.getLayoutManager().scrollToPosition(jsonArrayMensajes.length()-1);
                entro = false;
                mensaje = null;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(this.estatus != Constants.ESTATUS_SOLUCIONADO)
            ticketAyudaPresenter.cancelTime();
    }

}