package com.example.projetoencomendacorridatm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoencomendacorridatm.R;
import com.example.projetoencomendacorridatm.entidades.Percursos;

import java.util.List;

public class AdapterPercursos extends RecyclerView.Adapter<AdapterPercursos.MyViewHolder> {



    //Estrutura responsável por organizar os dados recuperados do banco nos componentes do layout a partir de uma lista com a classe de origem
    private List<Percursos> listPercurso;
    private Context context;

    public AdapterPercursos(List<Percursos> listPercurso, Context context) {
        this.listPercurso = listPercurso;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_percursos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Percursos percurso = listPercurso.get(position);
        holder.distancia.setText(percurso.distancia_percurso);
        holder.tempo.setText(percurso.tempo_percurso);

    }

    @Override
    public int getItemCount() {
        return listPercurso.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView distancia;
        TextView tempo;

        public MyViewHolder(View itemView) {
            super(itemView);
            distancia = itemView.findViewById(R.id.lblDistancia);
            tempo = itemView.findViewById(R.id.lblTempo);

        }

    }
}
