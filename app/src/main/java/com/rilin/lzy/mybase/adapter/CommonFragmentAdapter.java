package com.rilin.lzy.mybase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rilin.lzy.mybase.util.L;

import java.util.List;

/**
 * Created by dengyaming on 16/9/18.
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments;

    public CommonFragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments==null?0:mFragments.size();
    }

}
