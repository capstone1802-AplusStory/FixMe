package com.aplusstory.fixme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aplusstory.fixme.MainActivity;

import java.security.Permission;

public class SplashActivity extends Activity {
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        if(!CurrentLocationManager.isRunning()){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(PERMISSIONS, PermissionInfo.PROTECTION_DANGEROUS);
            } else {
                Intent it = new Intent(this, CurrentLocationManager.class);
                this.startForegroundService(it);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(!CurrentLocationManager.isRunning()) {
            Intent it = new Intent(this, CurrentLocationManager.class);
            this.startForegroundService(it);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
