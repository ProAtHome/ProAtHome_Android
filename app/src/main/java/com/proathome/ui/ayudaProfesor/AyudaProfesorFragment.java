package com.proathome.ui.ayudaProfesor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioTaskAyuda;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AyudaProfesorFragment extends Fragment {

    private Unbinder mUnbinder;
    @BindView(R.id.mensaje)
    TextInputEditText mensaje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return  view;
    }

    @OnClick(R.id.enviar)
    public void onClick(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesionProfesor",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            int idProfesor = fila.getInt(0);
            if(mensaje.getText().toString().trim().equalsIgnoreCase(""))
                Toast.makeText(getContext(), "Escribe un comentario.", Toast.LENGTH_LONG).show();
            else{
                ServicioTaskAyuda servicioTaskAyuda = new ServicioTaskAyuda(getContext(), idProfesor,
                        mensaje.getText().toString(), Constants.TIPO_PROFESOR);
                servicioTaskAyuda.execute();
                mensaje.setText("");
                Toast.makeText(getContext(), "Mensaje enviado...", Toast.LENGTH_LONG).show();
            }
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();
    }

}
