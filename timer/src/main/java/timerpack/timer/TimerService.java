package timerpack.timer;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.TextView;


public class TimerService extends Service{
    private int z_Stunde;
    private int z_Minute;
    private int z_Sekunde;
    private int z_Zeit;
    private long safeTime;
    private boolean isRunning;
    private boolean isStop = true;

    private TextView a_anzeige;

    private CountDownTimer timer;

    private Ringtone _ringtone;
    private Vibrator _vib;

     @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void run(int stunde, int minute, int sekunde, final TextView anzeige, final Ringtone ringtone, Vibrator vib)
    {
        z_Stunde = stunde;
        z_Minute = minute;
        z_Sekunde = sekunde;
        a_anzeige = anzeige;
        safeTime = 0;

        _ringtone = ringtone;
        _vib = vib;
        isStop = false;


        if (_ringtone.isPlaying())
        {
            _ringtone.stop();
            _vib.cancel();
        }

        z_Zeit = 3600 * z_Stunde + 60 * z_Minute + z_Sekunde;

        timer = new CountDownTimer(z_Zeit*1000, 1000)
        {
            @Override
            public void onFinish() {
                _ringtone.play();

                long[] pattern = {0, 500, 1000};
                _vib.vibrate(pattern, 0);

                a_anzeige.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
                isRunning = false;
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
                    _vib.cancel();
                }
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
            _vib.cancel();
        }
    }

    public void timerStart()
    {
        isStop = false;
     if (_ringtone.isPlaying())
        {
            _ringtone.stop();
            _vib.cancel();
        }

        timer = new CountDownTimer(safeTime, 1000)
        {

            @Override
            public void onFinish() {
                a_anzeige.setText(String.format("%02d:%02d:%02d", 0,0,0));
                isRunning = false;

                _ringtone.play();

                long[] pattern = {0, 500, 1000};
                _vib.vibrate(pattern, 0);
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
            _vib.cancel();
        }
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
}
