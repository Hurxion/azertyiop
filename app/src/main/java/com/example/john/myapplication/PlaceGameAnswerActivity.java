package com.example.john.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.TextView;

    /**
     * Created by acer on 06-Apr-16.
     */
    public class PlaceGameAnswerActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_place_game_answer);
            TextView textResult = (TextView) findViewById(R.id.textResult);
            Bundle b = getIntent().getExtras();
            Boolean goodAnswer = b.getBoolean("goodAnswer");
            Place currentPlace = (Place) b.getSerializable("currentPlace");
            Player currentPlayer = (Player) b.getSerializable("currentPlayer");
            if(goodAnswer){
                currentPlayer.addToScore(currentPlace.nbPoint);
                textResult.setText("Bravo " + currentPlayer.getName() + " bonne réponse, tu obtiens " + currentPlace.nbPoint*2 + " points , ton score est maintenant de " + currentPlayer.getScore());
            }
            else{
                textResult.setText("Dommage " + currentPlayer.getName() + " mauvaise réponse, tu obtiens quand même " + currentPlace.nbPoint + " points pour ta course, ton score est maintenant de " + currentPlayer.getScore());
            }

        }
        public void playagain(View o) {
            Intent intent = new Intent(this, GoogleMapsActivity.class);
            startActivity(intent);
        }
    }