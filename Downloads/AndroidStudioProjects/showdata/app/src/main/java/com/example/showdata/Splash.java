package com.example.showdata;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    public SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pref.getString("email","").isEmpty()){
                    Intent i = new Intent(Splash.this,Login.class);
                    finish();
                    startActivity(i);
                }else{
                    Intent i = new Intent(Splash.this,MainActivity.class);
                    finish();
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
