package com.robin.atm.ui.base;

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

    List<BaseActivity> list = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(this);
        LogUtil.v(list, " list-size: " + list.size());
    }

    CompositeSubscription mCompositeSubscription = null;

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }


    public void finishAll() {
        list.clear();
        finish();
    }

    public void back() {
        list.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.remove(this);
        LogUtil.v(list, " list-size: " + list.size());
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
