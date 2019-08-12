package ppla01.book_room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import javax.net.ssl.HttpsURLConnection;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by isramela on 01/03/18.
 */

public class Login extends Activity {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    SharedPreferences url;
    SharedPreferences.Editor edit_url;

    String postUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        url= getApplicationContext().getSharedPreferences("MyUrl", 0);
        edit_url=url.edit();
        edit_url.putString("url","http://10.3.181.198:3000/");
        edit_url.commit();
        postUrl=url.getString("url","");
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                return true;
            }
            return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.button_login);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                }else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                }if (cancel) {
                    focusView.requestFocus();
                }else {
                    String user = enkrip(email,100);
                    String kata_sandi = user+""+enkrip(password,100);
                    PostLogin search = new PostLogin(Login.this, -1, kata_sandi,postUrl,email);
                }
            }
        });
    }

    public  String enkrip(String password, int keyLength){
        String encrypted="";
        for(int i=0;i<password.length();i++)
        {
            int c=password.charAt(i);
            if(Character.isUpperCase(c))
            {
                c=c+(keyLength%26);
                if(c>'Z')
                    c=c-26;
            }
            else if(Character.isLowerCase(c))
            {
                c=c+(keyLength%26);
                if(c>'z')
                    c=c-26;
            }
            encrypted=encrypted+(char) c;
        }
        return encrypted;

    }
    public class PostLogin extends ArrayAdapter<DetailMeeting> {
        Context ctx;
        StringBuffer sb = new StringBuffer("");
        private SendRequest mAuthTask = null;
        String  password,new_url,email;
        boolean status = false;
        public PostLogin (Context context, int textViewResourceId,String password,String new_url,String email) {
            super(context,textViewResourceId);
            this.ctx = context;
            this.password=password;
            this.email = email;
            this.new_url=new_url;
            attemptLogin();
        }
        public class SendRequest extends AsyncTask<String, Void, String> {
            private final String mPassword;
            ProgressDialog dialog = new ProgressDialog(Login.this);
            JSONObject obj;
            @Override
            protected void onPreExecute(){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                super.onPreExecute();
            }
            SendRequest(String password) {
                mPassword = password;
            }
            protected String doInBackground(String... arg0) {
                try{
                    URL url = new URL(new_url+"/login");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("authentication", mPassword);
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
                        obj = new JSONObject(sb.toString());
                        try{
                            status = obj.getBoolean("status");
                            if (status) {
                                Intent i = new Intent(Login.this,MainActivity.class);
                                finish();
                                startActivity(i);
                            } else if(!status) {
                                return obj.getString("msg");
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
                    return new String("Error Connection");
                }
            }
            @Override
            protected void onPostExecute(String result) {
                if (status) {
                    try{
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email",email);
                        editor.putString("account",obj.getString("display_name"));
                        editor.putString("area_lokasi",obj.getString("areas"));
                        editor.putString("token", obj.getString("token"));
                        edit_url.putString("url",new_url);
                        editor.commit();
                        edit_url.commit();
                        dialog.dismiss();
                    }catch(Exception e){
                    }
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(getApplication(),result, Toast.LENGTH_SHORT).show();
                }
            }
        }
        public void attemptLogin() {
                mAuthTask = new SendRequest( password);
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
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
