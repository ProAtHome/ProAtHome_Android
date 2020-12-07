package com.proathome;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.servicios.profesor.ServicioTaskRegistroProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class registrarseProfesor extends AppCompatActivity {

    private Intent intent;
    private int mDayIni, mMonthIni, mYearIni, sDayIni, sMonthIni, sYearIni;
    public static final int DATE_ID = 0;
    public Calendar calendar = Calendar.getInstance();
    private ServicioTaskRegistroProfesor servicioTaskRegistroProfesor;
    private final String registrarProfesorREST = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/" +
            "profesor/agregarProfesor";
    @BindView(R.id.nombreET_RP)
    TextInputEditText nombreET;
    @BindView(R.id.fechaET_RP)
    TextInputEditText fechaET;
    @BindView(R.id.edadET_RP)
    TextInputEditText edadET;
    @BindView(R.id.correoET_RP)
    TextInputEditText correoET;
    @BindView(R.id.contraET_RP)
    TextInputEditText contrasenaET;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_profesor);
        mUnbinder = ButterKnife.bind(this);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sMonthIni = calendar.get(Calendar.MONTH);
        sYearIni = calendar.get(Calendar.YEAR);
    }

    private void colocarFecha(){
        if(mMonthIni >= 9 && mDayIni <= 9){
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
                DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                        (datePicker, year, month, day) -> {
                            mYearIni = year;
                            mMonthIni = month;
                            mDayIni = day;
                            colocarFecha();
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
        if(!nombreET.getText().toString().trim().equalsIgnoreCase("") &&
                !fechaET.getText().toString().trim().equalsIgnoreCase("")
                && !edadET.getText().toString().trim().equalsIgnoreCase("") &&
                !correoET.getText().toString().trim().equalsIgnoreCase("")
                && !contrasenaET.getText().toString().trim().equalsIgnoreCase("")){

            String nombre = String.valueOf(nombreET.getText());
            String fecha = String.valueOf(fechaET.getText());
            int edad = Integer.parseInt(String.valueOf(edadET.getText()));
            String correo = String.valueOf(correoET.getText());
            String contrasena = String.valueOf(contrasenaET.getText());
            servicioTaskRegistroProfesor = new ServicioTaskRegistroProfesor(this, registrarProfesorREST,
                    nombre, fecha, edad, correo, contrasena);
            servicioTaskRegistroProfesor.execute();
        }else{
            new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.PROFESOR)
                    .setTitleText("¡Error!")
                    .setConfirmButton("OK", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .setContentText("Llena todos los campos.")
                    .show();
        }
    }//Fin método registrar.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
