package com.example.john.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ConnectionActivity extends AppCompatActivity {

    private static final String TAG = "ConnectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Player player=new Player("Jordan",0);
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child("Jordan").setValue(player); //Ecriture


        DatabaseReference anotherOne= myRef.child("users").child("Jordan");

        // Read from the database
        anotherOne.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("name").getValue(String.class);

                //editText
                System.out.println(value);
                EditText widget = (EditText) findViewById(R.id.editText);
                widget.setText(value.toString());

                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
