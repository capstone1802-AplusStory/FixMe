package com.aplusstory.fixme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class AlarmOpacityActivity extends AppCompatActivity implements SettingsUIManager{
    Toolbar toolbar;
    SeekBar seekBar;
    private int percentage = 0;
    TextView textView;
    private UserSettingsManager dm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.dm == null){
            this.dm = new UserSettingsManager(this);
        }

        setContentView(R.layout.activity_settings_opacity);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        seekBar = (SeekBar) findViewById(R.id.seekBar3);
        seekBar.setProgress(AlarmOpacityActivity.this.dm.getTransparency());

        textView = (TextView) findViewById(R.id.textView2);
        textView.setText(seekBar.getProgress()+"%");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentage = seekBar.getProgress();
                Log.d(this.getClass().getName(), "value : " + Integer.toString(percentage));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                percentage = seekBar.getProgress();
                Log.d(this.getClass().getName(), "touch track start, value : " + Integer.toString(percentage));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                percentage = seekBar.getProgress();
                Log.d(this.getClass().getName(), "touch track end, value : " + Integer.toString(percentage));
                update();
            }
        });
    }

    public void update() {
        textView.setText(new StringBuilder().append(percentage)+"%");
        AlarmOpacityActivity.this.dm.setTransparency(this.percentage);
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
