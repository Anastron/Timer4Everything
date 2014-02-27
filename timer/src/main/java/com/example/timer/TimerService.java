package com.example.timer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by benza on 27.02.14.
 */
public class TimerService {

    static CountDownTimer timer;
    public static boolean isRunning;
    static String widgetString;

    public int onStartCommand(Intent intent, int flags, int startID)
    {
        return Service.START_REDELIVER_INTENT;
    }

    public void run (int min, final int time, final MediaPlayer mediaPlayer, final TextView satz, final Button btnAction)
    {
        timer = new CountDownTimer(min*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;

                satz.setText(String.format("%02d:%02d",
                        (millisUntilFinished / (1000 * 60)) % 60,
                        (millisUntilFinished / 1000) % 60));

                widgetString = String.format("%02d:%02d",
                        (millisUntilFinished / (1000 * 60)) % 60,
                        (millisUntilFinished / 1000) % 60);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                isRunning = false;
                satz.setText(String.format("%02d:%02d",00, 00));
                btnAction.setEnabled(true);
                mediaPlayer.start();
                if (time == 0)
                {
                 //   btnAction.setText(getString(R.string.txt_back_finish));
                 //   satz.setText(getString(R.string.txt_finish));
                }

            }

        }.start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
