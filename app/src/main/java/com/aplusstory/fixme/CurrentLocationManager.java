package com.aplusstory.fixme;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentLocationManager extends Service implements LocationDataManager, LocationListener {
    public static final long MIN_LOCA_UPDATE = 2 * 60 * 1000;
//    public static final long MIN_LOCA_UPDATE = 10000;
    public static final long DELAY_THREAD_LOOP = 10000;
    public static final double DISTANCE_THRESHOLD = 5.0;

    private static boolean isRunning = false;

    private LocationFileManager fm = null;
    private Recognizer moRecog = null;
    private LocationManager lm = null;
    private Thread thd = null;
    private boolean isEnabled = false;
    private Handler hd = null;
    private Location priviousLocation = null;
    private long tLocaReq = -1;
    private long dLocaReq = MIN_LOCA_UPDATE;

    public static boolean isRunning(){
        return CurrentLocationManager.isRunning;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CurrentLocationManager.isRunning = true;
        this.isEnabled = true;

        if(this.fm == null) {
            this.fm = new LocationFileManager(this);
        }
        if(this.lm == null) {
            this.lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }
        if(this.moRecog == null){
            this.moRecog = new UserMovementRecognizer(this);
        }
        if(this.hd == null){
            this.hd = new ServiceHandler();
        }
        if(this.thd == null || !this.thd.isAlive()){
            this.thd = new ServiceThread(this.hd);
            this.thd.start();
        }

        Log.d(CurrentLocationManager.class.getName(), "Service started");
////        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT);
//        NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel nc = nm.getNotificationChannel(NotificationUIService.NOTIFICATION_CHANNEL_ID);
//        Intent it = new Intent(this, TestLocationActivity.class);
//        PendingIntent ntPIntent = PendingIntent.getActivity(this,0, it, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification n = (new Notification.Builder(this, NotificationUIService.NOTIFICATION_CHANNEL_ID))
//                //                .setSmallIcon(R.drawable.FixMeIcon)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("FixMe")
//                .setContentText("GPS turned on")
//                .setVisibility(Notification.VISIBILITY_PRIVATE)
//                .setOngoing(true)
//                .setWhen(System.currentTimeMillis())
//                .setShowWhen(true)
//                .setOnlyAlertOnce(true)
//                .setContentIntent(ntPIntent)
//                .build();
        this.startForeground((int)System.currentTimeMillis(), new Notification());
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        DateFormat df = new SimpleDateFormat(LocationData.DATE_FORMAT_GMT, Locale.US);
        String dStr = df.format(date);
        double latitude =  location.getLatitude();
        double longtitude = location.getLongitude();

        Log.d(CurrentLocationManager.class.getName(),
             "You're now on : "
                + Double.toString(latitude)
                + ", " + Double.toString(longtitude)
                + ", at : "
                + dStr
        );

        if(this.fm != null){
            if( this.priviousLocation == null
            ||( (location.hasAccuracy() && priviousLocation.hasAccuracy() &&
                location.getAccuracy() >= priviousLocation.getAccuracy()) &&
                location.distanceTo(this.priviousLocation) > CurrentLocationManager.DISTANCE_THRESHOLD)){
                LocationData loca = new LocationData(now, latitude, longtitude);
                this.fm.setCurrentLocation(loca);
            }
        }
        this.lm.removeUpdates(this);
        this.priviousLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(CurrentLocationManager.class.getName(), "Status changed, provider : " + provider + "status : " + Integer.toString(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(CurrentLocationManager.class.getName(), provider + " enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(CurrentLocationManager.class.getName(), provider + " disabled");
    }

    public class ServiceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            CurrentLocationManager that = CurrentLocationManager.this;
            if(that.lm != null){
                LocationProvider gps = that.lm.getProvider(LocationManager.GPS_PROVIDER);
                LocationProvider net = that.lm.getProvider(LocationManager.NETWORK_PROVIDER);
                LocationProvider passive = that.lm.getProvider(LocationManager.PASSIVE_PROVIDER);
                boolean coasePermission = that.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
                boolean finePermission = that.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
                if(passive != null && (finePermission)){
                    Log.d(this.getClass().getName(), "passive provider request");
                    that.lm.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER
                            , CurrentLocationManager.MIN_LOCA_UPDATE
                            , 5
                            , that
                    );
                }
                if (gps != null && finePermission) {
                    Log.d(this.getClass().getName(), "gps provider request");
                    that.lm.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER
                            , CurrentLocationManager.MIN_LOCA_UPDATE
                            , 5
                            , that
                    );
                }
                if (net != null && coasePermission) {
                    Log.d(this.getClass().getName(), "network provider request");
                    that.lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER
                            , CurrentLocationManager.MIN_LOCA_UPDATE
                            , 5
                            , that
                    );
                }
            }
        }
    }

    public class ServiceThread extends Thread{
        Handler hd = null;

        public ServiceThread(Handler hd){
            this.hd = hd;
        }

        @Override
        public void run() {
            CurrentLocationManager that = CurrentLocationManager.this;
            if(this.hd != null){
                this.hd.sendEmptyMessage(0);
            }
            while(that.isEnabled){
                long now = System.currentTimeMillis();
                if(that.tLocaReq > 0) {
                    if (that.moRecog == null || !that.moRecog.isEnabled()) {
                        if (this.hd != null) {
                            Log.d(that.getClass().getName(), "No Recognizer");
                            this.hd.sendEmptyMessage(0);
                            that.tLocaReq = -1;
                        }
                    } else if (that.moRecog.checkCondition()) {
                        if (this.hd != null) {
                            Log.d(that.getClass().getName(), "Condition fullfilled");
                            this.hd.sendEmptyMessage(0);
                            that.tLocaReq = -1;
                        }
                    } else if(now - tLocaReq > that.dLocaReq){
                        if(this.hd != null){
                            Log.d(that.getClass().getName(), "Minimum time expired");
                            this.hd.sendEmptyMessage(0);
                            that.tLocaReq = -1;
                        }
                    }
                } else {
                    that.tLocaReq = now;
                }
                try{
                    Thread.sleep(CurrentLocationManager.DELAY_THREAD_LOOP);
                } catch (InterruptedException e){
                    return;
                }
            }
        }
    }

    @Override
    public void setFileManager(FileManager fm) {
        if(fm instanceof LocationFileManager){
            this.fm = (LocationFileManager) fm;
        }
    }

    @Override
    public void onDestroy() {
        if(this.fm != null){
            this.fm.destroy();
        }

        if(this.lm != null){
            this.lm.removeUpdates(this);
        }

        if(this.moRecog != null){
            this.moRecog.destroy();
        }

        if(this.thd != null && this.thd.isAlive()){
            this.thd.interrupt();
        }

        CurrentLocationManager.isRunning = false;

        super.onDestroy();
    }
}
