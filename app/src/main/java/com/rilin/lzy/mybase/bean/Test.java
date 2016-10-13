package com.rilin.lzy.mybase.bean;

import android.os.Handler;
import android.os.Message;

import java.util.List;

/**
 * Project:BaseProject
 * Author:zhouya
 * Date: 2016/9/3
 */
public class Test {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //设置显示数据

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void selectGroupByTitle(String type1,List<String> list){
        GetDataThread getDataThread = new GetDataThread(type1, list);
        getDataThread.start();
    }

    public class GetDataThread extends Thread{

        private List<String> list;
        private String type1;

        public GetDataThread(String type1,List<String> list){
            this.type1 = type1;
            this.list = list;
        }
        @Override
        public void run() {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    //查询
                    //查询结果
                    Message msg = Message.obtain();
                    msg.obj = "结果";
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

}
