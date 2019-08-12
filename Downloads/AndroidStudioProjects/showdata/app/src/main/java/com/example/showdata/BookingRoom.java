package com.example.showdata;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isramela on 22/08/18.
 */

public class BookingRoom extends AppCompatActivity {
    TextView text;
    EditText subject;
    EditText pic;
    EditText contact;
    EditText date;
    CheckBox repeat;
    Button booking;
    SharedPreferences pref;
    SharedPreferences url;
    SharedPreferences.Editor editor;
    String pic_meeting,pic_meeting2,contact_pic, contact_pic2,room,location,calendar,hour_start,minute_start,hour_end,minute_end,subject_meeting,
            token, tanggal2,postUrl,start,end,owner;
    boolean weekly = false;
    Calendar myCalendar = Calendar.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        text = (TextView) findViewById(R.id.roomname);
        subject = (EditText) findViewById(R.id.subject_meeting);
        pic = (EditText) findViewById(R.id.pic_meeting);
        contact = (EditText) findViewById(R.id.telephon_pic);
        date= (EditText) findViewById(R.id.dateMeeting) ;
        repeat = (CheckBox) findViewById(R.id.weekly);
        booking = (Button) findViewById(R.id.booking);

        pic_meeting=pref.getString("account","");
        contact_pic=pref.getString("phone","");
        room= pref.getString("room_name","");
        location = pref.getString("location_room","");
        calendar= pref.getString("calendar","");
        token = pref.getString("token", "");
        postUrl = url.getString("url","");

        text.setText(room+" "+location);
        date.setText(calendar);
        pic.setText(pic_meeting);
        contact.setText(contact_pic);
        editor.putString("date_time",calendar);
        editor.commit();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BookingRoom.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd-MMM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        String tanggal = sdf.format(myCalendar.getTime());
                        date.setText(tanggal);
                        editor.putString("date_time",tanggal);
                        editor.commit();
                    }
                },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final Spinner start_hour = (Spinner) findViewById(R.id.start_hour);
        start_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour_start = start_hour.getItemAtPosition(position).toString();
                editor.putString("start_hour", hour_start);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Spinner start_minute = (Spinner) findViewById(R.id.start_minute);
        start_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minute_start = start_minute.getItemAtPosition(position).toString();
                editor.putString("start_minute", minute_start);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Spinner end_hour = (Spinner) findViewById(R.id.end_hour);
        end_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour_end = end_hour.getItemAtPosition(position).toString();
                editor.putString("end_hour", hour_end);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Spinner end_minute = (Spinner) findViewById(R.id.end_minute);
        end_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minute_end = end_minute.getItemAtPosition(position).toString();
                editor.putString("end_minute", minute_end);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    weekly = true;
                    editor.putBoolean("weekly",weekly);
                    editor.commit();
                }else{
                    weekly =  false;
                    editor.putBoolean("weekly",weekly);
                    editor.commit();
                }
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                subject_meeting = subject.getText().toString();
                pic_meeting2 =pic.getText().toString();
                contact_pic2 = contact.getText().toString();
                tanggal2 = pref.getString("date_time","");
                boolean weekly = pref.getBoolean("weekly",false);
                String locations = pref.getString("location", "");
                String jamMulai = pref.getString("start_hour", "");
                String menitMulai = pref.getString("start_minute", "");
                start = jamMulai + ":" + menitMulai;
                String jamEnd = pref.getString("end_hour", "");
                String menitEnd = pref.getString("end_minute", "");
                end = jamEnd + ":" + menitEnd;

                if (subject_meeting.equals("")) {
                    subject.setError("Type subject meeting");
                    valid = false;
                }if(tanggal2.equals("")) {
                    date.setError("Select the date meeting");
                    valid = false;
                }if(pic.equals("")) {
                    pic.setError("Type the PIC");
                    valid = false;
                }if(contact.equals("")) {
                    contact.setError("Type contact number");
                    valid = false;
                }if (valid){
                    editor.putString("agenda",subject_meeting);
                    editor.putString("tanggal_meeting",tanggal2);
                    editor.putString("jam_mulai",start);
                    editor.putString("jam_selesai",end);
                    editor.putString("pemilik",pic_meeting2);
                    editor.putString("phone",contact_pic2);
                    editor.putBoolean("weekly",weekly);
                    editor.commit();

                    Searchroom search = new Searchroom(BookingRoom.this, -1, token, subject_meeting,
                            tanggal2, start, end,pic_meeting2,contact_pic2,weekly);
                }
            }
        });
    }

    public class Searchroom extends ArrayAdapter<DetailMeeting> {
        Context ctx;
        StringBuffer sb = new StringBuffer("");
        private SendRequest mAuthTask = null;
        String token, subject_meeting,locations,noPerson,date,start,end,pic,contact;
        boolean weekly;
        boolean status = false;
        public Searchroom (Context context, int textViewResourceId, String token, String subject_meeting,
                           String date, String start, String end, String pic, String contact, boolean weekly) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.token=token;
            this.subject_meeting = subject_meeting;
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
            private final String date;
            private final String start;
            private final String end;
            private final String pic;
            private final String contact;
            private final boolean weekly;
            SendRequest(String token, String subject, String date, String start, String end, String pic, String contact, boolean weekly) {
                this.token =token;
                this.subject = subject;
                this.date=date;
                this.start=start;
                this.end=end;
                this.pic=pic;
                this.contact=contact;
                this.weekly=weekly;
            }
            ProgressDialog dialog = new ProgressDialog(BookingRoom.this);
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
                    URL url = new URL(postUrl+"/direct_booking");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("token",token);
                    postDataParams.put("meeting_subject", subject);
                    postDataParams.put("area", location);
                    postDataParams.put("room_name", room);
                    postDataParams.put("date", date);
                    postDataParams.put("start_time", start);
                    postDataParams.put("end_time", end);
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
                                Intent i = new Intent(BookingRoom.this, NotificationRoom.class);
                                startActivity(i);
                            } else if (!status) {
                                return obj.getString("msg");
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
                if(status) {
                    dialog.dismiss();
                }else if (!status){
                    if(result.equals("session_expired")){
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(BookingRoom.this, Login.class);
                        finish();
                        startActivity(i);
                    }
                    dialog.dismiss();
                    Toast.makeText(BookingRoom.this,result, Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(BookingRoom.this,result, Toast.LENGTH_SHORT).show();
                }
            }
        }
        public void attemptLogin() {
            boolean cancel = false;
            View focusView = null;
            if (cancel) {

                focusView.requestFocus();
            } else {
                mAuthTask = new SendRequest(token, subject_meeting,date,start,end,pic,contact,weekly);
                mAuthTask.execute();
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
}
