package timerpack.timer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;


public class TimerService extends Service{
    private int z_Stunde;
    private int z_Minute;
    private int z_Sekunde;
    private int z_Zeit;
    private long safeTime;

    private boolean isRunning;
    private boolean isStop = true;
    private boolean _isVibration;
    private boolean _isRingtone;

    private TimeDisplay a_anzeige;

    private CountDownTimer timer;

    private Ringtone _ringtone;
    private Vibrator _vib;


     @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void run(int stunde, int minute, int sekunde, final TimeDisplay anzeige, final Ringtone ringtone, Vibrator vib, boolean isVibration,boolean isRingtone)
    {
        z_Stunde = stunde;
        z_Minute = minute;
        z_Sekunde = sekunde;
        a_anzeige = anzeige;
        safeTime = 0;

        _ringtone = ringtone;
        _vib = vib;
        isStop = false;

        _isVibration = isVibration;
        _isRingtone = isRingtone;



        if (_ringtone.isPlaying())
        {
            _ringtone.stop();
        }
//        if(_vib.hasVibrator())
            _vib.cancel();

        z_Zeit = 3600 * z_Stunde + 60 * z_Minute + z_Sekunde;

        timer = new CountDownTimer(z_Zeit*1000, 1000)
        {
            @Override
            public void onFinish() {
                if(_isRingtone)
                    _ringtone.play();


                long[] pattern = {0, 500, 1000};
                if(_isVibration)
                    _vib.vibrate(pattern, 0);


                a_anzeige.setTime(0, 0, 0);
                isRunning = false;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                a_anzeige.setTime((int)(millisUntilFinished / (1000*3600)), (int)((millisUntilFinished / (1000*60)) % 60), (int)((millisUntilFinished / 1000) % 60));

                safeTime = millisUntilFinished;

                if (_ringtone.isPlaying())
                {
                    _ringtone.stop();
                }
 //               if(_vib.hasVibrator())
                    _vib.cancel();
            }
        }.start();
    }

    public void timerStop()
    {
        if(isRunning()) {
            timer.cancel();
            isStop = true;
        }
        if(_ringtone.isPlaying())
        {
            isStop = true;
             _ringtone.stop();
         }
 //       if(_vib.hasVibrator())
            _vib.cancel();
            isStop = true;

    }

    public void timerStart()
    {
        isStop = false;
     if (_ringtone.isPlaying())
        {
            _ringtone.stop();
        }
//        if(_vib.hasVibrator()) {
            _vib.cancel();
//        }

        timer = new CountDownTimer(safeTime, 1000)
        {

            @Override
            public void onFinish() {
                a_anzeige.setTime(0, 0, 0);
                isRunning = false;

                if(_isRingtone)
                    _ringtone.play();


                long[] pattern = {0, 500, 1000};
                if(_isVibration)
                    _vib.vibrate(pattern, 0);

            }

            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                a_anzeige.setTime((int) (millisUntilFinished / (1000 * 3600)), (int) ((millisUntilFinished / (1000 * 60)) % 60), (int) ((millisUntilFinished / 1000) % 60));

                safeTime = millisUntilFinished;

            }
        }.start();
    }

    public void timerReset()
    {
        timer.cancel();
        a_anzeige.setTime(0, 0, 0);
        isRunning = false;
        isStop = true;

      if (_ringtone.isPlaying())
      {
            _ringtone.stop();
      }
 //    if(_vib.hasVibrator()) {
           _vib.cancel();
  //     }
    }
    public boolean isRunning() {
        return isRunning;
    }
    public boolean isStop(){
        return isStop;
    }
    public boolean isPlaying(){
        return _ringtone.isPlaying();
    }
    public boolean isVibration()
    {
        return _vib.hasVibrator();
    }


    // sieht aus als Funktioniert das nicht :(
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TimerService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
}
