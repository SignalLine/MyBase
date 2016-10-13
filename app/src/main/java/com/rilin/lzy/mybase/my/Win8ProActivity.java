package com.rilin.lzy.mybase.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.view.Win8Search;

public class Win8ProActivity extends BaseActivity implements View.OnClickListener {

    private Win8Search mProgress;
    private Button mButtonStart,mButtonEnd;

    @Override
    public int setViewId() {
        return R.layout.activity_win8_pro;
    }

    @Override
    public void initView() {
        mProgress = getView(R.id.win8_progress);
        mButtonEnd = getView(R.id.btn_end);
        mButtonStart = getView(R.id.btn_start);
    }

    @Override
    public void initData() {
        mButtonEnd.setOnClickListener(this);
        mButtonStart.setOnClickListener(this);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mProgress.start();
                break;
            case R.id.btn_end:
                mProgress.stop();
                break;
            default:break;
        }
    }
}
