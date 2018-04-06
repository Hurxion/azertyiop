package com.example.john.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by Untel on 27/03/2018.
 */

public class Player implements Serializable {
    private String name;
    private String password;
    private int score;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player(String n,String pass,int s){
        name = n;
        score = s;
        password = pass;
    }

    public void addToScore(int p){
        score += p;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(this.name).setValue(this); //Ecriture

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
