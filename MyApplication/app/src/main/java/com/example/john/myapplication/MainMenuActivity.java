package com.example.john.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static boolean logged;
    private static Player player;
    TrafficStats ts;
    long tStart;
    long initialRx;
    long initialTx;
    private static IntentFilter ifilter;
    private static Intent batteryStatus;
    private static int batteryPctInitial;
    private static int batteryPctCurrent;
    private static int scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button Jouer = (Button) findViewById(R.id.Jouer);
        Button Connexion = (Button) findViewById(R.id.connexion);
        Button Scores = (Button) findViewById(R.id.scores);
        Button Reglages = (Button) findViewById(R.id.Réglages);
        Button Inscription  = (Button) findViewById(R.id.Inscription);

         ts = new TrafficStats();
         tStart = System.currentTimeMillis();
         initialRx = ts.getMobileRxBytes();
         initialTx = ts.getMobileTxBytes();



        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);
        batteryPctInitial = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);


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

    @Override
    public void onResume() {
        super.onResume();

        TextView info  = (TextView) findViewById(R.id.Info);
        Button Jouer = (Button) findViewById(R.id.Jouer);

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
    }

    public void onClick(View v) {

        switch(v.getId()){
            case R.id.Jouer:
                Intent i = new Intent(MainMenuActivity.this, GoogleMapsActivity.class);
                i.putExtra("Player", player);
                i.setType("text/plain");
                startActivity(i);
                break;
            case R.id.connexion:
                Intent i1 = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(i1);
                break;
            case R.id.scores:
                Intent i2;
                i2 = new Intent(MainMenuActivity.this, ticTacToeEndActivity.class);
                startActivity(i2);
                break;
            case R.id.Réglages:
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                long laterRx=ts.getMobileRxBytes(); //Après
                double bandWidthRDown = (laterRx-initialRx)/elapsedSeconds;

                long tEnd2 = System.currentTimeMillis();
                long tDelta2 = tEnd2 - tStart;
                double elapsedSeconds2 = tDelta2 / 1000.0;
                long laterTx2 =ts.getMobileTxBytes(); //Après
                double bandWidthRx2Up = (laterTx2 -initialTx)/elapsedSeconds2;

                batteryStatus = this.registerReceiver(null, ifilter);
                batteryPctCurrent = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) - batteryPctInitial;

                Intent i3 = new Intent(MainMenuActivity.this, SettingsActivity.class);

                i3.putExtra("downLink", bandWidthRDown);
                i3.putExtra("upLink", bandWidthRx2Up);
                i3.putExtra("batteryPctCurrent", batteryPctCurrent);

                i3.setType("text/plain");
                startActivity(i3);
                break;
            case R.id.Inscription:
                Intent i4 = new Intent(MainMenuActivity.this, SignUpActivity.class);
                startActivity(i4);
                break;
        }


    }
}

// TrafficStats ts = new TrafficStats();
//long tStart = System.currentTimeMillis(); (Il faut conserver toujours ces trois variables)
//long initialRx=ts.getMobileRxBytes();
//long initialTx=ts.getMobileTxBytes();

/*DownLINK
*
*   long tEnd = System.currentTimeMillis();
    long tDelta = tEnd - tStart;
    double elapsedSeconds = tDelta / 1000.0;
*
* long laterRx=ts.getMobileRxBytes(); //Après
*
* bandWidthRx= (laterRx-initialRx)/elapsedSeconds;
*
* */

/*UpLINK
*
*   long tEnd = System.currentTimeMillis();
    long tDelta = tEnd - tStart;
    double elapsedSeconds = tDelta / 1000.0;
*
* long laterTx=ts.getMobileTxBytes(); //Après
*
* bandWidthRx= (laterTx-initialTx)/elapsedSeconds;
*
* */