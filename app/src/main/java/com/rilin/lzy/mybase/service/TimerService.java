package com.rilin.lzy.mybase.service;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.model.ClockModel;
import com.rilin.lzy.mybase.receiver.AlarmAlertWakeLock;
import com.rilin.lzy.mybase.receiver.ClockGetNextAlarm;
import com.rilin.lzy.mybase.util.ClockDBUtil;
import com.rilin.lzy.mybase.util.L;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lzy on 16/9/30.
 */
public class TimerService extends Service {

    public static final String ALARM_SERVICE_ACTION = "com.rilintech.hxk_301.service.AlarmService";
    public static final int NotificationToService = -2;
    public static final String KEY_ON = "0";
    public static final String KEY_OFF = "1";
    //每个铃声的uuid
    private int mClockUuid;
    //第二次显示闹铃弹框
    private int NTS;
    //当前响铃的实例
    private ClockModel clockModel;
    //保存音乐的map
    private Map<String, Integer> map;
    //音乐播放器
    private MediaPlayer alarmMusic;
    //系统震动
    private Vibrator mVibrator;
    //音频管理器
    private AudioManager mAudioManager;
    //当前系统音量
    private int current;
    //系统最大音量
    private int max;
    //
    private boolean isPlaying;

    private MyHandler handler;
    //闹铃弹框
    private static AlertDialog dialog;
    private static TextView clock_dialog_tag;
    private static TextView clock_dialog_time;
    private static TextView clock_dialog_ok;
    //唤醒屏幕
    private KeyguardManager keyguardManager;
    private KeyguardManager.KeyguardLock keyguardLock;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //收到客户端触发，如果服务尚未启动，则先执行onCreate()，再此进行服务初始化处理。
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //在收到stopService()时触发，我们将在此处理停止Service的相关事宜，例如停止后台线程的运行等等。
    @Override
    public void onDestroy() {
        //释放wakeLock
        wakeLock.release();
        super.onDestroy();
    }

    /**
     * 当系统内存少时，将要求终止服务，我们在此进行关闭服务的处理，例如保持相关的状态等,
     * 如服务部需保持特定状态，则无需重写此方法。
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 收到客户端请求时触发，由于onStartCommand()运行在主线程，将进行本次服务的初始化.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //手动返回START_STICKY，当service因内存不足被kill，当内存又有的时候，service又被重新创建，
        // 但是传入null的Intent
        flags = START_STICKY_COMPATIBILITY;

        if (intent != null) {
            if(ALARM_SERVICE_ACTION.equals(intent.getAction())){
                //cpu唤醒
                AlarmAlertWakeLock.acquireCpuWakeLock(this);
                //拿到传过来的数据
                myGetIntentData(intent);
                //不相等，表示是从Notification点击打开的，所以不再需要响铃和设置下次闹铃等
                if (NTS != NotificationToService) {
                    //点亮屏幕
                    openScreen();
                    //启动一分钟响铃计时
                    new Thread(new MyThread()).start();
                    //设置下次闹铃时间
                    setNextAlarm();
                    //给音乐map赋值
                    setMapData();
                    //设置闹铃响
                    setAlarm();
                }
                //弹出闹铃提示
                showDialog();

                //注册广播
                registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                        Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 拿到传过来的数据
     *
     * @param intent
     */
    private void myGetIntentData(Intent intent) {
        mClockUuid = intent.getIntExtra("uuid", -1);
        try {
            NTS = intent.getIntExtra("NTS", -1);
        } catch (Exception e) {

        }
        L.i("hu------", "传过来的uuid===" + mClockUuid);
        ClockDBUtil clockDBUtil = new ClockDBUtil(this);
        clockDBUtil.openDataBase();
        clockModel = clockDBUtil.getOneClockByUuid(mClockUuid + "");
        clockDBUtil.closeDataBase();
    }

    /**
     * 点亮屏幕
     */
    private void openScreen() {
        //获取电源管理器对象
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        //点亮屏幕
        wakeLock.acquire();
        //得到键盘锁管理器对象
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock("unLock");
        //解锁
        keyguardLock.disableKeyguard();
    }



