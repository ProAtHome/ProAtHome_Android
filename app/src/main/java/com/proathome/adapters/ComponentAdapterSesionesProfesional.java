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
import com.proathome.StaticActivity;
import com.proathome.utils.ComponentSesionesProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterSesionesProfesional extends RecyclerView.Adapter<ComponentAdapterSesionesProfesional.ViewHolderSesionesProfesional>{

    private List<ComponentSesionesProfesional> mComponents;
    private OnClickListener mListener;

    public ComponentAdapterSesionesProfesional(List<ComponentSesionesProfesional> mComponents) {
        this.mComponents = mComponents;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolderSesionesProfesional onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component_profesional, parent, false);
        return new ViewHolderSesionesProfesional(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSesionesProfesional holder, int position) {
        ComponentSesionesProfesional component = mComponents.get(position);
        holder.setClickListener(mListener, component);
        holder.tvNombreCliente.setText(component.getNombreCliente());
        holder.tvNivel.setText(component.obtenerNivel(component.getIdSeccion(), component.getIdNivel(), component.getIdBloque()));
        holder.tvHorario.setText(component.getHorario());
        holder.setOnClickListeners(component.getIdServicio(), component.getNombreCliente(), component.getDescripcion(), component.getCorreo(), component.getFoto(), component.getProfesional(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoServicio(), component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getIdSeccion(), component.getIdNivel(), component.getIdBloque(), component.getIdCliente());
    }

    public void add(ComponentSesionesProfesional component){
        mComponents.add(component);
        notifyItemInserted(mComponents.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    class ViewHolderSesionesProfesional extends RecyclerView.ViewHolder implements  View.OnClickListener{

        @BindView(R.id.tvNombreCliente)
        TextView tvNombreCliente;
        @BindView(R.id.tvNivel)
        TextView tvNivel;
        @BindView(R.id.tvHorario)
        TextView tvHorario;
        Context context;
        View view;

        String nombreCliente = "", descripcion = "", correo = "", foto = "", nivel = "", profesional = "", lugar = "", observaciones = "", tipoServicio = "", horario = "";
        int idSeccion, idNivel, idBloque, idServicio, tiempo, idCliente;
        double latitud, longitud;

        public ViewHolderSesionesProfesional(@NonNull View itemView){

            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }

        void setOnClickListeners(int idServicio, String nombreCliente, String descripcion, String correo, String foto, String profesional, String lugar, int tiempo, String observaciones,
                                 String tipoServicio, String horario, double latitud, double longitud, int idSeccion, int idNivel, int idBloque, int idCliente){
            this.idServicio = idServicio;
            this.nombreCliente = nombreCliente;
            this.descripcion = descripcion;
            this.correo = correo;
            this.foto = foto;
            this.profesional = profesional;
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
            this.idCliente = idCliente;
            view.setOnClickListener(this);
        }

        void setClickListener(OnClickListener listener, ComponentSesionesProfesional component){
            view.setOnClickListener(view -> listener.onClickProfesional(component));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, StaticActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Detalles del Servicio");
            intent.putExtra("idServicio", this.idServicio);
            intent.putExtra("nombreCliente", this.nombreCliente);
            intent.putExtra("descripcion", this.descripcion);
            intent.putExtra("correo", this.correo);
            intent.putExtra("foto", this.foto);
            intent.putExtra("profesional", this.profesional);
            intent.putExtra("lugar", this.lugar);
            intent.putExtra("tiempo", this.tiempo);
            intent.putExtra("observaciones", this.observaciones);
            intent.putExtra("tipoServicio", this.tipoServicio);
            intent.putExtra("horario", this.horario);
            intent.putExtra("latitud", this.latitud);
            intent.putExtra("longitud", this.longitud);
            intent.putExtra("idSeccion", this.idSeccion);
            intent.putExtra("idNivel", this.idNivel);
            intent.putExtra("idBloque", this.idBloque);
            intent.putExtra("idCliente", this.idCliente);
            context.startActivity(intent);
        }

    }

}
