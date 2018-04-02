package com.example.john.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

/**
 * Created by Untel on 27/03/2018.
 */

public class Place implements Serializable {
    String nom;
    LatLng latLng;
    Marker marker;
    int nbPoint;

    public Place(String n, LatLng l,int point){
        nom = n;
        latLng = l;
        nbPoint = point;

    }

}
