package com.example.john.myapplication;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import static java.lang.Math.toIntExact;
import static java.lang.Thread.sleep;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

/**
 * Created by Untel on 27/03/2018.
 */

public class Place implements Serializable {
    int id;
    String nom;
    transient LatLng latLng;
    transient Marker  marker;
    int nbPoint;
    double Long;
    double Lat;

    public Place(int Id ,String n, LatLng l,int point){
        id = Id;
        nom = n;
        latLng = l;
        nbPoint = point;
        Long = l.longitude;
        Lat = l.latitude;

    }



    public interface MyCallback {
        void onCallback(Place p);
    }

    public static int safeLongToInt(long l) {
        return (int) Math.max(Math.min(Integer.MAX_VALUE, l), Integer.MIN_VALUE);
    }
}
