package com.proathome.ui.cerrarSesion;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.proathome.R;
import com.proathome.adapters.ComponentAdapter;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.ServicioTaskSesionesEstudiante;
import com.proathome.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CerrarSesionFragment extends Fragment {

    private CerrarSesionViewModel cerrarSesionViewModel;
    @BindView(R.id.recyclerViewC)
    RecyclerView recyclerView;
    public static ComponentAdapter myAdapter;
    private String clasesHttpAddress = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    private ServicioTaskSesionesEstudiante sesionesTask;
    private Unbinder mUnbinder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cerrarSesionViewModel = ViewModelProviders.of(this).get(CerrarSesionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);
        int idCliente = 0;
        if (fila.moveToFirst()) {

            idCliente = fila.getInt(0);
            configAdapter();
            configRecyclerView();
            baseDeDatos.close();

        } else {

            baseDeDatos.close();

        }
        return root;
    }

    public void configAdapter(){

        myAdapter = new ComponentAdapter(new ArrayList<>());

    }

    private void configRecyclerView(){

        recyclerView.setAdapter(myAdapter);

    }

}