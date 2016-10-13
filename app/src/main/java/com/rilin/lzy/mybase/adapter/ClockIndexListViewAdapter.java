package com.rilin.lzy.mybase.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.model.ClockModel;
import com.rilin.lzy.mybase.my.alarm_timer.ClockInfoActivity;
import com.rilin.lzy.mybase.receiver.ClockGetNextAlarm;
import com.rilin.lzy.mybase.receiver.TimerReceiver;
import com.rilin.lzy.mybase.util.ClockDBUtil;
import com.rilin.lzy.mybase.util.L;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lzy on 16/9/30.
 */
public class ClockIndexListViewAdapter extends BaseAdapter {
    //所有闹铃集合
    private List<ClockModel> mListClock;
    private Context context;
    private List<String> list_clock;
    private List<String> list_beizhu;
    private List<String> list_bool;
    private LayoutInflater mInflater;
    public static final String KEY_ON = "0";
    public static final String KEY_OFF = "1";

    public ClockIndexListViewAdapter(Context context,
                                     List<String> list_clock,
                                     List<String> list_beizhu,
                                     List<String> list_bool) {
        this.context = context;
        this.list_clock = list_clock;
        this.list_beizhu = list_beizhu;
        this.list_bool = list_bool;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list_clock.size();
    }
    @Override
    public Object getItem(int position) {
        return list_clock.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview_clock_index,null);
            viewHolder = new ViewHolder();

            viewHolder.clock = (TextView) convertView.findViewById(R.id.clock);
            //标签,显示
            viewHolder.beizhu = (TextView) convertView.findViewById(R.id.beizhu);
            viewHolder.mSwitch = (Switch) convertView.findViewById(R.id.my_wsitch);

