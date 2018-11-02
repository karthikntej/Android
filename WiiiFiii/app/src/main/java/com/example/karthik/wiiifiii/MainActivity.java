package com.example.karthik.wiiifiii;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    WifiManager wifiManager;
    WiFiReceiver wiFiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();


        Button enableButton, disableButton;
        wiFiReceiver = new WiFiReceiver();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(wiFiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        enableButton = (Button) findViewById(R.id.enable);
        disableButton = (Button) findViewById(R.id.disable);



        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                wifiManager.startScan();

            }
        });

        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        unregisterReceiver(wiFiReceiver);
        super.onPause();

    }

    class WiFiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            List<ScanResult> wifiScanResult = wifiManager.getScanResults();
            Toast.makeText(context, "received", Toast.LENGTH_LONG).show();

        }
    }
}
