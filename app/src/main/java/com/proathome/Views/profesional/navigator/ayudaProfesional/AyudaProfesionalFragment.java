package com.proathome.Views.profesional.navigator.ayudaProfesional;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterTicket;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Views.fragments_compartidos.FragmentTicketAyuda;
import com.proathome.Views.fragments_compartidos.NuevoTicketFragment;
import com.proathome.Utils.pojos.ComponentTicket;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;

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
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONArray jsonArray = data.getJSONArray("mensaje");
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
                    Toast.makeText(getContext(), data.getString("mensaje"), Toast.LENGTH_LONG).show();
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_TICKETS_PROFESIONAL + this.idProfesional + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenProfesional(), WebServicesAPI.GET, null);
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
