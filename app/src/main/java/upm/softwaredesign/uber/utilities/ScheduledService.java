package upm.softwaredesign.uber.utilities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aleksandar on 26/03/2017.
 */

public class ScheduledService extends Service
{

    private Timer timer = new Timer();
    private HttpManager httpManager = new HttpManager(this);


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

            }
        }, 0, 5000);//5 seconds
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}