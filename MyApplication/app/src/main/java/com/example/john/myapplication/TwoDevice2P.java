package com.example.john.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TwoDevice2P extends AppCompatActivity {

    public static Activity act_2p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act_2p = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        Button recommencer= (Button) TwoDevice2P.act_2p.findViewById(R.id.btn_again);
        recommencer.setVisibility(View.GONE);
        recommencer.setEnabled(false);
    }


    public void recommencer(){
        CanvasViewDouble2P.recommencer2();
    }
}
