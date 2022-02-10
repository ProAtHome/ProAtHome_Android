package com.proathome.Interactors.profesional;

import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionInteractor;
import com.proathome.Interfaces.profesional.MatchSesion.MatchSesionPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchSesionInteractorImpl implements MatchSesionInteractor {

    private MatchSesionPresenter matchSesionPresenter;

    public MatchSesionInteractorImpl(MatchSesionPresenter matchSesionPresenter){
        this.matchSesionPresenter = matchSesionPresenter;
    }

    @Override
    public void getInfoSesion(int idSesion) {
        matchSesionPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response == null){
                matchSesionPresenter.showError("Error, ocurrió un problema con el servidor.");
            }else {
                if(!response.equals("null")){
                    JSONObject jsonObject = new JSONObject(response);
                    matchSesionPresenter.setInfoSesion(jsonObject);
                }else
                    matchSesionPresenter.showError("Sin datos, ocurrió un error.");
            }
            matchSesionPresenter.hideProgress();
        }, APIEndPoints.INFO_SESION_MATCH + idSesion, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void matchSesion(int idProfesional, int idSesion) {
        matchSesionPresenter.showProgress();
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", idProfesional);
            parametrosPUT.put("idSesion", idSesion);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                matchSesionPresenter.hideProgress();
                if(response != null)
                    matchSesionPresenter.successMatch(response);
                else
                    matchSesionPresenter.showError("Ocurrió un error inesperado");
            }, APIEndPoints.MATCH_SESION, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setImage(String foto) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            matchSesionPresenter.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

}
