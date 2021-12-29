package com.proathome.ui.inicioProfesional;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);
        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(getContext(),
                "sesionProfesional", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);
        if (fila.moveToFirst()) {
            idProfesional = fila.getInt(0);
            getSesiones();
            configAdapter();
            configRecyclerView();
            baseDeDatos.close();
        } else {
            baseDeDatos.close();
        }

        return root;
    }

    private void getSesiones(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if(!response.equals("[]")){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            myAdapter.add(DetallesSesionProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("nombreCliente"), object.getString("descripcion"), object.getString("correo"), object.getString("foto"),  object.getString("tipoServicio"), object.getString("horario"),
                                        "Soy yo", object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                        object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getInt("idCliente")));
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else{
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡AVISO!", "Usuario sin servicios disponibles.", false, null, null);
                }
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Error en el servidor, intente de nuevo más tarde.", false, null, null);
        }, APIEndPoints.GET_SESIONES_PROFESIONAL  + this.idProfesional, WebServicesAPI.GET, null);
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
    }
}