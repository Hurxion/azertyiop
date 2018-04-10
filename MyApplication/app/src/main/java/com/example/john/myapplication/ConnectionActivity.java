package com.example.john.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.john.myapplication.Place.safeLongToInt;


public class ConnectionActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ConnectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Player player = new Player("Jordan","assa",0);
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child("Jordan").setValue(player); //Ecriture

        Button rollDice = (Button) findViewById(R.id.rollDiceButton);
        rollDice.setOnClickListener(this);


        DatabaseReference anotherOne = myRef.child("users").child("Jordan");

        // Read from the database
        anotherOne.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               //widget.setText(value.toString());

                String value = dataSnapshot.child("name").getValue(String.class);
                EditText widget = (EditText) findViewById(R.id.editText);
                widget.setText(value);

                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rollDiceButton:
                Intent i = new Intent(ConnectionActivity.this, rollDiceActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }


}
