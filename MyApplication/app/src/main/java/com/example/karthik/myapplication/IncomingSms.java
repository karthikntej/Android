package com.example.karthik.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        String senderNum = "";
        String message = "";
        String date[];
        Detail detail = new Detail();

        Toast toast = Toast.makeText(context, "received", Toast.LENGTH_LONG);
        toast.show();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

        try {
            DatabaseHandler db = new DatabaseHandler(context);

            if (message.indexOf("was spent on ur HDFCBank CREDIT Card ending") > 0 ) {
                detail.amount = message.split(" was")[0].split("Rs.")[1];
                detail.place  = message.split(" at ")[1].split(".Avl")[0];
                date = message.split(" on ")[2].split(" at ")[0].split("-");
                detail.year  = Integer.parseInt(date[0]) ;
                detail.month = Integer.parseInt (date[1])-1;
                detail.day   = Integer.parseInt(date[2].split(":")[0]);
                detail.exactTime = date[2].split(":", 2)[1];


//                db.addEntry(context, detail);

                Log.d("testmsg", "added to db with month->" + detail.month + "<-") ;
            } else if (message.indexOf(" was withdrawn using your HDFC Bank Card ending ") > 0) {
                    detail.amount = message.split(" was")[0].split("Rs.")[1];
                    detail.place  = message.split(" at ")[1].split(". Avl")[0];
                    date = message.split(" on ")[1].split(" at ")[0].split("-");
                    detail.year  = Integer.parseInt(date[0]) ;
                    detail.month = Integer.parseInt (date[1])-1;
                    detail.day   = Integer.parseInt(date[2].split(":")[0]);
                    detail.exactTime = date[2].split(":", 2)[1];

//                    db.addEntry(context, detail);
            } else if (message.indexOf("Thank you for using Debit Card ending ") >= 0) {
                    detail.amount = message.split(" Rs.")[1].split(" in")[0];
                    detail.place  = message.split(" at ")[1].split(" on ")[0];
                    date = message.split(" on ")[1].split(" Avl bal:")[0].split("-");
                    detail.year  = Integer.parseInt(date[0]) ;
                    detail.month = Integer.parseInt (date[1])-1;
                    detail.day   = Integer.parseInt(date[2].split(":")[0]);
                    detail.exactTime = date[2].split(":", 2)[1];


                }
            db.addEntry(context, detail);

        } catch (Exception e) {
            Log.d("test", "GOT EXCEPTION!!!!!!!");
        }

    }
}
