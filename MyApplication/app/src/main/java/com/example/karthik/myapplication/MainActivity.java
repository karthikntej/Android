package com.example.karthik.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Detail> details;
    public static DatabaseHandler db;
    List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);


        db = new DatabaseHandler(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        List summery;
        summery = db.getMonths();

        int i ;
        for (i = 0; i < summery.size(); i++) {

            summery.add(i, months.get(Integer.parseInt( summery.get(i).toString())));
            summery.remove(i+1);

        }

        ArrayAdapter adapter = new ArrayAdapter <Detail>(this, R.layout.listview, summery);
        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String m = ((TextView)view).getText().toString();
                int num = months.indexOf(m);

                Intent summary = new Intent(getApplicationContext(), Summary.class);

                summary.putExtra("month", num);
                startActivity(summary);

            }
        });

    }
}