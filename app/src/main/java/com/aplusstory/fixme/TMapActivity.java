package com.aplusstory.fixme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TMapActivity extends AppCompatActivity implements com.aplusstory.fixme.MapFragment.OnFragmentInteractionListener {
    public static final String EXTRA_NAME_ARGUMENT = "location_result";

    FrameLayout fragmentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(this.getApplicationContext(),"FINE_LOCATION 권한 없음",Toast.LENGTH_LONG).show();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap);



//        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.mapfragment, mapFragment);
//        fragmentTransaction.commit();


    }

    @Override
    public void onFragmentInteraction(Bundle bd) {
        if(bd != null){
            Bundle arg = new Bundle(bd);
            Intent it = new Intent();
            it.putExtra(EXTRA_NAME_ARGUMENT, bd);
            this.setResult(RESULT_OK, it);
            this.finish();
        }
    }
}