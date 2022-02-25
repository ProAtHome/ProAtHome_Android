package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.MasTiempoInteractorImpl;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoInteractor;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoPresenter;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoView;

public class MasTiempoPresenterImpl implements MasTiempoPresenter {

    private MasTiempoView masTiempoView;
    private MasTiempoInteractor masTiempoInteractor;

    public MasTiempoPresenterImpl(MasTiempoView masTiempoView){
        this.masTiempoView = masTiempoView;
        masTiempoInteractor = new MasTiempoInteractorImpl(this);
    }

}
