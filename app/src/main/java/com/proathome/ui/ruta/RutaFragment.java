package com.proathome.ui.ruta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.proathome.RutaAvanzado;
import com.proathome.RutaBasico;
import com.proathome.R;
import com.proathome.RutaIntermedio;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioExamenDiagnostico;
import com.proathome.utils.Constants;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RutaFragment extends Fragment {

    private RutaViewModel rutaViewModel;
    private Unbinder mUnbinder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rutaViewModel = ViewModelProviders.of(this).get(RutaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ruta, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        int idCliente = 0;
        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
            ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(getContext(), idCliente, Constants.ESTATUS_EXAMEN);
            examen.execute();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

        return root;
    }

    @OnClick({R.id.btnBasico, R.id.btnIntermedio, R.id.btnAvanzado})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btnBasico:
                intent = new Intent(getContext(), RutaBasico.class);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
            case R.id.btnIntermedio:
                intent = new Intent(getContext(), RutaIntermedio.class);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
            case R.id.btnAvanzado:
                intent = new Intent(getContext(), RutaAvanzado.class);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }
  }

}