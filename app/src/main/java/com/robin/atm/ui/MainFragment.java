package com.robin.atm.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.robin.atm.R;
import com.robin.atm.ui.adapter.TabPagerAdapter;
import com.robin.atm.ui.base.BaseFragment;
import com.robin.atm.ui.base.ToolbarBaseFragment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by robin on 5/2/18.
 */

public class MainFragment extends ToolbarBaseFragment {

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    TabPagerAdapter adapter;

    List<BaseFragment> fragmentList = null;

    List<String> titleList = null;

    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void doAfterRootViewInit() {
        ButterKnife.bind(this, rootView);
        fragmentList = new LinkedList<>();

        titleList = Arrays.asList("推荐", "时政", "社会", "娱乐", "美女", "财经" ,"体育", "足球" ,"NBA");

        for (String s : titleList) {
            TabFragment tabFragment = new TabFragment();
            tabFragment.cs = s;
            fragmentList.add(tabFragment);
        }
        adapter = new TabPagerAdapter(getChildFragmentManager(), context, fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentList = null;
        ButterKnife.unbind(this);
    }
}
