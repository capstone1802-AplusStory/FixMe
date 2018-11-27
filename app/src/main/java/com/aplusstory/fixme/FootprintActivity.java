package com.aplusstory.fixme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class FootprintActivity extends AppCompatActivity {
    Toolbar toolbar;
    Fragment fragment;
    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_full_menu);

        if(this.fragmentManager == null) {
            this.fragmentManager = this.getSupportFragmentManager();
        }

        fragment = new PieChartFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.footprint_frame, fragment);
        fragmentTransaction.commit();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean rt = super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.ic_footprint_calendar:
                if(this.fragmentManager != null && !this.fragmentManager.isDestroyed()){
                    Fragment yearlyCalendarFragment = (Fragment) new YearlyCalendarFragment();
                    FragmentTransaction ft = this.fragmentManager.beginTransaction();
                    ft.replace(R.id.footprint_frame, yearlyCalendarFragment);
                    ft.addToBackStack(null);

                    ft.commit();
                }

                rt = true;
                break;
            case android.R.id.home:
                Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
                rt = true;
                break;
            default:
                //error
        }
        return rt;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chart_menu, menu);
        return true;
    }
}
