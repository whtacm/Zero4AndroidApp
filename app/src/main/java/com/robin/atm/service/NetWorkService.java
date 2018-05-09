package com.robin.atm.service;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.blankj.utilcode.util.NetworkUtils;
import com.robin.atm.ui.PermissionRequestActivity;
import com.robin.atm.util.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class NetWorkService extends BaseService {
    public final static String CATEGORY = "robin.intent.category.NetWorkService";
    public final static String ACTION_START_LISTENING = "robin.intent.action.START_LISTENING";
    public final static String ACTION_CONNECTED = "robin.intent.action.CONNECTED";


    Timer timer;
    TimerTask task;

    Handler childH = null;

    public NetWorkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.v("" + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                LogUtil.v("Denied");
                Intent intent = new Intent(this, PermissionRequestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PermissionRequestActivity.PERMISSION_SET,
                        new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS});
                startActivity(intent);
            } else {
                LogUtil.v("Granted");
            }
        }

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.addCategory(NetWorkService.CATEGORY);
                intent.setAction(NetWorkService.ACTION_CONNECTED);
                LogUtil.v("network listening...");
                if (NetworkUtils.isConnected()) {
                    sendBroadcast(intent);
                }

//                LogUtil.v("");
//                childH.sendEmptyMessage(1);
            }
        };
        timer.schedule(task, 2000, 3000);

//        HandlerThread thread = new HandlerThread("ChildThread");
//        thread.start();
//        childH = new Handler(thread.getLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                LogUtil.v(msg.toString());
//            }
//        };
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
