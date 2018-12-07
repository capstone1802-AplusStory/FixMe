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
                intent.putExtra("location", "중앙대학교");
                intent.putExtra("address", "서울특별시 동작구 흑석로 84");
                startActivity(intent);
            }
        });

        ImageButton addButton = (ImageButton) findViewById(R.id.add_favorite_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent = new Intent(getApplication(), TMapActivity.class);
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
                    String adr = bd.getString(MapFragment.KEY_ADDRESS);
                    arrayAdapter.add(adr);
                }
            }
        }
    }


}
