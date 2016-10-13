package com.rilin.lzy.mybase.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.observer.PeopleNewsPaper;
import com.rilin.lzy.mybase.observer.SubHuman;

/**
 * 具体请看输出日志
 *
 * I/PeopleNewsPaper: -----RegisterSubscriber-------
 * I/PeopleNewsPaper: -----RegisterSubscriber-------
 * I/PeopleNewsPaper: -----RegisterSubscriber-------
 * I/PeopleNewsPaper: -----sendPaper------------
 * I/SuHuman: ---小明--->>有新的报纸了,请查收
 * I/SuHuman: ---赵云--->>有新的报纸了,请查收
 * I/SuHuman: ---张飞--->>有新的报纸了,请查收
 * I/ObserverActivity: ---------发完报纸--------
 * I/PeopleNewsPaper: -----RemoveSubscriber----------
 * I/PeopleNewsPaper: -----sendPaper------------
 * I/SuHuman: ---赵云--->>有新的报纸了,请查收
 * I/SuHuman: ---张飞--->>有新的报纸了,请查收
 */
public class ObserverActivity extends AppCompatActivity {

    private static final String TAG = "ObserverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);

        initData();
        setData();
    }

    private void setData() {
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.observer_linear);

        TextView textView = new TextView(this);
        textView.setPadding(10,20,10,0);
        textView.setText("PeopleNewsPaper paper = new PeopleNewsPaper();\n" +
                "SubHuman xiaoming = new SubHuman(\"小明\");\n" +
                "SubHuman zhangfei = new SubHuman(\"张飞\");\n" +
                "SubHuman zhaoyun = new SubHuman(\"赵云\");\n" +
                "paper.RegisterSubscriber(xiaoming);\n" +
                "paper.RegisterSubscriber(zhaoyun);\n" +
                "paper.RegisterSubscriber(zhangfei);\n" +
                "paper.SendPaper();\n" +
                "Log.i(TAG,\"---------发完报纸--------\");\n" +
                "paper.RemoveSubscriber(xiaoming);\n" +
                "paper.SendPaper();");
        mLinearLayout.addView(textView);

        TextView textView1 = new TextView(this);
        textView1.setPadding(10,20,10,0);
        textView1.setText("* I/PeopleNewsPaper: -----RegisterSubscriber-------\n" +
                " * I/PeopleNewsPaper: -----RegisterSubscriber-------\n" +
                " * I/PeopleNewsPaper: -----RegisterSubscriber-------\n" +
                " * I/PeopleNewsPaper: -----sendPaper------------\n" +
                " * I/SuHuman: ---小明--->>有新的报纸了,请查收\n" +
                " * I/SuHuman: ---赵云--->>有新的报纸了,请查收\n" +
                " * I/SuHuman: ---张飞--->>有新的报纸了,请查收\n" +
                " * I/ObserverActivity: ---------发完报纸--------\n" +
                " * I/PeopleNewsPaper: -----RemoveSubscriber----------\n" +
                " * I/PeopleNewsPaper: -----sendPaper------------\n" +
                " * I/SuHuman: ---赵云--->>有新的报纸了,请查收\n" +
                " * I/SuHuman: ---张飞--->>有新的报纸了,请查收");

        mLinearLayout.addView(textView1);
    }

    private void initData() {
        PeopleNewsPaper paper = new PeopleNewsPaper();

        SubHuman xiaoming = new SubHuman("小明");
        SubHuman zhangfei = new SubHuman("张飞");
        SubHuman zhaoyun = new SubHuman("赵云");

        //订阅
        paper.RegisterSubscriber(xiaoming);
        paper.RegisterSubscriber(zhaoyun);
        paper.RegisterSubscriber(zhangfei);

        //发送报纸
        paper.SendPaper();

        Log.i(TAG,"---------发完报纸--------");

        //小明取消订阅报纸
        paper.RemoveSubscriber(xiaoming);
        //报纸
        paper.SendPaper();

    }
}
