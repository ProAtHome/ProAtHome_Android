package com.proathome.examen;

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
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioExamenDiagnostico;
import com.proathome.fragments.FragmentRutaGenerada;
import com.proathome.utils.Constants;
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
                            salirMsg();
                        }))
                        .setOnCancelListener(dialog -> {
                            salirMsg();
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

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"sesion", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

            int idCliente = 0;
            if (fila.moveToFirst()) {
                idCliente = fila.getInt(0);
                ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this, idCliente,
                        Constants.INFO_EXAMEN_FINAL, validarRespuestas());
                examen.execute();
                ServicioExamenDiagnostico examenGuardar = new ServicioExamenDiagnostico(this, idCliente, Diagnostico7.this,
                        Diagnostico7.class, Constants.EXAMEN_FINALIZADO, validarRespuestas(), 65);
                examenGuardar.execute();
            }else{
                baseDeDatos.close();
            }

            baseDeDatos.close();

        });

    }

    public void salirMsg(){
        new SweetAlert(this, SweetAlert.NORMAL_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡NO TE RINDAS!")
                .show();
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
