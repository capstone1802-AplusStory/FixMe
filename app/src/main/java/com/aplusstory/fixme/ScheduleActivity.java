package com.aplusstory.fixme;

//import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aplusstory.fixme.cal.OneDayView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ScheduleActivity extends AppCompatActivity
        implements ScheduleUIManager,
        ScheduleFragment.OnFragmentInteractionListener, MonthlyFragment.OnMonthChangeListener {
    Toolbar toolbar;
    private FragmentManager fgm = null;
    private Fragment schFrg = null;
    ScheduleManager dm = null;
    private List<String> monthlyList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);
        Calendar today = Calendar.getInstance();
        if(this.fgm == null){
            this.fgm = this.getSupportFragmentManager();
        }
        if(this.dm == null){
            this.dm = new ScheduleManager(this);
        }

        this.monthlyList = this.dm.getMonthlyList(today.get(Calendar.YEAR), today.get(Calendar.MONTH));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean rt = super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add_schedule:
                if(this.fgm != null && !this.fgm.isDestroyed()){
                    FragmentTransaction ft = this.fgm.beginTransaction();
                    this.schFrg = (Fragment) new ScheduleFragment();
                    ft.add(R.id.frame_schedule, this.schFrg);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                rt = true;
                break;
            case android.R.id.home:
                Toast.makeText(this, "Menu",Toast.LENGTH_SHORT).show();
                rt =  true;
                break;
            default:
                //error
        }

        return rt;
    }

    @Override
    public void onFragmentInteraction(Bundle arg) {
        if(arg.containsKey(ScheduleFragment.ARG_KEY_SCHEDULE)){
            if(this.dm.putData((ScheduleDataManager.ScheduleData)arg.getSerializable(ScheduleFragment.ARG_KEY_SCHEDULE))) {
                String savedMsg = "schedule saved";
                Toast.makeText(this, savedMsg, Toast.LENGTH_SHORT).show();
            }
        }else if(arg.containsKey(ScheduleFragment.ARG_KEY_DELETE) && arg.getBoolean(ScheduleFragment.ARG_KEY_DELETE)){
            //TODO
            String delMsg = "schedule deleted";
            Toast.makeText(this, delMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setDataManager(UserDataManager m) {
        if(m instanceof ScheduleDataManager){
            this.setDataManager((ScheduleDataManager)m);
        }
    }

    @Override
    public void setDataManager(ScheduleDataManager m) {
        if(m instanceof ScheduleManager){
            this.dm = (ScheduleManager) m;
        }
    }

    @Override
    public void onChange(int year, int month) {
        this.monthlyList = this.dm.getMonthlyList(year, month);
    }

    @Override
    public void onDayClick(OneDayView dayView) {
        Calendar c = dayView.getDay().getDay();
        Log.d(this.getClass().getName(), "onDayClick, " + c.getTime().toString());
        if(this.fgm != null && !this.fgm.isDestroyed()){
            FragmentTransaction ft = this.fgm.beginTransaction();
            this.schFrg = (Fragment) new ScheduleFragment();
            boolean hasSch = false;
            ScheduleDataManager.ScheduleData sch = null;
            for(String s : this.monthlyList){
                if((sch = this.dm.getData(s)) != null){
                    Calendar cBegin = Calendar.getInstance();
                    cBegin.setTime(new Date(sch.scheduleBegin));
                    Calendar cEnd = Calendar.getInstance();
                    cEnd.setTime(new Date(sch.scheduleEnd));
                    int date = c.get(Calendar.DAY_OF_MONTH);
                    if(date >= cBegin.get(Calendar.DAY_OF_MONTH) && date <= cEnd.get(Calendar.DAY_OF_MONTH)){
                        break;
                    }
                }
            }
            Bundle arg = new Bundle();
            if(sch != null) {
                Log.d(this.getClass().getName(), "loaded schedule json : \n" + sch.toString());
                arg.putSerializable(ScheduleFragment.ARG_KEY_SCHEDULE, sch);
            } else{
                arg.putLong(ScheduleFragment.ARG_KEY_TODAY, c.getTimeInMillis());
            }
            schFrg.setArguments(arg);
            ft.add(R.id.frame_schedule, this.schFrg);
                    ft.addToBackStack(null);
            ft.commit();
        }
    }
}
