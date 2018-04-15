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
        final SeekBar seek=(SeekBar) findViewById(R.id.seekBar);
        seek.setProgress(GoogleMapsActivity.getmInterval());
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GoogleMapsActivity.setmInterval(seekBarValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                seekBarValue = progress;
            }
        });


        int level = GoogleMapsActivity.getbatteryPctInitial()-GoogleMapsActivity.getBatteryPctCurrent();///(float) GoogleMapsActivity.getscale();
        TextView batterylvl= findViewById(R.id.batteryUsed);
        String lvl = "Batterie utilisée depuis le lancement de l'application : " + Integer.toString(level)+"%";
        batterylvl.setText(lvl);

        //Toolbar permettant de revenir à l'activité précédente
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setNavigationIcon(R.drawable.back18dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // Bouton permettant de supprimer tous les favoris
        Button b = findViewById(R.id.delete);


        }


    }

