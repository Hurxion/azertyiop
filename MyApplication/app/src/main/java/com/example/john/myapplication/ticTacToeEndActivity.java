package com.example.john.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ticTacToeEndActivity extends AppCompatActivity {

    private String winner;
    private Player currentPlayer;
    private Place currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_end);

        Bundle bundle = getIntent().getExtras();
        winner = bundle.getString("winner");
        currentPlayer=TwoDevice2P_names.currentPlayer;
        currentPlace=TwoDevice2P_names.currentPlace;
        TextView textResult = (TextView) findViewById(R.id.textResult);

        if(winner.equals("Player 1")){
            textResult.setText("Bravo " + currentPlayer.getName() + " tu as gagné, tu obtiens " + currentPlace.nbPoint*2 + " points , ton score est maintenant de " + currentPlayer.getScore());
        } else {
            textResult.setText("Dommage " + currentPlayer.getName() + " tu as perdu, tu obtiens quand même " + currentPlace.nbPoint + " points pour ta course, ton score est maintenant de " + currentPlayer.getScore());

        }

    }


    //ONclick
    public void playagain(View o) {
        Intent intent = new Intent(this, GoogleMapsActivity.class);
        intent.putExtra("Player",currentPlayer);
        intent.setType("text/plain");
        startActivity(intent);
        finish();
    }

    public void retour(View o){
        Intent i = new Intent(this, MainMenuActivity.class);
        i.putExtra("logged", true);
        i.putExtra("Player", currentPlayer);
        i.setType("text/plain");
        startActivity(i);
        finish();
    }
}