    /**
     * 一分钟后停止响铃和振动
     */
    private class MyHandler extends Handler {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    try {
                        isPlaying = alarmMusic.isPlaying();
                    } catch (Exception e) {
                        alarmMusic = null;
                        alarmMusic = new MediaPlayer();
                    }
                    if (isPlaying) {
                        alarmMusic.stop();
                        alarmMusic.reset();
                        alarmMusic.release();
                    }
                    mVibrator.cancel();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 响铃计时线程（一分钟）停止响铃
     *
     * @author ykt003
     */
    public class MyThread implements Runnable {
        @Override
        public void run() {
            Looper.prepare();
            try {
                Thread.sleep(60 * 1000);
                Message message = new Message();
                message.what = 1;
                handler = new MyHandler();
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Looper.loop();
        }
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.clock_dialog_layout, null);

        clock_dialog_tag = (TextView) view.findViewById(R.id.clock_dialog_tag);
        clock_dialog_time = (TextView) view.findViewById(R.id.clock_dialog_alarm);
        clock_dialog_ok = (TextView) view.findViewById(R.id.clock_dialog_ok);

        try {
            clock_dialog_tag.setText(clockModel.getTag());
            clock_dialog_time.setText(clockModel.getTime());
        } catch (Exception e) {
            clock_dialog_tag.setText("闹铃");
            clock_dialog_time.setText("12：00");
        }

        clock_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    isPlaying = alarmMusic.isPlaying();
                }catch (Exception e){
                    alarmMusic = null;
                    alarmMusic = new MediaPlayer();
                }

                if (isPlaying) {
                    alarmMusic.stop();
                    alarmMusic.reset();
                    alarmMusic.release();
                }
                //恢复系统之前的音量
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
                mVibrator.cancel();
                dialog.dismiss();
                unregisterReceiver(mHomeKeyEventReceiver);
                AlarmAlertWakeLock.releaseCpuLock();

            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        dialog.getWindow().setContentView(view);
    }

    /**
     * 存入音乐数据
     */
    private void setMapData() {

        map = new HashMap<>();

        map.put(getResources().getString(R.string.ring1), R.raw.a);
        map.put(getResources().getString(R.string.ring2), R.raw.b);
        map.put(getResources().getString(R.string.ring3), R.raw.c);
        map.put(getResources().getString(R.string.ring4), R.raw.d);
        map.put(getResources().getString(R.string.ring5), R.raw.e);
        map.put(getResources().getString(R.string.ring6), R.raw.f);
        map.put(getResources().getString(R.string.ring7), R.raw.g);
        map.put(getResources().getString(R.string.ring8), R.raw.h);
        map.put(getResources().getString(R.string.ring9), R.raw.i);
        map.put(getResources().getString(R.string.ring10), R.raw.j);
    }

