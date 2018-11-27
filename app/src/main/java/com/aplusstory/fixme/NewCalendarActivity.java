package com.aplusstory.fixme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class NewCalendarActivity extends FragmentActivity {

    private static final String TAG = MConfig.TAG;
    private static final String NAME = "NewCalendarActivity";
    private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());

    private TextView thisMonthTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_calendar);
//
//        Button addButton = (Button)findViewById(R.id.main_add_bt);
////        Button monthButton = findViewById(R.id.main_monthly_bt);
////        Button weekButton = findViewById(R.id.main_weekly_bt);
////        Button dayButton = findViewById(R.id.main_daily_bt);
//        thisMonthTv = findViewById(R.id.this_month_tv);
//
//        MonthlyFragment mf = (MonthlyFragment) getSupportFragmentManager().findFragmentById(R.id.monthly);
//
//        mf.setOnMonthChangeListener(new MonthlyFragment.OnMonthChangeListener() {
//
//            @Override
//            public void onChange(int year, int month) {
//                HLog.d(TAG, CLASS, "onChange " + year + "." + month);
//                thisMonthTv.setText(year + "." + (month + 1));
//            }
//
//            @Override
//            public void onDayClick(OneDayView dayView) {
//                Toast.makeText(NewCalendarActivity.this, "Click  " + dayView.get(Calendar.MONTH) + "/" + dayView.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//        });
//        addButton.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Toast.makeText(NewCalendarActivity.this, "새로운 일정을 추가해봅시다.",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return id == R.id.action_settings || super.onOptionsItemSelected(item);
        return true;
    }

}
