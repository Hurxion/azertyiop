package com.example.john.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GenerateDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_db);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("infos").child("nbQuestions").setValue(5);
        myRef.child("infos").child("nbPlaces").setValue(5);

        // Generate Question
        Question q = new Question(1,"Quella place occupe la ville de Montréal parmi les villes les plus peuplées du Canada?"
                ,"1ère"
                ,"2ème"
                ,"3ème"
                ,"2ème");


        myRef.child("questions").child(Integer.toString(q.getId())).setValue(q); //Ecrsiture

         q = new Question(2,"Combien d'habitants compte Montréal ?"
                ,"3 346 384"
                ,"2 498 489"
                ,"1 704 694"
                ,"1 704 694");

        myRef.child("questions").child(Integer.toString(q.getId())).setValue(q); //Ecrsiture

        q = new Question(3,"Combien d'arrondissements compte Montréal ?"
                ,"19"
                ,"12"
                ,"7"
                ,"19");

        myRef.child("questions").child(Integer.toString(q.getId())).setValue(q); //Ecrsiture

        q = new Question(4,"Quel est le nom de famille de la mairesse de Montréal ?"
                ,"Racine"
                ,"Plante"
                ,"Rose"
                ,"Plante");

        myRef.child("questions").child(Integer.toString(q.getId())).setValue(q);

        q = new Question(5,"Quel est le nom du premier explorateur de l'île de Montréal ?"
                ,"Samuel de Champlain"
                ,"Jacques Cartier"
                ," Pierre de Cavagnal"
                ,"Jacques Cartier");
        myRef.child("questions").child(Integer.toString(q.getId())).setValue(q);


        // Generate Place

        Place p = new Place(1,"Oratoire St Joseph",new LatLng(45.492585,-73.618355),75);
        myRef.child("places").child(Integer.toString(p.id)).setValue(p);
        p = new Place(2,"Station Mont-Royal",new LatLng(45.524305,-73.581546),200);
        myRef.child("places").child(Integer.toString(p.id)).setValue(p);

         p = new Place(3,"Station Université Montreal",new LatLng(45.50273312,-73.61833595),50);
        myRef.child("places").child(Integer.toString(p.id)).setValue(p);
         p  = new Place(4,"Station CDN",new LatLng(45.49629896,-73.62246992),100);
        myRef.child("places").child(Integer.toString(p.id)).setValue(p);

        p  = new Place(5,"Pont Jacques Cartier",new LatLng(45.521710,-73.541418),500);
        myRef.child("places").child(Integer.toString(p.id)).setValue(p);

    }
}
