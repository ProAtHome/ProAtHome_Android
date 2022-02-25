package com.proathome.Views.profesional.navigator.sesionesProfesional;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.Interfaces.profesional.Sesiones.SesionesPresenter;
import com.proathome.Interfaces.profesional.Sesiones.SesionesView;
import com.proathome.Presenters.profesional.SesionesPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterGestionarProfesional;
import com.proathome.Views.profesional.fragments.BuscarSesionFragment;
import com.proathome.Views.profesional.fragments.DetallesGestionarProfesionalFragment;
import com.proathome.Utils.PermisosUbicacion;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesProfesionalFragment extends Fragment implements SesionesView {

    private Unbinder mUnbinder;
    public static ComponentAdapterGestionarProfesional myAdapter;
    public static LottieAnimationView lottieAnimationView;
    private SesionesPresenter sesionesPresenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.recyclerGestionarProfesional)
    RecyclerView recyclerView;
    @BindView(R.id.fabNuevaSesion)
    FloatingActionButton fabNuevaSesion;
    @BindView(R.id.fabActualizar)
    FloatingActionButton fabActualizar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sesiones_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        sesionesPresenter = new SesionesPresenterImpl(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        sesionesPresenter.getSesiones(SharedPreferencesManager.getInstance(getContext()).getIDProfesional(), SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());
        configAdapter();
        configRecyclerView();
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterGestionarProfesional(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    private void showAlert() {
        PermisosUbicacion.showAlert(getContext(), SweetAlert.PROFESIONAL);
    }

    @OnClick({R.id.fabNuevaSesion, R.id.fabActualizar})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.fabNuevaSesion:
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    showAlert();
                } else {
                    BuscarSesionFragment nueva = new BuscarSesionFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    nueva.show(transaction, BuscarSesionFragment.TAG);
                }
                break;
            case R.id.fabActualizar:
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!",  error, false, null, null);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void setVisibilityLottie(int visibilityLottie) {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAdapterSesiones(JSONObject object) {
        try {
            myAdapter.add(DetallesGestionarProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                    object.getString("nombreCliente"), object.getString("correo"), object.getString("descripcion"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                    object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                    object.getString("fecha"), object.getString("tipoPlan"), object.getString("foto")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        mUnbinder.unbind();
    }

}