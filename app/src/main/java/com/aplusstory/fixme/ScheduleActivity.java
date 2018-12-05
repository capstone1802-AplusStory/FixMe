package com.aplusstory.fixme;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aplusstory.fixme.cal.OneDayView;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ScheduleUIManager,
        ScheduleFragment.OnFragmentInteractionListener, MonthlyFragment.OnMonthChangeListener,
        ScheduleListFragment.OnFragmentInteractionListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private FragmentManager fgm = null;
    private Fragment schFrg = null;
    ScheduleManager dm = null;
    private List<String> monthlyList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView appBarTitle = (TextView) findViewById(R.id.schedule_title);
        appBarTitle.setText("YYYY.MM");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_full_menu, this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Calendar today = Calendar.getInstance();
        if(this.fgm == null){
            this.fgm = this.getSupportFragmentManager();
        }

        if(this.dm == null){
            this.dm = new ScheduleManager(this);
        }

        this.monthlyList = this.dm.getMonthlyList(today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        View header = navigationView.getHeaderView(0);
        ImageView homeImage = (ImageView) header.findViewById(R.id.homeImage);
        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                    String fragmentTag = this.schFrg.getClass().getSimpleName();
                    ft.replace(R.id.frame_schedule, this.schFrg);
                    ft.addToBackStack(fragmentTag);
                    this.schFrg.getFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.commit();
                }
                rt = true;
                break;
        }

        return rt;
    }

    @Override
    public void onFragmentInteraction(Bundle arg) {
        Calendar c = Calendar.getInstance();
        if(arg.containsKey(ScheduleFragment.ARG_KEY_SCHEDULE)){
            ScheduleDataManager.ScheduleData sch = (ScheduleDataManager.ScheduleData)arg.getSerializable(ScheduleFragment.ARG_KEY_SCHEDULE);
            if(this.dm.putData(sch)) {
                String savedMsg = "schedule saved";
                switch(sch.tableColor){
                    case ScheduleDataManager.TableColor.RED:
                        break;
                    case ScheduleDataManager.TableColor.PINK:
                        break;
                    case ScheduleDataManager.TableColor.YELLOW:
                        break;
                    case ScheduleDataManager.TableColor.YELLOWGREEN:
                        break;
                    case ScheduleDataManager.TableColor.GREEN:
                        break;
                    case ScheduleDataManager.TableColor.MINT:
                        break;
                    case ScheduleDataManager.TableColor.SKYBLUE:
                        break;
                    case ScheduleDataManager.TableColor.BLUE:
                        break;
                    case ScheduleDataManager.TableColor.PURPLE:
                        break;
                }
                Toast.makeText(this, savedMsg, Toast.LENGTH_SHORT).show();
            }
            this.monthlyList = this.dm.getMonthlyList(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        }else if(arg.containsKey(ScheduleFragment.ARG_KEY_DELETE) && arg.getBoolean(ScheduleFragment.ARG_KEY_DELETE)){
            //TODO
            String delMsg = "schedule deleted";
            Toast.makeText(this, delMsg, Toast.LENGTH_SHORT).show();
        }else if(arg.containsKey(ScheduleListFragment.ARG_KEY_ADD) && arg.getBoolean(ScheduleListFragment.ARG_KEY_ADD)){
            Date today;
            if(arg.containsKey(ScheduleListFragment.ARG_KEY_TODAY)) {
                today = new Date(arg.getLong(ScheduleListFragment.ARG_KEY_TODAY));
            } else {
                today = new Date();
            }
            Log.d(this.getClass().getName(), "add schedule, date : " + today.toString());

            FragmentTransaction ft = this.fgm.beginTransaction();
            this.schFrg = (Fragment) new ScheduleFragment();
            this.schFrg.setArguments(new Bundle(arg));
            String fragmentTag = this.schFrg.getClass().getSimpleName();
            ft.replace(R.id.frame_schedule, this.schFrg);
            ft.addToBackStack(fragmentTag);
            this.schFrg.getFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
        }else if(arg.containsKey(ScheduleFragment.ARG_KEY_EDIT)){
            ScheduleDataManager.ScheduleData sch = (ScheduleDataManager.ScheduleData)arg.getSerializable(ScheduleFragment.ARG_KEY_EDIT);
            if(sch != null){
                Log.d(this.getClass().getName(), "edit schedule, schedule data : " + sch.toString());
            }else {
                Log.d(this.getClass().getName(), "null?!");
            }

            FragmentTransaction ft = this.fgm.beginTransaction();
            this.schFrg = (Fragment) new ScheduleFragment();
            this.schFrg.setArguments(new Bundle(arg));
            String fragmentTag = this.schFrg.getClass().getSimpleName();
            ft.replace(R.id.frame_schedule, this.schFrg);
            ft.addToBackStack(fragmentTag);
            this.schFrg.getFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
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
        TextView appBarTitle = (TextView) findViewById(R.id.schedule_title);
        StringBuilder sb = new StringBuilder()
        .append(year)
        .append(this.getString(R.string.year))
        .append(' ')
        .append(month + 1)
        .append(this.getString(R.string.month));
        appBarTitle.setText(sb.toString());
    }

    @Override
    public void onDayClick(OneDayView dayView) {
        Calendar c = dayView.getDay().getDay();
        Log.d(this.getClass().getName(), "onDayClick, " + c.getTime().toString());
        if(this.fgm != null && !this.fgm.isDestroyed()){
            FragmentTransaction ft = this.fgm.beginTransaction();
            this.schFrg = (Fragment) new ScheduleListFragment();
            boolean hasSch = false;
            ArrayList<ScheduleDataManager.ScheduleData> schList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            for(String s : this.monthlyList){
                ScheduleDataManager.ScheduleData sch;
                Log.d(this.getClass().getName(), "monthly schedule " + s);
                if((sch = this.dm.getData(s)) != null){
                    Calendar cBegin = Calendar.getInstance();
                    cBegin.setTime(new Date(sch.scheduleBegin));
                    Calendar cEnd = Calendar.getInstance();
                    cEnd.setTime(new Date(sch.scheduleEnd));
                    int date = c.get(Calendar.DAY_OF_MONTH);
                    if(date >= cBegin.get(Calendar.DAY_OF_MONTH) && date <= cEnd.get(Calendar.DAY_OF_MONTH)){
                        schList.add(sch);
                        sb.append(sch.toString());
                        sb.append('\n');
                    }
                }
            }

            Bundle arg = new Bundle();
            if(schList.size() > 0) {
                Log.d(this.getClass().getName(), "loaded schedule json : " + sb.toString());
            }

            arg.putSerializable(ScheduleListFragment.ARG_KEY_SCHEDULE_LIST, schList);
            arg.putLong(ScheduleFragment.ARG_KEY_TODAY, c.getTimeInMillis());

            schFrg.setArguments(arg);
            String fragmentTag = this.schFrg.getClass().getSimpleName();
            ft.replace(R.id.frame_schedule, this.schFrg);
            ft.addToBackStack(fragmentTag);
            this.schFrg.getFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gps_schedule) {

        } else if (id == R.id.nav_today_footprint) {
            startActivity(new Intent(this, FootprintActivity.class));
            finish();

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
