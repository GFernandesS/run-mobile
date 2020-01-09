package com.example.projetoencomendacorridatm.entidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



//Classe model responsável por armazenar em atributos e consequentemente no banco, os dados do percurso
public class Percursos {
    public String id_percurso;
    public String distancia_percurso;
    public String tempo_percurso;
    public String latitude_inicial;
    public String longitude_inicial;
    public String latitude_final;
    public String longitude_final;


    public Percursos(){
        id_percurso = FirebaseDatabase.getInstance().getReference().child("percursos").push().getKey();
    }



    public void salvarPercurso(String distancia_percurso, String tempo_percurso, String latitude_inicial, String longitude_inicial, String latitude_final, String longitude_final){
        this.distancia_percurso = distancia_percurso;
        this.latitude_inicial = latitude_inicial;
        this.longitude_inicial = longitude_inicial;
        this.latitude_final = latitude_final;
        this.longitude_final = longitude_final;
        this.tempo_percurso = tempo_percurso;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("percursos").child(id_percurso).setValue(this);

    }
}
