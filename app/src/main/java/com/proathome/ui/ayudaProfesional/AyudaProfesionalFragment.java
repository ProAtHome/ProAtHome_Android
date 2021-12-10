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
import com.proathome.ui.fragments.NuevoTicketFragment;
import com.proathome.servicios.ayuda.ServicioTaskObtenerTickets;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.utils.Constants;
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

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesionProfesional", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);

        if(fila.moveToFirst())
            this.idProfesional = fila.getInt(0);
        else
            baseDeDatos.close();

        baseDeDatos.close();

        configAdapter();
        configRecyclerView();

        ServicioTaskObtenerTickets obtenerTickets = new ServicioTaskObtenerTickets(getContext(), this.idProfesional,
                Constants.TIPO_USUARIO_PROFESIONAL);
        obtenerTickets.execute();

        return  view;
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
