package com.rilin.lzy.mybase.my;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.service.ReceiveNetMsgService;
import com.rilin.lzy.mybase.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetStatesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageBack;
    private List<String> mNetStates;
    private ListView mListView;
    private ArrayAdapter mAdapter;

    private static final String TAG = "NetStatesActivity";
    private ReceiveNetMsgService mNetMsgService;
    private boolean connectState;//记录当前网络状态

    private int currentIndex = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://有网络
                    mNetStates.add("当前网络状态为---" + ++currentIndex + "--->>>" + connectState + "---" + DateUtil.getDateTimeFormat(new Date()));
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    mNetStates.add("当前网络状态为---" + ++currentIndex + "--->>>" + connectState + "---" + DateUtil.getDateTimeFormat(new Date()));
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_states);
        Log.i(TAG,"NetStateActivity-----------");
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initView() {
        Log.i(TAG,"INITVIEW000000000000----");
        bind();
        Log.i(TAG,"INITVIEW111111111111----");
        mListView = (ListView) findViewById(R.id.net_list_view);
        mImageBack = (ImageView) findViewById(R.id.image_back);
        mImageBack.setOnClickListener(this);
    }

    private void bind() {
        Intent intent = new Intent(NetStatesActivity.this,ReceiveNetMsgService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void initData() {
        mNetStates = new ArrayList<>();
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mNetStates);
        mListView.setAdapter(mAdapter);
        Log.i(TAG,"INITDATE------------");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"----onServiceConnected----");
            mNetMsgService = ((ReceiveNetMsgService.MyBinder) service).getService();
            mNetMsgService.setNetConnectState(new ReceiveNetMsgService.GetNetConnectState() {
                @Override
                public void getNetState(boolean isConnected) {

                    connectState = isConnected;
                    if(connectState){//有网络
                        mHandler.sendEmptyMessage(1);
                        Log.i(TAG,"------有网络---" + connectState);
                    }else {//没有网络
                        mHandler.sendEmptyMessage(2);
                        Log.i(TAG,"------无网络---" + connectState);
                    }

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"SERVICE---断开");
        }
    };

    private void unbind(){
        if (mNetMsgService != null) {
            unbindService(serviceConnection);
            Log.i(TAG,"执行了unbind()----------");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
        }
    }
}
