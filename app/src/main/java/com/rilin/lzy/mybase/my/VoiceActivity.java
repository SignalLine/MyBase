package com.rilin.lzy.mybase.my;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.observer.UPlayer;
import com.rilin.lzy.mybase.observer.URecorder;
import com.rilin.lzy.mybase.util.SDcardUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VoiceActivity extends BaseActivity implements View.OnClickListener {

    private Button mButtonStart,mButtonStop,mButtonPlay,mButtonDelete;
    private ListView mListView;
    private String strTempFile = "ex_voice_";

    private File myRecAudioFile;
    private File myRecAudioDir;
    private File myPlayFile;
    private MediaRecorder mMediaRecorder01;

    private ArrayList<String> recordFiles;
    private ArrayAdapter<String> adapter;
    private TextView myTextView1;

    private boolean sdCardExit;
    private boolean isStopRecord;

    //点击按下录音---------------------
    private Button buttonPressToSpeak;
    //存储很多张话筒图片的数组
    private Drawable[] micImages;
    //话筒的图片
    private ImageView micImage;

    private RelativeLayout recordingContainer;
    private TextView recordingHint;
    private int BASE = 600;
    private int SPACE = 200;// 间隔取样时间

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if(what > 13){
                what = 13;
            }
            micImage.setImageDrawable(micImages[what]);
        }
    };
    private URecorder mRecorder;

    @Override
    public int setViewId() {
        return R.layout.activity_voice;
    }

    @Override
    public void initView() {
        mButtonStart = (Button) findViewById(R.id.voice_start);
        mButtonStop = (Button) findViewById(R.id.voice_stop);
        mButtonPlay = (Button) findViewById(R.id.voice_play);
        mButtonDelete = (Button) findViewById(R.id.voice_delete);
        myTextView1 = (TextView) findViewById(R.id.voice_tv_path);
        mListView = (ListView) findViewById(R.id.voice_list_view);
        //----------------
        micImage=(ImageView) findViewById(R.id.mic_image);
        recordingHint=(TextView) findViewById(R.id.recording_hint);
        recordingContainer=(RelativeLayout) findViewById(R.id.recording_container);
        buttonPressToSpeak=(Button) findViewById(R.id.buttonPressToSpeak);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[] {
                getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14) };
    }

    @Override
    public void initData() {
        //sd卡是否插入
        sdCardExit = SDcardUtils.isSDcardExists();
        //sd卡路径为录音的文件位置
        if(sdCardExit)
            myRecAudioDir = Environment.getExternalStorageDirectory();

        //取得sd卡所有的.amr文件
        getRecordFiles();

        adapter = new ArrayAdapter<String>(this,R.layout.item_voice_list,recordFiles);
        mListView.setAdapter(adapter);

        setListener();
    }

    private void setListener() {
        //录音
        mButtonStart.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //当有点选文件名时候讲删除及播放按钮Enable
                mButtonPlay.setEnabled(true);
                mButtonDelete.setEnabled(true);

                myPlayFile = new File(myRecAudioDir.getAbsolutePath() + File.separator
                                + ((CheckedTextView)view).getText());

                myTextView1.setText("你选的是:" + ((CheckedTextView)view).getText());
            }
        });
    }

    //取得sd卡所有的.amr文件
    private void getRecordFiles(){
        recordFiles = new ArrayList<>();
        if(sdCardExit){
            File[] files = myRecAudioDir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if(files[i].getName().contains(".")){
                        //读取.amr文件
                        String s = files[i].getName().substring(files[i].getName().indexOf("."));
                        if(s.toLowerCase().equals(".amr")){
                            recordFiles.add(files[i].getName());
                        }
                    }
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
            case R.id.voice_start://开始录音
                try {
                    if(!sdCardExit){
                        Toast.makeText(VoiceActivity.this, "没有sd卡", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //建立录音档
                    myRecAudioFile = File.createTempFile(strTempFile,".amr",myRecAudioDir);



//                    mMediaRecorder01 = new MediaRecorder();
////                    //设定录音来源为麦克风
//                    //Call this only before setOutputFormat().这里很重要，如果在setOutputFormat的后面调用的话，会报异常！
//                    mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
//                    mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//                    mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//                    mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
//                    mMediaRecorder01.prepare();

                    mRecorder = URecorder.getInstance();
                    mRecorder.start(myRecAudioFile.getAbsolutePath());

                    myTextView1.setText("录音中...");
                    mButtonStop.setEnabled(true);
                    mButtonPlay.setEnabled(false);
                    mButtonDelete.setEnabled(false);

//                    mMediaRecorder01.start();
                    isStopRecord = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.voice_stop://停止录音
//                mMediaRecorder01.stop();
//                //将录音文件名给adapter
                adapter.add(myRecAudioFile.getName());
//                mMediaRecorder01.release();
//                mMediaRecorder01 = null;
                if(mRecorder != null)
                    mRecorder.stop();

                myTextView1.setText("停止:" + myRecAudioFile.getName());

                mButtonStop.setEnabled(false);

                isStopRecord = true;
                break;
            case R.id.voice_play://播放录音
                if(myPlayFile != null && myPlayFile.exists()){
                    //播放
                    //openFile(myPlayFile);
                    UPlayer.getInstance().start(myPlayFile.getAbsolutePath());
                }
                break;
            case R.id.voice_delete://删除录音
                if (myPlayFile != null) {
                    adapter.remove(myPlayFile.getName());
                    if(myPlayFile.exists()){
                        myPlayFile.delete();
                        myTextView1.setText("完成删除");
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    //开始录音播放
    private void openFile(File myPlayFile) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        String type = getMIMEType(myPlayFile);
        intent.setDataAndType(Uri.fromFile(myPlayFile),type);
        startActivity(intent);
    }

    private String getMIMEType(File myPlayFile) {
        String end = myPlayFile.getName()
                .substring(myPlayFile.getName().lastIndexOf(".") + 1,myPlayFile.getName().length())
                .toLowerCase();
        String type = "";
        if(end.equals("mp3") || end.equals(".amr") || end.equals("aac") ||
                end.equals("mp4") || end.equals("mpeg")){
            type = "audio";
        }else if(end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")){
            type = "image";
        }else {
            type = "*";
        }
        type += "/*";

        return type;
    }

    @Override
    protected void onStop() {
        if(mMediaRecorder01 != null && !isStopRecord){
            //停止录音
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
        super.onStop();
    }

    /**
     * 按住说话listener
     *
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    try {
                        if (!sdCardExit) {
                            Toast.makeText(VoiceActivity.this, "未检测到SD卡", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        //建立录音档
                        myRecAudioFile = File.createTempFile(strTempFile,".amr",myRecAudioDir);
                        //开始录音
                        if(mRecorder != null){
                            mRecorder.stop();
                        }
                        mRecorder = URecorder.getInstance();

                        mRecorder.startWithAnimation(myRecAudioFile.getAbsolutePath(),mHandler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    recordingContainer.setVisibility(View.VISIBLE);
                    recordingHint.setText("手指上滑，取消发送");
                    recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    //在这里只是做了监听，并没有做与发送相关的处理
                    if (event.getY() < 0) {
                        recordingHint.setText("松开手指，取消发送");
                        recordingHint.setBackgroundResource(R.drawable.ease_recording_text_hint_bg);

                    } else {
                        recordingHint.setText("手指上滑，取消发送");
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);

                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    if(mRecorder != null)
                        mRecorder.stop();
                    //抬起手指，停止录音
                    recordingContainer.setVisibility(View.GONE);

                    if(event.getY() < 0){
                        if(myRecAudioFile.exists()){
                            myRecAudioFile.delete();
                            myRecAudioFile = null;
                        }
                        Toast.makeText(VoiceActivity.this, "取消录音", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    adapter.add(myRecAudioFile.getName());
                    Toast.makeText(getApplicationContext(), "录音时间为" + mRecorder.seconds + "S,--->" + myRecAudioFile.getName(), Toast.LENGTH_SHORT).show();
                    return true;
                default:

                    return false;
            }
        }
    }
}
