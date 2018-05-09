package com.robin.atm.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin.atm.R;
import com.robin.atm.ui.base.BaseFragment;

import java.util.List;

/**
 * Created by robin on 5/2/18.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    Context context;
    List<BaseFragment> fragmentList;
    List<String> titles;

    public TabPagerAdapter(FragmentManager fm, Context context, List<BaseFragment> fragmentList, List<String> titles) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        ImageView tabIcon = view.findViewById(R.id.tab_icon);
        TextView tabTitle = view.findViewById(R.id.tab_title);
        tabTitle.setText(titles.get(position));
        return view;
    }
}
