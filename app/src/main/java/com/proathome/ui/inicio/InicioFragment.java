package com.proathome.ui.inicio;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapter;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesGestionarFragment;
import com.proathome.ui.sesiones.SesionesFragment;
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

public class InicioFragment extends Fragment {

    public static ComponentAdapter myAdapter;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    private int idCliente = 0;
    private ProgressDialog progressDialog;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        getSesiones();
        configAdapter();
        configRecyclerView();

        return root;
    }

    private void iniciarProcesoRuta() throws JSONException {
        JSONObject parametros = new JSONObject();
        parametros.put("idCliente", this.idCliente);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

        }, APIEndPoints.INICIAR_PROCESO_RUTA, WebServicesAPI.POST, parametros);
        webServicesAPI.execute();
    }

    private void getSesiones(){
        progressDialog = ProgressDialog.show(getContext(), "Cargando Sesiones", "Espere, por favor...");
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            progressDialog.dismiss();
            if(response != null){
                try{
                    iniciarProcesoRuta();

                    if(!response.equals("null")){
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("sesiones");

                        if(jsonArray.length() == 0)
                            lottieAnimationView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            myAdapter.add(DetallesFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                                    object.getString("profesional"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                    object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getString("fecha"),
                                    object.getString("fotoProfesional"), object.getString("descripcionProfesional"), object.getString("correoProfesional"), object.getBoolean("sumar"),
                                    object.getString("tipoPlan"), object.getInt("profesionales_idprofesionales")));
                        }
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡AVISO!", "Usuario sin servicios disponibles.", false, null, null);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!",  "Error del servidor, intente de nuevo más tarde.", false, null, null);
        }, APIEndPoints.GET_SESIONES_CLIENTE  + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapter(new ArrayList<>());
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