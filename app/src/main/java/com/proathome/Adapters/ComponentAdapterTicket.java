package com.proathome.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.proathome.R;
import com.proathome.Views.fragments_compartidos.FragmentTicketAyuda;
import com.proathome.Views.cliente.navigator.ayuda.AyudaFragment;
import com.proathome.Views.profesional.navigator.ayudaProfesional.AyudaProfesionalFragment;
import com.proathome.Utils.pojos.ComponentTicket;
import com.proathome.Utils.Constants;
import com.proathome.Utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterTicket extends RecyclerView.Adapter<ComponentAdapterTicket.ViewHolderTicket> {

    private List<ComponentTicket> mComponentTicket;
    private OnClickListener mOnClickListener;

    public ComponentAdapterTicket(List<ComponentTicket> mComponentTicket){
        this.mComponentTicket = mComponentTicket;
    }

    @NonNull
    @Override
    public ComponentAdapterTicket.ViewHolderTicket onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topico, parent, false);
        return new ViewHolderTicket(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTicket holder, int position) {
        ComponentTicket componentTicket = mComponentTicket.get(position);
        holder.setClickListener(mOnClickListener, componentTicket);
        holder.tituloTopico.setText("Tópico: " + componentTicket.getTituloTopico());
        holder.topico = "Tópico: " + componentTicket.getTituloTopico();
        holder.descripcion = "Problema: " + componentTicket.getDescripcion();
        holder.noTicket = "No. de Ticket: " + componentTicket.getNoTicket();
        holder.tvEstatus.setText("Estatus: " + componentTicket.getEstatus());
        holder.estatus = componentTicket.getEstatusINT();
        holder.fechaCreacion.setText("Fecha de Creación: " + componentTicket.getFechaCreacion());
        holder.idTicket = componentTicket.getIdTicket();
        holder.tipoUsuario = componentTicket.getTipoUsuario();
        holder.categoria = componentTicket.getCategoria();
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return mComponentTicket.size();
    }

    public void add(ComponentTicket componentTicket){
        mComponentTicket.add(componentTicket);
        notifyItemInserted(mComponentTicket.size() -1);
    }

    class ViewHolderTicket extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View view;
        private Context contexto;
        private int idTicket, estatus, tipoUsuario;
        private String topico, descripcion, noTicket, categoria;
        @BindView(R.id.tituloTopico)
        TextView tituloTopico;
        @BindView(R.id.estatus)
        TextView tvEstatus;
        @BindView(R.id.fechaCreacion)
        TextView fechaCreacion;

        public ViewHolderTicket(@NonNull View itemView){
            super(itemView);
            this.view = itemView;
            this.contexto = itemView.getContext();
            if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
                tvEstatus.setTextColor(this.contexto.getResources().getColor(R.color.colorRosa));
            ButterKnife.bind(this, itemView);
        }

        void setOnClickListeners(){
            this.view.setOnClickListener(this);
        }

        void setClickListener(OnClickListener onClickListener, ComponentTicket componentTicket){
            this.view.setOnClickListener(view -> onClickListener.onClickTicket(componentTicket));
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("idTicket", this.idTicket);
            bundle.putString("topico", this.topico);
            bundle.putString("descripcion", this.descripcion);
            bundle.putString("noTicket", this.noTicket);
            bundle.putInt("estatus", this.estatus);
            bundle.putInt("tipoUsuario", this.tipoUsuario);
            bundle.putString("categoria", this.categoria);
            FragmentTransaction fragmentTransaction = null;
            if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE)
                fragmentTransaction = AyudaFragment.ayudaFragment.getFragmentManager().beginTransaction();
            else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
                fragmentTransaction = AyudaProfesionalFragment.ayudaProfesionalFragment.getFragmentManager().beginTransaction();
            FragmentTicketAyuda fragmentTicketAyuda = new FragmentTicketAyuda();
            fragmentTicketAyuda.setArguments(bundle);
            fragmentTicketAyuda.show(fragmentTransaction, "Mensajes");
        }
    }

}
