package com.proathome.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegistrarseProfesional extends AppCompatActivity {

    private Intent intent;
    private int mDayIni, mMonthIni, mYearIni, sDayIni, sMonthIni, sYearIni;
    public static final int DATE_ID = 0;
    public Calendar calendar = Calendar.getInstance();
    private ProgressDialog progressDialog;
    @BindView(R.id.nombreET_R)
    TextInputEditText nombreET;
    @BindView(R.id.paternoET_R)
    TextInputEditText paternoET;
    @BindView(R.id.maternoET_R)
    TextInputEditText maternoET;
    @BindView(R.id.fechaET_R)
    TextInputEditText fechaET;
    @BindView(R.id.celularET_R)
    TextInputEditText celularET;
    @BindView(R.id.telefonoET_R)
    TextInputEditText telefonoET;
    @BindView(R.id.direccionET_R)
    TextInputEditText direccionET;
    @BindView(R.id.genero)
    Spinner genero;
    @BindView(R.id.correoET_R)
    TextInputEditText correoET;
    @BindView(R.id.contraET_R)
    TextInputEditText contrasenaET;
    @BindView(R.id.contra2ET_R)
    TextInputEditText contrasena2ET;
    @BindView(R.id.checkTCP)
    MaterialCheckBox checkBox;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_profesional);
        mUnbinder = ButterKnife.bind(this);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sMonthIni = calendar.get(Calendar.MONTH);
        sYearIni = calendar.get(Calendar.YEAR);

        String[] datos= new String[]{"HOMBRE", "MUJER", "OTRO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_white, datos);
        genero.setAdapter(adapter);
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
        intent = new Intent(this, LoginProfesional.class);
        startActivity(intent);
        finish();
    }//Fin método iniciarSesion.

    public void registrar(View view){
        if(!nombreET.getText().toString().trim().equalsIgnoreCase("") && !paternoET.getText().toString().trim().equalsIgnoreCase("") && !maternoET.getText().toString().trim().equalsIgnoreCase("")
                && !fechaET.getText().toString().trim().equalsIgnoreCase("") && !celularET.getText().toString().trim().equalsIgnoreCase("") && !telefonoET.getText().toString().trim().equalsIgnoreCase("")
                && !direccionET.getText().toString().trim().equalsIgnoreCase("") && !correoET.getText().toString().trim().equalsIgnoreCase("")
                && !contrasenaET.getText().toString().trim().equalsIgnoreCase("") && !contrasena2ET.getText().toString().trim().equalsIgnoreCase("")){

            //Validamos numero, minuscula, mayuscula,
            if(contrasenaET.getText().toString().trim().matches(".*\\d.*") && contrasenaET.getText().toString().trim().matches(".*[a-z].*") && contrasenaET.getText().toString().trim().matches(".*[A-Z].*") && contrasenaET.getText().toString().trim().length() >= 8){
                //Verificar que las contraseñas sean iguales
                if(contrasenaET.getText().toString().trim().equals(contrasena2ET.getText().toString())) {
                    if(checkBox.isChecked())
                        registrarProfesional();
                    else
                        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ESPERA!", "Debes aceptar los Términos y Condiciones.", true, "OK", ()->{});
                }else
                    SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Las contraseñas no coinciden.", true, "OK", ()->{});
            }else
                SweetAlert.showMsg(this, SweetAlert.WARNING_TYPE, "¡ESPERA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.", true, "OK", ()->{});
        }else
            SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos correctamente.", true, "OK", ()->{});
    }//Fin método registrar.

    private void registrarProfesional(){
        JSONObject parametrosPost= new JSONObject();
        try {
            parametrosPost.put("nombre", nombreET.getText().toString());
            parametrosPost.put("paterno", paternoET.getText().toString());
            parametrosPost.put("materno", maternoET.getText().toString());
            parametrosPost.put("correo", correoET.getText().toString().trim());
            parametrosPost.put("celular", celularET.getText().toString());
            parametrosPost.put("telefono", telefonoET.getText().toString());
            parametrosPost.put("direccion", direccionET.getText().toString());
            parametrosPost.put("fechaNacimiento", fechaET.getText().toString());
            parametrosPost.put("genero", genero.getSelectedItem().toString());
            parametrosPost.put("contrasena", contrasenaET.getText().toString());

            progressDialog = ProgressDialog.show(this, "Registrando", "Por favor, espere...");
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject post = new JSONObject();
                        post.put("token", jsonObject.getString("token"));
                        post.put("correo", correoET.getText().toString());
                        WebServicesAPI fastServices = new WebServicesAPI(output -> {
                            progressDialog.dismiss();
                            SweetAlert.showMsg(this, SweetAlert.SUCCESS_TYPE, "¡GENIAL!",
                                    jsonObject.getString("mensaje"), true, "OK", ()->{
                                        startActivity(new Intent(this, LoginProfesional.class));
                                    });
                        }, Constants.IP_80 + "/assets/lib/Verificacion.php?enviarPro=true", WebServicesAPI.POST, post);
                        fastServices.execute();
                    }else{
                        progressDialog.dismiss();
                        SweetAlert.showMsg(this, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                    }

                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.REGISTRAR_PROFESIONAL, WebServicesAPI.POST, parametrosPost);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnTCP)
    public void onClick(){
        Uri uri = Uri.parse("https://proathome.com.mx/avisoprivacidad/profesional");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
