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
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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


import org.apache.http.HttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.os.SystemClock.sleep;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static double rangeRandom = 0.02; // Rayon maximum pour placer les marqueurs sur la map
    private static int mInterval = 500; // Nombre de ms entre deux tâches
    private GoogleMap mMap;
    private Circle mCircle;
    private LocationManager mLocationManager;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList; // Contient le résultat du scan Wifi
    private Map<String,ScanResult> wifiMap; // Contient le résultat du scan Wifi sans doublon
    private List<Marker> markerList; // Liste des Markers placés sur la carte
    private Handler mHandler;
    private Location Mylocation; // Localisation de l'utilisateur
    private LatLng MyLatLng;
    private AlertDialog alert;
    private AlertDialog.Builder b;
    private final Context ctx = this;
    private  static IntentFilter ifilter;
    private static Intent batteryStatus;
    private  static int batteryPctInitial;
    private static int batteryPctCurrent;
    private static int scale;
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


    // Getter and Setter pour mInterval
    public static void setmInterval(int p){
        mInterval = p;
    }
    public static int getmInterval(){return mInterval;}

    public static int getbatteryPctInitial(){return batteryPctInitial;}
    public static int getBatteryPctCurrent(){
        //mise à jour du niveau de batterie actuel
        batteryPctCurrent = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return batteryPctCurrent;}
    public static int getscale(){return scale;}



    //Tâche à effectuer tous les mInterval
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                //mis à jour niveau de batterie
                batteryStatus = ctx.registerReceiver(null, ifilter);

                // Récupération de la localisation de l'utilisateur
                Mylocation = getLastKnownLocation();
                if (Mylocation == null) {
                    return;
                }

                MyLatLng = new LatLng(Mylocation.getLatitude(), Mylocation.getLongitude());
                if(directDistance(MyLatLng,currentPlace.latLng)< rayonPlace) {
                    Toast.makeText(ctx, "Perdu !",
                            Toast.LENGTH_LONG).show();
                    sleep(2000);
                    Intent i = new Intent(GoogleMapsActivity.this, PlaceGameQuestionActivity.class);
                    currentPlayer.addToScore(currentPlace.nbPoint);
                    i.putExtra("currentPlace", currentPlace);
                    i.putExtra("player", currentPlayer);
                    startActivity(i);
                    changePlace();
                }

         /*       //On retire tous les marqueurs de la carte
                for (Marker m : markerList){
                    m.remove();
                }
                markerList.clear();

                //On remplit wifiList en scannant les réseaux Wifi
                detectWifi();
                //On Update wifiMap afin de n'avoir qu'un seul marqueur par réseau
                updateWifiMap();
                if (wifiList != null) {
                    //On parcourt WifiMap pour créer les marqueurs
                    for (ScanResult s : wifiMap.values()) {
                        Random r = new Random();
                        double randomValue = -rangeRandom + (rangeRandom - (-rangeRandom)) * r.nextDouble();
                        Random r2 = new Random();
                        double randomValue2 = -rangeRandom + (rangeRandom - (-rangeRandom)) * r2.nextDouble();
                        if(!s.SSID.equals("")) {
                            Marker m;
                            WifiRouter wifi = new WifiRouter(s.SSID, s.BSSID, s.capabilities, calculateSignalLevel(s.level, 5));
                            //Test de la sécurité pour savoir quel couleur associer au marqueur
                            if(s.capabilities.toLowerCase().contains("WPA".toLowerCase()) || s.capabilities.toLowerCase().contains("WEP".toLowerCase())){
                                 m = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Mylocation.getLatitude() + randomValue, Mylocation.getLongitude() + randomValue2))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            }
                            else{
                                 m = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Mylocation.getLatitude() + randomValue, Mylocation.getLongitude() + randomValue2))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            }
                            // On associe l'objet WifiRouter au marker et on l'ajoute sur la carte
                            m.setTag(wifi);
                            markerList.add(m);

                        }

                        }

                    }*/


            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        countDownText =  findViewById(R.id.countDownTimer);

        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = ctx.registerReceiver(null, ifilter);

        mToolbar = (Toolbar) findViewById(R.id.toolbarMap);
        setSupportActionBar(mToolbar);


        currentPlayer = new Player("Jo",100);
        Place p1 = new Place("Station Université Montreal",new LatLng(45.50273312,-73.61833595),50);
        Place p2 = new Place("Station CDN",new LatLng(45.49629896,-73.62246992),100);
        placeTab = new Place[]{p1,p2};
        currentPlace = placeTab[1];

        //Initialisation pour la carte GoogleMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = ctx.registerReceiver(null, ifilter);
        scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        batteryPctInitial = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

    /*   markerList = new ArrayList<>();
        wifiMap = new HashMap<String,ScanResult>();

       ;*/
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
        this.b.setNegativeButton("OK",null);
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
                            while(Mylocation == null) {
                                sleep(1000);
                                initLocation();
                            }
                            stopRepeatingTask();
                            changePlace();
                            startRepeatingTask();
                        }
                        else{
                        }
                    }
                });
            }
        });
        if (!this.LocalisationDisponible) {
            alert.show();
        }

        initLocation();
        mHandler = new Handler();
        // Démarrage de la tâche

        startRepeatingTask();
        changePlace();

    }



    /**Méthode appelée lorsqu'un utilisateur clique sur une fenêtre d'un marqueur
     On démarre l'activité DisplayNetworkActivity
     */

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent sendIntent = new Intent(GoogleMapsActivity.this, DisplayNetworkActivity.class);
        WifiRouter Routeur = (WifiRouter) marker.getTag();

        String ssid = Routeur.getSSID();
        String bssid = Routeur.getBSSID();
        String security = Routeur.getSecurite();
        int rssi = Routeur.getRSSI();
        sendIntent.putExtra("SSID", ssid);
        sendIntent.putExtra("BSSID", bssid);
        sendIntent.putExtra("security", security);
        sendIntent.putExtra("rssi", rssi);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

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
     * @param item : item sur lequel l'utilisateur a cliqué
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favori:
                Intent sendIntent = new Intent(GoogleMapsActivity.this, FavoritesActivity.class);
                startActivity(sendIntent);
                return true;

            case R.id.settings:
                Intent i = new Intent(GoogleMapsActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Met à jour le Booléen LocalisationDisponible selon les permissions et l'activation de la localisation
     */
    public void updateLocalisationDisponible(){
    LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    try {
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } catch(Exception ex) {}

    try {
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    } catch(Exception ex) {}

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
    public void initLocation(){
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
    }

        void startRepeatingTask() {
            mStatusChecker.run();
        }

        void stopRepeatingTask() {
            mHandler.removeCallbacks(mStatusChecker);
        }

    public void changePlace(){
        if(currentPlace.marker != null) {
            currentPlace.marker.remove();
        }
        currentPlace = placeTab[1];
        int strokeColor = 0xffff0000;
        //opaque red fill
        int shadeColor = 0x44ff0000;
        CircleOptions circleOptions = new CircleOptions().center(currentPlace.latLng).radius(rayonPlace).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
        mCircle = mMap.addCircle(circleOptions);
        currentPlace.marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentPlace.latLng.latitude, currentPlace.latLng.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        startTimer();

    }

    //start timer function
    void startTimer() {
        if(!LocalisationDisponible){
            return;
        }
        countDownTimer = new CountDownTimer(TimeOnRoad(MyLatLng.latitude,MyLatLng.longitude,currentPlace.latLng.latitude,currentPlace.latLng.latitude), 1000) {
            public void onTick(long millisUntilFinished) {
                int minutes = (int) millisUntilFinished / (60 * 1000);
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String time = String.format("%d:%02d", minutes, seconds);
                getSupportActionBar().setTitle(time);
            }
            public void onFinish() {
                Toast.makeText(ctx, "Perdu !",
                        Toast.LENGTH_LONG).show();
                changePlace();
                startTimer();
            }
        };
       countDownTimer.start();
    }


    public static double directDistance(LatLng l1,LatLng l2) {
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
        /* result_in_kms = "";
        String urls = "http://maps.google.com/maps/api/directions/xml?origin="
                + oriLatitude + "," + oriLongitude + "&destination=" + destLatitude
                + "," + destLongitude + "&sensor=false&units=metric";
        String tag[] = { "text" };
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            System.out.println("Response Code: " + conn.getResponseCode());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String res = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            System.out.println(res);
                 JSONObject jsonObject = new JSONObject();
            try {

                jsonObject = new JSONObject(stringBuilder.toString());

                JSONArray array = jsonObject.getJSONArray("routes");

                JSONObject routes = array.getJSONObject(0);

                JSONArray legs = routes.getJSONArray("legs");

                JSONObject steps = legs.getJSONObject(0);

                JSONObject distance = steps.getJSONObject("distance");

                Log.i("Distance", distance.toString());
                dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        //return Integer.parseInt(result_in_kms)/vitesseMarche;
        return 100000;
    }

    }