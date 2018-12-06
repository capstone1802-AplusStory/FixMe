package com.aplusstory.fixme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ScheduleTimeActivity extends AppCompatActivity  {
    public static final String  EXTRA_NAME_ARGUMENT = "schedule_time";
    public static final String  KEY_TIME_BEGIN = ScheduleDataManager.ScheduleData.KEY_DATE_SCHEDULE_BEGIN;
    public static final String  KEY_TIME_END = ScheduleDataManager.ScheduleData.KEY_DATE_SCHEDULE_END;

    Toolbar toolbar2;
    TextView textViewST,textViewSD, textViewET, textViewED;
    Calendar cStart, cEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_time);

        toolbar2 = (Toolbar) findViewById(R.id.stToolbar);
        setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        this.cStart = (Calendar)calendar.clone();
        this.cEnd = (Calendar)calendar.clone();

        final TimePickerDialog dialog = new TimePickerDialog(this, listenerST, hour, minute,false);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, listenerSD, year, month, day);
        final TimePickerDialog dialog2 = new TimePickerDialog(this, listenerET, hour, minute,false);
        final DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, listenerED, year, month, day);

        textViewST = (TextView) findViewById(R.id.startTime);
        textViewST.setText(String.format("%02d", hour)+":"+String.format("%02d", minute));
        textViewSD = (TextView) findViewById(R.id.startDate);
        textViewSD.setText(year + "년 " + (month + 1) + "월 " + day + "일");
        textViewET = (TextView) findViewById(R.id.endTime);
        textViewET.setText(String.format("%02d", hour) + ":"+String.format("%02d", minute));
        textViewED = (TextView) findViewById(R.id.endDate);
        textViewED.setText(year + "년 " + (month + 1) + "월 " + day + "일");

        textViewST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        textViewSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        textViewET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
            }
        });

        textViewED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener listenerST = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView textView = (TextView)findViewById(R.id.startTime);
            textView.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
            ScheduleTimeActivity.this.cStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
            ScheduleTimeActivity.this.cStart.set(Calendar.MINUTE, hourOfDay);
        }
    };

    private DatePickerDialog.OnDateSetListener listenerSD = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            TextView textView = (TextView)findViewById(R.id.startDate);
            textView.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
            ScheduleTimeActivity.this.cStart.set(Calendar.MONTH, monthOfYear);
            ScheduleTimeActivity.this.cStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

    };

    private TimePickerDialog.OnTimeSetListener listenerET = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView textView = (TextView)findViewById(R.id.endTime);
            textView.setText(String.format("%02d", hourOfDay)+":" + String.format("%02d", minute));
            ScheduleTimeActivity.this.cEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
            ScheduleTimeActivity.this.cEnd.set(Calendar.MINUTE, hourOfDay);
        }
    };

    private DatePickerDialog.OnDateSetListener listenerED = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            TextView textView = (TextView)findViewById(R.id.endDate);
            textView.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
            ScheduleTimeActivity.this.cEnd.set(Calendar.MONTH, monthOfYear);
            ScheduleTimeActivity.this.cEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

    };

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        Bundle bd = new Bundle();
        bd.putLong(ScheduleTimeActivity.KEY_TIME_BEGIN, this.cStart.getTimeInMillis());
        bd.putLong(ScheduleTimeActivity.KEY_TIME_END, this.cEnd.getTimeInMillis());
        it.putExtra(ScheduleTimeActivity.EXTRA_NAME_ARGUMENT, bd);
        this.setResult(RESULT_OK, it);
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_attribute_menu, menu);
        return true;
    }
}
