package com.proathome.ui.ruta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.ui.RutaAvanzado;
import com.proathome.ui.RutaBasico;
import com.proathome.R;
import com.proathome.ui.RutaIntermedio;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ServicioExamenDiagnostico;
import com.proathome.servicios.cliente.ServicioTaskRuta;
import com.proathome.ui.examen.Diagnostico1;
import com.proathome.utils.Constants;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaFragment extends Fragment {

    private Unbinder mUnbinder;
    public static ImageButton imgExamen;
    private int idCliente = 0;
    public static final int SECCIONES = 1;
    public static MaterialButton btnBasico;
    public static MaterialButton btnIntermedio;
    public static MaterialButton btnAvanzado;
    public static TextView textBasico;
    public static TextView textIntermedio;
    public static TextView textAvanzado;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ruta, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        imgExamen = root.findViewById(R.id.imgButtonExamen);
        btnBasico = root.findViewById(R.id.btnBasico);
        btnIntermedio = root.findViewById(R.id.btnIntermedio);
        btnAvanzado = root.findViewById(R.id.btnAvanzado);
        textBasico = root.findViewById(R.id.textBasico);
        textIntermedio = root.findViewById(R.id.textIntermedio);
        textAvanzado = root.findViewById(R.id.textAvanzado);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
            ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(getContext(), idCliente,
                    Constants.ESTATUS_EXAMEN);
            examen.execute();
            ServicioTaskRuta ruta = new ServicioTaskRuta(getContext(), idCliente, Constants.ESTADO_RUTA, SECCIONES);
            ruta.execute();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

        imgExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Tenemos un exámen para evaluar tus habilidades y colocarte en la ruta de aprendizaje de" +
                            " acuerdo a tus conocimientos, si no deseas realizar el exámen sigue tu camino desde un inicio.")
                    .setNegativeButton("Cerrar", (dialog, which) -> {

                    })
                    .setPositiveButton("EVALUAR", ((dialog, which) -> {
                        ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(getContext(),
                                idCliente, Constants.REINICIAR_EXAMEN);
                        examen.execute();
                        Intent intent = new Intent(getContext(), Diagnostico1.class);
                        startActivity(intent);
                    }))
                    .setOnCancelListener(dialog -> {

                    })
                    .show();
        });

        return root;
    }

    @OnClick({R.id.btnBasico, R.id.btnIntermedio, R.id.btnAvanzado})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btnBasico:
                intent = new Intent(getContext(), RutaBasico.class);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
            case R.id.btnIntermedio:
                intent = new Intent(getContext(), RutaIntermedio.class);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
            case R.id.btnAvanzado:
                intent = new Intent(getContext(), RutaAvanzado.class);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}