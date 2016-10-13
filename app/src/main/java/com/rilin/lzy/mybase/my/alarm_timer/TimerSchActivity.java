package com.rilin.lzy.mybase.my.alarm_timer;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.adapter.ClockIndexListViewAdapter;
import com.rilin.lzy.mybase.model.ClockModel;
import com.rilin.lzy.mybase.my.alarm_timer.ClockInfoActivity;
import com.rilin.lzy.mybase.util.ClockDBUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimerSchActivity extends BaseActivity implements View.OnClickListener {

    //标题文字
    private TextView tv_title;
    //添加闹铃
    private Button mAddClock;
    //所有闹铃列表
    private ListView mListView;
    //所有闹铃集合
    private List<ClockModel> mListClock;
    //时间集合
    private List<String> mListTime;
    //标签集合
    private List<String> mListTag;
    //开关集合
    private List<String> mListSwitch;
    //mListClock加载器
    private ClockIndexListViewAdapter adapter;
    //记录当前的ListView的位置
    private int CURRENT_LISTVIEW_ITEM_POSITION = 0;
    //日期
    private TextView tv_date;

    @Override
    public int setViewId() {
        return R.layout.activity_timer_sch;
    }

    @Override
    public void initView() {
        getAllClock();
        tv_date = (TextView) findViewById(R.id.tv_date);
        //初始化设置年月日
        getSysDate();

//        tv_title = (TextView) findViewById(R.id.layout_title).findViewById(R.id.tv_title);
//        tv_title.setText(getResources().getString(R.string.clock));

        mAddClock = (Button) findViewById(R.id.add_clock);
        mAddClock.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.clock_listview);
        adapter = new ClockIndexListViewAdapter(this,
                mListTime,
                mListTag,
                mListSwitch);
        mListView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }

    /**
     * 获取所有的闹铃,并拿到时间/标签/开关的集合
     */
    private void getAllClock() {
        ClockDBUtil clockDBUtil = new ClockDBUtil(getApplicationContext());
        clockDBUtil.openDataBase();
        mListClock = clockDBUtil.getAllClock();
        clockDBUtil.closeDataBase();

        mListTime = new ArrayList<>();
        mListTag = new ArrayList<>();
        mListSwitch = new ArrayList<>();

        for (ClockModel info : mListClock) {
            mListTime.add(info.getTime());
            mListTag.add(info.getTag());
            mListSwitch.add(info.getmSwitch());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_clock:
                Intent intent = new Intent(this, ClockInfoActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
                break;
            case R.id.iv_back:
                this.finish();
                break;

            default:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllClock();
        adapter = new ClockIndexListViewAdapter(this,
                mListTime,
                mListTag,
                mListSwitch);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //回到原来的位置
        mListView.setSelection(CURRENT_LISTVIEW_ITEM_POSITION);

    }

    private void getSysDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String date = format.format(new Date());
        tv_date.setText(date);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //得到当前ListView可见部分的第一个位置
        CURRENT_LISTVIEW_ITEM_POSITION = mListView.getFirstVisiblePosition();
    }
}
