package com.subhojit.example.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver","Registration status"+intent.getAction());

            //Create Pub Nub instance
            if(intent.getAction().equals(RegistrationIntentService.REGISTRATION_SUCCESS)){
                String token = intent.getStringExtra("deviceToken");
                pnbClient = new PubNubClient(getApplicationContext(),token);
            }
        }
    };

    private PubNubClient pnbClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("MainActivity", "onResume");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Check for Googleplay services status code
        GoogleApiAvailability gApi= GoogleApiAvailability.getInstance();
        int rCode = gApi.isGooglePlayServicesAvailable(getApplicationContext());
        if(rCode== ConnectionResult.SUCCESS){
            Intent intent = new Intent(this,RegistrationIntentService.class);
            startService(intent);
        }else
            Log.e("MainActivity","GooglePlay service is unavailable..!!");

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_ERROR));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
