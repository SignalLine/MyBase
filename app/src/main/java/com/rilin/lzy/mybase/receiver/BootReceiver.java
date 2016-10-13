package com.rilin.lzy.mybase.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rilin.lzy.mybase.model.ClockModel;
import com.rilin.lzy.mybase.util.ClockDBUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 设备关机重启后调用类
 * Created by lzy on 16/9/30.
 */
public class BootReceiver extends BroadcastReceiver {
    //所有闹铃的集合
    private List<ClockModel> mListClock;
    //闹钟管理者
    private AlarmManager alarmManager;
    //指定启动AlarmActivity组件
    private Intent intents;
    //等到服务的对象
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            //重新计算闹铃时间，并调前面的方法设置闹铃时间及闹铃间隔时间

            ClockDBUtil clockDBUtil = new ClockDBUtil(context);
            clockDBUtil.openDataBase();
            mListClock = clockDBUtil.getAllClock();
            clockDBUtil.closeDataBase();
            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            for (ClockModel model : mListClock) {

                if (model.getmSwitch().equals("0")) {

                    /*Calendar calendar = Calendar.getInstance();
                    // 设置日历的时间，主要是让日历的年月日和当前同步
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    // 设置日历的小时和分钟
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(model.getTime().substring(0, 2)));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(model.getTime().substring(3, 5)));
                    // 将秒和毫秒设置为0
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    // 建立Intent和PendingIntent来调用闹钟管理器
                    intents = new Intent(context, TimerReceiver.class);
                    intents.putExtra("uuid", Integer.parseInt(model.getUuid()));
                    pendingIntent = PendingIntent.getBroadcast(context,
                            Integer.parseInt(model.getUuid()), intents, 0);
                    //响铃
                    compareTime(System.currentTimeMillis(), calendar.getTimeInMillis(), pendingIntent,
                            calendar, alarmManager, model);*/

                    Calendar calendar = Calendar.getInstance();
                    // 建立Intent和PendingIntent来调用闹钟管理器
                    intents = new Intent(context, TimerReceiver.class);
                    intents.setAction(TimerReceiver.ALARM_ACTION);
                    intents.putExtra("uuid", Integer.parseInt(model.getUuid()));
                    pendingIntent = PendingIntent.getBroadcast(context,
                            Integer.parseInt(model.getUuid()), intents, 0);

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
                                    ClockGetNextAlarm.L("BootReceivcer下次闹铃时间是", nextCurrentTimeMillis);
                                    break;
                                } else {//不包含
                                    //继续循环
                                }
                            }
                        }

                    } else {//没有间隔时间
                        // 设置闹钟
                        calendar.setTimeInMillis(ClockGetNextAlarm.getNextAlarmTime(
                                ClockGetNextAlarm.DATE_MODE_WEEK,
                                model.getRepeat(),
                                model.getTime()));

                        if (calendar.getTimeInMillis() != 0) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    calendar.getTimeInMillis(),
                                    pendingIntent);
                        } else {
                            // 设置日历的时间，让日历的年月日和当前同步
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            // 设置日历的小时和分钟
                            calendar.set(Calendar.HOUR_OF_DAY,
                                    Integer.parseInt(model.getTime().substring(0, 2)));
                            calendar.set(Calendar.MINUTE,
                                    Integer.parseInt(model.getTime().substring(3, 5)));
                            // 将秒和毫秒设置为0
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            //判断是今天响铃还是明天响铃
                            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                                //响铃时间大于系统当前时间，今天响铃
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                //响铃时间小于系统当前时间，明天响铃
                                alarmManager.set(AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY,
                                        pendingIntent);
                            }
                        }
                    }
                } else {

                }
            }
        }
    }

    /**
     * @param systemCurrentTimeMillis 当前系统时间
     * @param currentTimeMillis       秒 和 毫秒 都归0的时间  整时响铃时间
     * @param pendingIntent
     */
    public void compareTime(long systemCurrentTimeMillis, long currentTimeMillis,
                            PendingIntent pendingIntent, Calendar calendar, AlarmManager alarmManager,
                            ClockModel model) {

        if (systemCurrentTimeMillis <= currentTimeMillis) {//系统时间小于等于正时响铃时间
            //闹铃响
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis, pendingIntent);
        } else if (systemCurrentTimeMillis > currentTimeMillis) {//系统时间大于正时响铃时间
            //如果间隔时间和重复都没有，就直接响一次 ，  "7,7,7,7,7,7,7"表示没有重复日
            if (model.getSpace().equals("0") && model.getRepeat().equals("7,7,7,7,7,7,7")) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis + AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                //看是否有间隔时间
                if (!model.getSpace().equals("0")) {//有间隔时间
                    //时间设置为下次响铃，也就是第一次响铃加上间隔时间
                    //calendar.set(Calendar.HOUR_OF_DAY,
                    //        Integer.parseInt(model.getTime().substring(0, 2)) + Integer.parseInt(model.getSpace()));

                    //计算今晚凌晨的时间
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    long nextDayTime = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY;
                    //下次响铃时间的毫秒
                    int ii = 0;
                    while (true) {
                        ii += 1;
                        calendar.set(Calendar.HOUR_OF_DAY,
                                Integer.parseInt(model.getTime().substring(0, 2)) + ii * Integer.parseInt(model.getSpace()));
                        if (calendar.getTimeInMillis() > systemCurrentTimeMillis) {

                            break;
                        }

                    }
                    long nextCurrentTimeMillis = calendar.getTimeInMillis();

                    if (nextCurrentTimeMillis <= nextDayTime) {//第二次响铃小于凌晨时间（在当天）
                        //闹铃响
                        alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                    } else {//第二次响铃大于凌晨时间（不在当天，明天）
                        /**
                         * 此处循环判断一周之内，每次响铃是否都在重复的星期内
                         */
                        //每周可以重复响铃的次数
                        int counts = 7 * 24 / (Integer.parseInt(model.getSpace()));
                        for (int i = 0; i < counts - ii; i++) {
                            //下次响铃时间的毫秒
                            nextCurrentTimeMillis += i * Integer.parseInt(model.getSpace()) * AlarmManager.INTERVAL_HOUR;
                            //设置时间为下次响铃的时间
                            calendar.setTimeInMillis(nextCurrentTimeMillis);
                            //获取下次响铃时间是周几,返回的数组是{1234567}表示：星期天，星期一，星期二，星期三，星期四，星期五，星期六
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                            //获取重复的日期的集合
                            List<String> listDays = new ArrayList<>();
                            for (int j = 0; j < model.getRepeat().split(",").length; j++) {
                                listDays.add(model.getRepeat().split(",")[j]);
                            }
                            Log.d("hu", "adapter=switch=" + listDays.contains(dayOfWeek + "") + "");
                            //下次响铃时间所在的日期是否包含在重复的日期里
                            if (listDays.contains(dayOfWeek + "")) {//包含
                                //设置响铃时间为此时的时间，并且跳出循环
                                alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                                break;
                            } else {//不包含
                                //继续循环
                            }
                        }
                    }

                } else {//没有间隔时间
                    for (int i = 1; i < 8; i++) {
                        //下次响铃时间，没有间隔时间，下次就是明天
                        //AlarmManager.INTERVAL_DAY=24 * 60 * 60 * 1000;
                        long nextDayClockTime = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY;
                        //设置时间为明天响铃时间
                        calendar.setTimeInMillis(nextDayClockTime);
                        //获取下次响铃时间是周几
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        //获取重复的日期的集合
                        List<String> listDays = new ArrayList<>();
                        for (int j = 0; j < model.getRepeat().split(",").length; j++) {
                            listDays.add(model.getRepeat().split(",")[j]);
                        }
                        //下次响铃时间所在的日期是否包含在重复的日期里
                        if (listDays.contains(dayOfWeek + "")) {//包含
                            //设置响铃时间为此时的时间，并且跳出循环
                            alarmManager.set(AlarmManager.RTC_WAKEUP, nextDayClockTime, pendingIntent);
                            break;
                        } else {//不包含
                            //继续循环
                        }

                    }
                }
            }
        }
    }
}
