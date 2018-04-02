package com.example.john.myapplication;

/**
 * Classe réprésentant un Routeur Wifi avec les informations requises
 */

public class WifiRouter {
    private String SSID;
    private String BSSID;
    private String Securite;
    private int RSSI;

    public WifiRouter(String ssidArg, String bssidArg, String securiteArg, int rssiArg){
        this.SSID = ssidArg;
        this.BSSID = bssidArg;
        this.Securite = securiteArg;
        this.RSSI = rssiArg;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String s) {
        this.SSID = s;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String s) {
        this.BSSID = s;
    }

    public String getSecurite() {
        return this.Securite;
    }

    public void setSecurite(String s) {
        this.Securite = s;
    }

    public int getRSSI() {
        return this.RSSI;
    }

    public void setRSSI(int p) {
        this.RSSI = p;
    }
}