package com.aplusstory.fixme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_settings);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_settings);
        navigationView.setNavigationItemSelectedListener(this);

        final String[] setlist1 = {
                this.getString(R.string.setting_item_alert_transperancy)
                ,this.getString(R.string.setting_item_alert_light_sensivity)
                ,this.getString(R.string.setting_item_alert_period)
        };

        ListView list1 = (ListView) findViewById(R.id.settingList1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setlist1);
        list1.setAdapter(arrayAdapter);
        list1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch(position) {
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), AlarmOpacityActivity.class);

                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent2 = new Intent(getApplicationContext(), LightSensActivity.class);
                                startActivity(intent2);
                                break;
                            case 2:
                                Intent intent3 = new Intent(getApplicationContext(), AlarmPeriodActivity.class);
                                startActivity(intent3);
                                break;
                        }
                    }
                }
        );

        final ArrayList<String> setlistV = new ArrayList<String>();
        setlistV.add(this.getString(R.string.setting_item_alert_vibration));
        setlistV.add(this.getString(R.string.setting_item_alert_activate));
        ListView listV = (ListView) findViewById(R.id.settingListV);
        SwitchListviewAdapter listviewAdapter = new SwitchListviewAdapter(this, R.layout.switch_list, setlistV);
        listV.setAdapter(listviewAdapter);

        final String[] setlist2 = {
                this.getString(R.string.setting_item_footprint_data_duration)
                ,this.getString(R.string.setting_item_footprint_location_bookmark)
        };
        ListView list2 = (ListView) findViewById(R.id.settingList2);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setlist2);
        list2.setAdapter(arrayAdapter2);

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), FootDataSettingsActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), SettingsFavoritesActivity.class);
                        break;
                }
                if(intent != null) {
                    startActivity(intent);
                }
            }
        });

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_settings);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            startActivity(new Intent(this, FootprintActivity.class));
            finish();
        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_settings);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
