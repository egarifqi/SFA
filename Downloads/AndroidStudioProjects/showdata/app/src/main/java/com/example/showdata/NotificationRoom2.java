package com.example.showdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by isramela on 15/03/18.
 */

public class NotificationRoom2 extends Activity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences url;
    TextView text;
    String token,subject,location, noperson, date, start,end,postUrl,pic,contact;
    boolean repeat;
    Booking bookedAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_room2);
        text = (TextView) findViewById(R.id.text_notif);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        String notif = pref.getString("message","");
        text.setText(notif);
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        postUrl=url.getString("url","");
        token = pref.getString("token","");
        subject = pref.getString("agenda","");
        location=pref.getString("lokasi","");
        noperson = pref.getString("noperson","");
        date= pref.getString("tanggal_meeting","");
        start = pref.getString("jam_mulai","");
        end = pref.getString("jam_selesai","");
        pic = pref.getString("pemilik","");
        contact=pref.getString("phone","");
        repeat = pref.getBoolean("weekly",false);

        Button done = (Button) findViewById(R.id.button_back);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bookedAgain = new Booking(NotificationRoom2.this, -1,token, subject, location,noperson, date, start, end,pic,contact,repeat);
            }
        });
    }

    public class Booking extends ArrayAdapter<DetailMeeting> {
        Context ctx;
        StringBuffer sb = new StringBuffer("");
        SendRequest mAuthTask = null;
        String token, subject_meeting,locations,noPerson,date,start,end,pic,contact;
        boolean weekly;
        boolean status = false;
        public Booking (Context context, int textViewResourceId, String token, String subject_meeting, String locations,
                        String noPerson, String date, String start, String end, String pic, String contact, boolean weekly) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.token=token;
            this.subject_meeting = subject_meeting;
            this.locations = locations;
            this.noPerson = noPerson;
            this.date=date;
            this.start = start;
            this.end = end;
            this.pic=pic;
            this.contact=contact;
            this.weekly=weekly;
            attemptLogin();
        }
        public class SendRequest extends AsyncTask<String, Void, String> {
            private final String token;
            private final String subject;
            private final String location;
            private final String noPerson;
            private final String date;
            private final String start;
            private final String end;
            private final String pic;
            private final String contact;
            private final boolean weekly;
            SendRequest(String token, String subject, String location, String noPerson
                    , String date, String start, String end, String pic, String contact, boolean weekly) {
                this.token =token;
                this.subject = subject;
                this.location=location;
                this.noPerson=noPerson;
                this.date=date;
                this.start=start;
                this.end=end;
                this.pic=pic;
                this.contact=contact;
                this.weekly=weekly;
            }
            ProgressDialog dialog = new ProgressDialog(NotificationRoom2.this);
            @Override
            protected void onPreExecute(){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                super.onPreExecute();
            }
            protected String doInBackground(String... arg0) {
                try {
                    URL url = new URL(postUrl+"/booking_room");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("token",token);
                    postDataParams.put("meeting_subject", subject);
                    postDataParams.put("area", location);
                    postDataParams.put("date", date);
                    postDataParams.put("start_time", start);
                    postDataParams.put("end_time", end);
                    postDataParams.put("participants", noPerson);
                    postDataParams.put("pic",pic);
                    postDataParams.put("contact",contact);
                    postDataParams.put("repeat",weekly);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();
                        JSONObject obj = new JSONObject(sb.toString());
                        try {
                            status = obj.getBoolean("status");
                            if (status) {
                                Intent i = new Intent(NotificationRoom2.this, AvailableRoom.class);
                                finish();
                                startActivity(i);
                            } else if (!status) {
                                return obj.getString("msg");
                            } else {
                            }
                        } catch (Exception e) {

                        }
                        return obj.getString("msg");
                    } else {
                        return new String("false : " + responseCode);
                    }
                } catch (Exception e) {
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
                        Intent i = new Intent(NotificationRoom2.this, Login.class);
                        finish();
                        startActivity(i);
                    }else{
                        dialog.dismiss();
                        Toast.makeText(NotificationRoom2.this,result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        public void attemptLogin() {
            boolean cancel = false;
            View focusView = null;
            if (cancel) {

                focusView.requestFocus();
            } else {
                mAuthTask = new SendRequest(token, subject_meeting,locations,noPerson,date,start,end,pic,contact,weekly);
                mAuthTask.execute();
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
