package com.rilin.lzy.mybase.observer;

import android.media.MediaPlayer;

import com.rilin.lzy.mybase.util.L;

/**
 * Created by rilintech on 16/10/12.
 * 播放录音
 */
public class UPlayer implements IVoiceManager {
    private final static String TAG = UPlayer.class.getSimpleName();

    private static UPlayer mUPlayer;
    private String path;
    private static MediaPlayer mPlayer;

    public static UPlayer getInstance(){
        if (mUPlayer == null) {
            mUPlayer = new UPlayer();
        }
        mPlayer = new MediaPlayer();

        return mUPlayer;
    }

    @Override
    public boolean start(String path) {
        this.path = path;
        try {
            //设置要播放的文件
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            //播放
            mPlayer.start();

        }catch(Exception e){
            L.i(TAG, "prepare() failed");
        }
        return false;
    }

    @Override
    public boolean stop() {
        if(mPlayer != null && mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        return false;
    }
}
