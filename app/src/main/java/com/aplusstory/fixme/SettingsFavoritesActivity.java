package com.aplusstory.fixme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingsFavoritesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> favlist;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_favorites);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        favlist = new ArrayList<String>();
        favlist.add("학교");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favlist);

        listView = (ListView) findViewById(R.id.favList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                intent.putExtra("nickname", String.valueOf(listView.getItemAtPosition(position)));
                startActivity(intent);
            }
        });

        Button addButton = (Button) findViewById(R.id.addFavButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start mapview -> activity_favorite
            }
        });
    }


}
