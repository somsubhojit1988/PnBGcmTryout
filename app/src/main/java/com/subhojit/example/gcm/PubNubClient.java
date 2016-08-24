package com.subhojit.example.gcm;

import android.content.Context;
import android.util.Log;



import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by subhojitsom on 23/8/16.
 */
public class PubNubClient {
    PubNub pClient;
    Context mCtx;
    private final String TAG = "PN_Client";
    private String deviceId;
    PubNubClient(Context ctx,String token){
        this.mCtx = ctx;
        this.deviceId = token;
        Log.d(TAG,"Creating PubNub Client Instance with devID: "+this.deviceId);
        PNConfiguration configuration = new PNConfiguration();
        configuration.setPublishKey(ctx.getString(R.string.PNB_PUBKEY));
        configuration.setSubscribeKey(ctx.getString(R.string.PNB_SUBKEY));
        pClient = new PubNub(configuration);
        subscribeForPushNotification();
        SubscribeCallback callback = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                Log.d(TAG,"Subscribe status: "+status.getStatusCode());
                if(status.getCategory()==PNStatusCategory.PNConnectedCategory){
                    Log.d(TAG,"Subscribe status Connected!! code: "+status.getStatusCode());
                    /*sendPushNotification();*/
                }else
                    Log.e(TAG,"Subscribe  not connected status");
            }
            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Log.d(TAG," ---- \n message: \n"+ message.getMessage() +"\n ---- \n ");
            }
            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };
        this.pClient.addListener(callback);
        pClient.subscribe().channels( Arrays.asList( mCtx.getString(R.string.PN_PUSH_CHANNEL)))
         .execute();
    }
    private void subscribeForPushNotification(){
        pClient.addPushNotificationsOnChannels()
                .pushType(PNPushType.GCM)
                .channels(Arrays.asList(this.mCtx.getString(R.string.PN_PUSH_CHANNEL)))
                .deviceId(this.deviceId)
                .async(new PNCallback<PNPushAddChannelResult>(){
            @Override
            public void onResponse(PNPushAddChannelResult result, PNStatus status){
                // handle response.
                Log.d(TAG,"Push notification subscription status: " +status.getCategory());


            }
        });
    }
    public void sendPushNotification(){
        JSONObject google_payload = null;
        try {
            google_payload=new JSONObject().put("data","Data for GCM subscribers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"Will publish: \n"+google_payload+"\n ______");

        Map<String, Object> payload = new HashMap<>();
        payload.put("pn_gcm",google_payload);
        pClient.publish()
        .message(payload)
        .channel(this.mCtx.getString(R.string.PN_PUSH_CHANNEL))
        .async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                /// handle publish result.
                Log.d("PNPublisher","Push notification publish"+ " Status.code: "+status.getStatusCode());
            }
        });
    }
}
