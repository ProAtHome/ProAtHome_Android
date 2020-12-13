package com.proathome.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.proathome.R;
import com.proathome.utils.ComponentMsgTickets;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentAdapterMsgTickets extends RecyclerView.Adapter<ComponentAdapterMsgTickets.ViewHolderMsgTickets> {

    public static List<ComponentMsgTickets> mComponentMsgTickets;

    public ComponentAdapterMsgTickets(List<ComponentMsgTickets> mComponentMsgTickets){
        this.mComponentMsgTickets = mComponentMsgTickets;
    }

    @NonNull
    @Override
    public ComponentAdapterMsgTickets.ViewHolderMsgTickets
                onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_ticket, parent,
                false);
        return new ViewHolderMsgTickets(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMsgTickets holder, int position) {
        ComponentMsgTickets componentMsgTickets = mComponentMsgTickets.get(position);
        holder.tvUsuario.setText(componentMsgTickets.getNombreUsuario());
        holder.tvMsg.setText(componentMsgTickets.getMensaje());
        if(componentMsgTickets.isOperador())
            holder.cardView.setCardBackgroundColor(Color.GRAY);
        else
            holder.cardView.setCardBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return mComponentMsgTickets.size();
    }

    public void remove(int position){
        mComponentMsgTickets.remove(position);
    }

    public void add(ComponentMsgTickets componentMsgTickets){
        mComponentMsgTickets.add(componentMsgTickets);
        notifyItemInserted(mComponentMsgTickets.size() - 1);
    }

    class ViewHolderMsgTickets extends RecyclerView.ViewHolder {

        private Context contexto;
        private View view;
        @BindView(R.id.tvUsuario)
        TextView tvUsuario;
        @BindView(R.id.tvMsg)
        TextView tvMsg;
        @BindView(R.id.cardViewMsg)
        MaterialCardView cardView;

        public ViewHolderMsgTickets(@NonNull View itemView){
            super(itemView);
            this.view = itemView;
            this.contexto = itemView.getContext();
            ButterKnife.bind(this, view);
        }

    }

}
