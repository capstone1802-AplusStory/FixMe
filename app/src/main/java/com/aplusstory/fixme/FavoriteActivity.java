package com.aplusstory.fixme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        TextView favoriteName = (TextView) findViewById(R.id.favoriteName);
        try {
            favoriteName.setText(intent.getStringExtra("location"));
        } catch (NullPointerException e) {
            favoriteName.setText("");
        }

        TextView favoriteAddress = (TextView) findViewById(R.id.favoriteAddress);
        try {
            favoriteAddress.setText(intent.getStringExtra("address"));
        } catch (NullPointerException e) {
            favoriteAddress.setText("");
        }

        final ImageView starImage = (ImageView) findViewById(R.id.starImage);

        final Button favoriteButton = (Button) findViewById(R.id.favoriteButton);
        favoriteButton.setText("즐겨찾기 해제");
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (String.valueOf(favoriteButton.getText())) {
                    case "즐겨찾기 해제":
                        favoriteButton.setText("즐겨찾기 등록");
                        starImage.setImageResource(R.drawable.favorite_star_dark_200);
                        break;
                    case "즐겨찾기 등록":
                        favoriteButton.setText("즐겨찾기 해제");
                        starImage.setImageResource(R.drawable.favorite_star_200);
                        break;
                }
            }
        });

        EditText favoriteNick = (EditText) findViewById(R.id.favoriteNickname);
        try {
            favoriteNick.setText(intent.getStringExtra("nickname"));
        } catch (NullPointerException e) {
            favoriteNick.setText("");
        }
        nickname = String.valueOf(favoriteNick.getText());
    }

    //  TODO: save data if clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_attribute_menu, menu);
        return true;
    }
}
