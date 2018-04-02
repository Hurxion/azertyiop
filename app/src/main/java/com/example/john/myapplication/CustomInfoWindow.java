package com.example.john.myapplication;


/**
 * Classe implémentant GoogleMap.InfoWindowAdapter, permettant ainsi de personnaliser
 * les fenêtres d'informations GoogleMap
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindow(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.info_window, null);

        TextView titre = view.findViewById(R.id.titre); // SSID du réseau
        TextView content = view.findViewById(R.id.content);// Sécurité du réseau
        ImageView image = view.findViewById(R.id.image);// Image représentant le niveau RSSI

        //Récupération du Routeur associé au marqueur
        WifiRouter Routeur = (WifiRouter) marker.getTag();

        int level = Routeur.getRSSI();
        int imageId;
        //On sélectionne la bonne image wifi selon le niveau RSSI
        switch(level){
            case 0:
                imageId = context.getResources().getIdentifier("wifi1".toLowerCase(),
                        "drawable", context.getPackageName());
                image.setImageResource(imageId);
                break;
            case 1:
                imageId = context.getResources().getIdentifier("wifi2".toLowerCase(),
                        "drawable", context.getPackageName());
                image.setImageResource(imageId);
                break;
            case 2:
                imageId = context.getResources().getIdentifier("wifi3".toLowerCase(),
                        "drawable", context.getPackageName());
                image.setImageResource(imageId);
                break;
            case 3:
                imageId = context.getResources().getIdentifier("wifi4".toLowerCase(),
                        "drawable", context.getPackageName());
                image.setImageResource(imageId);
                break;
            case 4:
                imageId = context.getResources().getIdentifier("wifi5".toLowerCase(),
                        "drawable", context.getPackageName());
                image.setImageResource(imageId);
                break;
            default:
                imageId = context.getResources().getIdentifier("nowifi".toLowerCase(),
                        "drawable", context.getPackageName());
                image.setImageResource(imageId);

        }

        titre.setText(Routeur.getSSID());
        if(Routeur.getSecurite().toLowerCase().contains("WPA".toLowerCase()) ||Routeur.getSecurite().toLowerCase().contains("WEP".toLowerCase())){
            content.setText("Sécurisé");
        }
        else{
            content.setText("Ouvert");
        }
        return view;

    }
}
