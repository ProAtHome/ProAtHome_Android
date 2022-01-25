package com.proathome.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.servicios.sesiones.ServiciosSesion;
import com.proathome.ui.InicioCliente;
import com.proathome.ui.SincronizarServicio;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.utils.SharedPreferencesManager;
import com.proathome.utils.WorkaroundMapFragment;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesFragment extends Fragment implements OnMapReadyCallback {

    private static Component mInstance;
    private GoogleMap mMap;
    public static final String TAG = "Detalles";
    public static int CLIENTE = 1;
    public static String fotoNombre, planSesion;
    public static boolean sumar;
    public static int idSesion = 0, idCliente = 0, idProfesional = 0, tiempoPasar = 0, idSeccion = 0,
            idNivel = 0, idBloque = 0;
    /*VARIABLE DE EXISTENCIA DE DATOS - BANCO*/
    public static boolean banco, procedenciaFin = false;
    public static ImageView fotoPerfil;
    public static Context contexto;
    @BindView(R.id.profesional)
    public TextView profesional;
    @BindView(R.id.horario)
    public TextView horario;
    @BindView(R.id.lugar)
    public TextView lugar;
    @BindView(R.id.tiempo)
    public TextView tiempo;
    @BindView(R.id.nivel)
    public TextView nivel;
    @BindView(R.id.observaciones)
    public TextView observaciones;
    @BindView(R.id.tipoServicio)
    public TextView tipoServicio;
    @BindView(R.id.descripcionTV)
    TextView descripcionProfesional;
    @BindView(R.id.correoTV)
    TextView correoProfesional;
    @BindView(R.id.tipoPlan)
    TextView tipoPlan;
    @BindView(R.id.perfilClienteCard)
    MaterialCardView perfilClienteCard;
    public static Fragment detallesFragment;
    public static MaterialButton iniciar;
    private NestedScrollView mScrollView;
    private Unbinder mUnbinder;
    private double longitud = -99.13320799999, latitud = 19.4326077;

    public DetallesFragment() {

    }

    public static Component getmInstance(int idServicio, String tipoServicio, String horario, String profesional, String lugar,
                                         int tiempo, String observaciones, double latitud, double longitud, int idSeccion,
                                         int idNivel, int idBloque, String fecha, String fotoProfesional, String descripcionProfesional,
                                         String correoProfesional, boolean sumar, String tipoPlan, int idProfesional) {

        mInstance = new Component();
        mInstance.setIdServicio(idServicio);
        mInstance.setProfesional(profesional);
        mInstance.setLugar(lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones(observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setFecha(fecha);
        mInstance.setTipoServicio(tipoServicio);
        mInstance.setHorario(horario);
        mInstance.setFotoProfesional(fotoProfesional);
        mInstance.setDescripcionProfesional(descripcionProfesional);
        mInstance.setCorreoProfesional(correoProfesional);
        mInstance.setSumar(sumar);
        mInstance.setTipoPlan(tipoPlan);
        mInstance.setIdProfesional(idProfesional);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);
        return mInstance;

    }

    @Override
    public void onResume() {
        super.onResume();
        ServiciosSesion.cambiarDisponibilidadCliente(idSesion, idCliente, false);
        ServiciosSesion.validarServicioFinalizadoCliente(idSesion, idCliente, getContext());
        if (procedenciaFin) {
            validarValoracionProfesional();
            procedenciaFin = false;
        } else {
            WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
                try{
                    if(response != null){
                        JSONObject data = new JSONObject(response);
                        if(data.getBoolean("respuesta")){
                            JSONObject jsonObject = data.getJSONObject("mensaje");
                            PagoPendienteFragment pagoPendienteFragment = new PagoPendienteFragment();
                            Bundle bundle = new Bundle();
                            if(jsonObject.getBoolean("bloquear")){
                                FragmentTransaction fragmentTransaction = null;
                                bundle.putDouble("deuda", jsonObject.getDouble("deuda"));
                                bundle.putString("sesion", Component.getSeccion(jsonObject.getInt("idSeccion")) +
                                        " / " + Component.getNivel(jsonObject.getInt("idSeccion"),
                                        jsonObject.getInt("idNivel")) + " / " + jsonObject.getInt("idBloque"));
                                bundle.putString("lugar", jsonObject.getString("lugar"));
                                bundle.putString("nombre", jsonObject.getString("nombre"));
                                bundle.putString("correo", jsonObject.getString("correo"));
                                bundle.putInt("idSesion", jsonObject.getInt("idSesion"));
                                fragmentTransaction = DetallesFragment.detallesFragment.getFragmentManager().beginTransaction();
                                pagoPendienteFragment.setArguments(bundle);
                                pagoPendienteFragment.show(fragmentTransaction, "Pago pendiente");
                            }
                        }else
                            SweetAlert.showMsg(this.contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.", false, null, null);
                    }else
                        SweetAlert.showMsg(this.contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.", false, null, null);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.BLOQUEAR_PERFIL + "/" + idCliente + "/" + SharedPreferencesManager.getInstance(getContext()).getTokenCliente(), WebServicesAPI.GET, null);
            bloquearPerfil.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        detallesFragment = DetallesFragment.this;
        DetallesFragment.contexto = getContext();

        Component component = new Component();
        Bundle bun = getArguments();
        fotoPerfil = view.findViewById(R.id.foto);
        iniciar = view.findViewById(R.id.iniciar);

        idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();

        if (bun.getString("fotoProfesional").equalsIgnoreCase("Sin foto")) {
            iniciar.setEnabled(false);
            iniciar.setBackgroundColor(getResources().getColor(R.color.colorGris));
            perfilClienteCard.setClickable(false);
        } else {
            iniciar.setEnabled(true);
        }
        if (bun.getString("descripcionProfesional").equalsIgnoreCase("Sin descripcion"))
            descripcionProfesional.setVisibility(View.INVISIBLE);
        else
            descripcionProfesional.setVisibility(View.VISIBLE);
        if (bun.getString("correoProfesional").equalsIgnoreCase("Sin correo"))
            correoProfesional.setVisibility(View.INVISIBLE);
        else
            correoProfesional.setVisibility(View.VISIBLE);

        idSesion = bun.getInt("idServicio");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesional.setText(bun.getString("profesional"));
        lugar.setText("Dirección: " + bun.getString("lugar"));
        tiempo.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        tiempoPasar = bun.getInt("tiempo");
        observaciones.setText("Observaciones: " + bun.getString("observaciones"));
        nivel.setText("Nivel: " + component.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"),
                bun.getInt("idBloque")));
        tipoServicio.setText("Tipo de Servicio: " + bun.getString("tipoServicio"));
        horario.setText("Horario: " + bun.getString("horario"));
        descripcionProfesional.setText(bun.getString("descripcionProfesional"));
        correoProfesional.setText(bun.getString("correoProfesional"));
        fotoNombre = bun.getString("fotoProfesional");
        sumar = bun.getBoolean("sumar");
        idSeccion = bun.getInt("idSeccion");
        idNivel = bun.getInt("idNivel");
        idBloque = bun.getInt("idBloque");
        tipoPlan.setText("DENTRO DEL PLAN: " + bun.getString("tipoPlan"));
        planSesion = bun.getString("tipoPlan");
        idProfesional = bun.getInt("idProfesional");

        /*Cargar datos del Perfil del Profesional*/


        iniciar.setOnClickListener(v -> {
            ServiciosSesion.cambiarDisponibilidadCliente(idSesion, idCliente, true);

            Intent intent = new Intent(getContext(), SincronizarServicio.class);
            intent.putExtra("perfil", CLIENTE);
            intent.putExtra("idSesion", idSesion);
            intent.putExtra("idPerfil", idCliente);
            intent.putExtra("tiempo", tiempoPasar);
            intent.putExtra("idSeccion", bun.getInt("idSeccion"));
            intent.putExtra("idNivel", bun.getInt("idNivel"));
            intent.putExtra("idBloque", bun.getInt("idBloque"));
            intent.putExtra("sumar", sumar);
            startActivity(intent);

            /*

            if (!this.planSesion.equalsIgnoreCase("PARTICULAR")) {
                if (banco) {
                    ServiciosSesion.cambiarDisponibilidadCliente(true)

                    Intent intent = new Intent(getContext(), SincronizarServicio.class);
                    intent.putExtra("perfil", CLIENTE);
                    intent.putExtra("idSesion", idSesion);
                    intent.putExtra("idPerfil", idCliente);
                    intent.putExtra("tiempo", tiempoPasar);
                    intent.putExtra("idSeccion", bun.getInt("idSeccion"));
                    intent.putExtra("idNivel", bun.getInt("idNivel"));
                    intent.putExtra("idBloque", bun.getInt("idBloque"));
                    intent.putExtra("sumar", sumar);
                    startActivity(intent);
                } else {
                    errorBancoMsg();
                }
            } else {
                if (banco) {

                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String idCardSesion = "idCard" + idSesion;
                    String idCard = myPreferences.getString(idCardSesion, "Sin valor");

                    if (idCard.equalsIgnoreCase("Sin valor")) {
                        PreOrdenServicio preOrdenServicio = new PreOrdenServicio();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        preOrdenServicio.show(fragmentTransaction, "PreOrden");
                    } else {

                        ServiciosSesion.cambiarDisponibilidadCliente(true)

                        Intent intent = new Intent(getContext(), SincronizarServicio.class);
                        intent.putExtra("perfil", CLIENTE);
                        intent.putExtra("idSesion", idSesion);
                        intent.putExtra("idPerfil", idCliente);
                        intent.putExtra("tiempo", tiempoPasar);
                        intent.putExtra("idSeccion", bun.getInt("idSeccion"));
                        intent.putExtra("idNivel", bun.getInt("idNivel"));
                        intent.putExtra("idBloque", bun.getInt("idBloque"));
                        intent.putExtra("sumar", sumar);
                        startActivity(intent);
                    }
                } else {
                    errorBancoMsg();
                }
            }*/

        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mMap == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.mapsDetalles);
                mapFragment.getMapAsync(this);
            }
        }

        if (!fotoNombre.equalsIgnoreCase("Sin foto"))
            setImageBitmap(fotoNombre);

    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            fotoPerfil.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

    private void validarValoracionProfesional(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(!jsonObject.getBoolean("valorado")){
                    FragmentTransaction fragmentTransaction = DetallesFragment.detallesFragment
                            .getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_CLIENTE);
                    EvaluarFragment evaluarFragment = new EvaluarFragment();
                    evaluarFragment.setArguments(bundle);
                    evaluarFragment.show(fragmentTransaction, "Evaluación");
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_VALORACION_PROFESIONAL + idSesion + "/" + idProfesional, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    private void showAlert() {
        new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos el permiso de ubicación para ofrecerte una mejor experiencia.")
                .setPositiveButton("Dar permiso", (dialog, which) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡OH NO!", "No podemos continuar sin el permiso de ubicación.",
                            true, "OK", ()->{
                                getFragmentManager().beginTransaction().detach(this).commit();
                            });
                })
                .setOnCancelListener(dialog -> {
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡OH NO!", "No podemos continuar sin el permiso de ubicación.",
                            true, "OK", ()->{
                                getFragmentManager().beginTransaction().detach(this).commit();
                            });
                })
                .show();
    }

    @OnClick(R.id.nuevoTopico)
    public void onClickNuevoTopico(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoUsuario", Constants.TIPO_USUARIO_CLIENTE);
        bundle.putInt("idSesion", idSesion);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        NuevoTicketFragment ticketAyuda = new NuevoTicketFragment();
        ticketAyuda.setArguments(bundle);
        ticketAyuda.show(fragmentTransaction, "Ticket");
    }

    @OnClick(R.id.perfilClienteCard)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", PerfilFragment.PERFIL_PROFESIONAL);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        PerfilFragment perfilFragment = new PerfilFragment();
        perfilFragment.setArguments(bundle);
        perfilFragment.show(fragmentTransaction, "Perfil - Profesional");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = getView().findViewById(R.id.scrollMapDetalles); //parent scrollview in xml, give
        // your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetalles))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        agregarMarca(googleMap, latitud, longitud);
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    public void agregarMarca(GoogleMap googleMap, double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu servicio."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
