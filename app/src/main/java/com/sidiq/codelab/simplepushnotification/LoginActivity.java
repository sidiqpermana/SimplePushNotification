package com.sidiq.codelab.simplepushnotification;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private AppPreference preference;
    private GoogleCloudMessaging googleCloudMessaging;
    private String GCM_PROJECT_ID = "807073670114";
    private int gcmIdCount = 0;
    private String gcmId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText)findViewById(R.id.edt_username);
        edtPassword = (EditText)findViewById(R.id.edt_password);
        btnLogin = (Button)findViewById(R.id.btn_login);

        preference = new AppPreference(LoginActivity.this);
        googleCloudMessaging = GoogleCloudMessaging.getInstance(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                new SendRequestToGcm(username, password).execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void toLoginActivity(Activity activity){
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    private class SendRequestToGcm extends AsyncTask<Void, Void, String> {
        private String username, password;
        private ProgressDialog dialog = null;

        public SendRequestToGcm(String username, String password){
            this.username = username;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle(getString(R.string.app_name));
            dialog.setMessage("Terhubung ke server...");
            dialog.show();
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String result = "";
            try {
                result = googleCloudMessaging.register(GCM_PROJECT_ID);
            } catch (Exception e) {
                // TODO: handle exception
                result = "";
                Log.d(LoginActivity.class.getSimpleName().toString(), e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == null || result.equals("")) {
                gcmIdCount++;
                if (gcmIdCount <= 3){
                    new SendRequestToGcm(username, password).execute();
                }else{
                    Toast.makeText(LoginActivity.this, "Gagal mendapatkan GCMID. Silakan coba lagi", Toast.LENGTH_LONG).show();
                }
            }else{
                gcmId = result;
                postLogin(username, password, gcmId);
            }
        }
    }

    private void postLogin(final String username, String password, String gcmId){
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage("Login...");
        dialog.show();

        RequestParams params = new RequestParams();
        params.put("email", username);
        params.put("password", password);
        params.put("gcmid", gcmId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(LoginActivity.this, ApiUrl.LOGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String response = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("status")==200){
                        AppPreference appPreference = new AppPreference(LoginActivity.this);
                        appPreference.setUsername(username);
                        MainActivity.toMainActivity(LoginActivity.this);
                        finish();
                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
            }
        });
    }
}
