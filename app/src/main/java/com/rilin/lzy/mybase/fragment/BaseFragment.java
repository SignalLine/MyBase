package com.rilin.lzy.mybase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.it.IT4Fragment;
import com.rilin.lzy.mybase.net.NetTaskUtils;

/**
 * Created by dengyaming on 16/9/13.
 */
public abstract class BaseFragment extends Fragment implements IT4Fragment{

    protected BaseActivity mActivity;
    private int screenHeight;
    private int screenWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();

        DisplayMetrics outMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenHeight = outMetrics.heightPixels;
        screenWidth = outMetrics.widthPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setViewId(),null,false);
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 得到屏幕高度
     *
     * @return
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destoryResouce();
    }

    protected final void startActivity(Class<?> class1) {
        Intent intent = new Intent(mActivity, class1);
        startActivity(intent);
    }

    protected NetTaskUtils getClient() {

        return NetTaskUtils.init();

    }

    protected void toast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
