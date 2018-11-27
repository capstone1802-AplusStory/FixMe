package com.aplusstory.fixme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class LightSensActivity extends AppCompatActivity implements  SettingsUIManager{
    Toolbar toolbar;
    SeekBar seekBar;

    private int level;

    private UserSettingsManager dm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.dm == null){
            this.dm = new UserSettingsManager(this);
        }

        setContentView(R.layout.activity_settings_lightsens);

        toolbar = (Toolbar) findViewById(R.id.lightsensBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(LightSensActivity.this.dm.getSensitivity());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LightSensActivity.this.level = progress;
                Log.d(this.getClass().getName(), "level : " + Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(this.getClass().getName(), "touch track start, level : " + Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(this.getClass().getName(), "touch track end, level : " + Integer.toString(seekBar.getProgress()));
                LightSensActivity.this.dm.setSensitivity(level);
            }
        });
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
