package com.proathome.Interfaces.activitys_compartidos.CambiarPassword;

public interface CambiarPasswordInteractor {

    void guardarPass(String nuevaPass, String nuevaPassRep, String token, String correo, String codigo, int tipoPerfil);

}
