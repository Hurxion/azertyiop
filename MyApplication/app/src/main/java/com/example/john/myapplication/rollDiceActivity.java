package com.example.john.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class rollDiceActivity extends AppCompatActivity implements SensorEventListener {


    public static final Random RANDOM = new Random();
    private ImageView imageView1;
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    public static int rollDiceResult;
    private boolean isRepeated;
    public static Activity act_rollDice;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_rollDice=this;
        setContentView(R.layout.activity_roll_dice);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

        Bundle bundle = getIntent().getExtras();
        isRepeated = bundle.getBoolean("isRepeated");

        if(isRepeated) {
            TextView result= (TextView) findViewById(R.id.result);
            result.setText("Vous avez obtenu le même resultat. \nVeuillez lancer le dé une autre fois.");
        }

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

    }

    public static int randomDiceValue() {
        return RANDOM.nextInt(6) + 1;
    }


    public void rollDices(){
        final Animation anim1 = AnimationUtils.loadAnimation(rollDiceActivity.this, R.anim.shake);
        final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rollDiceResult = randomDiceValue();
                int res = getResources().getIdentifier("dice_" + rollDiceResult, "drawable", "com.example.john.myapplication");
                imageView1.setImageResource(res);
                TextView result= (TextView) findViewById(R.id.result);
                result.setText("Le résultat est "+rollDiceResult+".");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(rollDiceActivity.this, TwoDevice2P.class);
                        startActivity(intent);
                    }
                }, 5000);

                sensorMan.unregisterListener(rollDiceActivity.this);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        anim1.setAnimationListener(animationListener);
        imageView1.startAnimation(anim1);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            mGravity = event.values.clone();// Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 10) {
                //Log.d("sensor", "shake detected w/ speed: " + mAccel);
                //Toast.makeText(this, "shake detected w/ speed: " + mAccel, Toast.LENGTH_SHORT).show();
                this.rollDices();

            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}



