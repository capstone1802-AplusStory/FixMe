package com.aplusstory.fixme;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import com.aplusstory.fixme.cal.OneDayView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FootprintActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,TodayFootPrintDataManager.LocationNamer
        , MonthlyFragment.OnMonthChangeListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    Fragment fragment;
    private FragmentManager fragmentManager = null;

    private TodayFootPrintDataManager dm = null;
    private Menu menuHide;
    private Date today = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_footprint);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_footprint);
        navigationView.setNavigationItemSelectedListener(this);

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

        if(this.fragmentManager == null) {
            this.fragmentManager = this.getSupportFragmentManager();
        }

        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH, 0);
        this.today = c.getTime();

        if(this.dm == null){
            this.dm = new TodayFootPrintDataManager(this, today);
            this.dm.setNamer(this);
        }

        fragment = new PieChartFragment();
        Bundle bd = new Bundle();
        ArrayList<FootprintDataManager.FootPrintData> dataArr = this.dm.getData();
        bd.putSerializable(FootprintDataManager.KEY_DATA, dataArr);
        bd.putLong(PieChartFragment.KEY_DATE, today.getTime());
        fragment.setArguments(bd);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.footprint_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_footprint);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chart_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean rt = super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.ic_footprint_calendar:
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed()){
                    Fragment yearlyCalendarFragment = (Fragment) new YearlyCalendarFragment();
                    String fragmentTag = yearlyCalendarFragment.getClass().getSimpleName();
                    FragmentTransaction ft = this.fragmentManager.beginTransaction();
                    ft.replace(R.id.footprint_frame, yearlyCalendarFragment);

                    ft.addToBackStack(fragmentTag);
                    yearlyCalendarFragment.getFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

//                    menuHide.findItem(R.id.ic_footprint_calendar).setVisible(false);
                    ft.commit();
//                    ft.disallowAddToBackStack();
                }

                rt = true;
                break;
        }
        return rt;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gps_schedule) {
            startActivity(new Intent(this, ScheduleActivity.class));
            finish();
        } else if (id == R.id.nav_today_footprint) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_footprint);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public String getName(LocationDataManager.LocationData location) {
        //TODO : if there's favorite with the location, get its name; otherwise, get address of it.
        return null;
    }

    @Override
    public String getName(LocationDataManager.PathData path) {
        //TODO : if there's favorite with the location, get its name; otherwise, default value.
        return this.getString(R.string.footprint_path_default);
    }

    @Override
    public void onChange(int year, int month) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(this.today);
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        this.today = c.getTime();
    }

    @Override
    public void onDayClick(OneDayView dayView) {
        if(this.fragmentManager == null){
            this.fragmentManager = this.getSupportFragmentManager();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, dayView.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, dayView.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, dayView.get(Calendar.DAY_OF_MONTH));

        Date date = cal.getTime();

        Fragment fragment = new PieChartFragment();
        Bundle bd = new Bundle();
        TodayFootPrintDataManager dm = new TodayFootPrintDataManager(this, date);
        dm.setNamer(this);
        ArrayList<FootprintDataManager.FootPrintData> dataArr = dm.getData();
        bd.putSerializable(FootprintDataManager.KEY_DATA, dataArr);
        bd.putLong(PieChartFragment.KEY_DATE, date.getTime());
        fragment.setArguments(bd);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.footprint_frame, fragment);
        fragmentTransaction.commit();
    }
}
