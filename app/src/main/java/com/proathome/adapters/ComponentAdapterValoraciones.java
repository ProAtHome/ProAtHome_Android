package com.proathome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proathome.R;
import com.proathome.utils.ComponentValoraciones;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterValoraciones extends RecyclerView.Adapter<ComponentAdapterValoraciones.ViewHolder> {

    private List<ComponentValoraciones> mComponents;

    public ComponentAdapterValoraciones(List<ComponentValoraciones> mComponents){
        this.mComponents = mComponents;
    }

    @NonNull
    @Override
    public ComponentAdapterValoraciones.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_valoraciones, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComponentValoraciones componentValoraciones = mComponents.get(position);
        holder.tvComentario.setText(componentValoraciones.getComentario());
        holder.ratingBar.setStepSize(0.5f);
        holder.ratingBar.setRating(componentValoraciones.getValoracion());
    }

    @Override
    public int getItemCount() {
        return mComponents.size();
    }

    public void add(ComponentValoraciones componentValoraciones){
        mComponents.add(componentValoraciones);
        notifyItemChanged(mComponents.size() -1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Context context;
        View view;
        @BindView(R.id.tvComentario)
        TextView tvComentario;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            context = itemView.getContext();
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

}
