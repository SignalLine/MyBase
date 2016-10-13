package com.rilin.lzy.mybase.my.alarm_timer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.adapter.ClockInfoRingAdapter;
import com.rilin.lzy.mybase.model.ClockModel;
import com.rilin.lzy.mybase.receiver.ClockGetNextAlarm;
import com.rilin.lzy.mybase.receiver.TimerReceiver;
import com.rilin.lzy.mybase.util.ClockDBUtil;
import com.rilin.lzy.mybase.util.L;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = ClockInfoActivity.class.getSimpleName();
    //是否为新建闹钟
    private boolean isAdd;
    private Intent intent;
    //时钟
    private TimePicker timePicker;
    //日历
    private Calendar calendar;
    //闹钟管理器
    private AlarmManager alarmManager;
    //保存/删除
    private Button button;
    //更新闹铃按钮
    private Button mButtonUpdate;
    //小时
    private int mHourOfDay;
    //分钟
    private int mMinute;
    //重复
    private CheckBox cb_sun, cb_mon, cb_tues, cb_wed, cb_thurs, cb_fri, cb_sat;
    //
    private List<CheckBox> mListCheckBox;
    //标签
    private TextView mTag;
    //间隔
    private TextView mSpace;
    //铃音
    private TextView mRing;
    //闹钟实例
    private ClockModel model;
    //当前闹铃请求码最大值
    private int requestCode;
    //标签的dialog
    private AlertDialog mTagDialog;
    //标签布局
    private LinearLayout mTagLinearLayout;
    //间隔时间的popupwindow
    private PopupWindow mSpacePopupWindow;
    //间隔时间布局
    private LinearLayout mSpaceLinearLayout;
    //铃声dialog
    private AlertDialog mRingDialog;
    //铃声布局
    private LinearLayout mRingLinearLayout;
    //铃声列表加载器
    private ClockInfoRingAdapter mRingAdapter;
    //开关
    public static final String KEY_ON = "0";
    public static final String KEY_OFF = "1";
    //不同闹铃间的标识
    private int uuid;
    //标题文字
    private TextView tv_title;


    @Override
    public int setViewId() {
        return R.layout.activity_clock_info;
    }

    @Override
    public void initView() {
        requestCode = mGetRequestCode();
        myGetIntent();

//        tv_title = (TextView) findViewById(R.id.layout_title).findViewById(R.id.tv_title);
//        tv_title.setText(getResources().getString(R.string.clock_add));

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        timePicker = (TimePicker) findViewById(R.id.timePiker);
        timePicker.setIs24HourView(true);

        cb_sun = (CheckBox) findViewById(R.id.sun);
        cb_mon = (CheckBox) findViewById(R.id.mon);
        cb_tues = (CheckBox) findViewById(R.id.tues);
        cb_wed = (CheckBox) findViewById(R.id.wed);
        cb_thurs = (CheckBox) findViewById(R.id.thurs);
        cb_fri = (CheckBox) findViewById(R.id.fri);
        cb_sat = (CheckBox) findViewById(R.id.sat);
        mListCheckBox = new ArrayList<>();
        mListCheckBox.add(cb_sun);
        mListCheckBox.add(cb_mon);
        mListCheckBox.add(cb_tues);
        mListCheckBox.add(cb_wed);
        mListCheckBox.add(cb_thurs);
        mListCheckBox.add(cb_fri);
        mListCheckBox.add(cb_sat);

        mTag = (TextView) findViewById(R.id.mtag);
        mTagLinearLayout = (LinearLayout) findViewById(R.id.tag_ll);
        mTagLinearLayout.setOnClickListener(this);
        mSpace = (TextView) findViewById(R.id.mspace);
        mSpaceLinearLayout = (LinearLayout) findViewById(R.id.space_ll);
        mSpaceLinearLayout.setOnClickListener(this);
        mRing = (TextView) findViewById(R.id.mring);
        mRingLinearLayout = (LinearLayout) findViewById(R.id.ring_ll);
        mRingLinearLayout.setOnClickListener(this);

        mButtonUpdate = (Button) findViewById(R.id.clock_bt_update);
        button = (Button) findViewById(R.id.clock_bt);
        if (isAdd == true) {
            //保存
            button.setText("保存");
        } else {
            //删除
            button.setText("删除");
            button.setBackgroundResource(R.color.red);
            mButtonUpdate.setVisibility(View.VISIBLE);
            setClockToView();
        }

    }

    /**
     * 如果是编辑，则把传过来的实例属性赋值给View
     */
    private void setClockToView() {
        timePicker.setCurrentHour(Integer.parseInt(model.getTime().substring(0, 2)));
        timePicker.setCurrentMinute(Integer.parseInt(model.getTime().substring(3, 5)));

        for (int j = 0; j < model.getRepeat().split(",").length; j++) {
            for (int i = 0; i < mListCheckBox.size(); i++) {
                if (mListCheckBox.get(i).getTag().equals(model.getRepeat().split(",")[j])) {
                    mListCheckBox.get(i).setChecked(true);
                }
            }
        }

        mTag.setText(model.getTag());
        mSpace.setText(setSpaceText(model.getSpace()));
        mSpace.setTag(model.getSpace());
        mRing.setText(model.getRing());

    }

    /**
     * 间隔显示的文字
     *
     * @param space
     * @return
     */
    private String setSpaceText(String space) {
        if (space.equals("0")) {
            space = "关闭";
        } else if (space.equals("3")) {
            space = "3小时";
        } else if (space.equals("6")) {
            space = "6小时";
        } else if (space.equals("12")) {
            space = "12小时";
        }
        return space;
    }

    @Override
    public void initData() {

    }

    /**
     * 拿到当前存储的所有_id的最大值+1后作为下个闹铃的请求码
     */
    private int mGetRequestCode() {
        int uuid = 0;
        ClockDBUtil clockDBUtil = new ClockDBUtil(getApplicationContext());
        clockDBUtil.openDataBase();
        uuid = clockDBUtil.getLastId() + 1;
        clockDBUtil.closeDataBase();

        return uuid;
    }

    private void myGetIntent() {
        isAdd = true;
        intent = getIntent();
        isAdd = intent.getBooleanExtra("isAdd", true);
        if (isAdd == false) {
            model = intent.getParcelableExtra("clockInstance");
            L.i(TAG,"model------------->>>" + model);
        }

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
            case R.id.clock_bt:
                if (isAdd) {
                    //保存闹铃
                    getClockInstance();
                    setAlrm(requestCode);
                    saveClockToData(model);

                    this.finish();
                } else {
                    //删除闹铃
                    ClockDBUtil clockDBUtil = new ClockDBUtil(this);
                    clockDBUtil.openDataBase();
                    int result = clockDBUtil.deleteOneClock(model.getId());
                    clockDBUtil.closeDataBase();
                    if (result == 1) {
                        cancelAlrm(Integer.parseInt(model.getUuid()));
                        ClockInfoActivity.this.finish();
                    }
                }
                break;
            case R.id.clock_bt_update:
                //更新闹铃
                int id = model.getId();
                uuid = Integer.parseInt(model.getUuid());

                cancelAlrm(uuid);
                getClockInstance();//此处如果不先赋值下，下面设置闹铃时的重复日期就会是上次的重复日期
                setAlrm(uuid);
                this.finish();

                ClockDBUtil clockDBUtil = new ClockDBUtil(this);
                clockDBUtil.openDataBase();
                clockDBUtil.updateClock(getClockInstance(), id + "");
                clockDBUtil.closeDataBase();

                break;

            case R.id.tag_ll:
                //标签
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.tag_dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText mTagTv = (EditText) view.findViewById(R.id.tag_et);
                TextView mCancel = (TextView) view.findViewById(R.id.cancel_tv);
                TextView mOk = (TextView) view.findViewById(R.id.ok_tv);

                builder.setCancelable(false);
                mTagDialog = builder.show();
                mTagDialog.getWindow().setContentView(view);
                mTagDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTagDialog.dismiss();
                    }
                });
                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTag.setText(mTagTv.getText());
                        mTagDialog.dismiss();
                    }
                });
                break;
            case R.id.space_ll:
                //间隔时间
                LayoutInflater inflater2 = LayoutInflater.from(this);
                View view2 = inflater2.inflate(R.layout.space_popupwindow_layout, null);

                final TextView mSpaceTextView1 = (TextView) view2.findViewById(R.id.space_tv_1);
                final TextView mSpaceTextView2 = (TextView) view2.findViewById(R.id.space_tv_2);
                final TextView mSpaceTextView3 = (TextView) view2.findViewById(R.id.space_tv_3);
                final TextView mSpaceTextView4 = (TextView) view2.findViewById(R.id.space_tv_4);

                mSpaceTextView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpace.setText(mSpaceTextView1.getText());
                        mSpace.setTag(mSpaceTextView1.getTag());
                        mSpacePopupWindow.dismiss();
                    }
                });
                mSpaceTextView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpace.setText(mSpaceTextView2.getText());
                        mSpace.setTag(mSpaceTextView2.getTag());
                        mSpacePopupWindow.dismiss();
                    }
                });
                mSpaceTextView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpace.setText(mSpaceTextView3.getText());
                        mSpace.setTag(mSpaceTextView3.getTag());
                        mSpacePopupWindow.dismiss();
                    }
                });
                mSpaceTextView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpace.setText(mSpaceTextView4.getText());
                        mSpace.setTag(mSpaceTextView4.getTag());
                        mSpacePopupWindow.dismiss();
                    }
                });

                mSpacePopupWindow = new PopupWindow(this);
                //设置宽高
                mSpacePopupWindow.setWidth((getWindowManager()
                        .getDefaultDisplay().getWidth() - Dp2Px(this, 15 * 2f)) / 2);
                mSpacePopupWindow.setHeight(Dp2Px(this, 48 * 4f));
                //填充
                mSpacePopupWindow.setContentView(view2);
                //设置背景透明
                mSpacePopupWindow.setBackgroundDrawable(new ColorDrawable(0));
                //外部可控
                mSpacePopupWindow.setOutsideTouchable(true);
                //加了下面这行，onItemClick才好用
                mSpacePopupWindow.setFocusable(true);
                //在控件之上
                int[] location = new int[2];
                mSpaceLinearLayout.getLocationOnScreen(location);
                mSpacePopupWindow.showAtLocation(mSpaceLinearLayout,
                        Gravity.NO_GRAVITY, location[0] + mSpacePopupWindow.getWidth(),
                        location[1] - mSpacePopupWindow.getHeight());

                break;
            case R.id.ring_ll:
                //铃声
                LayoutInflater inflater3 = LayoutInflater.from(this);
                View view3 = inflater3.inflate(R.layout.ring_dialog_layout, null);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);

                ListView mRingListView = (ListView) view3.findViewById(R.id.ring_list_view);
                final List<String> mListRing = new ArrayList<>();

                mListRing.add(getResources().getString(R.string.ring1));
                mListRing.add(getResources().getString(R.string.ring2));
                mListRing.add(getResources().getString(R.string.ring3));
                mListRing.add(getResources().getString(R.string.ring4));
                mListRing.add(getResources().getString(R.string.ring5));
                mListRing.add(getResources().getString(R.string.ring6));
                mListRing.add(getResources().getString(R.string.ring7));
                mListRing.add(getResources().getString(R.string.ring8));
                mListRing.add(getResources().getString(R.string.ring9));
                mListRing.add(getResources().getString(R.string.ring10));

                mRingAdapter = new ClockInfoRingAdapter(this, mListRing);
                mRingListView.setAdapter(mRingAdapter);
                mRingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mRing.setText(mListRing.get(position));
                        mRingDialog.dismiss();
                    }
                });

                mRingDialog = builder3.show();
                mRingDialog.getWindow().setContentView(view3);
                mRingDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                break;
            case R.id.iv_back:
                this.finish();
                break;

            default:
                break;
        }
    }

    /**
     * dp 转化px
     *
     * @param context
     * @param dp
     * @return
     */
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 删除闹铃
     *
     * @param uuid
     */
    public void cancelAlrm(int uuid) {
        Intent intent = new Intent(ClockInfoActivity.this,
                TimerReceiver.class);
        intent.putExtra("uuid", uuid);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(ClockInfoActivity.this, uuid,
                        intent, 0);
        alarmManager.cancel(pendingIntent);
        L.i("hu","删除掉得闹铃的uuid====="+uuid);

    }

    /**
     * 设置闹铃
     *
     * @param uuid
     */
    public void setAlrm(int uuid) {
        // 建立Intent和PendingIntent来调用闹钟管理器
        Intent intent = new Intent(ClockInfoActivity.this, TimerReceiver.class);
        intent.setAction(TimerReceiver.ALARM_ACTION);
        intent.putExtra("uuid", uuid);
        Log.d("hu","新建的闹铃uuid===="+uuid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ClockInfoActivity.this, uuid, intent, 0);

        String flag = KEY_OFF;
        //看是否有间隔时间
        if (!model.getSpace().equals("0")) {//有间隔时间
            //计算今晚凌晨的时间
            long nextDayTime = ClockGetNextAlarm.getTonightLastTime(System.currentTimeMillis());
            //第一次响铃时间的毫秒
            long nextCurrentTimeMillis = ClockGetNextAlarm.getFirstAlarmTime(System.currentTimeMillis(),
                    model.getTime(), model.getSpace());

            if (nextCurrentTimeMillis <= nextDayTime) {//第二次响铃小于凌晨时间（在当天）
                //闹铃响
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                ClockGetNextAlarm.L("info下次闹铃时间是", nextCurrentTimeMillis);
                flag = KEY_ON;
            } else {//第二次响铃大于凌晨时间（不在当天，明天）
                //每周可以重复响铃的次数
                int counts = 7 * 24 / (Integer.parseInt(model.getSpace()));
                for (int i = 0; i < counts; i++) {
                    //下次响铃时间的毫秒
                    if (i == 0) {
                        //已经是下次响铃时间了
                        nextCurrentTimeMillis += 0;
                    } else {
                        nextCurrentTimeMillis += Integer.parseInt(model.getSpace()) * AlarmManager.INTERVAL_HOUR;
                    }
                    //下次响铃时间所在的日期是否包含在重复的日期里
                    boolean bl = ClockGetNextAlarm.isContainsInRepeat(nextCurrentTimeMillis,model.getRepeat());
                    if (bl){//包含
                        //设置响铃时间为此时的时间，并且跳出循环
                        alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                        ClockGetNextAlarm.L("info下次闹铃时间是", nextCurrentTimeMillis);
                        flag = KEY_ON;
                        break;
                    } else {//不包含
                        //继续循环
                        flag = KEY_OFF;
                    }
                }
            }
        }else {//没有间隔时间

            // 设置闹钟
            calendar.setTimeInMillis(ClockGetNextAlarm.getNextAlarmTime(
                    ClockGetNextAlarm.DATE_MODE_WEEK,
                    model.getRepeat(),
                    model.getTime()));
            if (calendar.getTimeInMillis() != 0) {//!=0 说明有重复日期
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                ClockGetNextAlarm.L("info下次闹铃时间是", calendar.getTimeInMillis());
                flag = KEY_ON;
            } else {//=0 说明没有重复日期
                // 设置日历的时间，主要是让日历的年月日和当前同步
                calendar.setTimeInMillis(System.currentTimeMillis());
                // 设置日历的小时和分钟
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                // 将秒和毫秒设置为0
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    ClockGetNextAlarm.L("info下次闹铃时间是", calendar.getTimeInMillis());
                    flag = KEY_ON;
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY,
                            pendingIntent);
                    ClockGetNextAlarm.L("info下次闹铃时间是", calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
                    flag = KEY_ON;
                }
            }
        }
        model.setmSwitch(flag);
    }

    /**
     * 给闹钟的各个属性赋值,用以保存
     *
     * @return
     */
    private ClockModel getClockInstance() {

        if (isAdd){
            model = new ClockModel();
            model.setUuid(requestCode + "");
            model.setmSwitch(KEY_ON);
        }else {
            model.setUuid(uuid+"");
        }

        model.setTime(intToStr(timePicker.getCurrentHour()) + ":" + intToStr(timePicker.getCurrentMinute()));
        Log.d("hu", "time=====" + model.getTime());

        model.setRepeat(mGetTag(cb_sun) + "," +
                mGetTag(cb_mon) + "," +
                mGetTag(cb_tues) + "," +
                mGetTag(cb_wed) + "," +
                mGetTag(cb_thurs) + "," +
                mGetTag(cb_fri) + "," +
                mGetTag(cb_sat));
        model.setTag(mTag.getText().toString());
        model.setSpace(mSpace.getTag().toString());
        model.setRing(mRing.getText().toString());


        return model;
    }

    /**
     * CheckBox是否选中，重复日期选中了哪天
     *
     * @param cb
     * @return
     */
    private String mGetTag(CheckBox cb) {

        String tag;
        if (cb.isChecked() == false) {
            tag = "7";
        } else {
            tag = cb.getTag().toString();
        }
        return tag;
    }

    /**
     * 时间补全（2:7--->02:07）
     *
     * @param ii
     * @return
     */
    private String intToStr(int ii) {
        String i = ii + "";
        String s = "0";
        if (i.length() == 1) {
            s += i;
        } else {
            s = i;
        }
        return s;
    }

    /**
     * 保存闹铃
     */
    private void saveClockToData(ClockModel model) {
        ClockDBUtil clockDBUtil = new ClockDBUtil(getApplicationContext());
        clockDBUtil.openDataBase();
        clockDBUtil.addClock(model);
        clockDBUtil.closeDataBase();
    }
}
