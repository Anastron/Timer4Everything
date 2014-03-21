package timerpack.timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Created by benza on 21.03.2014.
 */
public class BGService extends Service {
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
/*
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakeLock");
        wakeLock.acquire();
*/
        Notification note = new Notification();
        Intent i=new Intent(this, BGService.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                Intent.FLAG_ACTIVITY_SINGLE_TOP);


        note.flags|=Notification.FLAG_NO_CLEAR;

        startForeground(1337, note);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return(START_STICKY);               // oder mit NOT probieren
    }
}
