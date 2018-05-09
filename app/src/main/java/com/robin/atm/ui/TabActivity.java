package com.robin.atm.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.robin.atm.R;
import com.robin.atm.ui.adapter.CustomTabViewPagerAdapter;
import com.robin.atm.ui.base.BaseFragment;
import com.robin.atm.ui.base.ToolbarBaseActivity;
import com.robin.atm.widget.NoScrollViewPager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by robin on 5/2/18.
 */

public class TabActivity extends ToolbarBaseActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.view_pager)
    NoScrollViewPager viewPager;

    CustomTabViewPagerAdapter pagerAdapter;

    List<BaseFragment> fragmentList = null;

    List<String> titleList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_tab;
    }

    protected void initView() {
        fragmentList = new LinkedList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new MeFragment());

        titleList = Arrays.asList("首页", "发现", "我的");
        setTitle(titleList.get(0));
        pagerAdapter = new CustomTabViewPagerAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(titleList.get(position));
                if (position == 2) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setCustomView(pagerAdapter.getTabView(i));
            tabLayout.getTabAt(i).setIcon(R.drawable.ic_launcher_background);
            tabLayout.getTabAt(i).setText(titleList.get(i));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
//    }
}
