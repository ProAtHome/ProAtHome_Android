package com.proathome.Views.profesional.navigator.ayudaProfesional;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.Interfaces.profesional.Ayuda.AyudaPresenter;
import com.proathome.Interfaces.profesional.Ayuda.AyudaView;
import com.proathome.Presenters.profesional.AyudaPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterTicket;
import com.proathome.Views.fragments_compartidos.FragmentTicketAyuda;
import com.proathome.Views.fragments_compartidos.NuevoTicketFragment;
import com.proathome.Utils.pojos.ComponentTicket;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AyudaProfesionalFragment extends Fragment implements AyudaView {

    private Unbinder mUnbinder;
    public static ComponentAdapterTicket componentAdapterTicket;
    public static LottieAnimationView lottieAnimationView;
    public static RecyclerView recyclerView;
    public static AyudaProfesionalFragment ayudaProfesionalFragment;
    private ProgressDialog progressDialog;
    private AyudaPresenter ayudaPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ayudaPresenter = new AyudaPresenterImpl(this);

        recyclerView = view.findViewById(R.id.recyclerTopicos);
        lottieAnimationView = view.findViewById(R.id.animation_viewAyuda);
        ayudaProfesionalFragment = this;

        configAdapter();
        configRecyclerView();
        ayudaPresenter.obtenerTickets(SharedPreferencesManager.getInstance(getContext()).getIDProfesional(), SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());

        return view;
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
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setVisibilityLottie(int visibilityLottie) {
        lottieAnimationView.setVisibility(visibilityLottie);
    }

    @Override
    public void setAdapterTickets(JSONObject jsonObject) {
        try {
            componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                    ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                    jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                    jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                    jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario"), jsonObject.getString("categoria")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        mUnbinder.unbind();
    }

}
