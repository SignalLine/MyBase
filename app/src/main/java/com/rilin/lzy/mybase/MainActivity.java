package com.rilin.lzy.mybase;


import android.graphics.Color;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;

import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.my.CompassActivity;
import com.rilin.lzy.mybase.my.DraggActivity;
import com.rilin.lzy.mybase.my.ThreeDActivity;
import com.rilin.lzy.mybase.my.UpdateAppActivity;
import com.rilin.lzy.mybase.my.VoiceActivity;
import com.rilin.lzy.mybase.my.alarm_timer.AlarmActivity;
import com.rilin.lzy.mybase.my.BottomSheetActivity;
import com.rilin.lzy.mybase.my.CircleMenuActivity;
import com.rilin.lzy.mybase.my.DatePickActivity;
import com.rilin.lzy.mybase.my.FloatButtonActivity;
import com.rilin.lzy.mybase.my.FrescoActivity;
import com.rilin.lzy.mybase.my.GreenDaoActivity;
import com.rilin.lzy.mybase.my.alarm_timer.TimerSchActivity;
import com.rilin.lzy.mybase.my.lunbo.GuideActivity;
import com.rilin.lzy.mybase.my.HeartBomActivity;
import com.rilin.lzy.mybase.my.HomeActivity;
import com.rilin.lzy.mybase.my.MyScrollActivity;
import com.rilin.lzy.mybase.my.NetStatesActivity;
import com.rilin.lzy.mybase.my.NotifyActivity;
import com.rilin.lzy.mybase.my.ObservableActivity;
import com.rilin.lzy.mybase.my.ObserverActivity;
import com.rilin.lzy.mybase.my.ParallaxToolbarScrollviewActivity;
import com.rilin.lzy.mybase.my.PhotoActivity;
import com.rilin.lzy.mybase.my.PinnerListViewActivity;
import com.rilin.lzy.mybase.my.PopupActivity;
import com.rilin.lzy.mybase.my.RecyclerUserActivity;
import com.rilin.lzy.mybase.my.lunbo.ShowADActivity;
import com.rilin.lzy.mybase.my.SimpleLoginActivity;
import com.rilin.lzy.mybase.my.UserInfoListActivity;
import com.rilin.lzy.mybase.my.Win8ProActivity;
import com.rilin.lzy.mybase.my.tv_vitamio.TVHomeActivity;
import com.rilin.lzy.mybase.util.L;
import com.rilin.lzy.mybase.util.SPUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = MainActivity.class.getSimpleName();
    private Button mButton,mButton2,mButton3,
                mButton4,mButton5,mButton6,
                mButton7,mButton8,mButton9,
                mButton10,mButton11,mButton12,
                mButton13,mButton14,mButton15,
                mButton16,mButton17,mButton18,
                mButton19,mButton20,mButton21,
                mButton22,mButton23,mButton24,
                mButton25,mButton26,mButton27,
                mButton28,mButton29,mButton30,
                mButtonChange,mButton31;


    @Override
    public int setViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mButton = getView(R.id.main_btn_1);
        mButton2 = getView(R.id.main_btn_2);
        mButton3 = getView(R.id.main_btn_3);
        mButton4 = getView(R.id.main_btn_4);
        mButton5 = getView(R.id.main_btn_5);
        mButton6 = getView(R.id.main_btn_6);
        mButton7 = getView(R.id.main_btn_7);
        mButton8 = getView(R.id.main_btn_8);
        mButton9 = getView(R.id.main_btn_9);
        mButton10 = getView(R.id.main_btn_10);
        mButton11 = getView(R.id.main_btn_11);
        mButton12 = getView(R.id.main_btn_12);
        mButton13 = getView(R.id.main_btn_13);
        mButton14 = getView(R.id.main_btn_14);
        mButton15 = getView(R.id.main_btn_15);
        mButton16 = getView(R.id.main_btn_16);
        mButton17 = getView(R.id.main_btn_17);
        mButton18 = getView(R.id.main_btn_18);
        mButton19 = getView(R.id.main_btn_19);
        mButton20 = getView(R.id.main_btn_20);
        mButton21 = getView(R.id.main_btn_21);
        mButton22 = getView(R.id.main_btn_22);
        mButton23 = getView(R.id.main_btn_23);
        mButton24 = getView(R.id.main_btn_24);
        mButton25 = getView(R.id.main_btn_25);
        mButton26 = getView(R.id.main_btn_26);
        mButton27 = getView(R.id.main_btn_27);
        mButton28 = getView(R.id.main_btn_28);
        mButton29 = getView(R.id.main_btn_29);
        mButton30 = getView(R.id.main_btn_30);
        mButton31 = getView(R.id.main_btn_31);

        mButtonChange = getView(R.id.main_change_color);
    }



    @Override
    public void initData() {
        mButton.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton10.setOnClickListener(this);
        mButton11.setOnClickListener(this);
        mButton12.setOnClickListener(this);
        mButton13.setOnClickListener(this);
        mButton14.setOnClickListener(this);
        mButton15.setOnClickListener(this);
        mButton16.setOnClickListener(this);
        mButton17.setOnClickListener(this);
        mButton18.setOnClickListener(this);
        mButton19.setOnClickListener(this);
        mButton20.setOnClickListener(this);
        mButton21.setOnClickListener(this);
        mButton22.setOnClickListener(this);
        mButton23.setOnClickListener(this);
        mButton24.setOnClickListener(this);
        mButton25.setOnClickListener(this);
        mButton26.setOnClickListener(this);
        mButton27.setOnClickListener(this);
        mButton28.setOnClickListener(this);
        mButton29.setOnClickListener(this);
        mButton30.setOnClickListener(this);
        mButton31.setOnClickListener(this);

        mButtonChange.setOnClickListener(this);

    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.main_btn_1:
                    startActivity(MyScrollActivity.class);
                    break;
                case R.id.main_btn_2:
                    startActivity(PinnerListViewActivity.class);
                    break;
                case R.id.main_btn_3:
                    startActivity(BottomSheetActivity.class);
                    break;
                case R.id.main_btn_4:
                    startActivity(PhotoActivity.class);
                    break;
                case R.id.main_btn_5:
                    startActivity(SimpleLoginActivity.class);
                    break;
                case R.id.main_btn_6:
                    startActivity(FrescoActivity.class);
                    break;
                case R.id.main_btn_7:
                    startActivity(FloatButtonActivity.class);
                    break;
                case R.id.main_btn_8:
                    startActivity(UserInfoListActivity.class);
                    break;
                case R.id.main_btn_9:
                    startActivity(RecyclerUserActivity.class);
                    break;
                case R.id.main_btn_10:
                    startActivity(NotifyActivity.class);
                    break;
                case R.id.main_btn_11:
                    startActivity(DatePickActivity.class);
                    break;
                case R.id.main_btn_12:
                    startActivity(CircleMenuActivity.class);
                    break;
                case R.id.main_btn_13:
                    startActivity(AlarmActivity.class);
                    break;
                case R.id.main_btn_14:
                    startActivity(HomeActivity.class);
                    break;
                case R.id.main_btn_15:
                    startActivity(ObservableActivity.class);
                    break;
                case R.id.main_btn_16:
                    startActivity(ParallaxToolbarScrollviewActivity.class);
                    break;
                case R.id.main_btn_17:
                    startActivity(PopupActivity.class);
                    break;
                case R.id.main_btn_18:
                    startActivity(Win8ProActivity.class);
                    break;
                case R.id.main_btn_19:
                    startActivity(HeartBomActivity.class);
                    break;
                case R.id.main_btn_20:
                    startActivity(NetStatesActivity.class);
                    break;
                case R.id.main_btn_21:
                    startActivity(ObserverActivity.class);
                    break;
                case R.id.main_btn_22:
                    startActivity(TVHomeActivity.class);
                    break;
                case R.id.main_btn_23:
                    startActivity(GuideActivity.class);
                    break;
                case R.id.main_btn_24:
                    startActivity(ShowADActivity.class);
                    break;
                case R.id.main_btn_25:
                    startActivity(GreenDaoActivity.class);
                    break;
                case R.id.main_btn_26:
                    startActivity(TimerSchActivity.class);
                    break;
                case R.id.main_btn_27://手势拖动
                    startActivity(DraggActivity.class);
                    break;
                case R.id.main_btn_28://录音
                    startActivity(VoiceActivity.class);
                    break;
                case R.id.main_btn_29://指南针
                    startActivity(CompassActivity.class);
                    break;
                case R.id.main_btn_30://版本检测更新
                    startActivity(UpdateAppActivity.class);
                    break;
                case R.id.main_change_color://换肤
                    switchNightMode();
                    break;
                case R.id.main_btn_31://3D画廊效果
                    startActivity(ThreeDActivity.class);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 切换日间夜间状态
     */
    private void switchNightMode() {
        boolean color = SPUtils.getColor(getApplicationContext());
        L.i(TAG,"-------------color----->>>" + color);
        if(!color){
            mButtonChange.setBackgroundColor(Color.WHITE);
            mButtonChange.setTextColor(Color.BLACK);
            mButtonChange.setText("日间皮肤");

            // 日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SPUtils.setColor(getApplicationContext(),true);
        }else {
            mButtonChange.setBackgroundColor(Color.BLACK);
            mButtonChange.setTextColor(Color.WHITE);
            mButtonChange.setText("夜间皮肤");

            // 夜间皮肤
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SPUtils.setColor(getApplicationContext(),false);
        }
        recreate();
    }
}

