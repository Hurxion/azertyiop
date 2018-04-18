package com.example.john.myapplication;

/**
 * Activité principale affichant les réseaux détectables sur une carte
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.apache.http.HttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.os.SystemClock.sleep;
import static com.example.john.myapplication.Place.safeLongToInt;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static double rangeRandom = 0.02; // Rayon maximum pour placer les marqueurs sur la map
    private static int mInterval = 500; // Nombre de ms entre deux tâches
    private GoogleMap mMap;
    private Circle mCircle;
    private LocationManager mLocationManager;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList; // Contient le résultat du scan Wifi
    private Map<String, ScanResult> wifiMap; // Contient le résultat du scan Wifi sans doublon
    private List<Marker> markerList; // Liste des Markers placés sur la carte
    private Handler mHandler;
    private Location Mylocation; // Localisation de l'utilisateur
    private LatLng MyLatLng;
    private AlertDialog alert;
    private AlertDialog.Builder b;
    private final Context ctx = this;
    private static Intent batteryStatus;
    private static int batteryPctInitial;
    private static int batteryPctCurrent;
    private static int scale;
    private static IntentFilter ifilter;
    private boolean gps_enabled;
    private boolean network_enabled;
    private static Place currentPlace;
    private static Player currentPlayer;
    private static int rayonPlace = 50;
    private static int vitesseMarche = 5; //Vitesse en km/h
    private static CountDownTimer countDownTimer;// distance en m minimum du lieu pour commencer le jeu
    private boolean LocalisationDisponible;// Permet de savoir si la localisation est disponible
    private static Place[] placeTab;
    private static Toolbar mToolbar;
    private static TextView countDownText;
    private static boolean countFinished;
    private static TextView InfoText;
    private static boolean cheatMode;
    private static LatLng cheatposition;
    private static Marker cheatMarker;


    // Getter and Setter pour mInterval
    public static void setmInterval(int p) {
        mInterval = p;
    }

    public static int getmInterval() {
        return mInterval;
    }

    public static int getbatteryPctInitial() {
        return batteryPctInitial;
    }

    public static int getBatteryPctCurrent() {
        //mise à jour du niveau de batterie actuel
        batteryPctCurrent = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return batteryPctCurrent;
    }

    public static int getscale() {
        return scale;
    }


    //Tâche à effectuer tous les mInterval
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                //mis à jour niveau de batterie

                // Récupération de la localisation de l'utilisateur
                Mylocation = getLastKnownLocation();
                if (Mylocation == null) {
                    return;
                }

                MyLatLng = new LatLng(Mylocation.getLatitude(), Mylocation.getLongitude());
                if (countFinished) {
                    Intent i = new Intent(GoogleMapsActivity.this, CountDownFinishedActivity.class);
                    currentPlayer.addToScore(-currentPlace.nbPoint);



                    countFinished = false;
                    i.putExtra("currentPlace", currentPlace);
                    i.putExtra("currentPlayer", currentPlayer);
                    startActivity(i);

                }
                if (directDistance(MyLatLng, currentPlace.latLng) < rayonPlace) {

                    currentPlayer.addToScore(currentPlace.nbPoint);



                    countFinished = false;
                    countDownTimer.cancel();

                    Intent i= new Intent(GoogleMapsActivity.this, gameElection.class);

                    i.putExtra("currentPlace", currentPlace);
                    i.putExtra("currentPlayer", currentPlayer);
                    startActivity(i);

                    /*
                    Intent i = new Intent(GoogleMapsActivity.this, PlaceGameQuestionActivity.class);
                    currentPlayer.addToScore(currentPlace.nbPoint);
                    countFinished = false;
                    countDownTimer.cancel();
                    i.putExtra("currentPlace", currentPlace);
                    i.putExtra("currentPlayer", currentPlayer);
                    startActivity(i);*/
                }

                if(opponentDetected()){
                    startDuel();
                }


            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        countDownText = findViewById(R.id.countDownTimer);
        InfoText = findViewById(R.id.Info);


        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        cheatMode = true;
        if (cheatMode) {
            cheatposition = new LatLng(45.5149172, -73.64364974);
        }

        //Initialisation pour la carte GoogleMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = ctx.registerReceiver(null, ifilter);
        scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        batteryPctInitial = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);


        Bundle bundle = getIntent().getExtras();
        currentPlayer = (Player) bundle.getSerializable("Player");

        countDownText.setTextColor(Color.WHITE);
        InfoText.setTextColor(Color.WHITE);


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHandler != null) {
            stopRepeatingTask();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHandler != null) {
            mHandler.removeCallbacks(mStatusChecker);
            if(countDownTimer != null){
                countDownTimer.cancel();
            }
            changePlace();

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        /// Avoir sa Localisation
        this.LocalisationDisponible = false;
        updateLocalisationDisponible();

        //Boîte d'alerte afin d'avertir l'utilisateur que la localisation est indisponible
        this.b = new AlertDialog.Builder(this);
        this.b.setTitle("Localisation Indisponible");
        this.b.setMessage("Veuillez autoriser et activer la localisation puis appuyer sur ok");
        this.b.setNegativeButton("OK", null);
        this.alert = b.create();

        // La boîte ne disparaîtra que si la localisation est disponible
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateLocalisationDisponible();
                        if (LocalisationDisponible) {
                            alert.dismiss();
                            while (Mylocation == null) {
                                sleep(1000);
                                initLocation();
                            }
                            stopRepeatingTask();
                            changePlace();
                        } else {
                        }
                    }
                });
            }
        });

        mHandler = new Handler();

        if (!this.LocalisationDisponible) {
            alert.show();
        } else {
            initLocation();
            changePlace();
        }


    }



    /**
     * Renvoie la dernière localisation connue de l'utilisateur
     */
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        /// Avoir sa Localisation
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (cheatMode && bestLocation != null) {
            bestLocation.setLatitude(cheatposition.latitude);
            bestLocation.setLongitude(cheatposition.longitude);
        }
        return bestLocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * Gestion du menu de la Toolbar
     *
     * @param item : item sur lequel l'utilisateur a cliqué
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu:
                currentPlayer.addToScore(-currentPlace.nbPoint);
                Intent i = new Intent(GoogleMapsActivity.this, MainMenuActivity.class);
                i.putExtra("logged", true);
                i.putExtra("Player", currentPlayer);
                i.setType("text/plain");
                startActivity(i);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Met à jour le Booléen LocalisationDisponible selon les permissions et l'activation de la localisation
     */
    public void updateLocalisationDisponible() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        this.LocalisationDisponible = (Build.VERSION.SDK_INT >= 18 &&
                gps_enabled &&
                network_enabled &&
                ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        return;

    }

    /**
     * Initialisation de la carte
     */
    public void initLocation() {
        Mylocation = getLastKnownLocation();
        if (Mylocation == null) {
            return;
        }
        MyLatLng = new LatLng(Mylocation.getLatitude(), Mylocation.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyLatLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Mylocation.getLatitude(), Mylocation.getLongitude()), 15.0f));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {

        }

        if (cheatMode) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    if (cheatMarker != null) {
                        cheatMarker.remove();
                    }
                    cheatMarker = mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    cheatposition = point;
                }
            });
        }
        //mMap.setOnInfoWindowClickListener(this);
        //CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
        //mMap.setInfoWindowAdapter(customInfoWindow);


    }


    /**
     * Gestion de la tâche à effectuer
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void readData(final Place.MyCallback myCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Place p = new Place(0, "", new LatLng(0,0),0);
                int value = safeLongToInt(dataSnapshot.child("infos").child("nbPlaces").getValue(Long.class));
                p.id = 1 + (int)(Math.random() * ((value - 1) + 1));
                p.nom = dataSnapshot.child("places").child(Integer.toString(p.id)).child("nom").getValue(String.class);
                p.Lat = (dataSnapshot.child("places").child(Integer.toString(p.id)).child("Lat").getValue(Double.class));
                p.Long = (double) dataSnapshot.child("places").child(Integer.toString(p.id)).child("Long").getValue(Double.class);
                p.nbPoint = safeLongToInt(dataSnapshot.child("places").child(Integer.toString(p.id)).child("nbPoint").getValue(Long.class));
                p.latLng = new LatLng(p.Lat,p.Long);
                myCallback.onCallback(p);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void changePlace() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        DatabaseReference anotherOne = myRef.child("infos").child("nbPlaces");
        readData(new Place.MyCallback() {
                     @Override
                     public void onCallback(Place p) {
                         if(currentPlace != null){
                             if (currentPlace.marker != null) {
                                 currentPlace.marker.remove();
                             }
                         }
                         if(mCircle!=null){
                             mCircle.remove();
                         }

                         currentPlace = p;
                         int strokeColor = 0xffff0000;
                         //opaque red fill
                         int shadeColor = 0x44ff0000;
                         CircleOptions circleOptions = new CircleOptions().center(currentPlace.latLng).radius(rayonPlace).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
                         mCircle = mMap.addCircle(circleOptions);
                         currentPlace.marker = mMap.addMarker(new MarkerOptions()
                                 .position(new LatLng(currentPlace.latLng.latitude, currentPlace.latLng.longitude))
                                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                         startTimer();
                         InfoText.setText(currentPlayer.getName() + " : Objectif " + currentPlace.nom);
                         startRepeatingTask();

                     }


                 }
        );
    }

    //start timer function
    void startTimer() {
        if (!LocalisationDisponible) {
            return;
        }
        countFinished = false;
        countDownTimer = new CountDownTimer(TimeOnRoad(MyLatLng.latitude, MyLatLng.longitude, currentPlace.latLng.latitude, currentPlace.latLng.longitude)*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int heures = (int) millisUntilFinished / (3600 * 1000);
                int minutes = (int) millisUntilFinished / (60 * 1000) % 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String time = String.format("%d:%02d:%02d",heures, minutes, seconds);
                countDownText.setText(time);
            }

            public void onFinish() {
                countFinished = true;
                countDownTimer.cancel();
            }
        };
        countDownTimer.start();
    }


    public static double directDistance(LatLng l1, LatLng l2) {
        double lat1 = l1.latitude;
        double lat2 = l2.latitude;
        double lon1 = l1.longitude;
        double lon2 = l2.longitude;
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    private int TimeOnRoad(double oriLatitude, double oriLongitude,
                           double destLatitude, double destLongitude) {

        return (int)(3600*directDistance(new LatLng(oriLatitude,oriLongitude),new LatLng(destLatitude,destLongitude))/5000);
    }

    public boolean opponentDetected(){
        return false;
    }

    public void startDuel(){

    }
}