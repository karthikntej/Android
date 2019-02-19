package com.example.karthik.recordaudio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    File PlayFolder;
    File RecordFolder;
    ListView listView;
    ListView responselistView;

    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> responseList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> responseAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.record);
        buttonStop = (Button) findViewById(R.id.stop);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.play);
        buttonStopPlayingRecording = (Button)findViewById(R.id.stopPlaying);
        listView = (ListView) findViewById(R.id.recordList);
        responselistView = (ListView) findViewById(R.id.responseList);
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(true);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        PlayFolder = new File (Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + "PlayFolder") ;
        if (!PlayFolder.exists()) {
            PlayFolder.mkdir();
        }

        RecordFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + "RecordFolder") ;
        if (!PlayFolder.exists()) {
            PlayFolder.mkdir();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

        responseAdapter = new ArrayAdapter<>(this, R.layout.list_item, responseList);
        responselistView.setAdapter(responseAdapter);

        displayAllFiles();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {
                    String newFileName;

                    newFileName = CreateRandomAudioFileName(5)  + "AudioRecording.mp3";
                    list.add(newFileName);

                    AudioSavePathInDevice = PlayFolder.getAbsolutePath() + "/" +
                                    newFileName ;

                    MediaRecorderReady();

                    try {

                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(MainActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(MainActivity.this, "Recording Stopped",
                        Toast.LENGTH_LONG).show();

                adapter.notifyDataSetChanged();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                Intent forMusic = new Intent(getBaseContext(), PlayMusic.class);
                startService(forMusic);
                Toast.makeText(MainActivity.this, "Started Playing in Background",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);



                stopService(new Intent(getBaseContext(), PlayMusic.class));

                File[] files = RecordFolder.listFiles();

                Log.d("check", ""+files.length);
                for (int i = 0; i < files.length; i++)
                {
                    Log.d("Files", "FileName:" + files[i].getName());
                    responseList.add(files[i].getName());
                }

                responseAdapter.notifyDataSetChanged();


                Toast.makeText(MainActivity.this, "Stopped Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String recordName = PlayFolder.getAbsolutePath() + "/" +
                        ((TextView)view).getText().toString();

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);


                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(recordName);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();



            }
        });

        responselistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String recordName = RecordFolder.getAbsolutePath() + "/" +
                        ((TextView)view).getText().toString();

                Log.d("check", "playing the respoinse");


                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(recordName);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();



            }
        });




    }

    private void displayAllFiles() {


        File[] files = PlayFolder.listFiles();

        Log.d("check", ""+files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
            list.add(files[i].getName());
        }

        adapter.notifyDataSetChanged();

    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
        mediaRecorder.setMaxDuration(5000);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

}