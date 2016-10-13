package com.rilin.lzy.mybase.my.tv_vitamio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.rilin.lzy.mybase.my.tv_vitamio.VideoView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.util.L;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

public class VideoPlayerActivity extends AppCompatActivity implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {

    private String mPath;
    public static String mTitle;
    private int nuberm;
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /** 当前亮度 */
    private float mBrightness = -1f;
    /** 当前缩放模式 */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;
    private View mLoadingView;
    private final String ACTION_NAME = "发送广播";
    public   String[] url;
    public   String[] name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        // ~~~ 检测Vitamio是否解压解码包
//        if (!LibsChecker.checkVitamioLibs(this, R.string.init_decoders))
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        int i=3;

        String urlname="http://221.181.41.123/live/31";
        url=new String[]{"http://202.102.79.114:554/live/tv22/playlist.m3u8","http://218.24.47.44/CCTV2.m3u8",urlname+"0"+(i++),
                urlname+"0"+(i++),urlname+"0"+(i++),urlname+"0"+(i++),urlname+"0"+(i++),
                urlname+"0"+(i++),urlname+"0"+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                urlname+(i++),urlname+(i++),urlname+(i++),urlname+(i++),
                "http://lm02.everyon.tv:80/ptv2/pld687/playlist.m3u8"
        };

        L.v("debuginfo", url.length+"--------");
        // ~~~ 获取播放地址和标题
        Intent intent = getIntent();
        nuberm = intent.getIntExtra("count",1);

        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mLoadingView = findViewById(R.id.video_loading);

        // ~~~ 绑定事件
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);

        // ~~~ 绑定数据
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        setPath();

    }

    public void setPath(){
        //获取播放地址
        mPath=url[nuberm];
        Log.e("debuginfo", "path:" + mPath);
        if (mPath.startsWith("http:"))
            mVideoView.setVideoURI(Uri.parse(mPath));

        else
            mVideoView.setVideoPath(mPath);
        //播放标题
        mTitle=name[nuberm];
        // 设置显示名称
        mMediaController = new MediaController(this);
        mMediaController.setFileName(name[nuberm]);
        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    public String setFileName() {
        return name[nuberm];
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null)
            mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null)
            mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBroadcastReceiver != null){
            unregisterReceiver(mBroadcastReceiver);
        }
        if (mVideoView != null)
            mVideoView.stopPlayback();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent1) {
            String action = intent1.getAction();
            if(action.equals(ACTION_NAME)){
                int count=intent1.getExtras().getInt("count");
                if (count==1) {
                    android.util.Log.e("debuginfo", "广播----------2");
                    if (nuberm>=name.length-1) {
                        nuberm=0;
                        Log.e("debuginfo", "广播------rrrr");
                        setPath();
                    }else {
                        nuberm++;
                        Log.e("debuginfo", "广播------rrrr");
                        setPath();
                    }
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    /** 手势结束 */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.mipmap.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.mipmap.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }

    private void stopPlayer() {
        if (mVideoView != null)
            mVideoView.pause();
    }

    private void startPlayer() {
        if (mVideoView != null)
            mVideoView.start();
    }

    private boolean isPlaying() {
        return mVideoView != null && mVideoView.isPlaying();
    }

    /** 是否需要自动恢复播放，用于自动暂停，恢复播放 */
    private boolean needResume;

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        switch (i) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                // 开始缓存，暂停播放
                if (isPlaying()) {
                    stopPlayer();
                    needResume = true;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                // 缓存完成，继续播放
                if (needResume)
                    startPlayer();
                mLoadingView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                // 显示 下载速度
                L.e("download rate:" + i1);
                // mListener.onDownloadRateChanged(arg2);
                break;
        }
        return true;
    }
}
