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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isramela on 04/03/18.
 */

public class AvailableRoom extends AppCompatActivity {
    protected ObjectAvailableRoom cReader;
    protected BookConfirmation room;
    protected ListView mList;
    String room_name,token,postUrl;
    StringBuffer sb = new StringBuffer("");
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences url;
    SharedPreferences.Editor edit_url;
    boolean status = false;
    TextView load_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2279A9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Available Room");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        edit_url= url.edit();
        postUrl = url.getString("url","");
        token = pref.getString("token", "");
        mList = (ListView)findViewById(R.id.mList);
        load_url =(TextView) findViewById(R.id.available_room);
        load_url.setVisibility(View.GONE);
        load_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_url.setVisibility(View.GONE);
                cReader = new ObjectAvailableRoom(AvailableRoom.this,-1,token);
            }
        });
        cReader = new ObjectAvailableRoom(AvailableRoom.this,-1,token);
        mList.setAdapter(cReader);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                if (cReader.getItem(pos).getRoomName() != null) {
                    room_name= cReader.getItem(pos).getRoomName();
                    showDialog();
                }
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
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Book Room");
        alertDialogBuilder
            .setMessage("Are you sure to book "+ room_name +" ?")
            .setIcon(R.drawable.icon)
            .setCancelable(false)
            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    room = new BookConfirmation(AvailableRoom.this,-1,token,room_name);
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
    public class BookConfirmation extends ArrayAdapter<RoomDetail>{
        AvailableRoom ctx;
        StringBuffer sb = new StringBuffer("");
        String token,room;
        private SendRequest mAuthTask = null;
        boolean status = false;
        public BookConfirmation(AvailableRoom context, int textViewResourceId, String token,String room) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.token = token;
            this.room=room;
            attemptLogin();
        }
        private void attemptLogin() {
            boolean cancel = false;
            View focusView = null;
            if (cancel) {
                focusView.requestFocus();
            } else {
                mAuthTask = new SendRequest(room,token);
                mAuthTask.execute();
            }
        }
        public class SendRequest extends AsyncTask<String, Void, String> {
            ProgressDialog dialog = new ProgressDialog(AvailableRoom.this);
            @Override
            protected void onPreExecute(){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                super.onPreExecute();
            }
            private final String room_name;
            private final String token;
            SendRequest(String room_name, String token) {
                this.room_name = room_name;
                this.token = token;
            }
            protected String doInBackground(String... arg0) {
                try{
                    URL url = new URL(postUrl+"/booking_confirmation");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("room_name",room_name);
                    postDataParams.put("token",token);
                    Log.e("params",postDataParams.toString());
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
                                Intent i = new Intent(AvailableRoom.this,NotificationRoom.class);
                                finish();
                                startActivity(i);
                            } else if(!status) {
                                editor.putString("message",obj.getString("msg"));
                                editor.commit();
                                Intent i = new Intent(AvailableRoom.this,NotificationRoom2.class);
                                finish();
                                startActivity(i);
                            }
                        }
                        catch (Exception e){
                        }
                        return obj.getString("msg");
                    }
                    else {
                        return new String("false : "+responseCode);
                    }
                }
                catch(Exception e){
                    return new String("timeout");
                }
            }
            @Override
            protected void onPostExecute(String result) {
                if(status){
                    dialog.dismiss();
                }
                else {
                    if(result.equals("session_expired")) {
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(AvailableRoom.this, Login.class);
                        finish();
                        startActivity(i);
                    }else{
                        dialog.dismiss();
                        Toast.makeText(AvailableRoom.this,result,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public class ObjectAvailableRoom extends ArrayAdapter<RoomDetail> {
        AvailableRoom ctx;
        StringBuffer sb = new StringBuffer("");
        String token;
        private SendRequest mAuthTask = null;
        boolean status = false;
        public ObjectAvailableRoom(AvailableRoom context, int textViewResourceId, String token) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.token = token;
            attemptLogin();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.list_view_available_room, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            RoomDetail currentItem = (RoomDetail) getItem(position);
            viewHolder.Name.setText(currentItem.getRoomName());
            viewHolder.Location.setText(currentItem.getRoomAddress());
            viewHolder.Facilities.setText(currentItem.getRoomFacilities());
            return convertView;
        }

        private class ViewHolder {
            TextView Name;
            TextView Location;
            TextView Facilities;
            public ViewHolder(View view) {
                Name = (TextView)view.findViewById(R.id.room_name);
                Location = (TextView) view.findViewById(R.id.room_location);
                Facilities = (TextView) view.findViewById(R.id.room_facilities);
            }
        }
        public void attemptLogin() {
            boolean cancel = false;
            View focusView = null;
            if (cancel) {
                focusView.requestFocus();
            } else {
                mAuthTask = new SendRequest(token);
                mAuthTask.execute();
            }
        }
        public class SendRequest extends AsyncTask<String, Void, String> {
            private String token;
            SendRequest(String token) {
                this.token=token;
            }
            ProgressDialog dialog = new ProgressDialog(AvailableRoom.this);
            @Override
            protected void onPreExecute(){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                super.onPreExecute();
            }
            protected String doInBackground(String... arg0) {
                try{
                    URL url = new URL(postUrl+"/available_room");
                    JSONObject postDataParams = new JSONObject();
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
                                JSONArray result = obj.getJSONArray("result");
                                return result.toString();
                            } else if(!status) {
                                return obj.getString("msg");
                            } else {
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
                    return new String("Error connection");
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (status) {
                    loadArray(result);
                    dialog.dismiss();
                }
                else if (!status){
                    if(result.equals("session_expired")){
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(AvailableRoom.this, Login.class);
                        finish();
                        startActivity(i);
                    }else{
                        dialog.dismiss();
                        load_url.setVisibility(View.VISIBLE);
                    }
                }else if (result.equals("Error Connection")){
                    dialog.dismiss();
                    load_url.setVisibility(View.VISIBLE);
            }
            }
        }
        public void loadArray(String res){
            try {
                JSONArray all_room = new JSONArray(res);
                for (int idx=0; idx<all_room.length(); idx++) {
                    JSONObject room_detail = all_room.getJSONObject(idx);
                    String _name = room_detail.getString("room_name");
                    String _facilities = room_detail.getString("facilities");
                    String _location = room_detail.getString("location");
                    Log.d("ruangan", _name);
                    RoomDetail room = new RoomDetail();
                    room.setRoomFacilities(_facilities);
                    room.setRoomAddress(_location);
                    room.setRoomName(_name);
                    this.add(room);
                }
            } catch (Exception e) {
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