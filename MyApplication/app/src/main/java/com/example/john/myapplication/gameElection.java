package com.example.john.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gameElection extends AppCompatActivity implements View.OnClickListener {


    Place currentPlace;
    Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_election);

        Button individuel = (Button) findViewById(R.id.individuel);
        Button duel = (Button) findViewById(R.id.duel);
        individuel.setOnClickListener(this);
        duel.setOnClickListener(this);
    }




    public void onClick(View v) {

        switch(v.getId()){
            case R.id.individuel:


                Intent i = getIntent();
                currentPlace =(Place) i.getSerializableExtra("currentPlace");
                currentPlayer =(Player) i.getSerializableExtra("currentPlayer");
                Intent nextActivity = new Intent(gameElection.this, PlaceGameQuestionActivity.class);
                nextActivity.putExtra("currentPlace", currentPlace);
                nextActivity.putExtra("currentPlayer", currentPlayer);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.duel:
                /*Intent i1 = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(i1);
                finish();*/

                Intent i2 = getIntent();
                currentPlace =(Place) i2.getSerializableExtra("currentPlace");
                currentPlayer =(Player) i2.getSerializableExtra("currentPlayer");

                Intent bluetoothActivity;
                bluetoothActivity = new Intent(gameElection.this, TwoDevice2P_names.class);
                bluetoothActivity.putExtra("currentPlace", currentPlace);
                bluetoothActivity.putExtra("currentPlayer", currentPlayer);
                startActivity(bluetoothActivity);
                finish();
                break;
        }
    }

}
