package com.example.projetoencomendacorridatm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projetoencomendacorridatm.R;
import com.example.projetoencomendacorridatm.adapter.AdapterPercursos;
import com.example.projetoencomendacorridatm.entidades.Percursos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tela_historico extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterPercursos percursosAdapter;
    private List<Percursos> percursoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_historico);
        //Configuração do adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        percursosAdapter = new AdapterPercursos(percursoList, getApplicationContext());
        recyclerView.setAdapter(percursosAdapter);

        recuperarPercursos();

    }

    private void recuperarPercursos(){
        percursoList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("percursos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot percursosda: dataSnapshot.getChildren()){
                    Percursos percurso = percursosda.getValue(Percursos.class);
                    percursoList.add(percurso);
                    percursosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickVoltar(View v){
        Intent i = new Intent(getApplicationContext(), tela_central.class);
        startActivity(i);
        finish();
    }
}
