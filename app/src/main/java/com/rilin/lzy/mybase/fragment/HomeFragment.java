package com.rilin.lzy.mybase.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.adapter.CommonFragmentAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements TabLayout.OnTabSelectedListener {


    private static final String TAG = HomeFragment.class.getSimpleName();
    private String mType;
    private TextView mTextTitle;
    private String mTitle;

    private TabLayout mTabLayout;

    private ViewPager mPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mType = arguments.getString("type");
        mTitle = arguments.getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        initData(view);

        return view;
    }

    private void initData(View view) {
        switch (mType) {
            case "home1":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);

                //第一页显示TabLayout
                mTabLayout = (TabLayout) view.findViewById(R.id.home_tab_bar);
                mPager = (ViewPager) view.findViewById(R.id.home_pager);
                List<Fragment> subFragments = new ArrayList<>();

                Fragment fragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type","sub1");
                bundle.putString("title","sub title1");
                fragment.setArguments(bundle);
                subFragments.add(fragment);

                fragment = new HomeFragment();
                bundle = new Bundle();
                bundle.putString("type","sub2");
                bundle.putString("title","sub title2");
                fragment.setArguments(bundle);
                subFragments.add(fragment);

                fragment = new HomeFragment();
                bundle = new Bundle();
                bundle.putString("type","sub3");
                bundle.putString("title","sub title3");
                fragment.setArguments(bundle);
                subFragments.add(fragment);

                CommonFragmentAdapter adapter = new CommonFragmentAdapter(getChildFragmentManager(),subFragments);

                mPager.setAdapter(adapter);
                //
                mTabLayout.setOnTabSelectedListener(this);
                TabLayout.Tab tab = mTabLayout.newTab();
                tab.setText("推荐");
                mTabLayout.addTab(tab);

                tab = mTabLayout.newTab();
                tab.setText("视频");
                mTabLayout.addTab(tab);

                tab = mTabLayout.newTab();
                tab.setText("图片");
                mTabLayout.addTab(tab);

                mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

                break;
            case "home2":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);
                break;
            case "home3":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);
                break;
            case "home4":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);
                break;
            case "sub1":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);
                break;
            case "sub2":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);
                break;
            case "sub3":
                mTextTitle.setText(mTitle);
                Log.i(TAG,"mTitle----->>" + mTitle);
                Log.i(TAG,"mType----->>" + mType);
                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        mTextTitle = (TextView) view.findViewById(R.id.fragment_home_title);
    }

    //TabLayout
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

        mPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
