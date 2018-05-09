package com.robin.atm.presenter.base;


import com.robin.atm.network.RobinFactory;
import com.robin.atm.ui.base.BaseActivity;
import com.robin.atm.network.*;
/**
 * Created by robin on 4/10/18.
 */

public abstract class BasePresenter<T extends BaseActivity> {

    protected BaseActivity activity;
    public static AtmAdApi atmAdApi = RobinFactory.getAtmAdApiSingleton();
}
