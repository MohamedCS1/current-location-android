package com.example.currentlocation;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    SharedPreference sharedPreference;
    Button buStart;
    Button buStop;
    TextView tvCurrentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreference = new SharedPreference(this);

        buStart = findViewById(R.id.bu_start);
        buStop = findViewById(R.id.bu_stop);

        tvCurrentLocation = findViewById(R.id.tv_location);

        buStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
            }
        });


        buStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationService();
                sharedPreference.clear();
            }
        });

        tvCurrentLocation.setText(sharedPreference.get_current_location());
    }

    protected boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {

                if (LocationService.class.getName().equals(service.service.getClassName()))
                {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService()
    {
        if (!isLocationServiceRunning())
        {
            Toast.makeText(this,"Start location service" ,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this ,LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
        }
    }

    private void stopLocationService()
    {
        Intent intent = new Intent(getApplicationContext() ,LocationService.class);
        intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
        getApplicationContext().startService(intent);
    }


}