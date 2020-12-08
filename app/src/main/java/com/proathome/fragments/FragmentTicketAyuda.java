package com.proathome.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterMsgTickets;
import com.proathome.servicios.ayuda.ServicioTaskMsgTicket;
import com.proathome.servicios.ayuda.ServicioTaskObtenerMsgTicket;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.utils.ComponentMsgTickets;
import com.proathome.utils.ComponentTicket;
import com.proathome.utils.SweetAlert;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentTicketAyuda extends DialogFragment {

    private Unbinder mUnbinder;
    private static ComponentTicket mComponentTicket;
    private static ComponentMsgTickets mComponentMsgTicket;
    public static RecyclerView recyclerView;
    public static ComponentAdapterMsgTickets componentAdapterMsgTickets;
    private int idEstudiante, idTicket;
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

    public FragmentTicketAyuda() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
    }

    public static ComponentTicket getmInstance(String tituloTopico, String estatus, String fechaCreacion, int idTicket, String descripcion, String noTicket){
        mComponentTicket = new ComponentTicket();
        mComponentTicket.setTituloTopico(tituloTopico);
        mComponentTicket.setEstatus(estatus);
        mComponentTicket.setFechaCreacion(fechaCreacion);
        mComponentTicket.setIdTicket(idTicket);
        mComponentTicket.setDescripcion(descripcion);
        mComponentTicket.setNoTicket(noTicket);

        return mComponentTicket;
    }

    public static ComponentMsgTickets getmInstance(String nombreUsuario, String mensaje, boolean operador){
        mComponentMsgTicket = new ComponentMsgTickets();
        mComponentMsgTicket.setNombreUsuario(nombreUsuario);
        mComponentMsgTicket.setMensaje(mensaje);
        mComponentMsgTicket.setOperador(operador);

        return mComponentMsgTicket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_ayuda, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null,
                1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            this.idEstudiante = fila.getInt(0);
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

        Bundle bundle = getArguments();
        this.idTicket = bundle.getInt("idTicket");
        tvTopico.setText(bundle.getString("topico"));
        tvDescripcion.setText(bundle.getString("descripcion"));
        tvTicket.setText(bundle.getString("noTicket"));

        recyclerView = view.findViewById(R.id.recyclerMsg);

        ServicioTaskObtenerMsgTicket obtenerMsgTicket = new ServicioTaskObtenerMsgTicket(getContext(), this.idEstudiante, this.idTicket);
        obtenerMsgTicket.execute();

        configAdapter();
        configRecyclerView();

        return view;
    }

    @OnClick({R.id.btnEnviarMsg, R.id.btnRegresar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnEnviarMsg:
                if(etEscribeMsg.getText().toString().trim().equalsIgnoreCase("")){
                    new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                            .setTitleText("¡ERROR!")
                            .setContentText("Escribe un mensaje para el operador.")
                            .show();
                }else{
                    ServicioTaskMsgTicket msgTicket = new ServicioTaskMsgTicket(getContext(), etEscribeMsg.getText().toString(), this.idEstudiante, false, this.idTicket);
                    msgTicket.execute();
                    etEscribeMsg.setText("");
                }
                break;
            case R.id.btnRegresar:
                dismiss();
                break;
        }

    }

    public static void configAdapter(){
        componentAdapterMsgTickets = new ComponentAdapterMsgTickets(new ArrayList<>());
    }

    public static void configRecyclerView(){
        recyclerView.setAdapter(componentAdapterMsgTickets);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

    }

}