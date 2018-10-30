package com.example.karthik.misscall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {

    static boolean ring=false;
    static boolean callReceived=false;
    static String callerPhoneNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);


        if(state==null)
            return;

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {

            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");
//            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "Incoming call from " + phoneNumber + "\nusing other method "
                    + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), Toast.LENGTH_LONG).show();
            ring = true;
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            callReceived=true;
            Toast.makeText(context, "Call received ", Toast.LENGTH_LONG).show();
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            Toast.makeText(context, "Inside if " + ring + "," + callReceived  , Toast.LENGTH_LONG).show();
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if(ring == true && callReceived == false)
            {
                ring = false;
                Toast.makeText(context, "It was A MISSED CALL from : " + callerPhoneNumber + "--", Toast.LENGTH_LONG).show();
            }
            callReceived = false;
        }
    }
}
