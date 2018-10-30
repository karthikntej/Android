package com.example.karthik.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.karthik.myapplication.MainActivity.db;

public class Summary extends AppCompatActivity {

    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        num = getIntent().getIntExtra("month", 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("testmsg", "In Summmmmary size is " + num) ;
        List<Detail> details = db.getAllDetails(num);
        List transactions = new ArrayList();


        float total = 0;

        for (int i = 0; i < details.size(); i++) {
            Detail detail = details.get(i);

            transactions.add("Rs." + detail.amount + " spent at " + detail.place + " on " +
                    detail.day +"/"+ (detail.month + 1) + "/" + detail.year + " at " + detail.exactTime );

            total += Float.parseFloat(detail.amount);
        }

        TextView total_view = (TextView) findViewById(R.id.totalTextView);
        total_view.setText("Total: " + total);
        ListView summaryList = (ListView)findViewById(R.id.summary_list);
        ArrayAdapter adapter = new ArrayAdapter <String>(this, R.layout.listview, transactions);
        summaryList.setAdapter(adapter);


    }
}
