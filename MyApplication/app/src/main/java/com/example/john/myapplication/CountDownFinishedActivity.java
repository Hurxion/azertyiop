package com.example.john.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by acer on 06-Apr-16.
 */
public class CountDownFinishedActivity extends AppCompatActivity {

    Player currentPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_game_answer);
        TextView textResult = (TextView) findViewById(R.id.textResult);
        Bundle b = getIntent().getExtras();
        Place currentPlace = (Place) b.getSerializable("currentPlace");
        currentPlayer = (Player) b.getSerializable("currentPlayer");
        String s = "Dommage " + currentPlayer.getName() + ", tu n'as pas réussi à te rendre à temps à " + currentPlace.nom + ". Tu perds " + currentPlace.nbPoint + " points , ton score est maintenant de " + currentPlayer.getScore();
        textResult.setText(s);


    }

    //ONclick
    public void playagain(View o) {
        Intent intent = new Intent(this, GoogleMapsActivity.class);
        intent.putExtra("Player",currentPlayer);
        intent.setType("text/plain");
        startActivity(intent);
        finish();
    }
}