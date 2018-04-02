package com.example.john.myapplication;

import java.io.Serializable;

/**
 * Created by Untel on 27/03/2018.
 */

public class Player implements Serializable {
    private String name;
    private int score;

    public Player(String n,int s){
        name = n;
        score = s;
    }

    public void addToScore(int p){
        score += p;
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
