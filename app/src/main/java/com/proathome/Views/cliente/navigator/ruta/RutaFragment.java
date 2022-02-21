package com.proathome.Views.cliente.navigator.ruta;

import android.app.ActivityOptions;
import android.content.Intent;
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
import com.proathome.Interfaces.cliente.Ruta.RutaPresenter;
import com.proathome.Interfaces.cliente.Ruta.RutaView;
import com.proathome.Presenters.cliente.RutaPresenterImpl;
import com.proathome.Servicios.cliente.ControladorRutaSecciones;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.RutaAvanzado;
import com.proathome.Views.cliente.RutaBasico;
import com.proathome.R;
import com.proathome.Views.cliente.RutaIntermedio;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.cliente.examen.Diagnostico1;
import com.proathome.Views.cliente.examen.Diagnostico2;
import com.proathome.Views.cliente.examen.Diagnostico3;
import com.proathome.Views.cliente.examen.Diagnostico4;
import com.proathome.Views.cliente.examen.Diagnostico5;
import com.proathome.Views.cliente.examen.Diagnostico6;
import com.proathome.Views.cliente.examen.Diagnostico7;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaFragment extends Fragment implements RutaView {

    private Unbinder mUnbinder;
    public static final int SECCIONES = 1;
    public static MaterialButton btnBasico;
    public static MaterialButton btnIntermedio;
    public static MaterialButton btnAvanzado;
    public static TextView textBasico;
    public static TextView textIntermedio;
    public static TextView textAvanzado;
    private RutaPresenter rutaPresenter;

    @BindView(R.id.imgButtonExamen)
    public ImageButton imgExamen;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ruta, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        rutaPresenter = new RutaPresenterImpl(this);

        btnBasico = root.findViewById(R.id.btnBasico);
        btnIntermedio = root.findViewById(R.id.btnIntermedio);
        btnAvanzado = root.findViewById(R.id.btnAvanzado);
        textBasico = root.findViewById(R.id.textBasico);
        textIntermedio = root.findViewById(R.id.textIntermedio);
        textAvanzado = root.findViewById(R.id.textAvanzado);

        rutaPresenter.getEstatusExamen(SharedPreferencesManager.getInstance(getContext()).getIDCliente(), SharedPreferencesManager.getInstance(getContext()).getTokenCliente());
        rutaPresenter.getEstadoRuta(SharedPreferencesManager.getInstance(getContext()).getIDCliente(), SECCIONES, SharedPreferencesManager.getInstance(getContext()).getTokenCliente());

        return root;
    }

    @OnClick({R.id.btnBasico, R.id.btnIntermedio, R.id.btnAvanzado, R.id.imgButtonExamen})
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
            case R.id.imgButtonExamen:
                btnExamen();
        }
    }

    private void btnExamen(){
        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                .setTitle("EXÁMEN DIAGNÓSTICO")
                .setMessage("Realiza nuestro examen diagnostico para poderte colocar en la mejor posición de acuerdo a tus conocimientos.")
                .setNegativeButton("Cerrar", (dialog, which) -> {

                })
                .setPositiveButton("EVALUAR", ((dialog, which) -> {
                    rutaPresenter.reiniciarExamen(SharedPreferencesManager.getInstance(getContext()).getIDCliente());
                }))
                .setOnCancelListener(dialog -> {

                })
                .show();
    }

    @Override
    public void showMsg(String titulo, String mensaje, int tipo) {
        SweetAlert.showMsg(getContext(), tipo, titulo, mensaje, false, null, null);
    }

    @Override
    public void successReinicio() {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", "Cuestionario reiniciado, suerte.", true, "VAMOS", ()->{
            imgExamen.setVisibility(View.INVISIBLE);
            startActivity(new Intent(getContext(), Diagnostico1.class));
        });
    }

    @Override
    public void setRutaActual(int idSeccion, int idNivel, int idBloque) {
        ControladorRutaSecciones rutaAprendizaje = new ControladorRutaSecciones(getContext(), idBloque, idNivel, idSeccion);
        rutaAprendizaje.evaluarRuta();
    }

    @Override
    public void setVisibilityButtonExamen(int visibility) {
        imgExamen.setVisibility(visibility);
    }

    @Override
    public void showDialogExamen() {
        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                .setTitle("CUSTIONARIO DIAGNÓSTICO")
                .setMessage("Tenemos un cuestionario para evaluar tus habilidades y colocarte en la ruta de aprendizaje de" +
                        " acuerdo a tus conocimientos, si no deseas realizar el Cuestionario sigue tu camino desde un inicio.")
                .setNegativeButton("Cerrar", (dialog, which) -> {
                    rutaPresenter.cancelarExamen(SharedPreferencesManager.getInstance(getContext()).getIDCliente());
                })
                .setPositiveButton("EVALUAR", ((dialog, which) -> {
                    startActivity(new Intent(getContext(), Diagnostico1.class));
                }))
                .setOnCancelListener(dialog -> {

                })
                .show();
    }

    @Override
    public void continuarExamen(int pregunta) {
        if(pregunta == 10)
            startActivity(new Intent(getContext(), Diagnostico2.class));
        else if(pregunta == 20)
            startActivity(new Intent(getContext(), Diagnostico3.class));
        else if(pregunta == 30)
            startActivity(new Intent(getContext(), Diagnostico4.class));
        else if(pregunta == 40)
            startActivity(new Intent(getContext(), Diagnostico5.class));
        else if(pregunta == 50)
            startActivity(new Intent(getContext(), Diagnostico6.class));
        else if(pregunta == 60)
            startActivity(new Intent(getContext(), Diagnostico7.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}