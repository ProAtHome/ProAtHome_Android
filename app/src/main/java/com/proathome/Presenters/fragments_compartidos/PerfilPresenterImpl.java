package com.proathome.Presenters.fragments_compartidos;

import android.graphics.Bitmap;
import com.proathome.Interactors.fragments_compartidos.PerfilInteractorImpl;
import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilInteractor;
import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilPresenter;
import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilView;

public class PerfilPresenterImpl implements PerfilPresenter {

    private PerfilView perfilView;
    private PerfilInteractor perfilInteractor;

    public PerfilPresenterImpl(PerfilView perfilView){
        this.perfilView = perfilView;
        perfilInteractor = new PerfilInteractorImpl(this);
    }

    @Override
    public void getValoracion(int idUsuario, int tipoPerfil) {
        perfilInteractor.getValoracion(idUsuario, tipoPerfil);
    }

    @Override
    public void showError(String error) {
        if(perfilView != null)
            perfilView.showError(error);
    }

    @Override
    public void setAdapter(String comentario, float valoracion) {
        if(perfilView != null)
            perfilView.setAdapter(comentario, valoracion);
    }

    @Override
    public void setInfoPerfil(String nombre, String correo, String descripcion) {
        if(perfilView != null)
            perfilView.setInfoPerfil(nombre, correo, descripcion);
    }

    @Override
    public void getFotoPerfil(int tipoPerfil) {
        perfilInteractor.getFotoPerfil(tipoPerfil);
    }

    @Override
    public void setFoto(Bitmap bitmap) {
        if(perfilView != null)
            perfilView.setFoto(bitmap);
    }

    @Override
    public void showProgress() {
        if(perfilView != null)
            perfilView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(perfilView != null)
            perfilView.hideProgress();
    }

}
