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
import com.proathome.ScrollActivity;
import com.proathome.ScrollActivityProfesor;
import com.proathome.StaticActivity;
import com.proathome.utils.Component;
import com.proathome.utils.ComponentProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterGestionarProfesor extends RecyclerView.Adapter<ComponentAdapterGestionarProfesor.ViewHolderProfesor> {

    private List<ComponentProfesor> mComponents;
    private OnClickListener mListener;

    public ComponentAdapterGestionarProfesor(List<ComponentProfesor> mComponents){
        this.mComponents = mComponents;
    }

    @NonNull
    @Override
    public ViewHolderProfesor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component_gestionar_profesor, parent, false);
        return new ViewHolderProfesor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProfesor holder, int position) {
        ComponentProfesor component = mComponents.get(position);
        Component componentAux = new Component();
        holder.setClickListener(mListener, component);
        holder.tvNivel.setText("Nivel: " + componentAux.obtenerNivel(component.getIdSeccion(), component.getIdNivel(), component.getIdBloque()));
        holder.tvTipo.setText("Tipo de Clase: " + component.getTipoClase());
        holder.tvHorario.setText("Horario: " + component.getHorario());
        holder.tvFecha.setText("Fecha:: " + component.getFecha());
        holder.tvActualizado.setText("Actualización: " + component.getActualizado());
        holder.setOnClickListeners(component.getIdClase(), component.getEstudiante(), component.getCorreoEstudiante(), component.getDescripcionEstudiante(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoClase(), component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getActualizado(), component.getIdSeccion(), component.getIdNivel(), component.getIdBloque(), component.getFecha(), component.getTipoPlan(), component.getFotoEstudiante());
    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    public void add(ComponentProfesor component){
        mComponents.add(component);
        notifyItemInserted(mComponents.size() - 1);
    }

    class ViewHolderProfesor extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        String tipoPlan = "", estudiante = "", correo = "", descripcion = "", lugar = "", observaciones = "", tipoClase = "", horario = "", actualizado = "", fecha = "", foto = "";
        int idClase, idSeccion, idNivel, idBloque, tiempo;
        double latitud, longitud;

        View view;

        public ViewHolderProfesor(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setOnClickListeners(int idClase, String estudiante, String correo,String descripcion, String lugar, int tiempo, String observaciones,
                                 String tipoClase, String horario, double latitud, double longitud, String actualizado, int idSeccion, int idNivel, int idBloque, String fecha, String tipoPlan, String foto) {
            this.idClase = idClase;
            this.estudiante = estudiante;
            this.correo = correo;
            this.descripcion = descripcion;
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
            this.actualizado = actualizado;
            this.tipoPlan = tipoPlan;
            this.foto = foto;
            view.setOnClickListener(this);
        }

        void setClickListener(OnClickListener listener, ComponentProfesor component) {
            view.setOnClickListener(view -> listener.onClickGestionarProfesor(component));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, StaticActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Gestión de clase");
            intent.putExtra("idClase", this.idClase);
            intent.putExtra("estudiante", this.estudiante);
            intent.putExtra("lugar", this.lugar);
            intent.putExtra("tiempo", this.tiempo);
            intent.putExtra("correo", this.correo);
            intent.putExtra("descripcion", this.descripcion);
            intent.putExtra("observaciones", this.observaciones);
            intent.putExtra("tipoClase", this.tipoClase);
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
