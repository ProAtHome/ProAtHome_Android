package com.proathome.Adapters;

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
import com.proathome.Views.cliente.ScrollActivity;
import com.proathome.Utils.pojos.Component;
import com.proathome.Utils.Constants;
import com.proathome.Utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder>{

    private List<Component> mComponents;
    private OnClickListener mListener;

    public ComponentAdapter(List<Component> mComponents) {
        this.mComponents = mComponents;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Component component = mComponents.get(position);
        holder.setClickListener(mListener, component);
        holder.tvNivel.setText("Nivel: " + component.obtenerNivel(component.getIdSeccion(),
                component.getIdNivel(), component.getIdBloque()));
        holder.tvTipo.setText("Tipo de Servicio: " + component.getTipoServicio());
        holder.tvHorario.setText("Horario: " + component.getHorario());
        holder.imgPhoto.setImageResource(component.getPhotoRes());
        holder.setOnClickListeners(component.getIdServicio(), component.getProfesional(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoServicio(),
                component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getIdSeccion(), component.getIdNivel(),
                component.getIdBloque(), component.getFecha(), component.getFotoProfesional(),
                component.getDescripcionProfesional(), component.getCorreoProfesional(), component.getSumar(),
                component.getTipoPlan(), component.getIdProfesional());
    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    public void add(Component component){
            mComponents.add(component);
            notifyItemInserted(mComponents.size() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        @BindView(R.id.imgPhoto)
        AppCompatImageView imgPhoto;
        @BindView(R.id.tvNivel)
        TextView tvNivel;
        @BindView(R.id.tvTipo)
        TextView tvTipo;
        @BindView(R.id.tvHorario)
        TextView tvHorario;
        Context context;

        String tipoPlan ="", profesional = "", lugar = "", observaciones = "", tipoServicio = "", horario = "",
                fecha = "", fotoProfesional = "", descripcionProfesional ="", correoProfesional = "";
        int idServicio, idSeccion, idNivel, idBloque, tiempo, idProfesional;
        double latitud, longitud;
        boolean sumar;

        View view;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setOnClickListeners(int idServicio, String profesional, String lugar, int tiempo, String observaciones,
                                 String tipoServicio, String horario, double latitud, double longitud,
                                 int idSeccion, int idNivel, int idBloque, String fecha, String fotoProfesional,
                                 String descripcionProfesional, String correoProfesional, boolean sumar, String tipoPlan,
                                 int idProfesional){
            this.idServicio = idServicio;
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
            this.fecha = fecha;
            this.fotoProfesional = fotoProfesional;
            this.descripcionProfesional = descripcionProfesional;
            this.correoProfesional = correoProfesional;
            this.sumar = sumar;
            this.tipoPlan = tipoPlan;
            this.idProfesional = idProfesional;
            view.setOnClickListener(this);
        }

        void setClickListener(OnClickListener listener, Component component){
            view.setOnClickListener(view -> listener.onClick(component));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ScrollActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Detalles");
            intent.putExtra("idServicio", this.idServicio);
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
            intent.putExtra("fecha", this.fecha);
            intent.putExtra("fotoProfesional", this.fotoProfesional);
            intent.putExtra("descripcionProfesional", this.descripcionProfesional);
            intent.putExtra("correoProfesional", this.correoProfesional);
            intent.putExtra("sumar", this.sumar);
            intent.putExtra("tipoPlan", this.tipoPlan);
            intent.putExtra("idProfesional", this.idProfesional);
            context.startActivity(intent);
        }

    }

}