package ppla01.book_room;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by isramela on 15/03/18.
 */

public class NotificationRoom extends Activity{
    TextView text;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_room);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.putBoolean("weekly",false);
        editor.commit();
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
