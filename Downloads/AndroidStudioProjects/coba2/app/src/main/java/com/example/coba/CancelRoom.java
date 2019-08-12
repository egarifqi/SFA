package ppla01.book_room;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by isramela on 06/03/18.
 */

public class CancelRoom extends AppCompatActivity {
    String roomName, roomAddress,roomLocation,roomId , token,subject_meeting,date_meeting,
    start_meeting,end_meeting, picture,postUrl;
    TextView location;
    TextView address;
    TextView name;
    TextView subject;
    TextView date;
    TextView time;
    ImageView image;
    Button cancel;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences url;
    SharedPreferences.Editor edit_url;
    ConfirmationCancel room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_room);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        edit_url= url.edit();
        postUrl = url.getString("url","");
        token = pref.getString("token", "");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2279A9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Information Booked Room");

        roomName = getIntent().getExtras().getString("nameroom");
        roomLocation = getIntent().getExtras().getString("location");
        roomAddress= getIntent().getExtras().getString("address");
        subject_meeting =getIntent().getExtras().getString("subject");
        date_meeting =getIntent().getExtras().getString("date");
        start_meeting =getIntent().getExtras().getString("start");
        end_meeting =getIntent().getExtras().getString("end");
        picture = getIntent().getExtras().getString("picture");
        roomId = getIntent().getExtras().getString("idRoom");

        name = (TextView) findViewById(R.id.room_name);
        location = (TextView) findViewById(R.id.room_location);
        address = (TextView) findViewById(R.id.room_address);
        subject = (TextView) findViewById(R.id.subject_meeting);
        date = (TextView) findViewById(R.id.date_meeting);
        time = (TextView) findViewById(R.id.time_meeting);
        image = (ImageView) findViewById(R.id.image_room);
        cancel = (Button) findViewById(R.id.button_cancel);

        name.setText(roomName);
        location.setText(roomLocation);
        address.setText(roomAddress);
        subject.setText(subject_meeting);
        date.setText(date_meeting);
        time.setText(start_meeting+" - "+end_meeting);

        UrlImageViewHelper.setUrlDrawable(image,picture);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showDialog();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel Room");
        alertDialogBuilder
            .setMessage("Are you sure to cancel "+ roomName +" ?")
            .setIcon(R.drawable.icon)
            .setCancelable(false)
            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    room = new ConfirmationCancel(CancelRoom.this,-1,token,roomId);
                    dialog.cancel();
                }
            })
            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class ConfirmationCancel extends ArrayAdapter<RoomDetail> {
        CancelRoom ctx;
        StringBuffer sb = new StringBuffer("");
        String token,roomId;
        private SendRequest mAuthTask = null;
        boolean status = false;
        public ConfirmationCancel(CancelRoom context, int textViewResourceId, String token,String roomId) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.token = token;
            this.roomId=roomId;
            attemptLogin();
        }

        public void attemptLogin() {
            boolean cancel = false;
            View focusView = null;
            if (cancel) {
                focusView.requestFocus();
            } else {
                mAuthTask = new SendRequest(roomId,token);
                mAuthTask.execute();
            }
        }
        public class SendRequest extends AsyncTask<String, Void, String> {
            private  String id;
            private String token;
            ProgressDialog dialog = new ProgressDialog(CancelRoom.this);
            @Override
            protected void onPreExecute(){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                super.onPreExecute();
            }
            SendRequest(String id, String token) {
                this.id = id;
                this.token=token;
            }
            protected String doInBackground(String... arg0) {
                try{
                    URL url = new URL(postUrl+"/cancel_booking");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id", id);
                    postDataParams.put("token",token);
                    Log.e("params",postDataParams.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line="";
                        while((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();
                        JSONObject obj = new JSONObject(sb.toString());
                        try{
                            status = obj.getBoolean("status");
                            if (status) {
                                Intent i = new Intent(CancelRoom.this,NotificationCancel.class);
                                finish();
                                startActivity(i);
                            } else if(!status) {
                                return obj.getString("msg");
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        return obj.getString("msg");
                    }
                    else {
                        return new String("false : "+responseCode);
                    }
                }
                catch(Exception e){
                    return new String("Error Connection");
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if(status){
                    dialog.dismiss();
                }
                else {
                    if(result.equals("session_expired")){
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(CancelRoom.this, Login.class);
                        finish();
                        startActivity(i);
                    }else{
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}




