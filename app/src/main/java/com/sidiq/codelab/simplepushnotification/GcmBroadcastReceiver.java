package com.sidiq.codelab.simplepushnotification;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver 
{

    @Override
    public void onReceive(Context context, Intent intent) 
        {
            // TODO Auto-generated method stub

            ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentServices.class.getName());
            Log.i("SimpleGCM", "Begin Broadcast");

            startWakefulService(context, intent.setComponent(comp));
        }
}
