package com.aplusstory.fixme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class AlarmPeriodActivity extends AppCompatActivity implements SettingsUIManager {
    Spinner spinner;
    Toolbar toolbar;

    private UserSettingsManager dm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.dm == null){
            this.dm = new UserSettingsManager(this);
        }

        setContentView(R.layout.activity_settings_alarmperiod);

        toolbar = (Toolbar) findViewById(R.id.periodBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        spinner = (Spinner) findViewById(R.id.periodSpinner);


        final String[] item = {"선택하세요", "5분마다", "10분마다", "30분마다", "1시간마다", "3시간마다"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(AlarmPeriodActivity.this.dm.getPeriod());

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position > 0) {
                            AlarmPeriodActivity.this.dm.setPeriod(position);
                        }
                        Log.d(this.getClass().getName(), parent.getItemAtPosition(position).toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d(this.getClass().getName(), "nothing selected");
                    }
                }
        );
    }

    @Override
    public void setDataManager(UserDataManager m) {
        if(m instanceof SettingsDataManager){
            this.setDataManager((SettingsDataManager)m);
        } else{
            Log.d(this.getClass().getName(), "Error : Wrong DataManager type");
            //TODO : error handle
        }
    }

    @Override
    public void setDataManager(SettingsDataManager m) {
        this.dm = (UserSettingsManager)m;
    }
}
