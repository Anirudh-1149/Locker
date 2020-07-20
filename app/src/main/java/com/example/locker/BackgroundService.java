package com.example.locker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.locker.Database.LockedApps;
import com.example.locker.Database.Password_Database;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class BackgroundService extends Service {

    LockedApps db = new LockedApps(this);
    Password_Database pd = new Password_Database(this);
    int flag = 0;
    String current_app="pp";
    Context context = this;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
            startMyOwnForeground();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.locker";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand: Started");
        repeat();
        return START_STICKY;
    }

    private Timer timer;

    public void repeat()
    {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, String.format("run: %s %d", current_app, flag));
                if (flag == 0) {
                    flag = 1;
                    Cursor cursor1 = db.getData();
                    Cursor cursor2 = pd.getData();
                    String pass = null;
                    ArrayList<String> apps = new ArrayList<>();

                    while (cursor1.moveToNext())
                        apps.add(cursor1.getString(0));

                    while (cursor2.moveToNext())
                        pass = cursor2.getString(0);
                    if(!currentPackage().equals("com.example.locker")&&!currentPackage().equals("pp"))
                    current_app = currentPackage();

                    if (apps.contains(current_app)) {
                        Intent intent1;
                        intent1 = new Intent(context, LockScreenActivity.class);
                        intent1.putExtra("password", pass);
                        intent1.putExtra("package_name", current_app);
                        intent1.setAction(Intent.ACTION_VIEW);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                        Log.i(TAG, "run: in background service" + current_app);
                    }
                } else if ((!currentPackage().equals("com.example.locker")) && (!currentPackage().equals(current_app)) && !currentPackage().equals("pp"))
                    {flag = 0;}
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    public void stopTimerTask()
    {
        if(timer!=null)timer.cancel();
        timer=  null;
    }

    public String currentPackage()
    {
        String topPackageName = "pp";
        UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*2, time);
        if(stats != null) {
            SortedMap<Long,UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
            }
            if(!mySortedMap.isEmpty()) {
                topPackageName =  Objects.requireNonNull(mySortedMap.get(mySortedMap.lastKey())).getPackageName();
            }
        }
        return topPackageName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

}
