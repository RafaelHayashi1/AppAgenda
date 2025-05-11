package com.example.agendaatividadesv2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaatividadesv2.DetalheAtividadeActivity;
import com.example.agendaatividadesv2.R;
import com.example.agendaatividadesv2.modelos.Atividade;

import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder> {

    private List<Atividade> atividades;
    private Context context;

    public AtividadeAdapter(Context context, List<Atividade> atividades) {
        this.context = context;
        this.atividades = atividades;
    }

    @NonNull
    @Override
    public AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_atividade, parent, false);
        return new AtividadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeViewHolder holder, int position) {
        Atividade atividade = atividades.get(position);
        holder.tvTitulo.setText(atividade.getTitulo());
        holder.tvHorario.setText(atividade.getHora());
        holder.tvLocal.setText(atividade.getLocal());

        holder.itemView.setOnClickListener(v -> {
            android.util.Log.d("AtividadeAdapter", "Clicou na atividade ID: " + atividade.getId());
            Intent intent = new Intent(context, DetalheAtividadeActivity.class);
            intent.putExtra("atividade_id", atividade.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    public void setAtividades(List<Atividade> novasAtividades) {
        this.atividades.clear();
        this.atividades.addAll(novasAtividades);
        notifyDataSetChanged();
    }

    static class AtividadeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvHorario, tvLocal;

        AtividadeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvHorario = itemView.findViewById(R.id.tvHorario);
            tvLocal = itemView.findViewById(R.id.tvLocal);
        }
    }
} 