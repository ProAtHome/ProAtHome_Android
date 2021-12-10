package com.proathome.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proathome.R;
import com.proathome.ui.StaticActivity;
import com.proathome.utils.Component;
import com.proathome.utils.ComponentProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterGestionarProfesional extends RecyclerView.Adapter<ComponentAdapterGestionarProfesional.ViewHolderProfesional> {

    private List<ComponentProfesional> mComponents;
    private OnClickListener mListener;

    public ComponentAdapterGestionarProfesional(List<ComponentProfesional> mComponents){
        this.mComponents = mComponents;
    }

    @NonNull
    @Override
    public ViewHolderProfesional onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component_gestionar_profesional, parent, false);
        return new ViewHolderProfesional(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProfesional holder, int position) {
        ComponentProfesional component = mComponents.get(position);
        Component componentAux = new Component();
        holder.setClickListener(mListener, component);
        holder.tvNivel.setText("Nivel: " + componentAux.obtenerNivel(component.getIdSeccion(), component.getIdNivel(), component.getIdBloque()));
        holder.tvTipo.setText("Tipo de Servicio: " + component.getTipoServicio());
        holder.tvHorario.setText("Horario: " + component.getHorario());
        holder.tvFecha.setText("Fecha:: " + component.getFecha());
        holder.tvActualizado.setText("Actualización: " + component.getActualizado());
        holder.setOnClickListeners(component.getIdServicio(), component.getCliente(), component.getCorreoCliente(), component.getDescripcionCliente(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoServicio(), component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getActualizado(), component.getIdSeccion(), component.getIdNivel(), component.getIdBloque(), component.getFecha(), component.getTipoPlan(), component.getFotoCliente());
    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    public void add(ComponentProfesional component){
        mComponents.add(component);
        notifyItemInserted(mComponents.size() - 1);
    }

    class ViewHolderProfesional extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvNivel)
        TextView tvNivel;
        @BindView(R.id.tvTipo)
        TextView tvTipo;
        @BindView(R.id.tvHorario)
        TextView tvHorario;
        @BindView(R.id.tvFecha)
        TextView tvFecha;
        @BindView(R.id.actualizado)
        TextView tvActualizado;
        Context context;

        String tipoPlan = "", cliente = "", correo = "", descripcion = "", lugar = "", observaciones = "", tipoServicio = "", horario = "", actualizado = "", fecha = "", foto = "";
        int idServicio, idSeccion, idNivel, idBloque, tiempo;
        double latitud, longitud;

        View view;

        public ViewHolderProfesional(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setOnClickListeners(int idServicio, String cliente, String correo,String descripcion, String lugar, int tiempo, String observaciones,
                                 String tipoServicio, String horario, double latitud, double longitud, String actualizado, int idSeccion, int idNivel, int idBloque, String fecha, String tipoPlan, String foto) {
            this.idServicio = idServicio;
            this.cliente = cliente;
            this.correo = correo;
            this.descripcion = descripcion;
            this.lugar = lugar;
            this.tiempo = tiempo;
            this.observaciones = observaciones;
            this.tipoServicio = tipoServicio;
            this.horario = horario;
            this.latitud = latitud;
            this.longitud = longitud;
            this.idSeccion = idSeccion;
            this.idNivel = idNivel;
            this.idBloque = idBloque;
            this.fecha = fecha;
            this.actualizado = actualizado;
            this.tipoPlan = tipoPlan;
            this.foto = foto;
            view.setOnClickListener(this);
        }

        void setClickListener(OnClickListener listener, ComponentProfesional component) {
            view.setOnClickListener(view -> listener.onClickGestionarProfesional(component));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, StaticActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Gestión del Servicio");
            intent.putExtra("idServicio", this.idServicio);
            intent.putExtra("cliente", this.cliente);
            intent.putExtra("lugar", this.lugar);
            intent.putExtra("tiempo", this.tiempo);
            intent.putExtra("correo", this.correo);
            intent.putExtra("descripcion", this.descripcion);
            intent.putExtra("observaciones", this.observaciones);
            intent.putExtra("tipoServicio", this.tipoServicio);
            intent.putExtra("horario", this.horario);
            intent.putExtra("latitud", this.latitud);
            intent.putExtra("longitud", this.longitud);
            intent.putExtra("fecha", this.fecha);
            intent.putExtra("idSeccion", this.idSeccion);
            intent.putExtra("idNivel", this.idNivel);
            intent.putExtra("idBloque", this.idBloque);
            intent.putExtra("tipoPlan", this.tipoPlan);
            intent.putExtra("foto", this.foto);
            context.startActivity(intent);
        }
    }
}
