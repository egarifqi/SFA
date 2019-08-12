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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isramela on 06/03/18.
 */

public class InfoRoom extends AppCompatActivity {
    String roomName, roomCapacities, roomFacilities,roomAddress,roomLocation,token,postUrl,tanggal;
    TextView location;
    TextView address;
    TextView name;
    TextView capacity;
    TextView facilities;
    TextView header;
    Calendar myCalendar = Calendar.getInstance();
    ListView mList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences url;
    SharedPreferences.Editor edit_url;
    RadioButton today, tomorrow;
    Button booking_room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_room);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2279A9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Room Information");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        edit_url= url.edit();
        postUrl = url.getString("url","");
        roomName = getIntent().getExtras().getString("name");
        roomLocation = getIntent().getExtras().getString("location");
        roomAddress = getIntent().getExtras().getString("address");
        roomCapacities = getIntent().getExtras().getString("capacity");
        roomFacilities = getIntent().getExtras().getString("facilities");
        token = pref.getString("token", "");

        name = (TextView) findViewById(R.id.room_name);
        location = (TextView) findViewById(R.id.room_location);
        address = (TextView) findViewById(R.id.room_address);
        capacity = (TextView) findViewById(R.id.room_capacity);
        facilities = (TextView) findViewById(R.id.room_facility);
        header = (TextView) findViewById(R.id.tanggal);
        mList = (ListView) findViewById(R.id.mList);
        today = (RadioButton) findViewById(R.id.today);
        booking_room = (Button) findViewById(R.id.booking);
        tomorrow = (RadioButton) findViewById(R.id.tomorrow);
        mList.setVisibility(View.GONE);

        name.setText(roomName);
        location.setText(roomLocation);
        address.setText(roomAddress);
        capacity.setText(roomCapacities);
        facilities.setText(roomFacilities);
        if(today.isChecked()==true) {
            Calendar c = Calendar.getInstance();
            String format = "dd-MMM-yyyy";
            SimpleDateFormat dateformat = new SimpleDateFormat(format,Locale.US);
            String datetime = dateformat.format(c.getTime());
            updateLabel(datetime);
            header.setText(datetime);
            tanggal=datetime;
        }

        booking_room.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.putString("room_name",roomName);
                editor.putString("location_room",roomLocation);
                editor.putString("calendar",tanggal);
                editor.commit();
                Intent intent = new Intent(v.getContext(), BookingRoom.class);
                finish();
                startActivity(intent);

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

    public void loadToday(View v){
        Calendar c = Calendar.getInstance();
        String format = "dd-MMM-yyyy";
        SimpleDateFormat dateformat = new SimpleDateFormat(format,Locale.US);
        String datetime = dateformat.format(c.getTime());
        updateLabel(datetime);
        header.setText(datetime);
        tanggal=datetime;
    }

    public void loadTomorrow(View v){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        String format = "dd-MMM-yyyy";
        SimpleDateFormat dateformat = new SimpleDateFormat(format,Locale.US);
        String datetime = dateformat.format(c.getTime());
        updateLabel(datetime);
        header.setText(datetime);
        tanggal=datetime;
    }

    public void showDatePicker(View v) {
        new DatePickerDialog(InfoRoom.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String date = sdf.format(myCalendar.getTime());
            header.setText(date);
            updateLabel(date);
            tanggal=date;
        }
    };
    private void updateLabel(String date) {
        final notAvailableRoom cReader = new notAvailableRoom(this, -1,date,roomName,token,roomAddress,roomLocation);
        mList.setVisibility(View.VISIBLE);
        mList.setAdapter(cReader);
        if(cReader.isEmpty()) {
//            mList.setVisibility(View.GONE);
        }else {
            mList.setVisibility(View.VISIBLE);
        }
    }
    public class notAvailableRoom extends ArrayAdapter<RoomNotAvailable> {
        Context ctx;
        StringBuffer sb = new StringBuffer("");
        private SendRequest mAuthTask = null;
        String token,date,roomName,roomAdress,roomLocation;
        boolean status=false;
        public notAvailableRoom(Context context, int textViewResourceId, String date, String roomName, String token,String address, String location) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.date=date;
            this.roomName = roomName;
            this.token = token;
            this.roomAdress=address;
            this.roomLocation=location;
            attemptLogin();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.list_not_available_room, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            RoomNotAvailable currentItem = (RoomNotAvailable) getItem(position);
            String [] separate;
            separate = currentItem.getDate().split(" ");
            viewHolder.date.setText(separate[1]+" "+separate[2]+" "+separate[3]+" "+separate[4]+" "+separate[5]);
            viewHolder.meeting.setText(currentItem.getMeeting());
            viewHolder.owner.setText(currentItem.getOwner());
            viewHolder.contact.setText(currentItem.getContact());
            viewHolder.pic.setText("Booked by "+currentItem.getPic());
            return convertView;
        }
        private class ViewHolder {
            TextView date;
            TextView meeting;
            TextView pic;
            TextView hour;
            TextView owner;
            TextView contact;
            public ViewHolder(View view) {
                date = (TextView)view.findViewById(R.id.date);
                meeting = (TextView) view.findViewById(R.id.meeting);
                pic = (TextView) view.findViewById(R.id.pic);
                hour = (TextView) view.findViewById(R.id.hour);
                owner = (TextView) view.findViewById(R.id.owner);
                contact =(TextView) view.findViewById(R.id.phone);
            }
        }
        public class SendRequest extends AsyncTask<String, Void, String> {
            private final String date;
            private final String roomName;
            private final String token;
            private final String address;
            private final String location;
            ProgressDialog dialog = new ProgressDialog(InfoRoom.this);

            @Override
            protected void onPreExecute(){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                super.onPreExecute();
            }
            SendRequest(String date,String roomName, String token,String address,String location) {
                this. date = date;
                this.roomName=roomName;
                this.token=token;
                this.address=address;
                this.location=location;
            }
            protected String doInBackground(String... arg0) {
                try{
                    URL url = new URL(postUrl+"/load_book_history");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("date",date);
                    postDataParams.put("token",token);
                    postDataParams.put("room_name",roomName);
                    postDataParams.put("location",address);
                    postDataParams.put("area",location);
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
                                JSONArray result = obj.getJSONArray("result");
                                return result.toString();
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
                if(status) {
                    dialog.dismiss();
                    loadArray(result);
                }
                else{
                    if(result.equals("session_expired")){
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(InfoRoom.this, Login.class);
                        finish();
                        startActivity(i);
                    }else{
                        dialog.dismiss();
                        Toast.makeText(InfoRoom.this,result,Toast.LENGTH_SHORT).show();
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
                mAuthTask = new SendRequest(date,roomName,token,roomAdress,roomLocation);
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
        public void loadArray(String res){
            try {
                String [] separate;
                String [] separate2;
                JSONArray all_room = new JSONArray(res);
                RoomNotAvailable [] temp = new RoomNotAvailable[all_room.length()];
                for (int idx=0; idx<all_room.length(); idx++) {
                    JSONObject room_detail = all_room.getJSONObject(idx);
                    String _name = room_detail.getString("meeting_subject");
                    String _id = room_detail.getString("date");
                    String _pic = room_detail.getString("pic");
                    String _contact = room_detail.getString("contact");
                    String subject = room_detail.getString("booked_by");
                    RoomNotAvailable room = new RoomNotAvailable ();
                    room.setMeeting(_name);
                    room.setDate(_id);
                    room.setPic(subject);
                    room.setOwner(_pic);
                    room.setContact(_contact);
//                    this.add(room);
                    temp[idx]=room;
                }
                RoomNotAvailable baru = new RoomNotAvailable();
                for (int i =0; i<temp.length;i++){
                    separate = temp[i].getDate().split(" ");
                    for (int j = i; j < temp.length; j++) {
                        separate2 = temp[j].getDate().split(" ");
                        if (separate[1].compareTo(separate2[1]) < 0) {
                           if (separate[2].compareTo(separate2[2]) > 0) {
                                baru = temp[i];
                                temp[i] = temp[j];
                                temp[j] = baru;
                           }else;
                        }else{
                            if(separate[2].compareTo(separate2[2]) < 0);
                            else{
                                baru = temp[i];
                                temp[i] = temp[j];
                                temp[j] = baru;
                            }
                        }
                    }this.add(temp[i]);
                }
            } catch (Exception e) {
            }
        }
    }
}


