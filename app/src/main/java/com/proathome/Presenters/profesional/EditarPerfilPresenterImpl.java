package com.proathome.Presenters.profesional;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.proathome.Interactors.profesional.EditarPerfilInteractorImpl;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilInteractor;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilPresenter;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilView;
import org.json.JSONObject;

public class EditarPerfilPresenterImpl implements EditarPerfilPresenter {

    private EditarPerfilInteractor editarPerfilInteractor;
    private EditarPerfilView editarPerfilView;

    public EditarPerfilPresenterImpl(EditarPerfilView editarPerfilView){
        this.editarPerfilView = editarPerfilView;
        editarPerfilInteractor = new EditarPerfilInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(editarPerfilView != null)
            editarPerfilView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(editarPerfilView != null)
            editarPerfilView.hideProgress();
    }

    @Override
    public void getReportes(int idProfesional, String token) {
        editarPerfilInteractor.getReportes(idProfesional, token);
    }

    @Override
    public void showError(String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.showError(mensaje);
    }

    @Override
    public void showSuccess(String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.showSuccess(mensaje);
    }

    @Override
    public void setVisibilityAvisos(boolean visivilityAvisos, String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.setVisibilityAvisos(visivilityAvisos, mensaje);
    }

    @Override
    public void getDatosPerfil(int idProfesional, String token) {
        editarPerfilInteractor.getDatosPerfil(idProfesional, token);
    }

    @Override
    public void setDatosPerfil(JSONObject jsonObject) {
        if(editarPerfilView != null)
            editarPerfilView.setDatosPerfil(jsonObject);
    }

    @Override
    public void getDatosBanco(int idProfesional, String token) {
        editarPerfilInteractor.getDatosBanco(idProfesional, token);
    }

    @Override
    public void setDatosBanco(JSONObject jsonObject) {
        if(editarPerfilView != null)
            editarPerfilView.setDatosBanco(jsonObject);
    }

    @Override
    public void getFotoPerfil(String foto) {
        editarPerfilInteractor.getFotoPerfil(foto);
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(editarPerfilView != null)
            editarPerfilView.setFotoBitmap(bitmap);
    }

    @Override
    public void actualizarPerfil(JSONObject jsonObject) {
        editarPerfilInteractor.actualizarPerfil(jsonObject);
    }

    @Override
    public void uploadImage(int idProfesional, Context context) {
        editarPerfilInteractor.uploadImage(idProfesional, context);
    }

    @Override
    public void getMediaGalery(ContentResolver contentResolver, Intent data) {
        editarPerfilInteractor.getMediaGalery(contentResolver, data);
    }

    @Override
    public void updateBancoService(JSONObject jsonObject) {
        editarPerfilInteractor.updateBancoService(jsonObject);
    }

}