    /**
     * 设置闹铃
     */
    private void setAlarm() {

        alarmMusic = MediaPlayer.create(this, R.raw.a);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);

        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            try {
                if (clockModel.getRing().equals(key)) {
                    alarmMusic = MediaPlayer.create(this, map.get(key));
                }
            } catch (Exception e) {
                alarmMusic = MediaPlayer.create(this, R.raw.a);
            }
        }
        // 设置为循环播放
        alarmMusic.setLooping(true);
        // 获取震动
        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        //震动时长与音乐播放时长一样,放在MediaPlayer.create()之后，不然报错null object reference
        //mVibrator.vibrate(alarmMusic.getDuration());
        mVibrator.vibrate(new long[]{1000, 2000, 1000, 2000, 1000}, 0);
        //调节到最大音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, AudioManager.FLAG_PLAY_SOUND);

        // 闹铃响
        alarmMusic.start();
    }

    /**
     * 设置下次闹铃响铃时间
     */
    private void setNextAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        // 建立Intent和PendingIntent来调用闹钟管理器
        Intent intent = new Intent(this, TimerService.class);
        intent.setAction(TimerService.ALARM_SERVICE_ACTION);
        intent.putExtra("uuid", Integer.parseInt(clockModel.getUuid()));
        L.i("hu", "设置下次响铃的uuid====" + Integer.parseInt(clockModel.getUuid()));
        PendingIntent pendingIntent = PendingIntent.getService(this,
                Integer.parseInt(clockModel.getUuid()), intent, 0);

        String flag = KEY_OFF;
        //看是否有间隔时间
        if (!clockModel.getSpace().equals("0")) {//有间隔时间
            //计算今晚凌晨的时间
            long nextDayTime = ClockGetNextAlarm.getTonightLastTime(System.currentTimeMillis());
            //下次响铃时间的毫秒
            long nextCurrentTimeMillis = ClockGetNextAlarm.getNextAlarm(System.currentTimeMillis(),
                    clockModel.getTime(), clockModel.getSpace());

            if (nextCurrentTimeMillis <= nextDayTime) {//第二次响铃小于凌晨时间（在当天）
                //闹铃响
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                flag = KEY_ON;
                //数据库显示的下一次闹钟的时间
                ClockGetNextAlarm.L("showActivity", nextCurrentTimeMillis);

            } else {//第二次响铃大于凌晨时间（不在当天，明天）
                //每周可以重复响铃的次数
                int counts = 7 * 24 / (Integer.parseInt(clockModel.getSpace()));
                for (int i = 0; i < counts; i++) {
                    //下次响铃时间的毫秒
                    if (i == 0) {
                        //已经是下次响铃时间了
                        nextCurrentTimeMillis += 0;
                    } else {
                        nextCurrentTimeMillis += Integer.parseInt(clockModel.getSpace()) * AlarmManager.INTERVAL_HOUR;
                    }
                    //下次响铃时间所在的日期是否包含在重复的日期里
                    boolean bl = ClockGetNextAlarm.isContainsInRepeat(nextCurrentTimeMillis, clockModel.getRepeat());
                    if (bl) {//包含
                        //设置响铃时间为此时的时间，并且跳出循环
                        alarmManager.set(AlarmManager.RTC_WAKEUP, nextCurrentTimeMillis, pendingIntent);
                        ClockGetNextAlarm.L("showActivity", nextCurrentTimeMillis);
                        flag = KEY_ON;
                        break;
                    } else {//不包含
                        //继续循环
                        flag = KEY_OFF;
                    }
                }
            }
        } else {//无间隔时间

            if (clockModel.getRepeat().equals("7,7,7,7,7,7,7")) {//代表没有重复日期
                //开关要关掉
                flag = KEY_OFF;
            } else {//有重复日期
                calendar.setTimeInMillis(ClockGetNextAlarm.getNextAlarmTime(
                        ClockGetNextAlarm.DATE_MODE_WEEK,
                        clockModel.getRepeat(),
                        clockModel.getTime()));
                //设置闹铃
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                ClockGetNextAlarm.L("showActivity", calendar.getTimeInMillis());
                //开关打开
                flag = KEY_ON;
            }
        }

        clockModel.setmSwitch(flag);
        //更新开关状态
        ClockDBUtil clockDBUtil = new ClockDBUtil(this);
        clockDBUtil.openDataBase();
        clockDBUtil.updateMSwitch(Integer.parseInt(clockModel.getmSwitch()),
                clockModel.getId());
        clockDBUtil.closeDataBase();
    }

    /**
     * home键的监听
     */
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    Intent it = new Intent(context, TimerService.class);
                    it.putExtra("NTS", NotificationToService);
                    it.putExtra("uuid", mClockUuid);
                    it.setAction(ALARM_SERVICE_ACTION);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getService(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setTicker(clockModel.getTag());
                    builder.setContentTitle(clockModel.getTime());
                    builder.setContentText(clockModel.getTag());
                    builder.setContentIntent(pendingIntent);

                    Notification notification = builder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;

                    notificationManager.notify(0, notification);

                    //关闭闹铃提示窗
                    if (NTS == NotificationToService) {
                        dialog.dismiss();
                        unregisterReceiver(mHomeKeyEventReceiver);
                    } else {
                        try {
                            isPlaying = alarmMusic.isPlaying();
                        }catch (Exception e){
                            alarmMusic = null;
                            alarmMusic = new MediaPlayer();
                        }
                        if (isPlaying) {
                            alarmMusic.stop();
                            alarmMusic.reset();
                            alarmMusic.release();
                        }
                        //恢复系统之前的音量
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
                        mVibrator.cancel();
                        dialog.dismiss();
                        unregisterReceiver(mHomeKeyEventReceiver);
                        AlarmAlertWakeLock.releaseCpuLock();
                        //释放wakeLock
                        wakeLock.release();
                    }

                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };


}
