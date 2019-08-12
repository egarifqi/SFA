package com.abhishekpanwar.receivedatajson;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Abhishek Panwar on 7/14/2017.
 */

public class fetchData extends AsyncTask<Void,Void,Void> {
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    String kodeodoo = "";
    String name = "";
    String harga = "";
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://10.3.181.198:3000/product?brand=ilike.*wardah");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
                name = name + line;
            }

            JSONArray JA = new JSONArray(data);
            for(int i =0 ;i <JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
//                singleParsed =  "Name:" + JO.get("name") + "\n"+
//                                "Password:" + JO.get("default_code") + "\n"+
//                                "Contact:" + JO.get("barcode") + "\n"+
//                                "Country:" + JO.get("gt_batam") + "\n";

                kodeodoo = (String) JO.get("default_code");

                dataParsed = dataParsed + kodeodoo +"\n" ;
            }

            JSONArray JB = new JSONArray(name);
            for(int i =0 ;i <JB.length(); i++){
                JSONObject JO = (JSONObject) JB.get(i);
                name = (String) JO.get("name");
                dataParsed = dataParsed + name +"\n" ;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.data.setText(this.dataParsed);
        MainActivity.nama.setText(this.dataParsed);

    }
}
