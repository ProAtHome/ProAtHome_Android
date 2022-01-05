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
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.ControladorRutaSecciones;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.servicios.cliente.ServiciosExamenDiagnostico;
import com.proathome.ui.RutaAvanzado;
import com.proathome.ui.RutaBasico;
import com.proathome.R;
import com.proathome.ui.RutaIntermedio;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.ui.examen.Diagnostico1;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
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
            ServiciosExamenDiagnostico.getEstatusExamen(idCliente, getContext());
            getEstadoRuta();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

        imgExamen.setOnClickListener(v ->{
            new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                    .setTitle("EXÁMEN DIAGNÓSTICO")
                    .setMessage("Realiza nuestro examen diagnostico para poderte colocar en la mejor posición de acuerdo a tus conocimientos.")
                    .setNegativeButton("Cerrar", (dialog, which) -> {

                    })
                    .setPositiveButton("EVALUAR", ((dialog, which) -> {
                        ServiciosExamenDiagnostico.reiniciarExamen(idCliente, getContext());
                    }))
                    .setOnCancelListener(dialog -> {

                    })
                    .show();
        });

        return root;
    }

    private void getEstadoRuta(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject rutaJSON = new JSONObject(response);
                int estado = rutaJSON.getInt("estado");
            /*if(estado == Constants.INICIO_RUTA){
    }else */    if(estado == Constants.RUTA_ENCURSO) {
                    int idBloque = rutaJSON.getInt("idBloque");
                    int idNivel = rutaJSON.getInt("idNivel");
                    int idSeccion = rutaJSON.getInt("idSeccion");
                    ControladorRutaSecciones rutaAprendizaje = new ControladorRutaSecciones(getContext(), idBloque, idNivel, idSeccion);
                    rutaAprendizaje.evaluarRuta();
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_ESTADO_RUTA + this.idCliente + "/" + SECCIONES, WebServicesAPI.GET, null);
        webServicesAPI.execute();
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