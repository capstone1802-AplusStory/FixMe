package com.aplusstory.fixme;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.skt.Tmap.TMapData;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

public class FootprintRoutineActivity extends AppCompatActivity implements FootprintFragment.OnFragmentInteractionListener{

    Toolbar toolbar;
    TextView arrivalTextview;
    TextView departureTextview;
    TextView totalLapse;
    TextView whenToWhen;
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

        if(locaIntent != null){
            Bundle moveBundle = new Bundle();
            moveBundle.putSerializable("moveBundle",locaIntent.getSerializableExtra("MovementData"));
            footprintFragment.setArguments(moveBundle);

            LocationDataManager.PathData moveIntent = (LocationDataManager.PathData) locaIntent.getSerializableExtra("MovdementData");

            int locNum = moveIntent.locaArr.length;
            TMapData tMapData = new TMapData();

            try {
                String startingPoint = tMapData.convertGpsToAddress(moveIntent.locaArr[0].latitude,moveIntent.locaArr[0].longitude);
                String endPoint = tMapData.convertGpsToAddress(moveIntent.locaArr[locNum-1].latitude,moveIntent.locaArr[locNum-1].longitude);
                departureTextview.setText(startingPoint);
                arrivalTextview.setText(endPoint);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            String startTime = String.format("%d :%d ",
                    TimeUnit.MILLISECONDS.toMinutes(moveIntent.dtBegin),
                    TimeUnit.MILLISECONDS.toSeconds(moveIntent.dtBegin) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(moveIntent.dtBegin))
            );
            String endTime = String.format("%d :%d ",
                    TimeUnit.MILLISECONDS.toHours(moveIntent.dtEnd),
                    TimeUnit.MILLISECONDS.toMinutes(moveIntent.dtEnd) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(moveIntent.dtEnd))
            );
            whenToWhen.setText(startTime + "출발 ~ "+ endTime + "도착");


            long lapseTime = moveIntent.locaArr[locNum-1].datetime - moveIntent.locaArr[0].datetime;

            String lapseTimeString = String.format("소요시간  : %d 시간 %d 분",
                    TimeUnit.MILLISECONDS.toHours(lapseTime),
                    TimeUnit.MILLISECONDS.toMinutes(lapseTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(lapseTime))
            );
            totalLapse.setText(lapseTimeString);

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
        departButton.setText(String.valueOf(departureTextview.getText()));
        departButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                intent.putExtra("location", departureTextview.getText());
                startActivity(intent);
            }
        });

        Button arriveButton = (Button) findViewById(R.id.arrivalButton);
        arriveButton.setText(String.valueOf(arrivalTextview.getText()));
        arriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                intent.putExtra("location", arrivalTextview.getText());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
