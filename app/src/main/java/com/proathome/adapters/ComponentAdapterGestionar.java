package com.proathome.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.proathome.R;
import com.proathome.ScrollActivity;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.OnClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterGestionar extends RecyclerView.Adapter<ComponentAdapterGestionar.ViewHolderGestionar>{

    private List<Component> mComponents;
    private OnClickListener mListener;

    public ComponentAdapterGestionar(List<Component> mComponents) {
        this.mComponents = mComponents;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolderGestionar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component_gestionar, parent, false);
        return new ViewHolderGestionar(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGestionar holder, int position) {

        Component component = mComponents.get(position);
        holder.setClickListener(mListener, component);
        holder.tvNivel.setText("Nivel: " + component.getNivel());
        holder.tvTipo.setText("Tipo de Clase: " + component.getTipoClase());
        holder.tvHorario.setText("Horario: " + component.getHorario());
        holder.tvProfesor.setText("Profesor: " + component.getProfesor());
        holder.tvActualizado.setText("Actualización: " + component.getActualizado());
        holder.setOnClickListeners(component.getIdClase(), component.getNivel(), component.getProfesor(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoClase(), component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getActualizado());

    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    public void add(Component component){

            mComponents.add(component);
            notifyItemInserted(mComponents.size() - 1);

    }

    class ViewHolderGestionar extends RecyclerView.ViewHolder implements  View.OnClickListener{

        @BindView(R.id.tvNivel)
        TextView tvNivel;
        @BindView(R.id.tvTipo)
        TextView tvTipo;
        @BindView(R.id.tvHorario)
        TextView tvHorario;
        @BindView(R.id.tvProfesor)
        TextView tvProfesor;
        @BindView(R.id.actualizado)
        TextView tvActualizado;
        Context context;

        String nivel = "", profesor = "", lugar = "", tiempo = "", observaciones = "", tipoClase = "", horario = "", actualizado = "";
        int idClase;
        double latitud, longitud;

        View view;

        public ViewHolderGestionar(@NonNull View itemView){

            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }

        void setOnClickListeners(int idClase, String nivel, String profesor, String lugar, String tiempo, String observaciones,
                                 String tipoClase, String horario, double latitud, double longitud, String actualizado){

            this.idClase = idClase;
            this.nivel = nivel;
            this.profesor = profesor;
            this.lugar = lugar;
            this.tiempo = tiempo;
            this.observaciones = observaciones;
            this.tipoClase = tipoClase;
            this.horario = horario;
            this.latitud = latitud;
            this.longitud = longitud;
            this.actualizado = actualizado;
            view.setOnClickListener(this);

        }

        void setClickListener(OnClickListener listener, Component component){

            view.setOnClickListener(view -> listener.onClick(component));

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ScrollActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Detalles de la Sesión");
            intent.putExtra("idClase", this.idClase);
            intent.putExtra("profesor", this.profesor);
            intent.putExtra("lugar", this.lugar);
            intent.putExtra("tiempo", this.tiempo);
            intent.putExtra("observaciones", this.observaciones);
            intent.putExtra("tipoClase", this.tipoClase);
            intent.putExtra("horario", this.horario);
            intent.putExtra("latitud", this.latitud);
            intent.putExtra("longitud", this.longitud);
            intent.putExtra("nivel", this.nivel);
            context.startActivity(intent);

        }

    }

}