package com.proathome.ui.sesionesProfesional;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
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
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterGestionarProfesional;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.fragments.BuscarSesionFragment;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.ui.fragments.DetallesGestionarProfesionalFragment;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesProfesionalFragment extends Fragment {

    private Unbinder mUnbinder;
    public static ComponentAdapterGestionarProfesional myAdapter;
    public static LottieAnimationView lottieAnimationView;
    private int idProfesional = 0;
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

        this.idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSesiones();
        configAdapter();
        configRecyclerView();
    }

    private void getSesiones(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if(!response.equals("[]")){
                    try{
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(!object.getBoolean("finalizado")){
                                myAdapter.add(DetallesGestionarProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                                        object.getString("nombreCliente"), object.getString("correo"), object.getString("descripcion"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                        object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                                        object.getString("fecha"), object.getString("tipoPlan"), object.getString("foto")));
                            }
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else{
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    SweetAlert.showMsg(getContext(),  SweetAlert.WARNING_TYPE, "¡AVISO!",  "Usuario sin servicios disponibles.", false, null, null);
                }
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!",  "Error en el servidor, intente de nuevo más tarde.", false, null, null);
        }, APIEndPoints.GET_SESIONES_PROFESIONAL  + this.idProfesional, WebServicesAPI.GET, null);
        webServicesAPI.execute();
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
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}