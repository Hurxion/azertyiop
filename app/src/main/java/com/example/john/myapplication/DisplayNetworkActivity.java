package com.example.john.myapplication;

/**
 * Activité permettant d'afficher plus d'informations sur le réseau lorsque
 * l'utilisateur a cliqué sur la fenêtre d'information
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;

public class DisplayNetworkActivity extends AppCompatActivity {

    public String SSID;
    public String BSSID;
    public String security;
    public FileManager fileManager = null;
    private Context ctx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        setContentView(R.layout.activity_display_network);

        this.ctx=this;

        //Barre d'outils afin de pouvoir revenir à l'activité précédente
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.back18dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);



            }
        });
        String file = "favorites.txt";
        this.fileManager = new FileManager();


        //Récupération des informations sur le réseau, passées par l'activité GoogleMaps
        Bundle bundle = getIntent().getExtras();
        SSID = bundle.getString("SSID");
        BSSID = bundle.getString("BSSID");
        security = bundle.getString("security");
        String network=this.SSID;


        TextView textView =  (TextView) findViewById(R.id.textView1);
        textView.setText(SSID);
        textView =  (TextView) findViewById(R.id.textView2);
        textView.setText(BSSID);
        textView =  (TextView) findViewById(R.id.textView3);
        textView.setText(security);

        try {
            if(this.fileManager.findInFile(file,network,this))
            {
                Button button = findViewById(R.id.favouriteButton);
                button.setText("Supprimer des Favoris");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * Fonction appelée lorsque le bouton "Partager" est cliqué
     * Démarre l'activité correspondant au partage sur les réseaux sociaux
     * @param view
     */
    public void shareHostSpot(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "SSID: "+ SSID+"\n"+"BSSID: "+BSSID+"\n"+"Security: "+security);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,"HotSpot"));

    }


    /**
     * Fonction appelée lorsque le bouton "Ajouter aux Favoris" est cliqué
     * @param view
     * @throws IOException
     */
    public void addToFavorites(View view) throws IOException {
        String file = "favorites.txt";
        String network = this.SSID;
        //Si le fichier existe alors on regarde si le réseau est déjà dans la liste
        if(this.fileManager.exists(file,this)){
                 //Si non alors on l'ajoute
            if(!this.fileManager.findInFile(file, network, this))
            {
                this.fileManager.appendToFile(file, network + "\n", this);
                Button button = findViewById(R.id.favouriteButton);
                button.setText("Supprimer des Favoris");

            } else {
                // Si oui alors on le supprime et on change le bouton
                this.fileManager.deleteLinefromFile(file,network,this);
                Button button = findViewById(R.id.favouriteButton);
                button.setText("Ajouter aux Favoris");

            }
         //Sinon on crée le fichier et on ajoute le réseau
        } else {
            if(this.fileManager.createFile(file,this)){
                this.fileManager.writeFile(file,network+"\n",this);
                Button button = findViewById(R.id.favouriteButton);
                button.setText("Supprimer des Favoris");
            }
        }

    }


}
