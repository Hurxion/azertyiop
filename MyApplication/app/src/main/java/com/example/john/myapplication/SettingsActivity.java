package com.example.john.myapplication;

/**
 * Page de réglage permettant de : - Régler la vitesse du scan
 *                                 - Supprimer tous les favoris
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {
    private int seekBarValue;
    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_settings);

        this.ctx = this;
        // SeekBar afin de régler la durée entre 2 scans

        Bundle bundle = getIntent().getExtras();
        int batteryPctCurrent = bundle.getInt("batteryPctCurrent");
        long upLink = bundle.getLong("upLink");
        long downLink = bundle.getLong("downLink");

        TextView batterylvl= findViewById(R.id.batteryUsed);
        TextView downLinkt = findViewById(R.id.downLink);
        TextView upLinkt = findViewById(R.id.upLink);

        String lvl = "Batterie utilisée depuis le lancement de l'application : " + Integer.toString(batteryPctCurrent)+"%";
        String upl = "Uplink : " + String.valueOf(upLink) + " b/s";
        String dwl = "Downlink : " + String.valueOf(downLink) + " b/s";

        batterylvl.setText(lvl);
        downLinkt.setText(dwl);
        upLinkt.setText(upl);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.back18dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, MainMenuActivity.class);
                i.putExtra("logged", false);
                i.setType("text/plain");
                startActivity(i);
                finish();

            }
        });


        }


    }

