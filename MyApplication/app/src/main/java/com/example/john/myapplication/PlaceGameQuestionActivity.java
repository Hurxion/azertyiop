package com.example.john.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.example.john.myapplication.Place.safeLongToInt;

public class PlaceGameQuestionActivity extends AppCompatActivity {
    TextView info;
    Question currentQ;
    TextView txtQuestion, times;
    Button button1, button2, button3;
    Place currentPlace;
    Player currentPlayer;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    ValueEventListener Listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_game_question);


        Intent i = getIntent();
        currentPlace =(Place) i.getSerializableExtra("currentPlace");
        currentPlayer =(Player) i.getSerializableExtra("currentPlayer");

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        times = (TextView) findViewById(R.id.timers);
        times.setText("");
// the textview in which the question will be displayed
// the three buttons,
// the idea is to set the text of three buttons with the options from question bank
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
// the textview in which score will be displayed
        info = (TextView) findViewById(R.id.score);
        getQuestion();
// method which will set the things up for our game
// button click listeners
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button1.getText().toString());
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button2.getText().toString());
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button3.getText().toString());
            }
        });

    }

    public void getAnswer(String AnswerString) {
        boolean goodAnswer;
        myRef.removeEventListener(Listener);
        if (currentQ.getANSWER().equals(AnswerString)) {

            goodAnswer = true;
        } else {
            goodAnswer = false;
        }
            Intent intent = new Intent(PlaceGameQuestionActivity.this,
                    PlaceGameAnswerActivity.class);
            Bundle b = new Bundle();
            b.putBoolean("goodAnswer", goodAnswer);
            b.putSerializable("currentPlace",currentPlace);
            b.putSerializable("currentPlayer",currentPlayer);// Your score
            intent.putExtras(b); // Put your score to your next
            startActivity(intent);
            finish();
        }

    private void setQuestionView() {
// the method which will put all things together
        txtQuestion.setText(currentQ.getQUESTION());
        button1.setText(currentQ.getOPTA());
        button2.setText(currentQ.getOPTB());
        button3.setText(currentQ.getOPTC());
        String s = currentPlayer.getName() + " à " + currentPlace.nom;
        info.setText(s);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
    }


     public void getQuestion(){

         // Read from the database
         Listener = new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 // This method is called once with the initial value and again
                 // whenever data at this location is updated.
                 currentQ = new Question();
                 int value = safeLongToInt(dataSnapshot.child("infos").child("nbQuestions").getValue(Long.class));
                 PlaceGameQuestionActivity.this.currentQ.setId( 1 + (int)(Math.random() * ((value - 1) + 1)));
                 PlaceGameQuestionActivity.this.currentQ.setQUESTION(dataSnapshot.child("questions").child(Integer.toString(currentQ.getId())).child("question").getValue(String.class));
                 currentQ.setOPTA(dataSnapshot.child("questions").child(Integer.toString(currentQ.getId())).child("opta").getValue(String.class));
                 currentQ.setOPTB(dataSnapshot.child("questions").child(Integer.toString(currentQ.getId())).child("optb").getValue(String.class));
                 currentQ.setOPTC(dataSnapshot.child("questions").child(Integer.toString(currentQ.getId())).child("optc").getValue(String.class));
                 currentQ.setANSWER(dataSnapshot.child("questions").child(Integer.toString(currentQ.getId())).child("answer").getValue(String.class));
                 setQuestionView();
                 EditText widget = (EditText) findViewById(R.id.editText);
                 //widget.setText(value.toString());

                 //Log.d(TAG, "Value is: " + value);
             }

             @Override
             public void onCancelled(DatabaseError error) {
                 // Failed to read value
                 //Log.w(TAG, "Failed to read value.", error.toException());
             }
         };
         myRef.addValueEventListener(Listener);
     }


}




