package com.proathome.ui.sesiones;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterGestionar;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.ServicioTaskSesionesEstudiante;
import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.utils.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesFragment extends Fragment {

    public static ComponentAdapterGestionar myAdapter;
    private String clasesHttpAddress = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    private ServicioTaskSesionesEstudiante sesionesTask;
    private Unbinder mUnbinder;
    private int idCliente = 0;
    @BindView(R.id.recyclerGestionar)
    RecyclerView recyclerView;
    @BindView(R.id.fabNuevaSesion)
    FloatingActionButton fabNuevaSesion;
    @BindView(R.id.fabActualizar)
    FloatingActionButton fabActualizar;

    @Override
    public void onResume() {

        super.onResume();
        sesionesTask = new ServicioTaskSesionesEstudiante(getContext(), clasesHttpAddress, this.idCliente, Constants.SESIONES_GESTIONAR);
        sesionesTask.execute();
        configAdapter();
        configRecyclerView();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sesiones, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);
        if (fila.moveToFirst()) {

            this.idCliente = fila.getInt(0);
            baseDeDatos.close();

        } else {

            baseDeDatos.close();

        }

        return root;

    }

    public void configAdapter(){

        myAdapter = new ComponentAdapterGestionar(new ArrayList<>());

    }

    private void configRecyclerView(){

        recyclerView.setAdapter(myAdapter);

    }

    @OnClick({R.id.fabNuevaSesion, R.id.fabActualizar})
    public void onClicked(View view){

        switch (view.getId()){

            case R.id.fabNuevaSesion:
                NuevaSesionFragment nueva = new NuevaSesionFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                nueva.show(transaction, NuevaSesionFragment.TAG);
                break;
            case R.id.fabActualizar:
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;

        }


    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }
}