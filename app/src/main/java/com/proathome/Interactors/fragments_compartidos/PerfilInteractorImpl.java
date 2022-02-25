package com.proathome.Interactors.fragments_compartidos;

import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilInteractor;
import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.fragments_compartidos.PerfilFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PerfilInteractorImpl implements PerfilInteractor {

    private PerfilPresenter perfilPresenter;

    public PerfilInteractorImpl(PerfilPresenter perfilPresenter){
        this.perfilPresenter = perfilPresenter;
    }

    @Override
    public void getValoracion(int idUsuario, int tipoPerfil) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                int numValoraciones = 0;
                double sumaValoraciones = 0;
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray == null){
                        perfilPresenter.showError("Ocurrió un problema, intenta más tarde.");
                    }else{
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getBoolean("valoraciones")){//Valoraciones del profesional
                                if(!jsonObject.getBoolean("error")){
                                    //Hay error.
                                    //Obtener promedio
                                    numValoraciones++;
                                    sumaValoraciones += Double.parseDouble(jsonObject.get("valoracion").toString());
                                    perfilPresenter.setAdapter(jsonObject.getString("comentario"), Float.parseFloat(jsonObject.get("valoracion").toString()));
                                }else
                                    perfilPresenter.setAdapter("El usuario no tiene valoraciones aún.", 0.0f);
                            }else//Perfil de profesional
                                perfilPresenter.setInfoPerfil(jsonObject.getString("nombre"), jsonObject.getString("correo"), jsonObject.getString("descripcion"));
                        }
                        //Promedio
                        double promedio = sumaValoraciones / numValoraciones;
                        if(numValoraciones == 0)
                            PerfilFragment.tvCalificacion.setText("0.00");
                        else
                            PerfilFragment.tvCalificacion.setText(String.format("%.2f", promedio));
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                    perfilPresenter.showError("Ocurrió un problema, intenta más tarde.");
                }
            }else
                perfilPresenter.showError("Ocurrió un problema, intenta más tarde.");
        }, tipoPerfil == PerfilFragment.PERFIL_CLIENTE ? APIEndPoints.GET_VALORACION_CLIENTE + idUsuario : APIEndPoints.GET_VALORACION_PROFESIONAL + idUsuario, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getFotoPerfil(int tipoPerfil) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            perfilPresenter.setFoto(response);
        }, APIEndPoints.FOTO_PERFIL, tipoPerfil == PerfilFragment.PERFIL_CLIENTE ? DetallesSesionProfesionalFragment.fotoNombre : DetallesFragment.fotoNombre);
        webServiceAPIAssets.execute();
    }

}
