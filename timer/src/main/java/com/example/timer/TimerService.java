package com.example.timer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.TextView;




public class TimerService extends Service{
    private int z_Stunde;
    private int z_Minute;
    private int z_Sekunde;
    private int z_Zeit;
    private boolean isRunning;

    private TextView a_anzeige;

    private CountDownTimer timer;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void run(int stunde, int minute, int sekunde, final TextView anzeige)
    {
        z_Stunde = stunde;
        z_Minute = minute;
        z_Sekunde = sekunde;
        a_anzeige = anzeige;


        z_Zeit = 3600 * z_Stunde + 60 * z_Minute + z_Sekunde;

        timer = new CountDownTimer(z_Zeit*1000, 1000)
        {

            @Override
            public void onFinish() {
                a_anzeige.setText(String.format("%02d:%02d:%02d", 0,0,0));
                isRunning = false;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                a_anzeige.setText(String.format("%02d:%02d:%02d", (millisUntilFinished / (1000*3600)),
                        (millisUntilFinished / (1000*60)) % 60,
                        (millisUntilFinished / 1000) % 60));

            }
        }.start();


    }

    public boolean isRunning() {
        return isRunning;
    }


}
