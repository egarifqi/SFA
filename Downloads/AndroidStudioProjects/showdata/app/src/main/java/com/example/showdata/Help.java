package com.example.showdata;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by isramela on 06/03/18.
 */

public class Help extends AppCompatActivity {

    LinearLayout contact;
    LinearLayout info;
    LinearLayout setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2279A9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Help");
        contact=(LinearLayout) findViewById(R.id.contact_us);
        info=(LinearLayout) findViewById(R.id.information);
        setting = (LinearLayout) findViewById(R.id.setting);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Help.this,Contact.class);
                finish();
                startActivity(i);

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Help.this,About.class);
                finish();
                startActivity(i);

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Help.this,LoadURL.class);
                finish();
                startActivity(i);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}


