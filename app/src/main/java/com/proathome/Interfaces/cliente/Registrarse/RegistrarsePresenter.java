package com.proathome.Interfaces.cliente.Registrarse;

public interface RegistrarsePresenter {

    void registrar(String nombre, String paterno, String materno, String correo, String celular,
                   String telefono, String direccion, String fecha, String genero, String pass);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void success(String mensaje);

}
