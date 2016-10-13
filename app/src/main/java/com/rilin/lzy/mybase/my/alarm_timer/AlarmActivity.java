package com.rilin.lzy.mybase.my.alarm_timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.qqtheme.framework.picker.TimePicker;


public class AlarmActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = AlarmActivity.class.getSimpleName();
    private CheckBox mCheckBox;
    private TextView mTextStartTime,mTextEndTime;
    private Button mButtonStart,mButtonEnd,mButtonSure;
    private ImageView mImageBack;
    private RadioGroup mRadioGroup;

    private long spaceTime;//间隔时间,毫秒值
    private boolean isOpen;//是否开启闹铃

    private int mMinute,mHour;//设置的结束时间

    @Override
    public int setViewId() {
        return R.layout.activity_alarm;
    }

    @Override
    public void initView() {
        mImageBack = getView(R.id.image_back);
        mButtonSure = getView(R.id.alarm_sure);
        mCheckBox = getView(R.id.alarm_cb_open);
        mTextStartTime = getView(R.id.alarm_tv_start_time);
        mTextEndTime = getView(R.id.alarm_tv_end_time);
        mButtonEnd = getView(R.id.alarm_btn_end_time);
        mButtonStart = getView(R.id.alarm_btn_start_time);
        mRadioGroup = getView(R.id.alarm_rg_tab);
    }

    @Override
    public void initData() {
        mCheckBox.setOnClickListener(this);
        mButtonEnd.setOnClickListener(this);
        mButtonStart.setOnClickListener(this);
        mButtonSure.setOnClickListener(this);
        mImageBack.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.alarm_rb_30m:
                        spaceTime = 30*60*1000;
                        Log.i(TAG,"选择时间间隔为---------->>>" + spaceTime);
                        break;
                    case R.id.alarm_rb_1h:
                        spaceTime = 60*60*1000;
                        Log.i(TAG,"选择时间间隔为---------->>>" + spaceTime);
                        break;
                    case R.id.alarm_rb_2h:
                        spaceTime = 2*60*60*1000;
                        Log.i(TAG,"选择时间间隔为---------->>>" + spaceTime);
                        break;
                }
            }
        });


    }

    public  String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_btn_start_time:
                //控制时间范围
                Calendar calendar = Calendar.getInstance();

                TimePicker timePicker = new TimePicker(mActivity);
                timePicker.setAnimationStyle(R.style.Animation_CustomPopup);
                timePicker.setLabel("分","秒");
                timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        mTextStartTime.setText(hour + ":" + minute);
                        Log.i(TAG,"startTime----->>>" + hour + ":" + minute);
                    }
                });
                timePicker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
                timePicker.show();
                break;
            case R.id.alarm_btn_end_time:
                final TimePickerView pvTime1 = new TimePickerView(mActivity, TimePickerView.Type.MONTH_DAY_HOUR_MIN);

                pvTime1.setTime(new Date());
                pvTime1.setCyclic(true);
                pvTime1.setCancelable(true);
                //时间选择后回调
                pvTime1.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        String time = getTime(date);
                        mTextEndTime.setText(time);
                        Log.i(TAG,"endTime----->>>" + time);
                        mHour = date.getHours();
                        mMinute = date.getMinutes();
                    }
                });
                pvTime1.show();
                break;
            case R.id.alarm_cb_open:
                if(!isOpen){
                    isOpen = true;
                }else {
                    isOpen = false;
                }
                Log.i(TAG,"isOpen------>> " + isOpen);
                mCheckBox.setChecked(isOpen);
                break;
            case R.id.image_back:
                finish();
                break;
            case R.id.alarm_sure:
                String result = "---------isOpen--->" + isOpen + "---startTime--->" + mTextStartTime.getText().toString()
                        + "---endTime--->" + mTextEndTime.getText().toString() + "---间隔时间--->" + spaceTime;
                toast(result);
                Log.i(TAG,result);

                startAlarm();

                break;
        }
    }

    private void startAlarm(){
        Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this,0,intent,0);

        long firstTime = SystemClock.elapsedRealtime();//开机之后到现在的运行时间
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(systemTime);

        //设置时区
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE,mMinute);
        calendar.set(Calendar.HOUR_OF_DAY,mHour);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        //选择的定时时间
        long selectTime = calendar.getTimeInMillis();
        //如果当前时间大于设置时间,那么就从第二天开始设置
        if(systemTime > selectTime){
            toast("设置的时间小于当前时间");
            calendar.add(Calendar.DAY_OF_MONTH,1);
            selectTime = calendar.getTimeInMillis();
        }

        //技术现在时间到设定时间差
        long time = selectTime - systemTime;
        firstTime += time;
        //闹铃注册
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,24*60*60*1000,sender);

        Log.i(TAG,"设置重复闹铃成功:------->>>" + firstTime);
        Log.i(TAG,"闹铃时间为:--------->>>>" + getTime(new Date(firstTime)));
    }


    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }
}
