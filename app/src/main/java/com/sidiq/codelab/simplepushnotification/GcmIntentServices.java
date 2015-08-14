package com.sidiq.codelab.simplepushnotification;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentServices extends IntentService 
{
    public static final String TAG = "GcmIntentService";
    public static final int NOTIFICATION_ID = 1;

    public GcmIntentServices(){
        super("IntelTrackerApp");
    }

/* (non-Javadoc)
 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
 */
@Override
protected void onHandleIntent(Intent intent)
    {
        // TODO Auto-generated method stub

        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        Log.i("IntelTrackerApp" , "Message Received: " + extras.getString("message"));
        
        if(!extras.isEmpty())
            {   
                if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
                    
                }else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)){
                
                }
                else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
                	//CommonUtilities.displayMessage(getApplicationContext(), extras.getString("message"));
                	generateNotification(getApplicationContext(), extras.getString("message"));
                }
            }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

	@SuppressLint("NewApi")
	private void generateNotification(Context context, String message) {
	    int icon = R.mipmap.ic_launcher;
	    long when = System.currentTimeMillis();
	    
	    //Log.d(Constant.APP_TAG, message);
	    
	    NotificationManager notificationManager = (NotificationManager)
	            context.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    String type = null, msg = null, title = null, sender = null;

		if (message != null){
			String[] psn = message.split("#");
			sender = psn[0];
			type = psn[1];
			title = psn[2];
			msg = psn[3];


			@SuppressWarnings("deprecation")
			Notification notification = new Notification(icon, msg, when);
			//Utils.printLog(message);

			if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
				notification.color = Color.parseColor("#143a99");
			}

			Intent notificationIntent = new Intent(context, MainActivity.class);

			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intent =
					PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
			notification.setLatestEventInfo(context, title, msg, intent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			// Play default notification sound
			notification.defaults |= Notification.DEFAULT_SOUND;

			// Vibrate if vibrate is enabled
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify((int)System.currentTimeMillis(), notification);
		}
	}
}
