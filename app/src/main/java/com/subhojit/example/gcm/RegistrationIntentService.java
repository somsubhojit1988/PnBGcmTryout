package com.subhojit.example.gcm;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by subhojitsom on 22/8/16.
 */
public class RegistrationIntentService extends IntentService{

    private final String TAG = "RegistrationService";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public RegistrationIntentService() {
        super("");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent>>");
        Intent regRes = null;
        try {
            String token = InstanceID.getInstance(this).getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
            Log.d(TAG,"RegistrationIntentService TOKEN: "+token);
            regRes = new Intent(REGISTRATION_SUCCESS);
            regRes.putExtra("deviceToken",token);
            //Send the token to application server
        } catch (IOException e) {
            Log.e(TAG,"RegistrationIntentService ERROR!!");
            regRes = new Intent(REGISTRATION_ERROR);
            e.printStackTrace();
        }
        if( LocalBroadcastManager.getInstance(this)!=null)
            LocalBroadcastManager.getInstance(this).sendBroadcast(regRes);
        else
            Log.e("RegtService","Cannot recover LocalBroadcastManager");
    }
}
