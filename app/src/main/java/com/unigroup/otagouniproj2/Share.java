package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Share extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        ImageView img = findViewById(R.id.shareImageView);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, check out this app I'm using: https://www.daybuildr.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NavDrawActivity.class);
        startActivity(intent);
        finish();
    }
}
