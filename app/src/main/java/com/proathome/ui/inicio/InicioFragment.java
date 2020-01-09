package com.proathome.ui.inicio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.ServicioTaskSesionesEstudiante;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapter;
import com.proathome.utils.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    public static ComponentAdapter myAdapter;
    private String clasesHttpAddress = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    private ServicioTaskSesionesEstudiante sesionesTask;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        ButterKnife.bind(this, root);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);
        int idCliente = 0;
        if (fila.moveToFirst()) {

            idCliente = fila.getInt(0);
            sesionesTask = new ServicioTaskSesionesEstudiante(getContext(), clasesHttpAddress, idCliente);
            sesionesTask.execute();
            configAdapter();
            configRecyclerView();
            baseDeDatos.close();

        } else {

            System.out.println("No hay sesión.");
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