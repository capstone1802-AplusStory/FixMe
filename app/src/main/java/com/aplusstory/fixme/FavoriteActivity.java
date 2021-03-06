package com.aplusstory.fixme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteActivity extends AppCompatActivity {
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_LOCATION_DATA = "locaData";
    public static final String KEY_LOCATION_NAME = "location";
    public static final String KEY_ADDRESS = "address";
    public static final String EXTRA_NAME_ARGUMENT = "favorite_refresh";

    private boolean registered = false;
    Toolbar toolbar;
    private String nickname;
    private FavoriteDataManager dm;
    private LocationDataManager.LocationData loca = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        this.nickname = String.valueOf(intent.getStringExtra(KEY_NICKNAME));

        if(this.dm == null){
            this.dm = new FavoriteDataManager(this);
        }

        if(!intent.hasExtra(KEY_LOCATION_DATA)) {
            this.loca = this.dm.getFavoriteLocation(this.nickname);
            if(this.loca != null){
                Log.d(this.getClass().getName(), loca.toString());
                this.registered = true;
            }
        }else {
            try {
                this.loca = (LocationDataManager.LocationData) intent.getSerializableExtra(nickname);
            }catch (Exception e){
                Log.d(this.getClass().getName(), e.toString());
            }
        }

        TextView favoriteName = (TextView) findViewById(R.id.favoriteName);
        try {
            favoriteName.setText(intent.getStringExtra(KEY_LOCATION_NAME));
        } catch (NullPointerException e) {
            favoriteName.setText("");
        }

        TextView favoriteAddress = (TextView) findViewById(R.id.favoriteAddress);
        try {
            favoriteAddress.setText(intent.getStringExtra(KEY_ADDRESS));
        } catch (NullPointerException e) {
            favoriteAddress.setText("");
        }

        final ImageView starImage = (ImageView) findViewById(R.id.starImage);
        final Button favoriteButton = (Button) findViewById(R.id.favoriteButton);
        if(this.registered){
            favoriteButton.setText("즐겨찾기 해제");
            starImage.setImageResource(R.drawable.favorite_star_200);
        }else{
            favoriteButton.setText("즐겨찾기 등록");
            starImage.setImageResource(R.drawable.favorite_star_dark_200);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteActivity that = FavoriteActivity.this;
                if(that.registered) {
                    favoriteButton.setText("즐겨찾기 등록");
                    starImage.setImageResource(R.drawable.favorite_star_dark_200);
                    if(that.dm != null){
                        that.dm.remove(that.nickname);
                    }
                }else{
                    favoriteButton.setText("즐겨찾기 해제");
                    starImage.setImageResource(R.drawable.favorite_star_200);
                    if(that.dm != null && that.loca != null){
                        that.dm.setFavoriteLocation(that.nickname, that.loca);
                    }
                }
                that.registered = !that.registered;
            }
        });

        EditText favoriteNick = (EditText) findViewById(R.id.favoriteNickname);
        try {
            favoriteNick.setText(this.nickname);
        } catch (NullPointerException e) {
            favoriteNick.setText("");
        }

    }

    //  TODO: save data if clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_attribute_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra(EXTRA_NAME_ARGUMENT, true);
        this.setResult(Activity.RESULT_OK, it);
        this.finish();
        super.onBackPressed();
    }
}
