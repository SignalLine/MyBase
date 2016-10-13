package com.rilin.lzy.mybase.my;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> mFragments;
    //系统状态栏设置
    public SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initSystemBar(this,"#ffffff");

        initView();
        initData(savedInstanceState);
    }

    /**
     * 设置状态栏颜色
     * @param activity
     */
    public void initSystemBar(AppCompatActivity activity,String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintColor(Color.parseColor(color));
    }

    private void initData(Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        //当Activity发生重复创建或者横竖屏切换,内部的Fragment会自动创建一边
        FragmentManager manager = getSupportFragmentManager();
        if (savedInstanceState == null) {//第一次创建
            Fragment fragment = new HomeFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putString("type","home1");
            bundle1.putString("title","home1");
            fragment.setArguments(bundle1);
            mFragments.add(fragment);

            fragment = new HomeFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putString("type","home2");
            bundle2.putString("title","home2");
            fragment.setArguments(bundle2);
            mFragments.add(fragment);

            fragment = new HomeFragment();
            Bundle bundle3 = new Bundle();
            bundle3.putString("type","home3");
            bundle3.putString("title","home3");
            fragment.setArguments(bundle3);
            mFragments.add(fragment);

            fragment = new HomeFragment();
            Bundle bundle4 = new Bundle();
            bundle4.putString("type","home4");
            bundle4.putString("title","home4");
            fragment.setArguments(bundle4);
            mFragments.add(fragment);

            FragmentTransaction transaction = manager.beginTransaction();
            int index = 0;
            for (Fragment f : mFragments){
                transaction.add(R.id.frame_container,f,"tag"+index);
                //隐藏
                transaction.hide(f);
                index++;
            }

            //默认显示第一个
            transaction.show(mFragments.get(0));
            transaction.commit();

        }else {
            //不是第一次创建,Fragment会自动的由Activity创建好
            for (int i = 0; i < 4; i++) {
                String tag = "tag" + i;
                Fragment f = manager.findFragmentByTag(tag);
                if (f != null) {
                    mFragments.add(f);
                }
            }
        }
    }

    private void initView() {
        RadioGroup group = (RadioGroup) findViewById(R.id.home_tab_bar);
        group.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = 0;
        switch (checkedId) {
            case R.id.home_tab_home1:
                index = 0;
                break;
            case R.id.home_tab_home2:
                index = 1;
                break;
            case R.id.home_tab_home3:
                index = 2;
                break;
            case R.id.home_tab_home4:
                index = 3;
                break;
        }
        switchFragment(index);
    }

    private void switchFragment(int index){
        if(index >= 0 && index < mFragments.size()){

            int size = mFragments.size();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            for (int i = 0; i < size; i++) {

                Fragment f = mFragments.get(i);

                if(i == index){
                    // 显示
                    transaction.show(f);
                }else{
                    // 隐藏
                    transaction.hide(f);
                }
            }
            transaction.commit();

        }
    }
}
