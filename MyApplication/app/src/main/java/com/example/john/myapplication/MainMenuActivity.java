package com.example.john.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button Jouer = (Button) findViewById(R.id.Jouer);
        Button Connexion = (Button) findViewById(R.id.connexion);
        Button Scores = (Button) findViewById(R.id.scores);
        Button Reglages = (Button) findViewById(R.id.Réglages);

        Jouer.setOnClickListener(this);
        Connexion.setOnClickListener(this);
        Scores.setOnClickListener(this);
        Reglages.setOnClickListener(this);
}
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.Jouer:
                Intent i = new Intent(MainMenuActivity.this, GoogleMapsActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.connexion:
                Intent i1 = new Intent(MainMenuActivity.this, GoogleMapsActivity.class);
                startActivity(i1);
                finish();
                break;
            case R.id.scores:
                Intent i2 = new Intent(MainMenuActivity.this, GoogleMapsActivity.class);
                startActivity(i2);
                finish();
                break;
            case R.id.Réglages:
                Intent i3 = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(i3);
                finish();
                break;
        }


    }
}
