package com.robin.atm.service;

import android.app.Service;
import android.content.Intent;

import com.robin.atm.util.LogUtil;

/**
 * Created by robin on 5/8/18.
 */

public abstract class BaseService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.v(this.getClass() + "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}
