package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoInteractor;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoPresenter;

public class MasTiempoInteractorImpl implements MasTiempoInteractor {

    private MasTiempoPresenter masTiempoPresenter;

    public MasTiempoInteractorImpl(MasTiempoPresenter masTiempoPresenter){
        this.masTiempoPresenter = masTiempoPresenter;
    }

}
