package com.proathome;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegistrarseCliente extends AppCompatActivity {

    private int mDayIni, mMonthIni, mYearIni, sDayIni, sMonthIni, sYearIni;
    public static final int DATE_ID = 0;
    public Calendar calendar = Calendar.getInstance();
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
    @BindView(R.id.checkTC)
    MaterialCheckBox checkBox;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_cliente);
        mUnbinder = ButterKnife.bind(this);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        sMonthIni = calendar.get(Calendar.MONTH);
        sYearIni = calendar.get(Calendar.YEAR);

        String[] datos= new String[]{"HOMBRE", "MUJER", "OTRO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, datos);
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

    public void errorMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this, tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setContentText(mensaje)
                .show();
    }

    public void verificacionCorreo(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("respuesta")){
                JSONObject post = new JSONObject();
                post.put("token", jsonObject.getString("token"));
                post.put("correo", correoET.getText().toString().trim());
                WebServicesAPI webServicesAPI = new WebServicesAPI(responseVerficacion -> {
                    new SweetAlert(this, SweetAlert.SUCCESS_TYPE, SweetAlert.CLIENTE)
                            .setTitleText("¡GENIAL!")
                            .setContentText(jsonObject.getString("mensaje"))
                            .setConfirmButton("OK", sweetAlertDialog -> {
                                startActivity(new Intent(this, MainActivity.class));
                            })
                            .show();
                }, APIEndPoints.VERIFICACION_CORREO, WebServicesAPI.POST, post);
                webServicesAPI.execute();
            }else{
                new SweetAlert(this, SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                        .setTitleText("¡ERROR!")
                        .setContentText(jsonObject.getString("mensaje"))
                        .show();
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public JSONObject getRegistro(){
        JSONObject parametrosPost= new JSONObject();

        try{
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
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        return  parametrosPost;
    }

    public void iniciarSesion(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }//Fin método iniciarSesion.

    public void registrar(){
        if(!nombreET.getText().toString().trim().equalsIgnoreCase("") && !paternoET.getText().toString().trim().equalsIgnoreCase("") && !maternoET.getText().toString().trim().equalsIgnoreCase("")
                && !fechaET.getText().toString().trim().equalsIgnoreCase("") && !celularET.getText().toString().trim().equalsIgnoreCase("") && !telefonoET.getText().toString().trim().equalsIgnoreCase("")
                && !direccionET.getText().toString().trim().equalsIgnoreCase("") && !correoET.getText().toString().trim().equalsIgnoreCase("")
                && !contrasenaET.getText().toString().trim().equalsIgnoreCase("") && !contrasena2ET.getText().toString().trim().equalsIgnoreCase("")){
            //Validamos numero, minuscula, mayuscula,
            if(contrasenaET.getText().toString().trim().matches(".*\\d.*") && contrasenaET.getText().toString().trim().matches(".*[a-z].*") && contrasenaET.getText().toString().trim().matches(".*[A-Z].*") && contrasenaET.getText().toString().trim().length() >= 8){
                //Verificar que las contraseñas sean iguales
                if(contrasenaET.getText().toString().trim().equals(contrasena2ET.getText().toString())){
                    if(checkBox.isChecked()){
                        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                            verificacionCorreo(response);
                        }, APIEndPoints.REGISTRAR_CLIENTE, WebServicesAPI.POST, getRegistro());
                        webServicesAPI.execute();
                    }else
                        errorMsg("¡ESPERA!", "Debes aceptar los Términos y Condiciones.", SweetAlert.ERROR_TYPE);
                }else
                    errorMsg("¡ERROR!", "Las contraseñas no coinciden.", SweetAlert.ERROR_TYPE);
            }else
                errorMsg("¡ESPERA!", "La contraseña debe contener mínimo 8 caracteres, 1 letra minúscula, 1 letra mayúscula y 1 número.", SweetAlert.WARNING_TYPE);
        }else
            errorMsg("¡ERROR!", "Llena todos los campos correctamente.", SweetAlert.ERROR_TYPE);

    }//Fin método registrar.

    public void btnTC(){
        Uri uri = Uri.parse("https://proathome.com.mx/avisoprivacidad/cliente");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void verDatePicker(){
        showDialog(DATE_ID);
    }//Fin método verDatePicker.

    @OnClick({R.id.btnTC, R.id.btnFecha, R.id.registrarseBTN, R.id.btnIniciarSesion})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnTC:
                btnTC();
                break;
            case R.id.btnFecha:
                verDatePicker();
                break;
            case R.id.registrarseBTN:
                registrar();
                break;
            case R.id.btnIniciarSesion:
                iniciarSesion();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
