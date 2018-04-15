package com.example.john.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static boolean logged;
    private static Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button Jouer = (Button) findViewById(R.id.Jouer);
        Button Connexion = (Button) findViewById(R.id.connexion);
        Button Scores = (Button) findViewById(R.id.scores);
        Button Reglages = (Button) findViewById(R.id.Réglages);
        Button Inscription  = (Button) findViewById(R.id.Inscription);



        TextView info  = (TextView) findViewById(R.id.Info);


        Bundle bundle = getIntent().getExtras();
        logged = bundle.getBoolean("logged");
        if(logged){
            player = (Player) bundle.getSerializable("Player");
            info.setText("Connecté en tant que " + player.getName() );

        }
        else{
            Jouer.setEnabled(false);
            info.setText("Vous devez créer un compte ou vous connecter pour Jouer");
        }


        Jouer.setOnClickListener(this);
        Connexion.setOnClickListener(this);
        Scores.setOnClickListener(this);
        Reglages.setOnClickListener(this);
        Inscription.setOnClickListener(this);

        //getQuestion();
}

    public void onClick(View v) {

        switch(v.getId()){
            case R.id.Jouer:
                Intent i = new Intent(MainMenuActivity.this, GoogleMapsActivity.class);
                i.putExtra("Player", player);
                i.setType("text/plain");
                startActivity(i);
                finish();
                break;
            case R.id.connexion:
                Intent i1 = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(i1);
                finish();
                break;
            case R.id.scores:
                Intent i2;
                i2 = new Intent(MainMenuActivity.this, ticTacToeEndActivity.class);
                startActivity(i2);
                finish();
                break;
            case R.id.Réglages:
                Intent i3 = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(i3);
                finish();
                break;
            case R.id.Inscription:
                Intent i4 = new Intent(MainMenuActivity.this, SignUpActivity.class);
                startActivity(i4);
                finish();
                break;
        }


    }
}
