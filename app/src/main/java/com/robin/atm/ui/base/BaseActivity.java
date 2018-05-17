package com.robin.atm.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.robin.atm.util.LogUtil;

import java.util.LinkedList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by robin on 4/18/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static List<Activity> list = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        LogUtil.v(list, " list-size: " + list.size());
    }

    private void addActivity(Activity activity) {
        list.add(activity);
    }

    private void removeActivity(Activity activity) {
        list.remove(activity);
    }

    CompositeSubscription mCompositeSubscription = null;

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }


    public void finishAll() {
        for (Activity activity : list) {
            activity.finish();
        }
        list.clear();
    }

    public void back() {
        list.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
        LogUtil.v(list, " list-size: " + list.size());
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
