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
import com.proathome.utils.ComponentSesionesProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterSesionesProfesor extends RecyclerView.Adapter<ComponentAdapterSesionesProfesor.ViewHolderSesionesProfesor>{

    private List<ComponentSesionesProfesor> mComponents;
    private OnClickListener mListener;

    public ComponentAdapterSesionesProfesor(List<ComponentSesionesProfesor> mComponents) {
        this.mComponents = mComponents;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolderSesionesProfesor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component_profesor, parent, false);
        return new ViewHolderSesionesProfesor(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSesionesProfesor holder, int position) {

        ComponentSesionesProfesor component = mComponents.get(position);
        holder.setClickListener(mListener, component);
        holder.tvNombreEstudiante.setText(component.getNombreEstudiante());
        holder.tvNivel.setText(component.obtenerNivel(component.getIdSeccion(), component.getIdNivel(), component.getIdBloque()));
        holder.tvHorario.setText(component.getHorario());
        holder.setOnClickListeners(component.getIdClase(), component.getNombreEstudiante(), component.getDescripcion(), component.getCorreo(), component.getFoto(), component.getProfesor(), component.getLugar(),
                component.getTiempo(), component.getObservaciones(), component.getTipoClase(), component.getHorario(), component.getLatitud(),
                component.getLongitud(), component.getIdSeccion(), component.getIdNivel(), component.getIdBloque());

    }

    public void add(ComponentSesionesProfesor component){

        mComponents.add(component);
        notifyItemInserted(mComponents.size() - 1);

    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    class ViewHolderSesionesProfesor extends RecyclerView.ViewHolder implements  View.OnClickListener{

        @BindView(R.id.tvNombreEstudiante)
        TextView tvNombreEstudiante;
        @BindView(R.id.tvNivel)
        TextView tvNivel;
        @BindView(R.id.tvHorario)
        TextView tvHorario;
        Context context;
        View view;

        String nombreEstudiante = "", descripcion = "", correo = "", foto = "", nivel = "", profesor = "", lugar = "", observaciones = "", tipoClase = "", horario = "";
        int idSeccion, idNivel, idBloque, idClase, tiempo;
        double latitud, longitud;

        public ViewHolderSesionesProfesor(@NonNull View itemView){

            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }

        void setOnClickListeners(int idClase, String nombreEstudiante, String descripcion, String correo, String foto, String profesor, String lugar, int tiempo, String observaciones,
                                 String tipoClase, String horario, double latitud, double longitud, int idSeccion, int idNivel, int idBloque){

            this.idClase = idClase;
            this.nombreEstudiante = nombreEstudiante;
            this.descripcion = descripcion;
            this.correo = correo;
            this.foto = foto;
            this.nivel = nivel;
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
            view.setOnClickListener(this);

        }

        void setClickListener(OnClickListener listener, ComponentSesionesProfesor component){

            view.setOnClickListener(view -> listener.onClickProfesor(component));

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, StaticActivity.class);
            intent.putExtra(Constants.ARG_NAME, "Detalles de Clase");
            intent.putExtra("idClase", this.idClase);
            intent.putExtra("nombreEstudiante", this.nombreEstudiante);
            intent.putExtra("descripcion", this.descripcion);
            intent.putExtra("correo", this.correo);
            intent.putExtra("foto", this.foto);
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
            context.startActivity(intent);

        }

    }

}
