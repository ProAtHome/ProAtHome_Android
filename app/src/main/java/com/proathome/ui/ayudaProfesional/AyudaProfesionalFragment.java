package com.proathome.ui.ayudaProfesional;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterTicket;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.fragments.FragmentTicketAyuda;
import com.proathome.ui.fragments.NuevoTicketFragment;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.utils.ComponentTicket;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AyudaProfesionalFragment extends Fragment {

    private Unbinder mUnbinder;
    private int idProfesional;
    public static ComponentAdapterTicket componentAdapterTicket;
    public static LottieAnimationView lottieAnimationView;
    public static RecyclerView recyclerView;
    public static AyudaProfesionalFragment ayudaProfesionalFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.recyclerTopicos);
        lottieAnimationView = view.findViewById(R.id.animation_viewAyuda);
        ayudaProfesionalFragment = this;

        this.idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();
        configAdapter();
        configRecyclerView();
        obtenerTickets();

        return  view;
    }

    private void obtenerTickets(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getBoolean("sinTickets")){
                            lottieAnimationView.setVisibility(View.VISIBLE);
                        } else{
                            lottieAnimationView.setVisibility(View.INVISIBLE);
                            componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                    ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                    jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                    jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                    jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario"), jsonObject.getString("categoria")));
                        }
                    }
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrió un error inseperado, intenta nuevamente.", false, null, null);
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_TICKETS_PROFESIONAL + this.idProfesional, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @OnClick(R.id.nuevoTopico)
    public void onClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoUsuario", Constants.TIPO_USUARIO_PROFESIONAL);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        NuevoTicketFragment ticketAyuda = new NuevoTicketFragment();
        ticketAyuda.setArguments(bundle);
        ticketAyuda.show(fragmentTransaction, "Ticket");
    }

    public static void configAdapter(){
        componentAdapterTicket = new ComponentAdapterTicket(new ArrayList<>());
    }

    public static void configRecyclerView(){
        recyclerView.setAdapter(componentAdapterTicket);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
