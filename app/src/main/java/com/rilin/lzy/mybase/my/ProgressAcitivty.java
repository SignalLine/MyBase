package com.rilin.lzy.mybase.my;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;


public class ProgressAcitivty extends BaseActivity implements View.OnClickListener {

    private Button btn_show_progress;
    private Button btn_show_un_progress;
    private Button btn_show_custom_progress;
    /** Notification */
    int notifyId = 102;
    /** Notification */
    int progress = 0;
    NotificationCompat.Builder mBuilder;
    Button btn_download_start;
    Button btn_download_pause;
    Button btn_download_cancel;

    public boolean isPause = false;

    public boolean isCustom = false;
    DownloadThread downloadThread;

    public Boolean indeterminate = false;

    @Override
    public int setViewId() {
        return R.layout.activity_progress;
    }

    @Override
    public void initView() {
        btn_show_progress = (Button) findViewById(R.id.btn_show_progress);
        btn_show_un_progress = (Button) findViewById(R.id.btn_show_un_progress);
        btn_show_custom_progress = (Button) findViewById(R.id.btn_show_custom_progress);
        btn_download_start = (Button) findViewById(R.id.btn_download_start);
        btn_download_pause = (Button) findViewById(R.id.btn_download_pause);
        btn_download_cancel = (Button) findViewById(R.id.btn_download_cancel);
        btn_show_progress.setOnClickListener(this);
        btn_show_un_progress.setOnClickListener(this);
        btn_show_custom_progress.setOnClickListener(this);
        btn_download_start.setOnClickListener(this);
        btn_download_pause.setOnClickListener(this);
        btn_download_cancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setWhen(System.currentTimeMillis())
                .setContentIntent(getDefaultIntent(0))
                // .setNumber(number)
                .setPriority(Notification.PRIORITY_DEFAULT)
                // .setAutoCancel(true)
                .setOngoing(false)// ture
                .setDefaults(Notification.DEFAULT_VIBRATE)
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND
                // requires VIBRATE permission
                .setSmallIcon(R.mipmap.icon);
    }

    public void showProgressNotify() {
        mBuilder.setContentTitle("showProgressNotify标题")
                .setContentText("showProgressNotify内容")
                .setTicker("showProgressNotify通知");
        if(indeterminate){
            mBuilder.setProgress(0, 0, true);
        }else{
            mBuilder.setProgress(100, progress, false);
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    private void showCustomProgressNotify(String status) {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_custom_progress);
        mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.mipmap.icon);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, "开始下载啦");
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, status);
        if(progress >= 100 || downloadThread == null){
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
        }else{
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
        }
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefaultIntent(0))
                .setTicker("showCustomProgressNotify通知");
        Notification nitify = mBuilder.build();
        nitify.contentView = mRemoteViews;
        mNotificationManager.notify(notifyId, nitify);
    }

    public void startDownloadNotify() {
        isPause = false;
        if (downloadThread != null && downloadThread.isAlive()) {
//			downloadThread.start();
        }else{
            downloadThread = new DownloadThread();
            downloadThread.start();
        }
    }

    public void pauseDownloadNotify() {
        isPause = true;
        if(!isCustom){
            mBuilder.setContentTitle("pauseDownloadNotify标题");
            setNotify(progress);
        }else{
            showCustomProgressNotify("pauseDownloadNotify通知showCustomProgressNotify");
        }
    }

    public void stopDownloadNotify() {
        if (downloadThread != null) {
            downloadThread.interrupt();
        }
        downloadThread = null;
        if(!isCustom){
            mBuilder.setContentTitle("stopDownloadNotify标题").setProgress(0, 0, false);
            mNotificationManager.notify(notifyId, mBuilder.build());
        }else{
            showCustomProgressNotify("stopDownloadNotify通知showCustomProgressNotify");
        }
    }

    public void setNotify(int progress) {
        mBuilder.setProgress(100, progress, false);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    class DownloadThread extends Thread {

        @Override
        public void run() {
            int now_progress = 0;
            // Do the "lengthy" operation 20 times
            while (now_progress <= 100) {
                // Sets the progress indicator to a max value, the
                // current completion percentage, and "determinate"
                if(downloadThread == null){
                    break;
                }
                if (!isPause) {
                    progress = now_progress;
                    if(!isCustom){
                        mBuilder.setContentTitle("downloadThread_progress标题");
                        if(!indeterminate){
                            setNotify(progress);
                        }
                    }else{
                        showCustomProgressNotify("downloadThread通知showCustomProgressNotify");
                    }
                    now_progress += 10;
                }
                try {
                    // Sleep for 1 seconds
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                }
            }
            // When the loop is finished, updates the notification
            if(downloadThread != null){
                if(!isCustom){
                    mBuilder.setContentText("downloadThread标题")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotificationManager.notify(notifyId, mBuilder.build());
                }else{
                    showCustomProgressNotify("downloadThread通知showCustomProgressNotify");
                }
            }
        }
    }

    @Override
    public void requestData() {}

    @Override
    public void destroyResource() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_progress:
                downloadThread = null;
                isCustom = false;
                indeterminate = false;
                showProgressNotify();
                break;
            case R.id.btn_show_un_progress:
                downloadThread = null;
                isCustom = false;
                indeterminate = true;
                showProgressNotify();
                break;
            case R.id.btn_show_custom_progress:
                downloadThread = null;
                isCustom = true;
                indeterminate = false;
                showCustomProgressNotify("下载中..");
                break;
            case R.id.btn_download_start:
                startDownloadNotify();
                break;
            case R.id.btn_download_pause:
                pauseDownloadNotify();
                break;
            case R.id.btn_download_cancel:
                stopDownloadNotify();
                break;
            default:
                break;
        }
    }
}
