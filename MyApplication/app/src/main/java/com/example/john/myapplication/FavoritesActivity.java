package com.example.john.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

/**
 * Activité affichant la liste des favoris et permettant d'en supprimer
 */
public class FavoritesActivity extends AppCompatActivity {

    public FileManager fileManager = null;
    public String fileName;
    public String list;
    public Context ctx;
    public boolean locked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_favourite_networks);

        // Toolbar permettant de revenir à l'activité précédente
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.back18dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //Bouton permettant de Supprimer
        Button unlock = findViewById(R.id.unlockButton);

        //Bouton permettant d'afficher uniquement la liste sans action possible
        Button lock = findViewById(R.id.lockButton);


        this.fileManager = new FileManager();
        this.fileName = "favorites.txt";
        this.locked = true;
        this.ctx = this;

        //Si le fichier est vide alors aucune action ne peut être effectuée
        if(fileManager.isEmpty(fileName,this.ctx)){
            unlock.setEnabled(false);
            lock.setEnabled(false);
        }
        //Sinon la page s'affiche par défaut en mode "Liste"
        else {
            unlock.setEnabled(true);
            lock.setEnabled(false);


            try {
                //Lecture du fichier favoris
                this.list = this.fileManager.readFile(fileName, this);
                final String[] listArray = this.list.split("\n");

                //Affichage dans un Linear Layout contenu dans un ScrollView
                if (listArray != null) {

                    LinearLayout linearLayout = findViewById(R.id.linearLayout1);
                    for (int i = 0; i < listArray.length; i++) {
                        //Chaque Favori est un bouton unclikable par défaut
                        final Button button = new Button(this);
                        button.setId(i);
                        button.setEnabled(false);
                        button.setTag(listArray[i]);
                        button.setText(listArray[i].split(" ")[0]);

                        //Si le bouton est cliqué alors le favori correspondant est supprimé
                        View.OnClickListener btnClick = new View.OnClickListener() {

                            public void onClick(View v) {
                                try {

                                    String lineToRemove = (String) v.getTag();
                                    fileManager.deleteLinefromFile(fileName, lineToRemove, ctx);
                                    v.setVisibility(View.GONE);


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        };
                        button.setOnClickListener(btnClick);
                        linearLayout.addView(button);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Fonction appelée lorsque le bouton "Supprimer" est cliqué
     * @param view
     */
    public void unlockButtons(View view) {
        if (locked) {
            Button unlock = findViewById(R.id.unlockButton);
            Button lock = findViewById(R.id.lockButton);

            unlock.setEnabled(false);
            lock.setEnabled(true);
            LinearLayout mLayout = findViewById(R.id.linearLayout1);
            //On rend chaque bouton cliquable et on modifie le texte ainsi que sa couleur
            for (int i = 0; i < mLayout.getChildCount(); i++) {
                Button mButton = (Button) mLayout.getChildAt(i);
                CharSequence oldButtonText = mButton.getText();
                int blackColorValue = Color.BLACK;
                mButton.setText("Supprimer " + oldButtonText);
                mButton.setTextColor(blackColorValue);
                mButton.setEnabled(true);
            }
            locked = false;
        }
    }

    /**
     * Méthode appelée lorsque le bouton "Liste" est cliqué
     * @param view
     */
    public void lockButtons(View view) {
        if (!locked) {
            LinearLayout mLayout = findViewById(R.id.linearLayout1);
            Button unlock = findViewById(R.id.unlockButton);
            Button lock = findViewById(R.id.lockButton);

            unlock.setEnabled(true);
            lock.setEnabled(false);
            for (int i = 0; i < mLayout.getChildCount(); i++) {
                Button mButton = (Button) mLayout.getChildAt(i);
                String oldButtonText = mButton.getText().toString();
                String newButtonText = oldButtonText.split(" ")[1];
                mButton.setText(newButtonText);
                int greyColorValue = Color.GRAY;
                mButton.setTextColor(greyColorValue);
                mButton.setEnabled(false);
            }
            locked = true;
        }
    }
}
