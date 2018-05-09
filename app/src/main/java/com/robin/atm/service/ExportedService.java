package com.robin.atm.service;

import android.content.Intent;
import android.os.IBinder;

public class ExportedService extends BaseService {
    public ExportedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
