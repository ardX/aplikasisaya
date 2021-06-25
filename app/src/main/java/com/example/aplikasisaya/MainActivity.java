package com.example.aplikasisaya;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSIONS_ALL = 1;

    private static final int UPDATE_INTERVAL = 4000;
    private static final int FASTEST_UPDATE_INTERVAL = 2000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("mobile");

        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS_ALL);
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                Log.i(TAG, latitude + " " + longitude);
            }
        };
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest = locationRequest;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startApp();
    }

    @Override
    protected void onPause() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_ALL) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                startApp();
            } else {
                Toast.makeText(MainActivity.this, "Please accept all permissions!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startApp() {
        Log.i(TAG, "Setting up location services...");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            /*if (getSharedPreferences("MOBILE", 0) == null || !getSharedPreferences("MOBILE", 0).contains("user")) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }*/

            ((TextView) findViewById(R.id.txtuser)).setText(getSharedPreferences("MOBILE", 0).getString("user", ""));
            ((TextView) findViewById(R.id.txtemail)).setText(getSharedPreferences("MOBILE", 0).getString("email", ""));
            ((TextView) findViewById(R.id.txtphone)).setText(getSharedPreferences("MOBILE", 0).getString("ponsel", ""));

            findViewById(R.id.btnGempa).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, TabActivity.class));
                }
            });

            findViewById(R.id.btnNotif).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NotifActivity.class));
                }
            });

            findViewById(R.id.btnkeluar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSharedPreferences("MOBILE", 0).edit().clear().apply();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }
            });

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, MapFragment.newInstance());
            fragmentTransaction.commit();
        }
    }

}