package com.robin.atm.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.animation.DecelerateInterpolator;

import com.robin.atm.R;
import com.robin.atm.util.LogUtil;

import butterknife.Bind;

/**
 * Created by robin on 5/2/18.
 */

public abstract class ToolbarBaseActivity extends BaseActivity {
    abstract protected int provideContentViewId();

    Toolbar toolbar;

    AppBarLayout appBarLayout;

    boolean isHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());

        appBarLayout = findViewById(R.id.toolbarC);
        toolbar = findViewById(R.id.toolbar);

        if (toolbar == null || appBarLayout == null) {
            throw new RuntimeException("subclass of ToolbarBaseActivity must have a toolbar");
        }
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setElevation(10.6f);
        }
    }


    public void hideOrShowToolbar(boolean flag) {
        appBarLayout.animate()
                .translationY(!flag ? 0 : -appBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
//        isHidden = !isHidden;
    }
}
