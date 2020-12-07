package com.proathome.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.ayuda.ServicioTaskNuevoTicket;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NuevoTicketFragment extends DialogFragment {

    private Unbinder mUnbinder;
    private int idEstudiante;
    @BindView(R.id.etTopico)
    TextInputEditText etTopico;
    @BindView(R.id.etDescripcion)
    TextInputEditText etDescripcion;

    public NuevoTicketFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_ticket, container, false);
        mUnbinder = ButterKnife.bind(this, view);

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
            ServicioTaskNuevoTicket nuevoTicket = new ServicioTaskNuevoTicket(getContext(), Constants.TIPO_USUARIO_ESTUDIANTE,
                    etTopico.getText().toString(), etDescripcion.getText().toString(), "2020-12-5",
                        Constants.ESTATUS_SIN_OPERADOR, this.idEstudiante, this);
            nuevoTicket.execute();
        }else{
            new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡ERROR!")
                    .setContentText("Llena los campos correctamente.")
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}