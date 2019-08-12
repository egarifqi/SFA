package com.example.showdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by isramela on 15/03/18.
 */

public class NotificationCancel extends Activity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_cancel);
        text = (TextView) findViewById(R.id.text_notif);

        Button done = (Button) findViewById(R.id.button_book);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                finish();
                startActivity(intent);

            }
        });
    }
}
