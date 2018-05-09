package com.robin.atm.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robin.atm.R;
import com.robin.atm.ui.base.ToolbarBaseActivity;
import com.robin.atm.ui.base.ToolbarBaseFragment;
import com.robin.atm.util.LogUtil;

/**
 * Created by robin on 5/2/18.
 */

public class MeFragment extends ToolbarBaseFragment {
    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void doAfterRootViewInit() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.v("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.v("");
    }

    @Override
    protected void doWhenUserVisible() {
        ((ToolbarBaseActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    protected void doWhenUserInvisible() {
        ((ToolbarBaseActivity) getActivity()).getSupportActionBar().show();
    }
}
