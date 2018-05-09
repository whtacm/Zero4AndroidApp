package com.robin.atm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.robin.atm.R;

/**
 * Created by robin on 5/3/18.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NoScrollViewPager, 0, 0);
        this.isScroll = ta.getBoolean(R.styleable.NoScrollViewPager_isScroll, false);
        ta.recycle();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    /**
     *
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onTouchEvent(ev);
        } else {
            return true;// 消费
        }
    }

    /**
     * @param scroll
     */
    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }
}
