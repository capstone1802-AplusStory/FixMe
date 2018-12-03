package com.aplusstory.fixme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class ScheduleAlarmIntervalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_NAME_ARGUMENT = "alarm_interval";

    Spinner spinner;
    Toolbar toolbar;

    private String[] items = {"선택하세요", "1분 전", "5분 전", "10분 전", "30분 전", "1시간 전", "6시간 전"};
    private int almInt = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_alarm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        spinner = (Spinner) findViewById(R.id.alarmSpinner);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(this.getClass().getName(), "selected : " + this.items[position]);
        this.almInt = position - 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        if(this.almInt >= 0) {
            it.putExtra(EXTRA_NAME_ARGUMENT, this.almInt);
            this.setResult(RESULT_OK, it);
            this.finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_attribute_menu, menu);
        return true;
    }
}
