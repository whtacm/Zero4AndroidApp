package com.robin.atm.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.robin.atm.App;
import com.robin.atm.BuildConfig;
import com.robin.atm.R;
import com.robin.atm.entity.data.Ad;
import com.robin.atm.presenter.MainPresenter;
import com.robin.atm.service.ExportedService;
import com.robin.atm.service.NetWorkService;
import com.robin.atm.ui.base.BaseActivity;
import com.robin.atm.util.LogUtil;
import com.robin.atm.util.NetworkUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    MainPresenter presenter = null;

    /**
     * 初始点开app且无广告缓存青黄下，默认提供的ad视频来源
     */
    public List<String> defaultAdList = new LinkedList<>();

    /**
     * 当前播放视频索引
     */
    public int curAdIndex = 0;

    public String curUrl = null;

    /**
     * 默认qr地址
     */
    private String qrUrl = null;

    /**
     * 当前播放的广告对象列表
     */
    List<Ad> curAdList = new LinkedList<>();

    /**
     * ad视频的代理url列表
     */
    private List<String> proxyUrlList = new LinkedList<>();

    /**
     * 是否使用代理转换url
     */
    private boolean useProxyUrl = true;

    private TickReceiver receiver = null;

    @Bind(R.id.id_qr)
    ImageView qr;

    @Bind(R.id.id_videoView)
    VideoView videoView;

    @Bind(R.id.id_loading)
    ProgressBar loading;

    @Bind(R.id.log_txv)
    EditText logEdt;

    private Status lastNetworkStatus = Status.BAD;

    enum Status {
        BAD,
        GOOD
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        defaultAdList.add(String.format("android.resource://%s/%d", getPackageName(), R.raw.ad0));
        defaultAdList.add(String.format("android.resource://%s/%d", getPackageName(), R.raw.ad4));

        qrUrl = String.format("android.resource://%s/%d", getPackageName(), R.raw.qr);

        MediaController controller = new MediaController(this);
        if (!BuildConfig.ENV_DEBUG) {
            controller.setVisibility(View.GONE);
        } else {
            LogUtil.sbLog = new StringBuffer();
            logEdt.setVisibility(View.VISIBLE);
        }
        videoView.setMediaController(controller);
        addListener();

        presenter = new MainPresenter(this);
        presenter.getData();
    }

    protected void addListener() {
        IntentFilter filter = new IntentFilter();
        filter.addCategory(NetWorkService.CATEGORY);
        filter.addAction(NetWorkService.ACTION_CONNECTED);
        receiver = new TickReceiver();
        registerReceiver(receiver, filter);

        startService(new Intent(this, NetWorkService.class));
//        startService(new Intent(this, ExportedService.class));

        videoView.setOnPreparedListener(mp -> {
            dismissProgressDialog();
        });

        videoView.setOnCompletionListener(mp -> {
            playNextVideo(mp);
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            playNextVideo(mp);
            return true;
        });

        Subscription s = Observable.interval(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    LogUtil.v("tick...");
                    presenter.getData();
                });
        addSubscription(s);

        if (BuildConfig.ENV_DEBUG) {
            Subscription log = Observable.interval(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        logEdt.setText(NetworkUtil.getMac() + "\n" + LogUtil.sbLog.toString());
                        if (LogUtil.sbLog.length() > 1024 * 1024 * 1) {
                            LogUtil.sbLog.delete(0, LogUtil.sbLog.length());
                        }
                    });
            addSubscription(log);
        }
    }

    public void doAfterLoadData(List<Ad> ads) {
        LogUtil.v("doAfterLoadData");

        useProxyUrl = true;
        if (ads == null) {
            lastNetworkStatus = Status.BAD;
            for (Ad ad : App.db.query(Ad.class)) {
                if (App.getProxy().isCached(ad.mediaURL)) {
                    curAdList.add(ad);
                }
            }
            if (curAdList.size() == 0) {
                useProxyUrl = false;
                for (String s : defaultAdList) {
                    Ad ad = new Ad();
                    ad.mediaURL = s;
                    ad.issueCode = qrUrl;
                    curAdList.add(ad);
                }
            }
        } else {
            lastNetworkStatus = Status.GOOD;
            if (ads.size() == 0) {
                useProxyUrl = false;
                for (String s : defaultAdList) {
                    Ad ad = new Ad();
                    ad.mediaURL = s;
                    ad.issueCode = qrUrl;
                    curAdList.add(ad);
                }
            } else {
                curAdList = ads;
                App.db.delete(Ad.class);
                App.db.insert(ads, ConflictAlgorithm.Replace);
            }
        }

        if (useProxyUrl) {
            proxyUrlList.clear();
            for (Ad ad : curAdList) {
                proxyUrlList.add(App.getProxy()
                        .getProxyUrl(ad.mediaURL));
//                App.getProxy().registerCacheListener((cacheFile, url, percentsAvailable) -> {
//                    LogUtil.v(String.format("%s  %s  %d", cacheFile.toString(), url, percentsAvailable));
//                }, ad.mediaURL);
            }
        }

        if (!videoView.isPlaying()) {
            playVideo();
        }
    }


    /**
     *
     */
    private void playVideo() {
        LogUtil.v(String.format("url: %s\nproxy: %s\nqrcode: %s",
                curAdList.get(curAdIndex).mediaURL,
                useProxyUrl ? proxyUrlList.get(curAdIndex) : "",
                curAdList.get(curAdIndex).issueCode));

        curUrl = curAdList.get(curAdIndex).mediaURL;

        LogUtil.v(String.format("%d %s", curAdIndex, curUrl));

        videoView.setVideoURI(Uri.parse(useProxyUrl ?
                proxyUrlList.get(curAdIndex) : curAdList.get(curAdIndex).mediaURL));
        videoView.start();

//        curAdIndex = 0;
        Glide.with(this)
                .load(useProxyUrl ? curAdList.get(curAdIndex).issueCode : Uri.parse(qrUrl))
//                .placeholder(R.drawable.img_default)
                .crossFade()
                .into(qr);
    }

    /**
     * @param mp
     */
    private void playNextVideo(MediaPlayer mp) {
        showProgressDialog();
        LogUtil.v("" + curAdIndex);
        curAdIndex++;
        curAdIndex %= curAdList.size();
        curUrl = curAdList.get(curAdIndex).mediaURL;

        LogUtil.v(String.format("%d %s", curAdIndex, curUrl));

        LogUtil.v(String.format("url: %s\nproxy: %s\nqrcode: %s",
                curAdList.get(curAdIndex).mediaURL,
                useProxyUrl ? proxyUrlList.get(curAdIndex) : "",
                curAdList.get(curAdIndex).issueCode));

        videoView.setVideoURI(Uri.parse(useProxyUrl ?
                proxyUrlList.get(curAdIndex) : curAdList.get(curAdIndex).mediaURL));
        videoView.start();
        Glide.with(this)
                .load(useProxyUrl ? curAdList.get(curAdIndex).issueCode : Uri.parse(qrUrl))
                .crossFade()
                .into(qr);
    }


    private void showProgressDialog() {
        loading.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        loading.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unregisterReceiver(receiver);
        LogUtil.sbLog = null;
    }


    /**
     *
     */
    public class TickReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (lastNetworkStatus == Status.BAD) {
                presenter.getData();
            }
        }
    }
}
