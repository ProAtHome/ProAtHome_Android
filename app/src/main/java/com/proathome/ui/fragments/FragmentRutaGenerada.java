package com.proathome.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.ui.examen.EvaluarRuta;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentRutaGenerada extends DialogFragment {

    private Unbinder mUnbinder;
    public static MaterialButton ruta;
    public static TextView nivel;
    public static int aciertos = 0;
    private int idCliente = 0;
    @BindView(R.id.rutainicio)
    MaterialButton rutainicio;

    public FragmentRutaGenerada() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ruta_generada, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

        ruta = view.findViewById(R.id.ruta);
        nivel = view.findViewById(R.id.nivel);
        rutainicio.setOnClickListener(v ->{
            dismiss();
        });

        ruta.setOnClickListener(v ->{
            EvaluarRuta evaluarRuta = new EvaluarRuta(aciertos);
            rutaExamen(evaluarRuta);
        });

        return view;
    }

    public void rutaExamen(EvaluarRuta evaluarRuta){
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("idCliente", this.idCliente);
            parametros.put("idSeccion", evaluarRuta.getSeccion());
            parametros.put("idNivel", evaluarRuta.getNivel());
            parametros.put("idBloque", evaluarRuta.getBloque());
            parametros.put("horas", 0);
            parametros.put("fecha_registro", "Hoy");
            parametros.put("sumar", true);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                dismiss();
            }, APIEndPoints.NUEVA_RUTA_EXAMEN, WebServicesAPI.POST, parametros);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}