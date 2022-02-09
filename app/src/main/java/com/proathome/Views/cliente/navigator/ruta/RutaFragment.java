package com.proathome.Views.cliente.navigator.ruta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.cliente.ControladorRutaSecciones;
import com.proathome.Servicios.cliente.ServiciosExamenDiagnostico;
import com.proathome.Views.cliente.RutaAvanzado;
import com.proathome.Views.cliente.RutaBasico;
import com.proathome.R;
import com.proathome.Views.cliente.RutaIntermedio;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;

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

        idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        ServiciosExamenDiagnostico.getEstatusExamen(idCliente, getContext());
        getEstadoRuta();

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
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject rutaJSON = data.getJSONObject("mensaje");
                    int estado = rutaJSON.getInt("estado");
            /*if(estado == Constants.INICIO_RUTA){
    }else */        if(estado == Constants.RUTA_ENCURSO) {
                        int idBloque = rutaJSON.getInt("idBloque");
                        int idNivel = rutaJSON.getInt("idNivel");
                        int idSeccion = rutaJSON.getInt("idSeccion");
                        ControladorRutaSecciones rutaAprendizaje = new ControladorRutaSecciones(getContext(), idBloque, idNivel, idSeccion);
                        rutaAprendizaje.evaluarRuta();
                    }
                }else
                    Toast.makeText(getContext(), data.getString("mensaje"), Toast.LENGTH_LONG).show();
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_ESTADO_RUTA + this.idCliente + "/" + SECCIONES + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
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