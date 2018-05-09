package com.robin.atm.presenter;

import com.blankj.utilcode.util.NetworkUtils;
import com.robin.atm.entity.data.Ad;
import com.robin.atm.presenter.base.BasePresenter;
import com.robin.atm.ui.MainActivity;
import com.robin.atm.util.LogUtil;
import com.robin.atm.util.NetworkUtil;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by robin on 4/18/18.
 */

public class MainPresenter extends BasePresenter {

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
    }

//    String mac = "e0:76:d0:66:2a:98";

    public void getData() {
        LogUtil.v(String.format("%s: %s", "Mac", NetworkUtil.getMac()));

        if (NetworkUtils.isConnected()) {
            Subscription s = atmAdApi.getPlayList(NetworkUtil.getMac())
                    .delay(0, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .map(data -> {
                        LogUtil.v("" + data.data.size());
                        return data.data;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(meizhis -> {
                        for (Ad meizhi : meizhis) {
                            LogUtil.v(meizhi.mediaURL);
                        }
                        ((MainActivity) activity).doAfterLoadData(meizhis);
                    }, throwable -> {
                        LogUtil.v("onError");
                        ((MainActivity) activity).doAfterLoadData(null);
                    });

            activity.addSubscription(s);
        } else {
            LogUtil.v("Network BAD!!!");
            ((MainActivity) activity).doAfterLoadData(null);
        }

    }
}
