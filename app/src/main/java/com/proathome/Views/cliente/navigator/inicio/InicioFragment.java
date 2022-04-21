package com.proathome.Views.cliente.navigator.inicio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentPresenter;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentView;
import com.proathome.Presenters.cliente.inicio.InicioFragmentPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapter;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.ServicioCliente;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InicioFragment extends Fragment implements InicioFragmentView {

    public static ComponentAdapter myAdapter;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    private InicioFragmentPresenter inicioFragmentPresenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        inicioFragmentPresenter = new InicioFragmentPresenterImpl(this);

        if(NetworkValidate.validate(getContext()))
            inicioFragmentPresenter.getSesiones(SharedPreferencesManager.getInstance(getContext()).getIDCliente(), getContext());
        else
            Toast.makeText(getContext(), "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();

        configAdapter();
        configRecyclerView();

        return root;
    }

    /*
    private void iniciarProcesoRuta() throws JSONException {
        JSONObject parametros = new JSONObject();
        parametros.put("idCliente", this.idCliente);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

        }, APIEndPoints.INICIAR_PROCESO_RUTA, WebServicesAPI.POST, parametros);
        webServicesAPI.execute();
    }*/


    public void configAdapter(){
        myAdapter = new ComponentAdapter(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando Sesiones", "Espere, por favor...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!",  error, false, null, null);
    }

    @Override
    public void setLottieVisible() {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAdapter(JSONObject object) {
        try {
            myAdapter.add(DetallesFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                    object.getString("profesional"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                    object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getString("fecha"),
                    object.getString("fotoProfesional"), object.getString("descripcionProfesional"), object.getString("correoProfesional"), object.getBoolean("sumar"),
                    object.getString("tipoPlan"), object.getInt("profesionales_idprofesionales"), object.getBoolean("finalizado")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}