package com.aplusstory.fixme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//TODO : save druation in year-month-day format.

public class FootDataSettingsActivity extends AppCompatActivity implements SettingsUIManager{
    Toolbar toolbar;
    Spinner spinner;

    private UserSettingsManager dm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.dm == null){
            this.dm = new UserSettingsManager(this);
        }

        setContentView(R.layout.activity_settings_data);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        spinner = (Spinner) findViewById(R.id.maintainSpinner);

        final String[] item = {"선택하세요", "없음", "1주일", "1개월", "6개월", "1년"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(FootDataSettingsActivity.this.dm.getDuration());

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position > 0) {
                            FootDataSettingsActivity.this.dm.setDuration(position);
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
