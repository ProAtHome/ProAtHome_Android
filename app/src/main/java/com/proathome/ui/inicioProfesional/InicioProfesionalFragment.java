package com.proathome.ui.inicioProfesional;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterSesionesProfesional;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.ui.fragments.DetallesGestionarProfesionalFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.ui.sesionesProfesional.SesionesProfesionalFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InicioProfesionalFragment extends Fragment {

    public static ComponentAdapterSesionesProfesional myAdapter;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    private int idProfesional = 0;
    private ProgressDialog progressDialog;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();
        getSesiones();
        configAdapter();
        configRecyclerView();

        return root;
    }

    private void getSesiones(){
        progressDialog = ProgressDialog.show(getContext(), "Cargando Servicios", "Espere, por favor...");
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            progressDialog.dismiss();
            JSONObject data = new JSONObject(response);
            if(data.getBoolean("respuesta")){
                try{
                    JSONArray jsonArray = data.getJSONArray("mensaje");

                    if(jsonArray.length() == 0)
                        lottieAnimationView.setVisibility(View.VISIBLE);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        myAdapter.add(DetallesSesionProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("nombreCliente"), object.getString("descripcion"), object.getString("correo"), object.getString("foto"),  object.getString("tipoServicio"), object.getString("horario"),
                                "Soy yo", object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getInt("idCliente")));
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!",  data.getString("mensaje"), false, null, null);
        }, APIEndPoints.GET_SESIONES_PROFESIONAL  + this.idProfesional + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterSesionesProfesional(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
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