package com.robin.atm.ui;

import android.widget.TextView;

import com.robin.atm.R;
import com.robin.atm.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by robin on 5/2/18.
 */

public class TabFragment extends BaseFragment {
    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_tab;
    }

    public String cs;

    @Bind(R.id.tag)
    TextView tag;

    @Override
    protected void doAfterRootViewInit() {
        ButterKnife.bind(this, rootView);
        tag.setText(cs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
