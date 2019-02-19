package com.example.karthik.recordaudio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PlayMusic extends Service {

    Thread thread;
    boolean running = true;
    File[] files;
    File PlayFolder,RecordFolder;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    String AudioSavePathInDevice = null;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    Random random ;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        list = intent.getStringArrayListExtra("key");
//        Log.d("check", "Before background " + list);

        doBackGroundWork();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
        Log.d("check", "onDestroy");
    }




    private void doBackGroundWork() {
        Log.d("check", "doBackGroundWork");
        PlayFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + "PlayFolder") ;

        RecordFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + "RecordFolder") ;

        if (!RecordFolder.exists()) {
            RecordFolder.mkdir();
        }
        files = PlayFolder.listFiles();
        random = new Random();



        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                int amplitude, j, period;

                for (int i = 0; i < files.length && running; i++) {
                    Log.d("check", "song "+ files[i]);
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(files[i].toString());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    try {
                           Thread.sleep(mediaPlayer.getDuration());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                    recordAudio();

//                    mediaRecorder.stop();

                    j = 0;
                    period = 8;
                    try {

                        while (j < period){
                            Thread.sleep(1000);
                            amplitude = mediaRecorder.getMaxAmplitude();
                            Log.d("check", "amplitude: " + amplitude);

                            if (amplitude < 2000) {
                                j++;
                                Log.d("check", "No response yet");
                                continue;
                            }

                            j = 0;
                            period = 2;

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mediaRecorder.stop();

                }
                stopSelf();
            }


        });
        thread.start();

    }

    private void recordAudio() {

        String newFileName;

        newFileName = CreateRandomAudioFileName(5)  + "AudioRecording.mp3";
        AudioSavePathInDevice = RecordFolder.getAbsolutePath() + "/" +
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


    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
//        mediaRecorder.setMaxDuration(time);
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


}
