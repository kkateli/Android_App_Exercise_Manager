package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Support extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        TextView link = findViewById(R.id.brokenTextLink);
        String linkText = "Contact us for support via our\n" +
                " <a href='https://www.daybuildr.com/contact/'>web page</a>.";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NavDrawActivity.class);
        startActivity(intent);
        finish();
    }
}
