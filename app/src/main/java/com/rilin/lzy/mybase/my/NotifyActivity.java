package com.rilin.lzy.mybase.my;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.rilin.lzy.mybase.MainActivity;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;

import java.io.File;


public class NotifyActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_show;
    private Button btn_bigstyle_show;
    private Button btn_show_progress;
    private Button btn_show_cz;
    private Button btn_clear;
    private Button btn_clear_all;
    private Button btn_show_custom;
    private Button btn_show_intent_act;
    private Button btn_show_intent_apk;
    NotificationCompat.Builder mBuilder;
    int notifyId = 100;

    @Override
    public int setViewId() {
        return R.layout.activity_notify;
    }

    @Override
    public void initView() {
        btn_show = (Button)findViewById(R.id.btn_show);
        btn_bigstyle_show = (Button)findViewById(R.id.btn_bigstyle_show);
        btn_show_progress = (Button)findViewById(R.id.btn_show_progress);
        btn_show_cz = (Button)findViewById(R.id.btn_show_cz);
        btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_clear_all = (Button)findViewById(R.id.btn_clear_all);
        btn_show_custom = (Button)findViewById(R.id.btn_show_custom);
        btn_show_intent_act = (Button)findViewById(R.id.btn_show_intent_act);
        btn_show_intent_apk = (Button)findViewById(R.id.btn_show_intent_apk);


        btn_show.setOnClickListener(this);
        btn_bigstyle_show.setOnClickListener(this);
        btn_show_progress.setOnClickListener(this);
        btn_show_cz.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_clear_all.setOnClickListener(this);
        btn_show_custom.setOnClickListener(this);
        btn_show_intent_act.setOnClickListener(this);
        btn_show_intent_apk.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("测试标题")//设置通知栏标题
                .setContentText("测试内容")//设置通知栏显示内容
                .setContentIntent(getDefaultIntent(Notification.FLAG_AUTO_CANCEL))//设置通知栏点击意图
//				.setNumber(number)//设置通知集合的数量
                .setTicker("测试通知来了啊")//通知首次出现在通知栏,带上升效果
                .setWhen(System.currentTimeMillis())//通知产生的时间,在通知信息里面显示,一般是获取到系统时间
                .setPriority(Notification.PRIORITY_DEFAULT)//设置通知优先级
//				.setAutoCancel(true)//设置这个标志用户单击面板就可以让通知将自动取消
                .setOngoing(false)//true 设置他为一个正在进行的通知,他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式在等待
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音,闪灯震动效果的最简单最一致的方式是使用当前的用户默认设置使用default属性
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小Icon
    }

    public void showNotify(){
        mBuilder.setContentText("showNotify标题")
                .setContentText("showNotify内容")
                .setTicker("showNotify通知");
        mNotificationManager.notify(notifyId,mBuilder.build());
    }

    public void showBigStyleNotify(){
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle("showBigStyleNotify~~~~~");
        String[] events = new String[5];
        //Moves enents into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        mBuilder.setContentTitle("showBigStyleNotify标题")
                .setContentText("showBigStyleNotify内容")
                .setStyle(inboxStyle)
                .setTicker("showBigStyleNotify通知");

        mNotificationManager.notify(notifyId,mBuilder.build());
    }

    public void showCzNotify(){
        //		Notification mNotification = new Notification();
//		Notification mNotification  = new Notification.Builder(this).getNotification();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//		//PendingIntent Ìø×ª¶¯×÷
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, getIntent(), 0);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("showCzNotify通知")
                .setContentTitle("showCzNotify标题")
                .setContentText("showCzNotify内容")
                .setContentIntent(pendingIntent);
        Notification mNotification = mBuilder.build();
        mNotification.icon = R.mipmap.ic_launcher;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT   FLAG_AUTO_CANCEL
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        mNotification.tickerText = "showCzNotify通知内容";
        mNotification.when=System.currentTimeMillis();
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//		mNotification.setLatestEventInfo(this, "sdfsafas", "sfdlkfsl", null);
        mNotificationManager.notify(notifyId, mNotification);
    }

    public void showIntentActivityNotify(){
        // Notification.FLAG_ONGOING_EVENT  Flag;Notification.FLAG_AUTO_CANCEL
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
        mBuilder.setAutoCancel(true)
                .setContentTitle("showIntentActivityNotify标题")
                .setContentText("showIntentActivityNotify内容")
                .setTicker("showIntentActivityNotify通知");
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    public void showIntentApkNotify(){
        // Notification.FLAG_ONGOING_EVENT   Flag;Notification.FLAG_AUTO_CANCEL
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
        mBuilder.setAutoCancel(true)
                .setContentTitle("showIntentApkNotify")
                .setContentText("showIntentApkNotify")
                .setTicker("showIntentApkNotify");
        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(android.content.Intent.ACTION_VIEW);
        String apk_path = "file:///android_asset/cs.apk";
//		Uri uri = Uri.parse(apk_path);
        Uri uri = Uri.fromFile(new File(apk_path));
        apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        // context.startActivity(intent);
        PendingIntent contextIntent = PendingIntent.getActivity(this, 0,apkIntent, 0);
        mBuilder.setContentIntent(contextIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    @Override
    public void requestData() {}

    @Override
    public void destroyResource() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                showNotify();
                break;
            case R.id.btn_bigstyle_show:
                showBigStyleNotify();
                break;
            case R.id.btn_show_cz:
                showCzNotify();
                break;
            case R.id.btn_clear:
                clearNotify(notifyId);
                break;
            case R.id.btn_clear_all:
                clearAllNotify();
                break;
            case R.id.btn_show_intent_act:
                showIntentActivityNotify();
                break;
            case R.id.btn_show_intent_apk:
                showIntentApkNotify();
                break;
            case R.id.btn_show_progress:
                startActivity(new Intent(getApplicationContext(), ProgressAcitivty.class));
                break;
            case R.id.btn_show_custom:
                startActivity(new Intent(getApplicationContext(), CustomActivity.class));
                break;
            default:
                break;
        }
    }
}
