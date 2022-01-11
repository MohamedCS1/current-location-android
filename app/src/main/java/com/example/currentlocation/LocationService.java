package com.example.currentlocation;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    SharedPreference sharedpreference;

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            double latitude = locationResult.getLastLocation().getLatitude();
            double longitude = locationResult.getLastLocation().getLongitude();
            sharedpreference.insert_current_location("Longitude -> " + longitude + " Latitude -> " + latitude);
            Log.d("LOCATION UPDATE", "Longitude -> " + longitude + " Latitude -> " + latitude);
            Toast.makeText(getApplicationContext() ,"Longitude -> " + longitude + " Latitude -> " + latitude ,Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{resultIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("Location Service")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setContentText("Running")
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("this channel use by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback ,Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID ,builder.build());
    }

    void stopLocationService()
    {
        stopSelf();
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
          throw new UnsupportedOperationException("Not yet Implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedpreference = new SharedPreference(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
        {
            String action = intent.getAction();
            Toast.makeText(getApplicationContext() ,action ,Toast.LENGTH_SHORT).show();
            if (action != null)
            {
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE))
                {
                    startLocationService();
                }
                else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE))
                {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
