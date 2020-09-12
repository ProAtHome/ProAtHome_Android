package com.proathome.fragments;

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
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioExamenDiagnostico;
import com.proathome.controladores.estudiante.ServicioTaskRuta;
import com.proathome.controladores.estudiante.ServicioTaskRutaExamen;
import com.proathome.examen.EvaluarRuta;
import com.proathome.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentRutaGenerada extends DialogFragment {

    private Unbinder mUnbinder;
    public static MaterialButton ruta;
    public static TextView nivel;
    public static int aciertos = 0;
    private int idEstudiante = 0;
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
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idEstudiante = fila.getInt(0);
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
            ServicioTaskRutaExamen rutaExamen = new ServicioTaskRutaExamen(getContext(), idEstudiante, evaluarRuta.getSeccion(), evaluarRuta.getNivel(), evaluarRuta.getBloque(), 0, true);
            rutaExamen.execute();
            dismiss();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}