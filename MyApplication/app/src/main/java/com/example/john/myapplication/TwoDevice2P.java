package com.example.john.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class TwoDevice2P extends AppCompatActivity implements View.OnClickListener{

    public static Activity act_2p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act_2p = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        Button finir= (Button) findViewById(R.id.finir);
        finir.setOnClickListener(this);

    }


    public void onClick(View v) {

        switch(v.getId()){
            case R.id.finir:

                Intent intent = new Intent(this, ticTacToeEndActivity.class);
                intent.putExtra("winner", "Player 2");
                startActivity(intent);
                rollDiceActivity.act_rollDice.finish();
                TwoDevice2P.act_2p.finish();
        }
    }

}
