package com.proathome.Interfaces.profesional.Registrarse;

public interface RegistrarseProfesionalPresenter {

    void registrar(String nombre, String paterno, String materno, String fecha, String celular, String telefono,
                   String direccion, String correo, String pass, String genero);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void toLogin(String mensaje);

}
