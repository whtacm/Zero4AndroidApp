package com.robin.atm.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.robin.atm.util.LogUtil;

/**
 * Created by robin on 4/20/18.
 */


public class VideoRunnable implements Runnable {
    public Handler handler = null;

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                LogUtil.v(msg.toString());
            }
        };
        Looper.loop();
    }
}
