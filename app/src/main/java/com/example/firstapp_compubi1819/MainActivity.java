package com.example.firstapp_compubi1819;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.content.pm.PackageManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //2nd class - Collecting location
    //It is necessary to configure build.gradle
    //https://medium.com/@droidbyme/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e
    static  int transitions =0;
    private FusedLocationProviderClient fusedLocationClient;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private int locationRequestCode = 1000;
    @Override
    protected void onStop() {
        super.onStop();
        transitions++;
        //Callback methodos in an android app -> https://developer.android.com/reference/android/app/Activity#ActivityLifecycle
        Toast.makeText(this, "Inside OnStop callback method "+transitions, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            TextView txtLocation = (TextView) findViewById(R.id.text1);
                            txtLocation.setText(String.format(Locale.US, "Last Known %s -- %s", wayLatitude, wayLongitude));
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transitions++;
        Toast.makeText(getApplicationContext(), "Inside OnCreate callback method "+
                transitions, Toast.LENGTH_LONG).show();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        transitions++;
        Toast.makeText(getApplicationContext(), "Inside OnStart callback method "+transitions, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Inside OnDestroy callback method "+transitions++, Toast.LENGTH_LONG);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Inside OnPause callback method "+transitions++, Toast.LENGTH_LONG);
    }

    @Override
    protected void onResume() {

        super.onResume();
        Toast.makeText(this, "Inside OnResume callback method "+transitions++, Toast.LENGTH_LONG).show();

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);


        } else {
            // already permission granted
            //Show the last known location
            //To use this feature, GPS must be turned on your device
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                // Logic to handle location object
                                TextView txtLocation = (TextView) findViewById(R.id.text1);
                                txtLocation.setText(String.format(Locale.US, "Last Known %s -- %s", wayLatitude, wayLongitude));
                            }
                        }
                    });
        }

    }

    public void doSomething(View view){
        // Create the text message with a string
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Something to show");
        sendIntent.setType("text/plain");
        Toast.makeText(this, "Invoking another activity", Toast.LENGTH_LONG).show();
        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }


    }
}
