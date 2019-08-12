package ppla01.book_room;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;

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


