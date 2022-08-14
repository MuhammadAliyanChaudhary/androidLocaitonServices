package com.macdev.androidlocationservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    Button getLocation;
    TextView latLong, place;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getLocation = findViewById(R.id.getLocationBtn);
        latLong = findViewById(R.id.txtLatLong);
        place = findViewById(R.id.txtPlace);





        try{

            if(ActivityCompat.checkSelfPermission(this, mPermission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {mPermission}, REQUEST_CODE_PERMISSION);

            }

        }catch (Exception e){
            e.printStackTrace();
        }




        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(MainActivity.this);


                if(gps.canGetLocation){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();


                    try{
                        placeName(latitude, longitude);

                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }else{
                    gps.showSettingAlert();
                }





            }
        });


    }


    public void placeName(double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);


        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);
        place.setText(cityName + " "+ stateName + " " + countryName);

        latLong.setText("Your Latitude is "+latitude + "and Your Longitude is "+ longitude);

    }
}