package timerpack.timer;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.TextView;




public class TimerService extends Service{
    private int z_Stunde;
    private int z_Minute;
    private int z_Sekunde;
    private int z_Zeit;
    private long safeTime;
    private boolean isRunning;
    private boolean isStop;

    private TextView a_anzeige;

    private CountDownTimer timer;

    private Ringtone _ringtone;

     @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void run(int stunde, int minute, int sekunde, final TextView anzeige, final Ringtone ringtone)
    {
        z_Stunde = stunde;
        z_Minute = minute;
        z_Sekunde = sekunde;
        a_anzeige = anzeige;
        safeTime = 0;

        _ringtone = ringtone;
        isStop = false;


        if (_ringtone.isPlaying())
        {
            _ringtone.stop();
        }

        z_Zeit = 3600 * z_Stunde + 60 * z_Minute + z_Sekunde;

        timer = new CountDownTimer(z_Zeit*1000, 1000)
        {


            @Override
            public void onFinish() {
                _ringtone.play();

                a_anzeige.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
                isRunning = false;
                isStop = true;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                a_anzeige.setText(String.format("%02d:%02d:%02d", (millisUntilFinished / (1000*3600)),
                        (millisUntilFinished / (1000*60)) % 60,
                        (millisUntilFinished / 1000) % 60));

                safeTime = millisUntilFinished;

                if (_ringtone.isPlaying())
                {
                    _ringtone.stop();
                }
            }
        }.start();


    }

    public void timerStop()
    {
        timer.cancel();
        isStop = true;
     if(_ringtone.isPlaying())
        {
             _ringtone.stop();
        }
    }

    public void timerStart()
    {
        isStop = false;
     if (_ringtone.isPlaying())
        {
            _ringtone.stop();
        }

        timer = new CountDownTimer(safeTime, 1000)
        {

            @Override
            public void onFinish() {
                a_anzeige.setText(String.format("%02d:%02d:%02d", 0,0,0));
                isRunning = false;
                isStop = true;

                _ringtone.play();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                a_anzeige.setText(String.format("%02d:%02d:%02d", (millisUntilFinished / (1000*3600)),
                        (millisUntilFinished / (1000*60)) % 60,
                        (millisUntilFinished / 1000) % 60));

                safeTime = millisUntilFinished;

            }
        }.start();
    }

    public void timerReset()
    {
        timer.cancel();
        a_anzeige.setText("00:00:00");
        isRunning = false;
        isStop = true;

      if (_ringtone.isPlaying())
        {
            _ringtone.stop();
        }
    }
    public boolean isRunning() {
        return isRunning;
    }
    public boolean isStop(){
        return isStop;
    }



}
