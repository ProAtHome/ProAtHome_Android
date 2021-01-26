package com.proathome.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterMsgTickets;
import com.proathome.servicios.ayuda.ServicioTaskMsgTicket;
import com.proathome.servicios.ayuda.ServicioTaskObtenerMsgTicket;
import com.proathome.servicios.ayuda.ServicioTaskTicketSolucion;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.utils.ComponentMsgTickets;
import com.proathome.utils.ComponentTicket;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentTicketAyuda extends DialogFragment {

    private Unbinder mUnbinder;
    private static ComponentTicket mComponentTicket;
    private static ComponentMsgTickets mComponentMsgTicket;
    public static boolean primeraVez = true;
    public static RecyclerView recyclerView;
    public static ComponentAdapterMsgTickets componentAdapterMsgTickets;
    private int idUsuario, idTicket, estatus;
    public Timer timer;
    private int tipoUsuario;
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
        if(categoria.equalsIgnoreCase("queja_profesor"))
            cat = "Queja a Profesor";
        else if(categoria.equalsIgnoreCase("queja_estudiante"))
            cat = "Queja a Estudiante";
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

        Bundle bundle = getArguments();
        this.idTicket = bundle.getInt("idTicket");
        this.estatus = bundle.getInt("estatus");
        tvTopico.setText(bundle.getString("topico"));
        tvDescripcion.setText(bundle.getString("descripcion"));
        tvTicket.setText(bundle.getString("noTicket"));
        tvCategoria.setText("Categoría: " + getCategoria(bundle.getString("categoria")));
        this.tipoUsuario = bundle.getInt("tipoUsuario");

        setIdUsuario();

        System.out.println(bundle.getInt("tipoUsuario"));
        if(bundle.getInt("tipoUsuario") == Constants.TIPO_USUARIO_PROFESOR){
            toolbar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
            tvTicket.setTextColor(getResources().getColor(R.color.color_secondary));
            btnEnviarMsg.setBackgroundColor(getResources().getColor(R.color.color_secondary));
            btnFinalizarTicket.setBackgroundColor(getResources().getColor(R.color.color_secondary));
            btnRegresar.setBackgroundColor(getResources().getColor(R.color.color_secondary));
        }

        recyclerView = view.findViewById(R.id.recyclerMsg);

        ServicioTaskObtenerMsgTicket obtenerMsgTicket = new ServicioTaskObtenerMsgTicket(getContext(), this.idUsuario, this.idTicket, this.tipoUsuario);
        obtenerMsgTicket.execute();

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
            nuevosMensajes();
        }

        return view;
    }

    public void setIdUsuario(){
        if(this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null,
                    1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

            if(fila.moveToFirst()){
                this.idUsuario = fila.getInt(0);
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();
        }else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESOR){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesionProfesor", null,
                    1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

            if(fila.moveToFirst()){
                this.idUsuario = fila.getInt(0);
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();
        }
    }

    @OnClick({R.id.btnEnviarMsg, R.id.btnRegresar, R.id.btnFinalizarTicket})
    public void onClick(View view){
        int tipoSweet = this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE ?
                SweetAlert.ESTUDIANTE : SweetAlert.PROFESOR;
        switch (view.getId()){
            case R.id.btnEnviarMsg:
                if(etEscribeMsg.getText().toString().trim().equalsIgnoreCase("")){
                    new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, tipoSweet)
                            .setTitleText("¡ERROR!")
                            .setContentText("Escribe un mensaje para el operador.")
                            .show();
                }else{
                    FragmentTicketAyuda.recyclerView.getLayoutManager().scrollToPosition(componentAdapterMsgTickets.getItemCount()-1);
                    ServicioTaskMsgTicket msgTicket = new ServicioTaskMsgTicket(getContext(), etEscribeMsg.getText().toString(), this.idUsuario,
                            false, this.idTicket, this.tipoUsuario);
                    msgTicket.execute();
                    etEscribeMsg.setText("");
                }
                break;
            case R.id.btnRegresar:
                dismiss();
                break;
            case R.id.btnFinalizarTicket:
                new SweetAlert(getContext(), SweetAlert.WARNING_TYPE, tipoSweet)
                        .setTitleText("¡ESPERA!")
                        .setContentText("¿Seguro quieres finalizar el ticket?")
                        .setConfirmButton("SI", sweetAlertDialog -> {
                            ServicioTaskTicketSolucion ticketSolucion = new ServicioTaskTicketSolucion(getContext(),
                                    this.idTicket, this.tipoUsuario);
                            ticketSolucion.execute();
                            sweetAlertDialog.dismissWithAnimation();
                            dismiss();
                        })
                        .show();
                break;
        }

    }

    public void nuevosMensajes(){
        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        ServicioTaskObtenerMsgTicket obtenerMsgTicket =
                                new ServicioTaskObtenerMsgTicket(getContext(), idUsuario, idTicket, tipoUsuario);
                        obtenerMsgTicket.execute();
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }
        };

        timer.schedule(task, 0, 1500);
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
        if(this.estatus != Constants.ESTATUS_SOLUCIONADO)
            timer.cancel();
    }

}