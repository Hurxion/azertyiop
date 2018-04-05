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

    public Place() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        DatabaseReference anotherOne = myRef.child("infos").child("nbPlaces");
        readData(new MyCallback() {
            @Override
            public void onCallback(Place p) {
                while(p.nom == null){
                    id = p.id;
                    nom = p.nom;
                    nbPoint = p.nbPoint;
                    Lat = p.Lat;
                    Long = p.Long;
                    latLng = new LatLng(Lat,Long);
                }


            }
        });
    }



    public void readData(final MyCallback myCallback) {
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         final DatabaseReference myRef = database.getReference();
          myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Place p = new Place(0, "", new LatLng(0,0),0);
                int value = safeLongToInt(dataSnapshot.child("infos").child("nbPlaces").getValue(Long.class));
                p.id = 1 + (int)(Math.random() * ((value - 1) + 1));
                p.nom = dataSnapshot.child("places").child(Integer.toString(p.id)).child("nom").getValue(String.class);
                p.Lat = (dataSnapshot.child("places").child(Integer.toString(p.id)).child("Lat").getValue(Double.class));
                p.Long = (double) dataSnapshot.child("places").child(Integer.toString(p.id)).child("Long").getValue(Double.class);
                p.nbPoint = safeLongToInt(dataSnapshot.child("places").child(Integer.toString(p.id)).child("nbPoint").getValue(Long.class));
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCallback.onCallback(p);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public interface MyCallback {
        void onCallback(Place p);
    }

    public static int safeLongToInt(long l) {
        return (int) Math.max(Math.min(Integer.MAX_VALUE, l), Integer.MIN_VALUE);
    }
}
