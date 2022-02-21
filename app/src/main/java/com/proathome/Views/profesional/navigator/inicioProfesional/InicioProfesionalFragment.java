package com.proathome.Views.profesional.navigator.inicioProfesional;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.Interfaces.profesional.Inicio.InicioFragmentPresenter;
import com.proathome.Interfaces.profesional.Inicio.InicioFragmentView;
import com.proathome.Presenters.profesional.inicio.InicioFragmentPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterSesionesProfesional;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InicioProfesionalFragment extends Fragment implements InicioFragmentView {

    public static ComponentAdapterSesionesProfesional myAdapter;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    private ProgressDialog progressDialog;
    private InicioFragmentPresenter inicioFragmentPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        inicioFragmentPresenter = new InicioFragmentPresenterImpl(this);
        inicioFragmentPresenter.getSesiones(SharedPreferencesManager.getInstance(getContext()).getIDProfesional(), SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());

        configAdapter();
        configRecyclerView();

        return root;
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterSesionesProfesional(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando Servicios", "Espere, por favor...");
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
    public void setAdapterSesiones(JSONObject object) {
        try {
            myAdapter.add(DetallesSesionProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("nombreCliente"), object.getString("descripcion"), object.getString("correo"), object.getString("foto"),  object.getString("tipoServicio"), object.getString("horario"),
                    "Soy yo", object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                    object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getInt("idCliente")));
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