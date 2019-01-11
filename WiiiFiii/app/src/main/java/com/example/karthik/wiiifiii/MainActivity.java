package com.example.karthik.wiiifiii;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    WifiManager wifiManager;
    WiFiReceiver wiFiReceiver;
    String wifis[];
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();

        Switch switchWifi;

        wiFiReceiver = new WiFiReceiver();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(wiFiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        wifiManager.startScan();

        list = (ListView) findViewById(R.id.list);
        switchWifi = (Switch) findViewById(R.id.switchWifi);

        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    wifiManager.startScan();
                } else {
                    wifiManager.setWifiEnabled(false);
                }

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

            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            Toast.makeText(context, "received", Toast.LENGTH_LONG).show();
            Log.d("listOfWifi", "before " + wifis.length);
            wifis = new String[wifiScanList.size()];
            for(int i = 0; i < wifiScanList.size(); i++){
                wifis[i] = ((wifiScanList.get(i)).toString());
            }

            Log.d("listOfWifi", "after " + wifis.length);
            String filtered[] = new String[wifiScanList.size()];
            int counter = 0;
//            for (String eachWifi : wifis) {
//                String[] temp = eachWifi.split(",");
//
//                filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
//
//                counter++;
//
//            }

            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.list, filtered));
        }
    }
}
