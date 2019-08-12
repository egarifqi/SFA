package bookingcar.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by isramela on 21/06/18.
 */

public class FragmentListBooking extends Fragment {

    private ObjectMyRoom cReaderRoom;
    SharedPreferences spref;
    SharedPreferences.Editor editor;
    private String token,idCar,link,name,plate;
    private ListView mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_booking,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spref = getContext().getSharedPreferences("MyPref", 0);
        editor = spref.edit();
        token = spref.getString("token", "");
        link = spref.getString("url","");
        cReaderRoom = new ObjectMyRoom(getActivity(),-1,token);
        mList = (ListView)view.findViewById(R.id.mList);
        mList.setAdapter(cReaderRoom);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (cReaderRoom.getItem(position).getId() != null) {
                    idCar = cReaderRoom.getItem(position).getId();
                    name = cReaderRoom.getItem(position).getType();
                    plate = cReaderRoom.getItem(position).getLicense_plate();
                    showDialog();
                }
            }
        });
    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Cancel Booking Car");
        alertDialogBuilder
                .setMessage("Apakah anda yakin membatalkan mobil "+name+" "+plate+" ?")
                .setIcon(R.drawable.splash)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        attemptLogin2(token,idCar);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void attemptLogin2(String token, String idCar){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();
        final String url = link+"booking/"+token+"/"+idCar;
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        dialog.dismiss();
                        Intent i = new Intent(getActivity(), CancelBooking.class);
                        getActivity().finish();
                        startActivity(i);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            VolleyError eror = new VolleyError(new String(error.networkResponse.data));
                            error = eror;
                            String err_status,err_msg;
                            try{
                                JSONObject err = new JSONObject(error.getMessage());
                                err_status = err.getString("status");
                                err_msg = err.getString("msg");
                            } catch ( Exception er) {
                                err_status = error.getMessage();
                                err_msg= error.getMessage();
                            }
                            Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }catch (Exception e){
                            dialog.dismiss();
                            Toast.makeText(getActivity(),"Koneksi Eror",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        queue.add(dr);
    }

    public class ObjectMyRoom extends ArrayAdapter<BookingCar> {
        Context ctx;
        String token;
        boolean status = false;
        public ObjectMyRoom(Context context, int textViewResourceId,String token) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.token=token;
            attemptLogin(token);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.list_car, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            BookingCar currentItem = (BookingCar) getItem(position);
            viewHolder.Name.setText(currentItem.getType());
            viewHolder.Plate.setText(currentItem.getLicense_plate());
            viewHolder.Location.setText(currentItem.getLocation());
            viewHolder.Trip.setText(currentItem.getPickup_time());
            UrlImageViewHelper.setUrlDrawable(viewHolder.Image,currentItem.getImage());
            return convertView;
        }
        private class ViewHolder {
            TextView Name;
            TextView Plate;
            TextView Location;
            TextView Trip;
            ImageView Image;
            public ViewHolder(View view) {
                Name = (TextView)view.findViewById(R.id.name);
                Plate = (TextView) view.findViewById(R.id.plate);
                Location = (TextView) view.findViewById(R.id.location);
                Trip = (TextView) view.findViewById(R.id.trip);
                Image = (ImageView) view.findViewById(R.id.img);
            }
        }
        public void attemptLogin(String token){
            final RequestQueue queue = Volley.newRequestQueue(getActivity());
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
            final String url = link+"booking/"+token;

            // prepare the Request
            StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONArray obj = new JSONArray(response);
                                loadArray(obj.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            try{
                                VolleyError eror = new VolleyError(new String(error.networkResponse.data));
                                error = eror;
                                String err_msg;
                                try{
                                    JSONObject err = new JSONObject(error.getMessage());
                                    err_msg = err.getString("msg");
                                } catch ( Exception er) {
                                    err_msg= error.getMessage();
                                }
                                Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }catch (Exception e){
                                dialog.dismiss();
                                Toast.makeText(getActivity(),"Error connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
            );

            // add it to the RequestQueue
            queue.add(getRequest);

        }
        public void loadArray(String res){
            try {
                JSONArray car = new JSONArray(res);
                if (car.length()==0){
                }
                else{
                    for (int idx=0; idx<car.length(); idx++) {
                        JSONObject car_detail = car.getJSONObject(idx);
                        String id = car_detail.getString("_id");
                        String type = car_detail.getString("type");
                        String plate = car_detail.getString("license_plate");
                        String location = car_detail.getString("location");
                        String pickup = car_detail.getString("pickup_time");
                        String image = car_detail.getString("img_filename");

                        BookingCar mobil = new BookingCar();
                        mobil.setId(id);
                        mobil.setType(type);
                        mobil.setLicense_plate(plate);
                        mobil.setLocation(location);
                        mobil.setPickup_time(pickup);
                        mobil.setImage(link+"cars/image/"+image);
                        this.add(mobil);
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
