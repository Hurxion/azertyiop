package com.example.john.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ticTacToeEndActivity extends AppCompatActivity implements View.OnClickListener{

    private String winner;
    private Player currentPlayer;
    private Place currentPlace;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_end);
        Button photo= (Button) findViewById(R.id.photo);


        Bundle bundle = getIntent().getExtras();
        winner = bundle.getString("winner");
        currentPlayer=TwoDevice2P_names.currentPlayer;
        currentPlace=TwoDevice2P_names.currentPlace;
        TextView textResult = (TextView) findViewById(R.id.textResult);

        if(winner.equals("Player 2")){
            textResult.setText("Dommage " + currentPlayer.getName() + " vous avez perdu, vous obtenez quand même " + currentPlace.nbPoint + " points pour ta course, votre score est maintenant de " + currentPlayer.getScore());

            //            photo.setVisibility(View.GONE);

        } else if(winner.equals("Player 1")) {
            currentPlayer.addToScore(currentPlace.nbPoint);
            textResult.setText("Bravo " + currentPlayer.getName() + " vous avez gagné, vous obtenez " + currentPlace.nbPoint*2 + " points , votre score est maintenant de " + currentPlayer.getScore());

        } else if(winner.equals("noWinner")){
            textResult.setText("Dommage " + currentPlayer.getName() + " aucun joueur n'a gagné, vous obtenez quand même " + currentPlace.nbPoint + " points pour ta course, votre score est maintenant de " + currentPlayer.getScore());
        }


        photo.setOnClickListener(this);
        TwoDevice2P_names.act_2p_names.finish();

    }


    //ONclick
    public void playagain(View o) {
        Intent intent = new Intent(this, GoogleMapsActivity.class);
        intent.putExtra("Player",currentPlayer);
        intent.setType("text/plain");
        startActivity(intent);
        finish();
    }

    public void retour(View o){
        Intent i = new Intent(this, MainMenuActivity.class);
        i.putExtra("logged", true);
        i.putExtra("Player", currentPlayer);
        i.setType("text/plain");
        startActivity(i);
        finish();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            /*Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageBitmap);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "image"));*/

            Uri selectedImageUri = data.getData();
            /*InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);*/



            /*String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    imageBitmap, "picture", null);
            Uri uri = Uri.parse(path);*/
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
            share.putExtra(Intent.EXTRA_TEXT, "J'ai gagné un jeu duel avec Tic-Tac-Go!");
            startActivity(Intent.createChooser(share, "Partagez votre photo!"));

        }
    }



    /*private void shareImage(Bitmap bitmap){
        // save bitmap to cache directory
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "J'ai gagné un jeu duel avec Tic-Tac-Go!");
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Partagez votre photo!"));
        }
    }*/


    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.photo:
                    dispatchTakePictureIntent();
                break;
        }
    }




}
