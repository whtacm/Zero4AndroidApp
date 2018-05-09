package com.robin.atm;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.danikula.videocache.HttpProxyCacheServer;
import com.litesuits.orm.LiteOrm;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by robin on 4/18/18.
 */

public class App extends Application {
    private static HttpProxyCacheServer proxy = null;

    public static LiteOrm db;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        if (proxy == null) {
            proxy = new HttpProxyCacheServer.Builder(this)
                    .maxCacheSize(1024 * 1024 * 1024)
                    .build();
        }

        CrashReport.initCrashReport(this);

        db = LiteOrm.newSingleInstance(this, "atm.ad");
//        LogUtil.v(getCacheDir().getAbsolutePath());

        if (BuildConfig.ENV_DEBUG) {
            db.setDebugged(true);
        }
    }

    /**
     * @return
     */
    public static HttpProxyCacheServer getProxy() {
        return proxy;
    }


//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
