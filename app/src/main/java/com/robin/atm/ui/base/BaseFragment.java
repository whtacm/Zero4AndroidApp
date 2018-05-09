package com.robin.atm.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robin.atm.util.LogUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by robin on 5/2/18.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    protected abstract int provideLayoutResId();

    protected Context context;

    CompositeSubscription mCompositeSubscription = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(provideLayoutResId(), container, false);
        context = getActivity();
        doAfterRootViewInit();
        LogUtil.v("" + this.getClass());
        return rootView;
    }


    protected void doAfterRootViewInit() {
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.v("" + this.getClass());
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.v("" + this.getClass());
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.v("" + this.getClass());
    }


    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v("" + this.getClass());
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
