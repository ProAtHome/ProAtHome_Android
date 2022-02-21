package com.proathome.Interfaces.profesional.Inicio;

public interface InicioInteractor {

    void validarTokenSesion(int idProfesional, String token);

    void cargarPerfil(int idProfesional, String token);

}
