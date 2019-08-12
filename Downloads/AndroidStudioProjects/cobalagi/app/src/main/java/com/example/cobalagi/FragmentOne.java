package com.example.cobalagi;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import android.widget.Toast;
//

public class FragmentOne extends Fragment {

//    SendMessage SM;
    String data ="";
    String dataParsed1 = "";
    String dataParsed2 = "";
    String dataParsed3 = "";
    //    String singleParsed ="";
    String kode = "";
    String name = "";
    String price = "";
    public static TextView kodeodoo, nama, harga;

    String[] kodeodooArray = {"00001", "00002", "00003", "00004", "00005", "00006", "00007", "00008",
            "00001", "00002", "00003", "00004", "00005", "00006", "00007", "00008", "00001", "00002",
            "00003", "00004", "00005", "00006", "00007", "00008"};
    String[] namaprodukArray = {"Wardah Refill Instaperfect Mineralight Matte BB Cushion 14 Creme, 15 gr (NEW)", "Wardah Lipstik, 20 gr",
            "Wardah Lip Cream, 40 ml", "Wardah Lipstik, 40 gr", "Wardah Lip Cream, 60 ml",
            "Wardah Lipstik, 60 gr", "Wardah Lip Cream, 80 ml", "Wardah Lipstik, 80 gr",
            "Wardah Lip Cream, 20 ml", "Wardah Lipstik, 20 gr", "Wardah Lip Cream, 40 ml",
            "Wardah Lipstik, 40 gr", "Wardah Lip Cream, 60 ml", "Wardah Lipstik, 60 gr",
            "Wardah Lip Cream, 80 ml", "Wardah Lipstik, 80 gr", "Wardah Lipstik, 60 gr",
            "Wardah Lip Cream, 80 ml", "Wardah Lipstik, 80 gr", "Wardah Lipstik, 60 gr",
            "Wardah Lip Cream, 80 ml", "Wardah Lipstik, 80 gr", "Wardah Lipstik, 60 gr",
            "Wardah Lip Cream, 80 ml"};
    String[] hargaArray = {"135.000", "115.000", "25.000", "35.000", "45.000", "55.000", "65.000",
            "75.000", "85.000", "95.000", "105.000", "115.000", "125.000", "15.000", "15.000", "15.000",
            "85.000", "95.000", "105.000", "115.000", "125.000", "15.000", "15.000", "15.000"};
    String[] stockArray = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    String[] qtyArray = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

    ArrayList<String> orderedODOO = new ArrayList<String>();
    ArrayList<String> orderedname = new ArrayList<String>();
    ArrayList<String> orderedprice = new ArrayList<String>();
    ArrayList<String> orderedstock = new ArrayList<String>();
    ArrayList<String> orderedqty = new ArrayList<String>();

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    TextView formOdoo, formNama, formHarga;
    EditText formStock, formQty;

