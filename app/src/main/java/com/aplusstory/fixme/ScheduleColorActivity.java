package com.aplusstory.fixme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;

public class ScheduleColorActivity extends AppCompatActivity{
    public static final String EXTRA_NAME_ARGUMENT = "color_argument";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_color);

        recyclerView = (RecyclerView) findViewById(R.id.colorRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<SettingsColorInfo> colorInfoArrayList = new ArrayList<>();
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_red_circle_padding, ScheduleDataManager.TableColor.RED));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_pink_circle, ScheduleDataManager.TableColor.PINK));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_yellow_circle, ScheduleDataManager.TableColor.YELLOW));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_yellowgreen_circle, ScheduleDataManager.TableColor.LIGHTGREEN));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_green_circle, ScheduleDataManager.TableColor.GREEN));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_mint_circle, ScheduleDataManager.TableColor.CYAN));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_skyblue_circle, ScheduleDataManager.TableColor.SKYBLUE));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_blue_circle, ScheduleDataManager.TableColor.BLUE));
        colorInfoArrayList.add(new SettingsColorInfo(R.drawable.ic_purple_circle, ScheduleDataManager.TableColor.PURPLE));

        ScheduleColorRecyclerAdapter colorRecyclerAdapter = new ScheduleColorRecyclerAdapter(colorInfoArrayList);
        recyclerView.setAdapter(colorRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_attribute_menu, menu);
        return true;
    }
}
