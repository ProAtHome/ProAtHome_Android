package com.proathome.Views.profesional.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalPresenter;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalView;
import com.proathome.Presenters.profesional.DetallesSesionProfesionalPresenterImpl;
import com.proathome.R;
import com.proathome.Utils.NetworkValidate;
import com.proathome.Views.activitys_compartidos.SincronizarServicio;
import com.proathome.Views.fragments_compartidos.NuevoTicketFragment;
import com.proathome.Views.fragments_compartidos.PerfilFragment;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.WorkaroundMapFragment;
import com.proathome.Utils.pojos.ComponentSesionesProfesional;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SweetAlert;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetallesSesionProfesionalFragment extends Fragment implements OnMapReadyCallback, DetallesSesionProfesionalView {

    private static ComponentSesionesProfesional mInstance;
    public static final String TAG = "Detalles del Servicio";
    public static int PROFESIONAL = 2, idCliente;
    public static int idSesion = 0, idProfesional = 0;
    private int tiempoPasar = 0;
    private GoogleMap mMap;
    private NestedScrollView mScrollView;
    private Unbinder mUnbinder;
    private double longitud = -99.13320799999, latitud = 19.4326077;
    public static CircleImageView fotoPerfil;
    public static String fotoNombre;
    public static boolean procedenciaFin = false;
    public static MaterialButton iniciar;
    public static Fragment fragmentDetallesProf;
    private DetallesSesionProfesionalPresenter detallesSesionProfesionalPresenter;

    @BindView(R.id.nombreTV)
    TextView nombreTV;
    @BindView(R.id.descripcionTV)
    TextView descripcionTV;
    @BindView(R.id.correoTV)
    TextView correoTV;
    @BindView(R.id.direccionTV)
    TextView direccionTV;
    @BindView(R.id.tiempoTV)
    TextView tiempoTV;
    @BindView(R.id.nivelTV)
    TextView nivelTV;
    @BindView(R.id.tipoTV)
    TextView tipoTV;
    @BindView(R.id.horarioTV)
    TextView horarioTV;
    @BindView(R.id.observacionesTV)
    TextView observacionesTV;
    @BindView(R.id.fechaTV)
    TextView fechaTV;

    public DetallesSesionProfesionalFragment() {

    }

    public static ComponentSesionesProfesional getmInstance(int idServicio, String fecha, String nombreCliente, String descripcion, String correo,
                                                            String foto, String tipoServicio, String horario, String profesional,
                                                            String lugar, int tiempo, String observaciones, double latitud, double longitud,
                                                            int idSeccion, int idNivel, int idBloque, int idCliente, boolean finalizado) {

        mInstance = new ComponentSesionesProfesional();
        mInstance.setIdServicio(idServicio);
        mInstance.setFecha(fecha);
        mInstance.setNombreCliente(nombreCliente);
        mInstance.setDescripcion(descripcion);
        mInstance.setCorreo(correo);
        mInstance.setFoto(foto);
        mInstance.setProfesional("Profesional Asignado: " + profesional);
        mInstance.setLugar("Lugar - Dirección: " + lugar);
        mInstance.setTiempo(tiempo);
        mInstance.setObservaciones("Observaciones: " + observaciones);
        mInstance.setLatitud(latitud);
        mInstance.setLongitud(longitud);
        mInstance.setTipoServicio("Tipo de Servicio: " + tipoServicio);
        mInstance.setHorario("Horario: " + horario);
        mInstance.setIdSeccion(idSeccion);
        mInstance.setIdNivel(idNivel);
        mInstance.setIdBloque(idBloque);
        mInstance.setIdCliente(idCliente);
        mInstance.setPhotoRes(R.drawable.img_button);
        mInstance.setType(Constants.STATIC);
        mInstance.setFinalizado(finalizado);
        return mInstance;

    }

    @Override
    public void onResume() {
        super.onResume();
        detallesSesionProfesionalPresenter.cambiarDisponibilidadProfesional(idSesion, idProfesional, false);
        detallesSesionProfesionalPresenter.validarServicioFinalizadoProfesional(idSesion, idProfesional, getContext());
        if (procedenciaFin) {
            detallesSesionProfesionalPresenter.validarvaloracionCliente(idSesion, idCliente);
            procedenciaFin = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_sesion_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        detallesSesionProfesionalPresenter = new DetallesSesionProfesionalPresenterImpl(this);

        Bundle bun = getArguments();
        ComponentSesionesProfesional componentSesionesProfesional = new ComponentSesionesProfesional();
        fotoPerfil = view.findViewById(R.id.foto);
        iniciar = view.findViewById(R.id.iniciar);

        fragmentDetallesProf = DetallesSesionProfesionalFragment.this;

        idSesion = bun.getInt("idServicio");
        this.fotoNombre = bun.getString("foto");
        latitud = bun.getDouble("latitud");
        longitud = bun.getDouble("longitud");
        nombreTV.setText(bun.getString("cliente"));
        fechaTV.setText(bun.getString("fecha"));
        descripcionTV.setText(bun.getString("descripcion"));
        correoTV.setText(bun.getString("correo"));
        direccionTV.setText(bun.getString("lugar"));
        tiempoPasar = bun.getInt("tiempo");
        tiempoTV.setText("Tiempo: " + obtenerHorario(bun.getInt("tiempo")));
        nivelTV.setText("Nivel: " + componentSesionesProfesional.obtenerNivel(bun.getInt("idSeccion"), bun.getInt("idNivel"), bun.getInt("idBloque")));
        tipoTV.setText(bun.getString("tipoServicio"));
        horarioTV.setText(bun.getString("horario"));
        observacionesTV.setText(bun.getString("observaciones"));
        idCliente = bun.getInt("idCliente");
        idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();

        iniciar.setOnClickListener(v -> {
            if(NetworkValidate.validate(getContext())){
                detallesSesionProfesionalPresenter.cambiarDisponibilidadProfesional(idSesion, idProfesional, true);

                Intent intent = new Intent(getContext(), SincronizarServicio.class);
                intent.putExtra("perfil", PROFESIONAL);
                intent.putExtra("idSesion", idSesion);
                intent.putExtra("idPerfil", idProfesional);
                intent.putExtra("tiempo", tiempoPasar);
                intent.putExtra("idSeccion", bun.getInt("idSeccion"));
                intent.putExtra("idNivel", bun.getInt("idNivel"));
                intent.putExtra("idBloque", bun.getInt("idBloque"));
                startActivity(intent);
            }else
                Toast.makeText(getContext(), "No tienes conexión a Intenet o es muy inestable", Toast.LENGTH_LONG).show();
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
            } else {
                SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesSesionProfesional);
                mapFragment.getMapAsync(this);
            }
        }

        detallesSesionProfesionalPresenter.getFotoPerfil(fotoNombre);
    }

    @OnClick(R.id.perfilClienteCard)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", PerfilFragment.PERFIL_CLIENTE);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        PerfilFragment perfilFragment = new PerfilFragment();
        perfilFragment.setArguments(bundle);
        perfilFragment.show(fragmentTransaction, "Perfil - Cliente");
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

    public void errorMsg(){
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡OH NO!",
                "No podemos continuar sin el permiso de ubicación.", true, "OK", ()->{
                    getFragmentManager().beginTransaction().detach(this).commit();
                });
    }

    @OnClick(R.id.nuevoTopico)
    public void onClickNuevoTopico(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoUsuario", Constants.TIPO_USUARIO_PROFESIONAL);
        bundle.putInt("idSesion", idSesion);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        NuevoTicketFragment ticketAyuda = new NuevoTicketFragment();
        ticketAyuda.setArguments(bundle);
        ticketAyuda.show(fragmentTransaction, "Ticket");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = getView().findViewById(R.id.scrollMapDetallesProfesional); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsDetallesSesionProfesional))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        agregarMarca(latitud, longitud);
    }

    public void agregarMarca(double lat, double longi){
        LatLng ubicacion = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Aquí será tu servicio."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }

    public String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(bitmap != null && fotoPerfil != null)
            fotoPerfil.setImageBitmap(bitmap);
    }

    @Override
    public void showError(String mensaje) {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
