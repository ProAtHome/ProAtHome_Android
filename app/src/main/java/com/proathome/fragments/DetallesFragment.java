package com.proathome.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.proathome.SincronizarClase;
import com.proathome.servicios.clase.ServicioTaskFinalizarClase;
import com.proathome.servicios.clase.ServicioTaskSincronizarClases;
import com.proathome.servicios.WorkaroundMapFragment;
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.servicios.estudiante.ServicioTaskBancoEstudiante;
import com.proathome.servicios.estudiante.ServicioTaskBloquearPerfil;
import com.proathome.servicios.estudiante.ServicioTaskPreOrden;
import com.proathome.servicios.profesor.ServicioTaskFotoDetalles;
import com.proathome.servicios.valoracion.ServicioValidarValoracion;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetallesFragment extends Fragment implements OnMapReadyCallback {

    private static Component mInstance;
    private GoogleMap mMap;
    public static final String TAG = "Detalles";
    public static int ESTUDIANTE = 1, PROCEDENCIA_DETALLES_FRAGMENT = 1;
    public static String fotoNombre, planSesion;
    public static boolean sumar;
    public static int idSesion = 0, idEstudiante = 0, idProfesor = 0, tiempoPasar = 0, idSeccion = 0,
            idNivel = 0, idBloque = 0;
    /*VARIABLE DE EXISTENCIA DE DATOS - BANCO*/
    public static boolean banco, procedenciaFin = false;
    public static ImageView foto;
    public static Context contexto;
    @BindView(R.id.profesor)
    public TextView profesor;
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
    @BindView(R.id.tipoClase)
    public TextView tipoClase;
    @BindView(R.id.descripcionTV)
    TextView descripcionProfesor;
    @BindView(R.id.correoTV)
    TextView correoProfesor;
    @BindView(R.id.tipoPlan)
    TextView tipoPlan;
    @BindView(R.id.perfilEstudianteCard)
    MaterialCardView perfilEstudianteCard;
    public static Fragment detallesFragment;
    public static MaterialButton iniciar;
    private NestedScrollView mScrollView;
    private Unbinder mUnbinder;
    private double longitud = -99.13320799999, latitud = 19.4326077;

    public DetallesFragment() {

    }

    public static Component getmInstance(int idClase, String tipoClase, String horario, String profesor, String lugar,
                                         int tiempo, String observaciones, double latitud, double longitud, int idSeccion,
                                         int idNivel, int idBloque, String fecha, String fotoProfesor, String descripcionProfesor,
                                         String correoProfesor, boolean sumar, String tipoPlan, int idProfesor) {

        mInstance = new Component();
        mInstance.setIdClase(idClase);
        mInstance.setProfesor(profesor);
        mInstance.setLugar(lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones(observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setFecha(fecha);
        mInstance.setTipoClase(tipoClase);
        mInstance.setHorario(horario);
        mInstance.setFotoProfesor(fotoProfesor);
        mInstance.setDescripcionProfesor(descripcionProfesor);
        mInstance.setCorreoProfesor(correoProfesor);
        mInstance.setSumar(sumar);
        mInstance.setTipoPlan(tipoPlan);
        mInstance.setIdProfesor(idProfesor);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.SCROLL);
        return mInstance;

    }

    @Override
    public void onResume() {
        super.onResume();
        ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(getContext(),
                idSesion, idEstudiante, DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, false);
        sincronizarClases.execute();
        ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(getContext(), idSesion,
                idEstudiante, Constants.VALIDAR_CLASE_FINALIZADA_AMBOS_PERFILES, DetallesFragment.ESTUDIANTE);
        finalizarClase.execute();
        if (procedenciaFin) {
            ServicioValidarValoracion validarValoracion = new ServicioValidarValoracion(idSesion, idProfesor,
                    ServicioValidarValoracion.PROCEDENCIA_ESTUDIANTE);
            validarValoracion.execute();
            procedenciaFin = false;
        } else {
            ServicioTaskBloquearPerfil bloquearPerfil = new ServicioTaskBloquearPerfil(getContext(), idEstudiante,
                    DetallesFragment.PROCEDENCIA_DETALLES_FRAGMENT);
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
        foto = view.findViewById(R.id.foto);
        iniciar = view.findViewById(R.id.iniciar);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if (fila.moveToFirst()) {
            idEstudiante = fila.getInt(0);
        } else {
            baseDeDatos.close();
        }

        baseDeDatos.close();

        if (bun.getString("fotoProfesor").equalsIgnoreCase("Sin foto")) {
            iniciar.setEnabled(false);
            perfilEstudianteCard.setClickable(false);
        } else {
            iniciar.setEnabled(true);
        }
        if (bun.getString("descripcionProfesor").equalsIgnoreCase("Sin descripcion"))
            descripcionProfesor.setVisibility(View.INVISIBLE);
        else
            descripcionProfesor.setVisibility(View.VISIBLE);
        if (bun.getString("correoProfesor").equalsIgnoreCase("Sin correo"))
            correoProfesor.setVisibility(View.INVISIBLE);
        else
            correoProfesor.setVisibility(View.VISIBLE);

        idSesion = bun.getInt("idClase");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        profesor.setText(bun.getString("profesor"));
        lugar.setText("Dirección: " + bun.getString("lugar"));
        tiempo.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        tiempoPasar = bun.getInt("tiempo");
        observaciones.setText("Observaciones: " + bun.getString("observaciones"));
        nivel.setText("Nivel: " + component.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"),
                bun.getInt("idBloque")));
        tipoClase.setText("Tipo de Clase: " + bun.getString("tipoClase"));
        horario.setText("Horario: " + bun.getString("horario"));
        descripcionProfesor.setText(bun.getString("descripcionProfesor"));
        correoProfesor.setText(bun.getString("correoProfesor"));
        fotoNombre = bun.getString("fotoProfesor");
        sumar = bun.getBoolean("sumar");
        idSeccion = bun.getInt("idSeccion");
        idNivel = bun.getInt("idNivel");
        idBloque = bun.getInt("idBloque");
        tipoPlan.setText("DENTRO DEL PLAN: " + bun.getString("tipoPlan"));
        planSesion = bun.getString("tipoPlan");
        idProfesor = bun.getInt("idProfesor");

        /*Cargar datos del Perfil del Profesor*/


        iniciar.setOnClickListener(v -> {

            ServicioTaskSincronizarClases sincronizarClases =
                    new ServicioTaskSincronizarClases(getContext(), idSesion, idEstudiante,
                            DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, true);
            sincronizarClases.execute();

            Intent intent = new Intent(getContext(), SincronizarClase.class);
            intent.putExtra("perfil", ESTUDIANTE);
            intent.putExtra("idSesion", idSesion);
            intent.putExtra("idPerfil", idEstudiante);
            intent.putExtra("tiempo", tiempoPasar);
            intent.putExtra("idSeccion", bun.getInt("idSeccion"));
            intent.putExtra("idNivel", bun.getInt("idNivel"));
            intent.putExtra("idBloque", bun.getInt("idBloque"));
            intent.putExtra("sumar", sumar);
            startActivity(intent);

            /*

            if (!this.planSesion.equalsIgnoreCase("PARTICULAR")) {
                if (banco) {
                    ServicioTaskSincronizarClases sincronizarClases =
                            new ServicioTaskSincronizarClases(getContext(), idSesion, idEstudiante,
                                    DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, true);
                    sincronizarClases.execute();

                    Intent intent = new Intent(getContext(), SincronizarClase.class);
                    intent.putExtra("perfil", ESTUDIANTE);
                    intent.putExtra("idSesion", idSesion);
                    intent.putExtra("idPerfil", idEstudiante);
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
                        PreOrdenClase preOrdenClase = new PreOrdenClase();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        preOrdenClase.show(fragmentTransaction, "PreOrden");
                    } else {
                        ServicioTaskSincronizarClases sincronizarClases =
                                new ServicioTaskSincronizarClases(getContext(), idSesion, idEstudiante,
                                        DetallesFragment.ESTUDIANTE, Constants.CAMBIAR_DISPONIBILIDAD, true);
                        sincronizarClases.execute();

                        Intent intent = new Intent(getContext(), SincronizarClase.class);
                        intent.putExtra("perfil", ESTUDIANTE);
                        intent.putExtra("idSesion", idSesion);
                        intent.putExtra("idPerfil", idEstudiante);
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

        ServicioTaskFotoDetalles fotoDetalles = new ServicioTaskFotoDetalles(getContext(), this.fotoNombre, ESTUDIANTE);
        if (!fotoNombre.equalsIgnoreCase("Sin foto"))
            fotoDetalles.execute();

    }

    public void errorBancoMsg(){
        new SweetAlert(getContext(), SweetAlert.WARNING_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡AVISO!")
                .setContentText("Revisa que tus datos bancarios estén registrados en EDITAR PERFIL.")
                .show();
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
                    errorMsg();
                })
                .setOnCancelListener(dialog -> {
                    errorMsg();
                })
                .show();
    }

    public void errorMsg() {
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡OH NO!")
                .setContentText("No podemos continuar sin el permiso de ubicación.")
                .setConfirmButton("OK", sweetAlertDialog -> {
                    getFragmentManager().beginTransaction().detach(this).commit();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    @OnClick(R.id.nuevoTopico)
    public void onClickNuevoTopico(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoUsuario", Constants.TIPO_USUARIO_ESTUDIANTE);
        bundle.putInt("idSesion", idSesion);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        NuevoTicketFragment ticketAyuda = new NuevoTicketFragment();
        ticketAyuda.setArguments(bundle);
        ticketAyuda.show(fragmentTransaction, "Ticket");
    }

    @OnClick(R.id.perfilEstudianteCard)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", PerfilFragment.PERFIL_PROFESOR);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        PerfilFragment perfilFragment = new PerfilFragment();
        perfilFragment.setArguments(bundle);
        perfilFragment.show(fragmentTransaction, "Perfil - Profesor");
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
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu clase."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
