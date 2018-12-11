package com.aplusstory.fixme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsFavoritesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> favlist;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    private int REQUEST_RESULT = 1;
    private FavoriteDataManager dm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_favorites);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if(this.dm == null){
            this.dm = new FavoriteDataManager(this);
            favlist = new ArrayList<String>(this.dm.getFavoriteList());
        }else {
            favlist = new ArrayList<String>();
            favlist.add("학교");
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favlist);

        listView = (ListView) findViewById(R.id.favList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SettingsFavoritesActivity that = SettingsFavoritesActivity.this;
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                String nickname = String.valueOf(listView.getItemAtPosition(position));
                intent.putExtra("nickname", nickname);
                intent.putExtra("location", "중앙대학교");
                intent.putExtra("address", "서울특별시 동작구 흑석로 84");
//                if(that.dm != null && that.dm.getFavoriteList().contains(nickname)) {
//                    intent.putExtra(FavoriteActivity.KEY_LOCATION_DATA, that.dm.getFavoriteLocation(nickname));
//                }
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });


        ImageButton addButton = (ImageButton) findViewById(R.id.add_favorite_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFavoritesActivity that = SettingsFavoritesActivity.this;
                Intent intent = new Intent(that.getApplicationContext(), TMapActivity.class);
                Bundle bd = new Bundle();
                LocationDataManager.LocationData currentLoca = LocationFileManager.getCurrentLocation(that);
                bd.putDouble(MapFragment.KEY_LONGITUDE, currentLoca.longitude);
                bd.putDouble(MapFragment.KEY_LATITUDE, currentLoca.latitude);
                intent.putExtra(TMapActivity.EXTRA_NAME_ARGUMENT, bd);
                startActivityForResult(intent,REQUEST_RESULT);

                // start mapview -> activity_favorite
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(resultCode == RESULT_OK && data != null) {
                if(data.hasExtra(TMapActivity.EXTRA_NAME_ARGUMENT)){
                    Bundle bd = data.getBundleExtra(TMapActivity.EXTRA_NAME_ARGUMENT);
                    String adr = bd.getString(MapFragment.KEY_ADDRESS, null);
                    double lat = bd.getDouble(MapFragment.KEY_LATITUDE, 0.0);
                    double lon = bd.getDouble(MapFragment.KEY_LONGITUDE, 0.0);
                    LocationDataManager.LocationData loca = new LocationDataManager.LocationData(System.currentTimeMillis(), lat, lon);
                    if(this.dm != null){
                        this.dm.setFavoriteLocation(adr, loca);
                        this.arrayAdapter.clear();
                        this.favlist = new ArrayList<String>(this.dm.getFavoriteList());
                        this.arrayAdapter.addAll(favlist);
                        this.arrayAdapter.notifyDataSetChanged();
                    }else {
                        arrayAdapter.add(adr);
                    }
                }

                if(data.hasExtra(FavoriteActivity.EXTRA_NAME_ARGUMENT)
                && data.getBooleanExtra(FavoriteActivity.EXTRA_NAME_ARGUMENT, false)) {
                    if (this.dm != null) {
                        this.dm.refresh();
                        this.arrayAdapter.clear();
                        this.favlist = new ArrayList<String>(this.dm.getFavoriteList());
                        this.arrayAdapter.addAll(favlist);
                        this.arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }


}