            viewHolder.clock.setTag(position);
            viewHolder.mSwitch.setTag(position);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.clock.setTag(position);
            viewHolder.mSwitch.setTag(position);
        }

        //数据库查询
        ClockDBUtil clockDBUtil = new ClockDBUtil(context);
        clockDBUtil.openDataBase();
        mListClock = clockDBUtil.getAllClock();
        clockDBUtil.closeDataBase();
        viewHolder.clock.setText(list_clock.get(position) + "");
        viewHolder.beizhu.setText(list_beizhu.get(position) + getiInfomation(mListClock.get(position)));
        viewHolder.mSwitch.setChecked(strToBool(list_bool.get(position)));

        //设置字体颜色
        if(viewHolder.mSwitch.isChecked()){
            viewHolder.beizhu.setTextColor(context.getResources().getColor(R.color.c666));
            viewHolder.clock.setTextColor(context.getResources().getColor(R.color.c666));
        }else {
            viewHolder.clock.setTextColor(Color.parseColor("#ffb9b9b9"));
            viewHolder.beizhu.setTextColor(Color.parseColor("#ffb9b9b9"));
        }

        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ClockDBUtil clockDBUtil = new ClockDBUtil(context);
                clockDBUtil.openDataBase();
                mListClock = clockDBUtil.getAllClock();
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();
                //建立Intent和PendingIntent 来调用闹钟管理器
                Intent intent = new Intent(context, TimerReceiver.class);
                intent.setAction(TimerReceiver.ALARM_ACTION);
                intent.putExtra("uuid", Integer.parseInt(mListClock.get(position).getUuid()));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        Integer.parseInt(mListClock.get(position).getUuid()), intent, 0);

                if (isChecked) {
                    //给开启服务的闹铃字体设置颜色
                    finalViewHolder1.beizhu.setTextColor(Color.parseColor("#ff636363"));
                    finalViewHolder1.clock.setTextColor(Color.parseColor("#ff636363"));

                    clockDBUtil.updateMSwitch(Integer.parseInt(ClockInfoActivity.KEY_ON),
                            mListClock.get(position).getId());


                    //看是否有间隔时间
                    if (!mListClock.get(position).getSpace().equals("0")) {//有间隔时间
                        //计算今晚凌晨的时间
                        long nextDayTime = ClockGetNextAlarm.getTonightLastTime(System.currentTimeMillis());
                        //第一次响铃时间的毫秒
                        long nextCurrentTimeMillis = ClockGetNextAlarm.getFirstAlarmTime(System.currentTimeMillis(),
                                mListClock.get(position).getTime(), mListClock.get(position).getSpace());

                        if (nextCurrentTimeMillis <= nextDayTime) {//第二次响铃小于凌晨时间（在当天）
                            //闹铃响
                            alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                            buttonView.setChecked(true);
                            clockDBUtil.updateMSwitch(Integer.parseInt(ClockInfoActivity.KEY_ON),
                                    mListClock.get(position).getId());
                            finalViewHolder1.beizhu.setTextColor(Color.parseColor("#ff636363"));
                            finalViewHolder1.clock.setTextColor(Color.parseColor("#ff636363"));
                            ClockGetNextAlarm.L("adapter下次闹铃时间是", nextCurrentTimeMillis);
                        } else {//第二次响铃大于凌晨时间（不在当天，明天）
                            //每周可以重复响铃的次数
                            int counts = 7 * 24 / (Integer.parseInt(mListClock.get(position).getSpace()));
                            for (int i = 0; i < counts; i++) {
                                //下次响铃时间的毫秒
                                if (i == 0) {
                                    //已经是下次响铃时间了
                                    nextCurrentTimeMillis += 0;
                                } else {
                                    nextCurrentTimeMillis += Integer.parseInt(mListClock.get(position).getSpace()) * AlarmManager.INTERVAL_HOUR;
                                }
                                //下次响铃时间所在的日期是否包含在重复的日期里
                                boolean bl = ClockGetNextAlarm.isContainsInRepeat(nextCurrentTimeMillis,mListClock.get(position).getRepeat());
                                if (bl){//包含
                                    //设置响铃时间为此时的时间，并且跳出循环
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                                    buttonView.setChecked(true);
                                    clockDBUtil.updateMSwitch(Integer.parseInt(ClockInfoActivity.KEY_ON),
                                            mListClock.get(position).getId());
                                    finalViewHolder1.beizhu.setTextColor(Color.parseColor("#ff636363"));
                                    finalViewHolder1.clock.setTextColor(Color.parseColor("#ff636363"));
                                    ClockGetNextAlarm.L("adapter下次闹铃时间是", nextCurrentTimeMillis);
                                    break;
                                } else {//不包含
                                    //继续循环
                                    //到此没有设置闹铃，开关状态为关闭
                                    buttonView.setChecked(false);
                                    clockDBUtil.updateMSwitch(Integer.parseInt(ClockInfoActivity.KEY_OFF),
                                            mListClock.get(position).getId());
                                    finalViewHolder1.beizhu.setTextColor(Color.parseColor("#ffb9b9b9"));
                                    finalViewHolder1.clock.setTextColor(Color.parseColor("#ffb9b9b9"));
                                }
                            }
                        }
                    } else {//没有间隔时间
                        // 设置闹钟
                        calendar.setTimeInMillis(ClockGetNextAlarm.getNextAlarmTime(
                                ClockGetNextAlarm.DATE_MODE_WEEK,
                                mListClock.get(position).getRepeat(),
                                mListClock.get(position).getTime()));

                        if (calendar.getTimeInMillis() != 0) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    calendar.getTimeInMillis(),
                                    pendingIntent);
                            ClockGetNextAlarm.L("adapter下次闹铃时间是", calendar.getTimeInMillis());
                        } else {
                            // 设置日历的时间，让日历的年月日和当前同步
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            // 设置日历的小时和分钟
                            calendar.set(Calendar.HOUR_OF_DAY,
                                    Integer.parseInt(mListClock.get(position).getTime().substring(0, 2)));
                            calendar.set(Calendar.MINUTE,
                                    Integer.parseInt(mListClock.get(position).getTime().substring(3, 5)));
                            // 将秒和毫秒设置为0
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            //判断是今天响铃还是明天响铃
                            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                                //响铃时间大于系统当前时间，今天响铃
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                ClockGetNextAlarm.L("adapter下次闹铃时间是", calendar.getTimeInMillis());
                            } else {
                                //响铃时间小于系统当前时间，明天响铃
                                alarmManager.set(AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY,
                                        pendingIntent);
                                ClockGetNextAlarm.L("adapter下次闹铃时间是", calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
                            }
                        }
                    }
                    L.i("hu", "开启的闹铃的uuid====" + Integer.parseInt(mListClock.get(position).getUuid()));
                    L.i("hu", "isChecked===true=" + position);
                } else {
                    //未开启的闹铃设置颜色
                    finalViewHolder1.beizhu.setTextColor(Color.parseColor("#ffb9b9b9"));
                    finalViewHolder1.clock.setTextColor(Color.parseColor("#ffb9b9b9"));

                    clockDBUtil.updateMSwitch(Integer.parseInt(ClockInfoActivity.KEY_OFF),
                            mListClock.get(position).getId());

                    alarmManager.cancel(pendingIntent);
                    L.i("hu","取消掉得闹铃的uuid===="+Integer.parseInt(mListClock.get(position).getUuid()));
                    L.i("hu", "isChecked==false==" + position);
                }
                clockDBUtil.closeDataBase();
            }
        });

        final ViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockDBUtil clockDBUtil = new ClockDBUtil(context);
                clockDBUtil.openDataBase();
                mListClock = clockDBUtil.getAllClock();
                clockDBUtil.closeDataBase();

                Intent intent = new Intent(context, ClockInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("clockInstance", mListClock.get(Integer.parseInt(
                        finalViewHolder.clock.getTag().toString())));
                L.i("---ClockInfoActivity----clockInstance---->>>>" + mListClock.get(Integer.parseInt(
                        finalViewHolder.clock.getTag().toString())));
                bundle.putBoolean("isAdd", false);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {

        TextView clock;
        TextView beizhu;
        Switch mSwitch;

    }

    /**
     * 转化成Boolean类型
     *
     * @param s
     * @return
     */
    protected Boolean strToBool(String s) {
        boolean flag = false;

        if ("0".equals(s)) {
            flag = true;
        } else if ("1".equals(s)) {
            flag = false;
        }
        return flag;
    }


    /**
     * 设置显示信息
     *
     * @param model
     * @return
     */
    private String getiInfomation(ClockModel model) {

        String mSpace = model.getSpace();
        if (mSpace.equals("0")) {
            mSpace = "  间隔  关  ";
        } else {
            mSpace = "  间隔  " + model.getSpace() + "小时 ";
        }

        String mRepeat = model.getRepeat();
        String mRepeat2 = "周";
        for (int i = 0; i < mRepeat.split(",").length; i++) {
            String s = mRepeat.split(",")[i];
            if (s.equals("0")) {
                s = "日 ";
            }
            if (s.equals("1")) {
                s = "一 ";
            }
            if (s.equals("2")) {
                s = "二 ";
            }
            if (s.equals("3")) {
                s = "三 ";
            }
            if (s.equals("4")) {
                s = "四 ";
            }
            if (s.equals("5")) {
                s = "五 ";
            }
            if (s.equals("6")) {
                s = "六 ";
            }
            if (s.equals("7")) {
                s = "";
            }
            mRepeat2 += s;
        }
        if (mRepeat2.equals("周")) {
            mRepeat2 = "";
        }

        return mSpace + mRepeat2;
    }
}
