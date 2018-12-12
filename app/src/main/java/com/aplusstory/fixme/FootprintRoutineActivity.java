package com.aplusstory.fixme;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.skt.Tmap.TMapData;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

public class FootprintRoutineActivity extends AppCompatActivity implements FootprintFragment.OnFragmentInteractionListener{

    Toolbar toolbar;
    TextView arrivalTextview;
    TextView departureTextview;
    TextView totalLapse;
    TextView whenToWhen;

    private LocationDataManager.PathData pathData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent locaIntent = getIntent();
        setContentView(R.layout.activity_footprint_routine);
        FootprintFragment footprintFragment = new FootprintFragment();

        arrivalTextview = (TextView) findViewById(R.id.arrivalText);
        departureTextview = (TextView) findViewById(R.id.departureText);
        totalLapse = (TextView) findViewById(R.id.totalLapse);
        whenToWhen = (TextView) findViewById(R.id.when_to_when);
        final String[] startAddress = new String[1];
        final String[] arriveAddress = new String[1];
        if(locaIntent != null){
            Bundle moveBundle = new Bundle();
            moveBundle.putSerializable("MovementData",locaIntent.getSerializableExtra("MovementData"));
            footprintFragment.setArguments(moveBundle);

            this.pathData = (LocationDataManager.PathData) locaIntent.getSerializableExtra("MovementData");

            int locNum = this.pathData.locaArr.length;
            TMapData tMapData = new TMapData();

            try {
                tMapData.convertGpsToAddress(this.pathData.locaArr[0].latitude, this.pathData.locaArr[0].longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        Log.d(FootprintRoutineActivity.class.toString(), "address : " + s);
                         if(s != null){
                             departureTextview.setText(s);
                         }else {
                             departureTextview.setText("출발");
                         }
//                        Button departButton = (Button) findViewById(R.id.departButton);
//                        departButton.setText(String.valueOf(departureTextview.getText()));
                    }
                });
                tMapData.convertGpsToAddress(this.pathData.locaArr[locNum - 1].latitude, this.pathData.locaArr[locNum - 1].longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        Log.d(FootprintRoutineActivity.class.toString(), "address : " + s);
                        if(s != null) {
                            arrivalTextview.setText(s);
                        }else {
                            arrivalTextview.setText("도착");
                        }
//                        Button arriveButton = (Button) findViewById(R.id.arrivalButton);
//                        arriveButton.setText(String.valueOf(arrivalTextview.getText()));
                    }
                });

            } catch (Exception e){
                Log.d(this.getClass().getName(), e.toString());
                e.printStackTrace();
            }
//            String startTime = String.format("%d :%d ",
//                    TimeUnit.MILLISECONDS.toHours(moveIntent.locaArr[0].datetime),
//                    TimeUnit.MILLISECONDS.toMinutes(moveIntent.locaArr[0].datetime) -
//                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(moveIntent.locaArr[0].datetime))
//            );
//            String endTime = String.format("%d :%d ",
//                    TimeUnit.MILLISECONDS.toHours(moveIntent.locaArr[locNum-1].datetime),
//                    TimeUnit.MILLISECONDS.toMinutes(moveIntent.locaArr[locNum-1].datetime) -
//                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(moveIntent.locaArr[locNum-1].datetime))
//            );
//
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String startTime = dateFormat.format(new Date(this.pathData.locaArr[0].datetime));
            String endTime = dateFormat.format(new Date(this.pathData.locaArr[locNum-1].datetime));

            whenToWhen.setText(startTime + "출발 ~ "+ endTime + "도착");

            long lapseTime = this.pathData.locaArr[locNum-1].datetime - this.pathData.locaArr[0].datetime;

            String lapseTimeString = String.format("소요시간  : %d 시간 %d 분",
                    TimeUnit.MILLISECONDS.toHours(lapseTime),
                    TimeUnit.MILLISECONDS.toMinutes(lapseTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(lapseTime))
            );
            totalLapse.setText(lapseTimeString);
            departureTextview.setText(startAddress[0]);
            arrivalTextview.setText(arriveAddress[0]);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.footPrintContainer,footprintFragment);
        fragmentTransaction.commit();


//        departureTextview.setText("중앙대학교");
//
//        arrivalTextview.setText("스타시티 건대");
//
//        TextView whenToWhen = (TextView) findViewById(R.id.when_to_when);
//        whenToWhen.setText("17:30 출발 ~ 18:30 도착");
//
//        TextView totalLapse = (TextView) findViewById(R.id.totalLapse);
//        totalLapse.setText("소요시간: 1시간 00분");

        Button departButton = (Button) findViewById(R.id.departButton);
//        departButton.setText(String.valueOf(departureTextview.getText()));
        departButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootprintRoutineActivity that = FootprintRoutineActivity.this;
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                intent.putExtra(FavoriteActivity.KEY_NICKNAME, departureTextview.getText());
                intent.putExtra(FavoriteActivity.KEY_LOCATION_DATA, that.pathData.locaArr[0]);
                startActivity(intent);
            }
        });

        Button arriveButton = (Button) findViewById(R.id.arrivalButton);
//        arriveButton.setText(String.valueOf(arrivalTextview.getText()));
        arriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootprintRoutineActivity that = FootprintRoutineActivity.this;
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                intent.putExtra(FavoriteActivity.KEY_NICKNAME, arrivalTextview.getText());
                intent.putExtra(FavoriteActivity.KEY_LOCATION_DATA, that.pathData.locaArr[that.pathData.locaArr.length - 1]);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
