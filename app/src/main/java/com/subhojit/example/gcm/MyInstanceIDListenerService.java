package com.subhojit.example.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

/**
 * Created by subhojitsom on 22/8/16.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService{

    private String TAG = "InstIDListenServ";
    @Override  public void onTokenRefresh() {
        Log.d(TAG,"Will be refreshing from IstanceIDListener");
        Intent intent = new Intent(this,RegistrationIntentService.class);

        startService(intent);
    }
}
