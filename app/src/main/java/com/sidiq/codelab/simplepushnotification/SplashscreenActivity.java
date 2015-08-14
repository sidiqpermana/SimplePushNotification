package com.sidiq.codelab.simplepushnotification;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        new DelayAsync().execute();
    }

    class DelayAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Thread.sleep(4000);
            }catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppPreference appPreference = new AppPreference(SplashscreenActivity.this);
            if (appPreference.getUsername().equals("")){
                LoginActivity.toLoginActivity(SplashscreenActivity.this);
                finish();
            }else{
                MainActivity.toMainActivity(SplashscreenActivity.this);
                finish();
            }
        }
    }
}
