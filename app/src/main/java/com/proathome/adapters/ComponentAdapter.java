package com.proathome.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.proathome.R;
import com.proathome.utils.Component;
import com.proathome.utils.OnClickListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder>{

    private List<Component> mComponents;
    private OnClickListener mListener;

    public ComponentAdapter(List<Component> mComponents, OnClickListener mListener) {
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
        holder.tvNivel.setText(component.getNivel());
        holder.tvTipo.setText(component.getTipoClase());
        holder.tvHorario.setText(component.getHorario());
        holder.imgPhoto.setImageResource(component.getPhotoRes());

    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    public void add(Component component){

            mComponents.add(component);
            notifyItemInserted(mComponents.size() - 1);

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imgPhoto)
        AppCompatImageView imgPhoto;
        @BindView(R.id.tvNivel)
        TextView tvNivel;
        @BindView(R.id.tvTipo)
        TextView tvTipo;
        @BindView(R.id.tvHorario)
        TextView tvHorario;

        View view;

        public ViewHolder(@NonNull View itemView){

            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }

        void setClickListener(OnClickListener listener, Component component){

            view.setOnClickListener(view -> listener.onClick(component));

        }

    }

}