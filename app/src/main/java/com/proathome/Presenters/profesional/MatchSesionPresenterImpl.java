package com.proathome.Presenters.profesional;

import android.graphics.Bitmap;
import com.proathome.Interactors.profesional.MatchSesionInteractorImpl;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionInteractor;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionPresenter;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionView;
import org.json.JSONObject;

public class MatchSesionPresenterImpl implements MatchSesionPresenter {

    private MatchSesionView matchSesionView;
    private MatchSesionInteractor matchSesionInteractor;

    public MatchSesionPresenterImpl(MatchSesionView matchSesionView){
        this.matchSesionView = matchSesionView;
        matchSesionInteractor = new MatchSesionInteractorImpl(this);
    }

    @Override
    public void getInfoSesion(int idSesion) {
        matchSesionInteractor.getInfoSesion(idSesion);
    }

    @Override
    public void showError(String error) {
        if(matchSesionView != null)
            matchSesionView.showError(error);
    }

    @Override
    public void setInfoSesion(JSONObject jsonObject) {
        if(matchSesionView != null)
            matchSesionView.setInfoSesion(jsonObject);
    }

    @Override
    public void matchSesion(int idProfesional, int idSesion, JSONObject serviciosDisponibles,
                            JSONObject serviciosPendientes, String fechaActual, String fechaServicio,
                                String horario, int rango) {
        matchSesionInteractor.matchSesion(idProfesional, idSesion, serviciosDisponibles, serviciosPendientes,
                fechaActual, fechaServicio, horario, rango);
    }

    @Override
    public void successMatch(String mensaje) {
        if(matchSesionView != null)
            matchSesionView.successMatch(mensaje);
    }

    @Override
    public void getImage(String foto) {
        matchSesionInteractor.setImage(foto);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        if(matchSesionView != null)
            matchSesionView.setImageBitmap(bitmap);
    }

    @Override
    public void showProgress() {
        if(matchSesionView != null)
            matchSesionView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(matchSesionView != null)
            matchSesionView.hideProgress();
    }

}
