package com.aplusstory.fixme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ScheduleRepeationActivity extends AppCompatActivity
        implements ScheduleRepeatWeeklyFragment.OnFragmentInteractionListener
        , View.OnClickListener
    ,DatePickerDialog.OnDateSetListener
{
    public static final String EXTRA_NAME_ARGUMENT = "repeat_argument";
    public static final String ARGUMENT_KEY_REPEAT_TEXT = "repeat_text";
    public static final String ARGUMENT_KEY_REPEAT_CODE = ScheduleDataManager.ScheduleData.KEY_REPEAT_TYPE_CODE;
    public static final String ARGUMENT_KEY_REPEAT_WEEKLY = ScheduleDataManager.ScheduleData.KEY_REPEAT_DAY_OF_WEEK;
    public static final String ARGUMENT_KEY_REPEAT_END = ScheduleDataManager.ScheduleData.KEY_REPEAT_END;

    private Toolbar toolbar;
    private TextView textViewED, textViewRD;
    private Fragment weeklyFragment = null;
    private FragmentManager fragmentManager = null;
    private DatePickerDialog datePickerDialog = null;
//    String dates = "none";
    private Bundle arg = null;
    private Button noneButton, dailyButton, weeklyButton, monthlyButton, yearlyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            this.arg = new Bundle(savedInstanceState);
        } else {
            this.arg = new Bundle();
        }
        setContentView(R.layout.activity_schedule_repeation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Intent it = this.getIntent();
        Bundle bd = null;
        int code = -1;
        long endDataTime = -1;
        String str = null;

        if(it != null && it.hasExtra(EXTRA_NAME_ARGUMENT)){
            bd = it.getBundleExtra(EXTRA_NAME_ARGUMENT);
            if(bd != null){
                code = bd.getInt(ARGUMENT_KEY_REPEAT_CODE, -1);
                endDataTime = bd.getLong(ARGUMENT_KEY_REPEAT_END, -1);
                str = bd.getString(ARGUMENT_KEY_REPEAT_TEXT, null);
            }
        }




        Calendar calendar = Calendar.getInstance();
        if(endDataTime > 0){
            calendar.setTimeInMillis(endDataTime);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        this.datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        textViewRD = (TextView) findViewById(R.id.repeatDay);
        if(code >= 0 && str != null){
            textViewRD.setText(str);
        }else {
            textViewRD.setText(R.string.repeat_type_none);
        }

        textViewED = (TextView) findViewById(R.id.endRDate);
        textViewED.setText(year + "년 " + (month + 1) + "월 " + day + "일");
        textViewED.setOnClickListener(this);
        if(this.fragmentManager == null){
            this.fragmentManager = this.getSupportFragmentManager();
        }

        noneButton = (Button) findViewById(R.id.noneButton);
        noneButton.setOnClickListener(this);

        dailyButton = (Button) findViewById(R.id.dailyButton);
        dailyButton.setOnClickListener(this);

        weeklyButton = (Button) findViewById(R.id.weeklyButton);
        weeklyButton.setOnClickListener(this);

        monthlyButton = (Button) findViewById(R.id.monthlyButton);
        monthlyButton.setOnClickListener(this);

        yearlyButton = (Button) findViewById(R.id.yearlyButton);
        yearlyButton.setOnClickListener(this);

        Button selectedButton = noneButton;

        if(code >= 0){
            switch (code){
                case ScheduleDataManager.RepeatDuration.REPEAT_DAYLY:
                    selectedButton = dailyButton;
                    break;
                case ScheduleDataManager.RepeatDuration.REPEAT_WEEKLY:
                    selectedButton = weeklyButton;
                    break;
                case ScheduleDataManager.RepeatDuration.REPEAT_MONTHLY:
                    selectedButton = monthlyButton;
                    break;
                case ScheduleDataManager.RepeatDuration.REPEAT_YEARLY:
                    selectedButton = yearlyButton;
                    break;
                default:
                    //...?
            }
        }

        selectedButton.setSelected(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_attribute_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean rt = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.schedule_confirm:
//            ScheduleFragment scheduleFragment = new ScheduleFragment();
//            ScheduleRepeatData scheduleData = new ScheduleRepeatData(dates, repeatState);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("scheduleData", scheduleData);
//            scheduleFragment.setArguments(bundle);
            Intent intent = new Intent();
            intent.putExtra(ScheduleRepeationActivity.EXTRA_NAME_ARGUMENT, this.arg);
            setResult(RESULT_OK, intent);
            finish();
//            break;
            default:
                Log.d(this.getClass().getName(), "not confirmed");
        }
        return rt;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView textView = (TextView) findViewById(R.id.endRDate);
        textView.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.arg.putLong(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_END, calendar.getTime().getTime());
    }

    @Override
    public void onFragmentInteraction(Bundle arg) {
        if(arg.containsKey(ScheduleRepeatWeeklyFragment.ARG_PARAM_CHECK_DAY)){
            StringBuilder text = new StringBuilder(this.getString(R.string.repeat_type_weekly));
            boolean[] checkDay = arg.getBooleanArray(ScheduleRepeatWeeklyFragment.ARG_PARAM_CHECK_DAY);
            boolean cond = false;

            if(checkDay != null) {
                for (int i = 0; i < checkDay.length; i++) {
                    if (checkDay[i]) {
                        cond = true;
                        switch (i) {
                            case 0:
                                text.append(this.getString(R.string.sun));
                                break;
                            case 1:
                                text.append(this.getString(R.string.mon));
                                break;
                            case 2:
                                text.append(this.getString(R.string.tue));
                                break;
                            case 3:
                                text.append(this.getString(R.string.wed));
                                break;
                            case 4:
                                text.append(this.getString(R.string.thu));
                                break;
                            case 5:
                                text.append(this.getString(R.string.fri));
                                break;
                            case 6:
                                text.append(this.getString(R.string.sat));
                                break;
                            default:
                                //something wrong
                        }
                    }
                }
            }
            if(cond) {
                String str = text.toString();
                this.textViewRD.setText(str);
                this.arg.putInt(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE, ScheduleDataManager.RepeatDuration.REPEAT_WEEKLY);
                this.arg.putBooleanArray(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_WEEKLY, checkDay);
                this.arg.putString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT, str);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String str;
        switch(v.getId()){
//            case R.id.repeatDay:
//                break;
            case R.id.endRDate:
                if(this.datePickerDialog == null) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DATE);
                    this.datePickerDialog = new DatePickerDialog(this, this, year, month, day);
                }
                this.datePickerDialog.show();
                break;
            case R.id.noneButton:
                noneButton.setSelected(true);
                dailyButton.setSelected(false);
                weeklyButton.setSelected(false);
                monthlyButton.setSelected(false);
                yearlyButton.setSelected(false);

                str = this.getString(R.string.repeat_type_none);
                this.textViewRD.setText(str);

                if(this.arg.containsKey(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE)){
                    this.arg.remove(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE);
                }
                this.arg.putString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT, str);
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed() && this.weeklyFragment != null){
                    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
                    final FragmentTransaction remove = fragmentTransaction.hide(this.weeklyFragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.dailyButton:
                noneButton.setSelected(false);
                dailyButton.setSelected(true);
                weeklyButton.setSelected(false);
                monthlyButton.setSelected(false);
                yearlyButton.setSelected(false);

                str = this.getString(R.string.repeat_type_daily);
                this.textViewRD.setText(str);

                this.arg.putInt(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE,
                        ScheduleDataManager.RepeatDuration.REPEAT_DAYLY);
                this.arg.putString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT, str);
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed() && this.weeklyFragment != null){
                    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
                    final FragmentTransaction remove = fragmentTransaction.hide(this.weeklyFragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.weeklyButton:
                noneButton.setSelected(false);
                dailyButton.setSelected(false);
                weeklyButton.setSelected(true);
                monthlyButton.setSelected(false);
                yearlyButton.setSelected(false);
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed()){
                    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
                    if(this.weeklyFragment == null) {
                        this.weeklyFragment = (Fragment) new ScheduleRepeatWeeklyFragment();
                        fragmentTransaction.add(R.id.fragment_blank, this.weeklyFragment);
                    } else {
                        fragmentTransaction.show(this.weeklyFragment);
                    }

                    fragmentTransaction.commit();
                }
                break;
            case R.id.monthlyButton:
                noneButton.setSelected(false);
                dailyButton.setSelected(false);
                weeklyButton.setSelected(false);
                monthlyButton.setSelected(true);
                yearlyButton.setSelected(false);

                str = this.getString(R.string.repeat_type_monthly);
                this.textViewRD.setText(str);

                this.arg.putInt(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE,
                        ScheduleDataManager.RepeatDuration.REPEAT_MONTHLY);
                this.arg.putString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT, str);
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed() && this.weeklyFragment != null){
                    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
                    final FragmentTransaction remove = fragmentTransaction.hide(this.weeklyFragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.yearlyButton:

                noneButton.setSelected(false);
                dailyButton.setSelected(false);
                weeklyButton.setSelected(false);
                monthlyButton.setSelected(false);
                yearlyButton.setSelected(true);

                str = this.getString(R.string.repeat_type_yearly);
                this.textViewRD.setText(str);

                this.arg.putInt(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE,
                        ScheduleDataManager.RepeatDuration.REPEAT_YEARLY);
                this.arg.putString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT, str);
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed() && this.weeklyFragment != null){
                    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
                    final FragmentTransaction remove = fragmentTransaction.hide(this.weeklyFragment);
                    fragmentTransaction.commit();
                }
                break;
        }
    }
}