    ListView listView;
    SendMessage SM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_one, container, false);
        kodeodoo = (TextView) rootView.findViewById(R.id.kode_odoo);
        nama = (TextView) rootView.findViewById(R.id.nama_produk);
        harga = (TextView) rootView.findViewById(R.id.harga);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final OrderArrayAdapter OAA = new OrderArrayAdapter(getActivity(), kodeodooArray, namaprodukArray,
                hargaArray, stockArray, qtyArray);

        listView = (ListView) view.findViewById(R.id.historical_listview_id);
        listView.setAdapter(OAA);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog = new AlertDialog.Builder(getActivity());
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.form_input, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setTitle("Input Order");

                formOdoo = (TextView) dialogView.findViewById(R.id.kode_odoo_form);
                formOdoo.setText(kodeodooArray[position]);
                formNama = (TextView) dialogView.findViewById(R.id.nama_produk_form);
                formNama.setText(namaprodukArray[position]);
                formHarga = (TextView) dialogView.findViewById(R.id.harga_form);
                formHarga.setText(hargaArray[position]);

                formStock = (EditText) dialogView.findViewById(R.id.stock_form);
                formQty = (EditText) dialogView.findViewById(R.id.qty_form);

                formStock.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String stockform = formStock.getText().toString();

                        if (!stockform.isEmpty()) {
                            int intstockform = Integer.parseInt(stockform);
                            int qtyform = intstockform * 3 - 2;
                            if (qtyform >= 0) {
                                formQty.setHint(String.valueOf(qtyform));
                            } else {
                                formQty.setHint("0");
                            }
                        } else {
                            formQty.setHint("0");
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                dialog.setPositiveButton("ORDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mStock = formStock.getText().toString();
                        if (mStock.isEmpty()) {
                            formStock.setError("field is empty");
//                            formStock.requestFocus();
                            Toast.makeText(getActivity(), "Mohon isi stock sebelum isi jumlah yang di order", Toast.LENGTH_LONG).show();
                        } else {
                            orderedODOO.add(formOdoo.getText().toString());
                            orderedname.add(formNama.getText().toString());
                            orderedprice.add(formHarga.getText().toString());
                            orderedstock.add(formStock.getText().toString());
                            orderedqty.add(formQty.getText().toString());

                            dialog.dismiss();

                            stockArray[position] = formStock.getText().toString();
                            qtyArray[position] = formQty.getText().toString();

                            OAA.notifyDataSetChanged();

                            Toast.makeText(getActivity(),"Stock : " + stockArray[position] + ", Qty : " + qtyArray[position], Toast.LENGTH_LONG).show();
                            SM.sendData(orderedODOO, orderedname, orderedprice, orderedstock, orderedqty);
                        }


                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    interface SendMessage {
        void sendData(ArrayList<String> kirimKodeOdoo, ArrayList<String> kirimNamaProduk,
                      ArrayList<String> kirimHargaProduk, ArrayList<String> kirimStock,
                      ArrayList<String> kirimQty);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

//    @Override
//    protected Void doInBackground(Void... voids) {
//        try {
//            URL url = new URL("http://10.3.181.198:3000/product?brand=ilike.*wardah");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            InputStream inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line = "";
//            while(line != null){
//                line = bufferedReader.readLine();
//                data = data + line;
//                name = name + line;
//            }
//
//            JSONArray JA = new JSONArray(data);
//            for(int i =0 ;i <JA.length(); i++){
//                JSONObject JO = (JSONObject) JA.get(i);
//                kode = "" + JO.get("default_code") + "\n";
//                name = "" + JO.get("name") + "\n";
//                price = "" + JO.get("gt_batam") + "\n";
//
//                dataParsed1 = dataParsed1 + kode +"\n" ;
//                dataParsed2 = dataParsed2 + name +"\n" ;
//                dataParsed3 = dataParsed3 + price +"\n" ;
//            }
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//
//        MainActivity.kodeodoo.setText(this.dataParsed1);
//        FragmentOne.nama.setText(this.dataParsed2);
//        FragmentOne.harga.setText(this.dataParsed3);
////        MainActivity.nama.setText(this.dataParsed);
//
//    }
}

//public class FetchData extends AsyncTask<Void,Void,Void> {
//    String data ="";
//    String dataParsed1 = "";
//    String dataParsed2 = "";
//    String dataParsed3 = "";
//    //    String singleParsed ="";
//    String kode = "";
//    String name = "";
//    String price = "";
//    @Override
//    protected Void doInBackground(Void... voids) {
//        try {
//            URL url = new URL("http://10.3.181.198:3000/product?brand=ilike.*wardah");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            InputStream inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line = "";
//            while(line != null){
//                line = bufferedReader.readLine();
//                data = data + line;
//                name = name + line;
//            }
//
//            JSONArray JA = new JSONArray(data);
//            for(int i =0 ;i <JA.length(); i++){
//                JSONObject JO = (JSONObject) JA.get(i);
//                kode = "" + JO.get("default_code") + "\n";
//                name = "" + JO.get("name") + "\n";
//                price = "" + JO.get("gt_batam") + "\n";
//
//                dataParsed1 = dataParsed1 + kode +"\n" ;
//                dataParsed2 = dataParsed2 + name +"\n" ;
//                dataParsed3 = dataParsed3 + price +"\n" ;
//            }
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//
//        FragmentOne.kodeodoo.setText(this.dataParsed1);
//        FragmentOne.nama.setText(this.dataParsed2);
//        FragmentOne.harga.setText(this.dataParsed3);
////        MainActivity.nama.setText(this.dataParsed);
//
//    }
//}


