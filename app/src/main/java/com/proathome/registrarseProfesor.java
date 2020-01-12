package com.proathome;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.proathome.controladores.ServicioTaskRegistroEstudiante;
import com.proathome.controladores.ServicioTaskRegistroProfesor;
import com.proathome.utils.Constants;

import java.util.Calendar;

import butterknife.BindView;

public class registrarseProfesor extends AppCompatActivity {

    private Intent intent;
    private int mDayIni, mMonthIni, mYearIni, sDayIni, sMonthIni, sYearIni;
    public static final int DATE_ID = 0;
    public Calendar calendar = Calendar.getInstance();
    EditText nombreET;
    EditText fechaET;
    EditText edadET;
    EditText correoET;
    EditText contraET;
    private ServicioTaskRegistroProfesor servicioTaskRegistroProfesor;
    private final String registrarProfesorREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/agregarProfesor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_profesor);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sMonthIni = calendar.get(Calendar.MONTH);
        sYearIni = calendar.get(Calendar.YEAR);
        nombreET = (EditText)findViewById(R.id.nombreET_RP);
        fechaET = (EditText)findViewById(R.id.fechaET_RP);
        edadET = (EditText)findViewById(R.id.edadET_RP);
        correoET = (EditText)findViewById(R.id.correoET_RP);
        contraET = (EditText)findViewById(R.id.contraET_RP);

    }

    private void colocarFecha(){

        if(mMonthIni > 9 && mDayIni < 9){

            fechaET.setText((mYearIni) + "-" + (mMonthIni + 1) + "-" + "0" + mDayIni);

        }else if(mMonthIni < 9 && mDayIni > 9){

            fechaET.setText((mYearIni) + "-" + "0" + (mMonthIni + 1) + "-" + mDayIni);

        }else if(mMonthIni > 8 && mDayIni > 9){

            fechaET.setText((mYearIni) + "-" + (mMonthIni + 1) + "-" + mDayIni);

        }else{

            fechaET.setText((mYearIni) + "-" + "0" + (mMonthIni + 1) + "-" + "0" + mDayIni);

        }

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            mYearIni = year;
            mMonthIni = month;
            mDayIni = dayOfMonth;
            colocarFecha();

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id){

            case DATE_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mYearIni = year;
                        mMonthIni = month;
                        mDayIni = day;
                        colocarFecha();
                    }
                }, sYearIni, sMonthIni, sDayIni);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                return dialog;
        }

        return null;

    }

    public void verDatePickerP(View view){

        showDialog(DATE_ID);

    }//Fin método verDatePicker.

    public void iniciarSesion(View view){

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }//Fin método iniciarSesion.

    public void registrar(View view){

        if(!nombreET.getText().toString().trim().equalsIgnoreCase("") && !fechaET.getText().toString().trim().equalsIgnoreCase("")
                && !edadET.getText().toString().trim().equalsIgnoreCase("") && !correoET.getText().toString().trim().equalsIgnoreCase("")
                && !contraET.getText().toString().trim().equalsIgnoreCase("")){

            String nombre = String.valueOf(nombreET.getText());
            String fecha = String.valueOf(fechaET.getText());
            int edad = Integer.parseInt(String.valueOf(edadET.getText()));
            String correo = String.valueOf(correoET.getText());
            String contrasena = String.valueOf(contraET.getText());
            servicioTaskRegistroProfesor = new ServicioTaskRegistroProfesor(this, registrarProfesorREST, nombre, fecha, edad, correo, contrasena);
            servicioTaskRegistroProfesor.execute();

            intent = new Intent(this, loginProfesor.class);
            startActivity(intent);
            finish();

        }else{

            Toast.makeText(this, "Llena todos los campos.", Toast.LENGTH_SHORT).show();

        }

    }//Fin método registrar.
}
