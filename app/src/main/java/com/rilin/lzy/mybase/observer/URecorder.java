package com.rilin.lzy.mybase.observer;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;

import com.rilin.lzy.mybase.util.L;

import java.io.IOException;
import java.util.Date;

/**
 * Created by rilintech on 16/10/12.
 */
public class URecorder implements IVoiceManager {
    private final static String TAG = URecorder.class.getSimpleName();
    private String path;
    private static MediaRecorder mRecorder;
    public boolean isStart;
    public long startTime;
    public int seconds;

    private static URecorder mURecorder;
//    private URecorder(){
//        throw new UnsupportedOperationException("It is not ok");
//    }

    public static URecorder getInstance(){
        if (mURecorder == null) {
            mURecorder = new URecorder();
        }
//        mRecorder = new MediaRecorder();
        return mURecorder;
    }
    public boolean startWithAnimation(String path, final Handler handler){
        start(path);

        new Thread(){
            @Override
            public void run() {
                if(isStart){
                    try {
                        while (isStart) {
                            android.os.Message msg = new android.os.Message();
                            msg.what = mRecorder.getMaxAmplitude() * 13 / 0x7FFF;
                            handler.sendMessage(msg);
                            SystemClock.sleep(200);
                        }
                    } catch (Exception e) {
                        // from the crash report website, found one NPE crash from
                        // one android 4.0.4 htc phone
                        // maybe handler is null for some reason
                        L.e("voice", e.toString());
                    }
                }
            }
        }.start();
        startTime = new Date().getTime();

        return false;
    }

    //开始播放
    @Override
    public boolean start(String path) {
        mRecorder = new MediaRecorder();
        this.path = path;
        //设置音源为Micphone
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置封装格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(path);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            L.e(TAG, "prepare() failed");
        }
        //录音
        mRecorder.start();
        isStart = true;

        return false;
    }

    @Override
    public boolean stop() {
        if(mRecorder != null && isStart){
            isStart = false;
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            seconds = (int) (new Date().getTime() - startTime) / 1000;

        }
        return false;
    }

}
