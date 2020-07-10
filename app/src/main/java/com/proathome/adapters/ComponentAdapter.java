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
        holder.tvNivel.setText(component.obtenerNivel(component.getIdSeccion(), component.getIdNivel(), component.getIdBloque()));
        holder.tvTipo.setText(component.getTipoClase());
        holder.tvHorario.setText(component.getHorario());
        holder.imgPhoto.setImageResource(component.getPhotoRes());
        holder.setOnClickListeners(component.getIdClase(), component.getProfesor(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoClase(), component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getIdSeccion(), component.getIdNivel(), component.getIdBloque(), component.getFecha());

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

        String profesor = "", lugar = "", observaciones = "", tipoClase = "", horario = "", fecha = "";
        int idClase, idSeccion, idNivel, idBloque, tiempo;
        double latitud, longitud;

        View view;

        public ViewHolder(@NonNull View itemView){

            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }

        void setOnClickListeners(int idClase, String profesor, String lugar, int tiempo, String observaciones,
                                 String tipoClase, String horario, double latitud, double longitud, int idSeccion, int idNivel, int idBloque, String fecha){

            this.idClase = idClase;
            this.profesor = profesor;
            this.lugar = lugar;
            this.tiempo = tiempo;
            this.observaciones = observaciones;
            this.tipoClase = tipoClase;
            this.horario = horario;
            this.latitud = latitud;
            this.longitud = longitud;
            this.idSeccion = idSeccion;
            this.idNivel = idNivel;
            this.idBloque = idBloque;
            this.fecha = fecha;
            view.setOnClickListener(this);

        }

        void setClickListener(OnClickListener listener, Component component){

            view.setOnClickListener(view -> listener.onClick(component));

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ScrollActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Detalles");
            intent.putExtra("idClase", this.idClase);
            intent.putExtra("profesor", this.profesor);
            intent.putExtra("lugar", this.lugar);
            intent.putExtra("tiempo", this.tiempo);
            intent.putExtra("observaciones", this.observaciones);
            intent.putExtra("tipoClase", this.tipoClase);
            intent.putExtra("horario", this.horario);
            intent.putExtra("latitud", this.latitud);
            intent.putExtra("longitud", this.longitud);
            intent.putExtra("idSeccion", this.idSeccion);
            intent.putExtra("idNivel", this.idNivel);
            intent.putExtra("idBloque", this.idBloque);
            intent.putExtra("fecha", this.fecha);
            context.startActivity(intent);

        }

    }

}