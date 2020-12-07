package com.proathome.fragments;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.proathome.R;
import com.proathome.utils.ComponentTicket;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentTicketAyuda extends DialogFragment {

    private Unbinder mUnbinder;
    private static ComponentTicket mComponentTicket;

    public FragmentTicketAyuda() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
    }

    public static ComponentTicket getmInstance(String tituloTopico, String estatus, String fechaCreacion){
        mComponentTicket = new ComponentTicket();
        mComponentTicket.setTituloTopico(tituloTopico);
        mComponentTicket.setEstatus(estatus);
        mComponentTicket.setFechaCreacion(fechaCreacion);

        return mComponentTicket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_ayuda, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}