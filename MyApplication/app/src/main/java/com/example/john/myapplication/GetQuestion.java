package com.example.john.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import static com.example.john.myapplication.Place.safeLongToInt;

public class GetQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseMultiQuery firebaseMultiQuery = new FirebaseMultiQuery(myRef);
        final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();
        allLoad.addOnCompleteListener(this, new AllOnCompleteListener());

    }



    private class AllOnCompleteListener implements OnCompleteListener<Map<DatabaseReference, DataSnapshot>> {
        @Override
        public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                final Map<DatabaseReference, DataSnapshot> result = task.getResult();
                // Look up DataSnapshot objects using the same DatabaseReferences you passed into FirebaseMultiQuery
                DataSnapshot dataSnapshot = result.get(myRef);
                Question q = new Question(1,"", "", "", "","");
                int value = safeLongToInt(dataSnapshot.child("infos").child("nbQuestions").getValue(Long.class));
                q.setId( 1 + (int)(Math.random() * ((value - 1) + 1)));
                q.setQUESTION(dataSnapshot.child("questions").child(Integer.toString(q.getId())).child("question").getValue(String.class));
                q.setOPTA(dataSnapshot.child("questions").child(Integer.toString(q.getId())).child("opta").getValue(String.class));
                q.setOPTB(dataSnapshot.child("questions").child(Integer.toString(q.getId())).child("optb").getValue(String.class));
                q.setOPTC(dataSnapshot.child("questions").child(Integer.toString(q.getId())).child("optc").getValue(String.class));
                q.setANSWER(dataSnapshot.child("questions").child(Integer.toString(q.getId())).child("answer").getValue(String.class));
                Intent i = new Intent(GetQuestion.this, MainMenuActivity.class);
                i.putExtra("Question", q);
                i.setType("text/plain");
                startActivity(i);
                finish();

            }
            else {
            }
            // Do stuff with views
        }
    }

}
