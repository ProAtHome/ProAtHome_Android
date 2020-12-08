package com.proathome.ui.ayuda;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterTicket;
import com.proathome.fragments.NuevoTicketFragment;
import com.proathome.servicios.ayuda.ServicioTaskObtenerTickets;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AyudaFragment extends Fragment {

    private Unbinder mUnbinder;
    private int idEstudiante;
    public static ComponentAdapterTicket componentAdapterTicket;
    public static LottieAnimationView lottieAnimationView;
    public static RecyclerView recyclerView;
    public static AyudaFragment ayudaFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.recyclerTopicos);
        lottieAnimationView = view.findViewById(R.id.animation_viewAyuda);
        ayudaFragment = this;

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst())
            this.idEstudiante = fila.getInt(0);
        else
            baseDeDatos.close();

        baseDeDatos.close();

        configAdapter();
        configRecyclerView();

        ServicioTaskObtenerTickets obtenerTickets = new ServicioTaskObtenerTickets(getContext(), this.idEstudiante);
        obtenerTickets.execute();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.nuevoTopico)
    public void onClick(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        NuevoTicketFragment ticketAyuda = new NuevoTicketFragment();
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
