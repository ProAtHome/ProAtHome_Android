package com.proathome.Interfaces.cliente.MainActivity;

import android.content.Context;

public interface MainActivityInteractor {

    void latidoSQL();

    void login(String correo, String pass, Context context);

    void sesionExistente(Context context);

}
