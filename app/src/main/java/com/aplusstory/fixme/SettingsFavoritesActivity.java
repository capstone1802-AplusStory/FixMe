package com.aplusstory.fixme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    // add to list
    public void myOnClick(View view) {
    }

}
