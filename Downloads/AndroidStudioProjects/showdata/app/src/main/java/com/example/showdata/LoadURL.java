package com.example.showdata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by isramela on 06/03/18.
 */

public class LoadURL extends AppCompatActivity {
    private EditText Editurl;
    private SharedPreferences url;
    private SharedPreferences.Editor edit_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_url);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2279A9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Setting");
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        edit_url=url.edit();
        Editurl =(EditText) findViewById(R.id.url);

        Button mEmailSignInButton = (Button) findViewById(R.id.button_url);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_url =  Editurl.getText().toString();
                edit_url.putString("url",new_url);
                edit_url.commit();
                Intent i = new Intent(LoadURL.this,Login.class);
                finish();
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent i = new Intent(LoadURL.this, Help.class);
            finish();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}


