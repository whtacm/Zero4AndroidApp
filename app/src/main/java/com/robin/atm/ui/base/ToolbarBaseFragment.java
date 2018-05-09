package com.robin.atm.ui.base;

/**
 * Created by robin on 5/2/18.
 */

public abstract class ToolbarBaseFragment extends BaseFragment {


    @Override
    protected void doAfterRootViewInit() {
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((ToolbarBaseActivity) getActivity()).getSupportActionBar().show();
    }
}
