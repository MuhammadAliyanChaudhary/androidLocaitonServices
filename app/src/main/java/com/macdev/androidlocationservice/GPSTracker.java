package com.macdev.androidlocationservice;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;

    boolean isGpsEnabled =false;
    boolean isNetworkEnabled =false;
    boolean canGetLocation = false;

    Location location;

    protected LocationManager locationManager;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 30;  //30 meters
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;  // 1 min

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        try{
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGpsEnabled && !isNetworkEnabled){

            }else{
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager
                            .NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                    Log.v("Network", "Network Enabling");



                    if(locationManager!= null){

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }

                    if(location!= null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();


                    }


                }

                if(isGpsEnabled){
                    if(location== null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                        if(locationManager!=null){

                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        }

                        if(location!=null){

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();


                        }
                    }
                }

            }






        }catch(Exception e){

            e.printStackTrace();

        }



        return location;

    }


    public double getLatitude(){
        if(location!=null){
            latitude = location.getLatitude();
        }
        return latitude;

    }

    public double getLongitude(){
        if(location!=null){
            longitude = location.getLongitude();

        }
        return longitude;
    }


    public boolean canGetLocation(){
        return this.canGetLocation;
    }



    public void showSettingAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS Settings for my app");
        alertDialog.setMessage("Gps is found not enabled on your device. Do you want to turn it on and go to the settings?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);


            }
        });


        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });

        alertDialog.show();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged( Location location) {

    }

    @Override
    public void onLocationChanged( List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled( String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled( String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
