package com.proathome.ui.examen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.servicios.cliente.ServiciosExamenDiagnostico;
import com.proathome.ui.fragments.FragmentRutaGenerada;
import com.proathome.utils.Constants;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Diagnostico7 extends AppCompatActivity {

    private Unbinder mUnbinder;
    private boolean finalizado = false;
    public static boolean ultimaPagina = false;
    @BindView(R.id.resp1)
    TextInputEditText resp1;
    @BindView(R.id.resp2)
    TextInputEditText resp2;
    @BindView(R.id.resp3)
    TextInputEditText resp3;
    @BindView(R.id.resp4)
    TextInputEditText resp4;
    @BindView(R.id.resp5)
    TextInputEditText resp5;
    @BindView(R.id.btnFinalizar)
    MaterialButton btnFinalizar;
    @BindView(R.id.cerrarExamen)
    FloatingActionButton cerrarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico7);
        mUnbinder = ButterKnife.bind(this);

        cerrarExamen.setOnClickListener(v ->{
            if(finalizado){
                finish();
            }else{
                new MaterialAlertDialogBuilder(this,
                        R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                        .setTitle("EXÁMEN DIAGNÓSTICO")
                        .setMessage("Al salir durante el examen perderás el progreso de ésta sección.")
                        .setNegativeButton("Salir", (dialog, which) -> {
                            finish();
                        })
                        .setPositiveButton("Cancelar", ((dialog, which) -> {
                            SweetAlert.showMsg(Diagnostico7.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, null);
                        }))
                        .setOnCancelListener(dialog -> {
                            SweetAlert.showMsg(Diagnostico7.this, SweetAlert.NORMAL_TYPE, "¡EY!", "¡NO TE RINDAS!", false, null, null);
                        })
                        .show();
            }
        });

        btnFinalizar.setOnClickListener(v ->{
            resp1.setEnabled(false);
            resp2.setEnabled(false);
            resp3.setEnabled(false);
            resp4.setEnabled(false);
            resp5.setEnabled(false);
            btnFinalizar.setEnabled(false);
            finalizado = true;

            Diagnostico7.ultimaPagina = true;

            FragmentRutaGenerada rutaGenerada = new FragmentRutaGenerada();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            rutaGenerada.show(fragmentTransaction, "Ruta");

            int idCliente = SharedPreferencesManager.getInstance(this).getIDCliente();
            ServiciosExamenDiagnostico.getInfoExamenFinal(idCliente, validarRespuestas(), Diagnostico7.this);
            ServiciosExamenDiagnostico.actualizarEstatusExamen(Constants.EXAMEN_FINALIZADO, idCliente, validarRespuestas(), 65, Diagnostico7.this,Diagnostico7.this, Diagnostico7.class);
        });

    }

    public int validarRespuestas(){
        int puntuacion = 0;
        if(resp1.getText().toString().trim().equalsIgnoreCase("Nanorobotics is the emerging" +
                " technology field creating machines or robots whose components are at or close to the scale of a nanometer"))
            puntuacion++;
        if(resp2.getText().toString().trim().equalsIgnoreCase("Nanobots, Nanoids, Nanites," +
                " Nanomachines and Nanomites"))
            puntuacion++;
        if(resp3.getText().toString().trim().equalsIgnoreCase("Cancer"))
            puntuacion++;
        if(resp4.getText().toString().trim().equalsIgnoreCase("Yes, It is"))
            puntuacion++;
        if(resp5.getText().toString().trim().equalsIgnoreCase("Yes, It is"))
            puntuacion++;

        return puntuacion;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
